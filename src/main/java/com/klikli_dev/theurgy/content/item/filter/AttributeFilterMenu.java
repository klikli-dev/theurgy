// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.content.behaviour.filter.FilterMode;
import com.klikli_dev.theurgy.content.behaviour.filter.attribute.ItemAttribute;
import com.klikli_dev.theurgy.content.storage.ComponentItemStorage;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttributeFilterMenu extends AbstractFilterMenu {

    public FilterMode filterMode;
    public List<Pair<ItemAttribute, Boolean>> selectedAttributes;

    protected AttributeFilterMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected AttributeFilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static AttributeFilterMenu create(int id, Inventory inv, ItemStack stack) {
        return new AttributeFilterMenu(MenuTypeRegistry.ATTRIBUTE_FILTER.get(), id, inv, stack);
    }

    public static AttributeFilterMenu create(int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        return new AttributeFilterMenu(MenuTypeRegistry.ATTRIBUTE_FILTER.get(), id, inv, extraData);
    }

    @Override
    protected ComponentItemStorage createGhostInventory() {
        return new ComponentItemStorage(this.contentHolder, DataComponentRegistry.FILTER_ITEMS.get(), 2);
    }

    @Override
    protected int getPlayerInventoryXOffset() {
        return 40;
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 107;
    }

    @Override
    protected void addFilterSlots() {
        this.addSlot(new ResourceHandlerSlot(this.ghostInventory, (slot, resource, amount) -> this.ghostInventory.set(slot, resource, amount), 0, 16, 24));
        this.addSlot(new ResourceHandlerSlot(this.ghostInventory, (slot, resource, amount) -> this.ghostInventory.set(slot, resource, amount), 1, 22, 59) {
            @Override
            public boolean mayPickup(@NotNull Player playerIn) {
                return false;
            }
        });
    }

    @Override
    protected void init(Inventory inv, ItemStack contentHolderIn) {
        super.init(inv, contentHolderIn);

        ItemStack stack = new ItemStack(Items.NAME_TAG);
        stack.set(DataComponents.ITEM_NAME,
                Component.literal("Selected Tags").withStyle(ChatFormatting.RESET, ChatFormatting.BLUE));
        this.ghostInventory.set(1, ItemResource.of(stack), stack.getCount());
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ContainerInput clickTypeIn, @NotNull Player player) {
        if (slotId == 37)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        if (slotIn.index == 37)
            return false;
        return super.canDragTo(slotIn);
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slotIn) {
        if (slotIn.index == 37)
            return false;
        return super.canTakeItemForPickAll(stack, slotIn);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        if (index == 37)
            return ItemStack.EMPTY;
        if (index == 36) {
            this.ghostInventory.set(1, ItemResource.of(ItemStack.EMPTY), 0);
            return ItemStack.EMPTY;
        }
        if (index < 36) {
            ItemStack stackToInsert = this.playerInventory.getItem(index);
            ItemStack copy = stackToInsert.copy();
            copy.setCount(1);
            this.ghostInventory.set(0, ItemResource.of(copy), copy.getCount());
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void initAndReadInventory(ItemStack filterItem) {
        super.initAndReadInventory(filterItem);

        this.filterMode = this.contentHolder.getOrDefault(DataComponentRegistry.FILTER_MODE, FilterMode.ACCEPT_LIST_OR);

        this.selectedAttributes = new ArrayList<>();

        var tag = filterItem.getOrDefault(DataComponentRegistry.FILTER_ATTRIBUTES, CustomData.EMPTY).copyTag();
        ListTag attributes = tag.getList("MatchedAttributes").orElseGet(ListTag::new);
        attributes.forEach(inbt -> {
            CompoundTag compound = (CompoundTag) inbt;
            this.selectedAttributes.add(Pair.of(ItemAttribute.of(this.player.registryAccess(), compound), compound.getBoolean("Inverted").orElse(false)));
        });
    }


    @Override
    protected void saveData(ItemStack contentHolder) {
        contentHolder.set(DataComponentRegistry.FILTER_MODE, this.filterMode);

        ListTag attributes = new ListTag();
        this.selectedAttributes.forEach(at -> {
            if (at == null)
                return;
            CompoundTag compoundNBT = new CompoundTag();
            at.getFirst().serializeNBT(this.player.registryAccess(), compoundNBT);
            compoundNBT.putBoolean("Inverted", at.getSecond());
            attributes.add(compoundNBT);
        });

        var tag = new CompoundTag();
        tag.put("MatchedAttributes", attributes);
        contentHolder.set(DataComponentRegistry.FILTER_ATTRIBUTES, CustomData.of(tag));
    }

    @Override
    public void clearContents() {
        this.selectedAttributes.clear();
    }
}
