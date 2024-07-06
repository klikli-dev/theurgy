// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface ItemAttribute {
    List<ItemAttribute> types = new ArrayList<>();

    ItemAttribute standard = register(StandardAttributes.DUMMY);
    ItemAttribute inTag = register(new InTagAttribute(ItemTags.LOGS));
    ItemAttribute addedBy = register(new AddedByAttribute("dummy"));
    ItemAttribute hasEnchant = register(EnchantAttribute.EMPTY);
    ItemAttribute hasFluid = register(FluidContentsAttribute.EMPTY);
    ItemAttribute hasName = register(new ItemNameAttribute("dummy"));

    static ItemAttribute register(ItemAttribute attributeType) {
        types.add(attributeType);
        return attributeType;
    }

    @Nullable
    static ItemAttribute of(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        for (ItemAttribute itemAttribute : types)
            if (itemAttribute.canRead(nbt))
                return itemAttribute.readNBT(pRegistries, nbt.getCompound(itemAttribute.getNBTKey()));
        return null;
    }

    default boolean appliesTo(ItemStack stack, Level world) {
        return this.appliesTo(stack);
    }

    boolean appliesTo(ItemStack stack);

    default List<ItemAttribute> listAttributesOf(ItemStack stack, Level world) {
        return this.listAttributesOf(stack);
    }

    List<ItemAttribute> listAttributesOf(ItemStack stack);

    String getTranslationKey();

    void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt);

    ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt);

    default void serializeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        CompoundTag compound = new CompoundTag();
        this.writeNBT(pRegistries, compound);
        nbt.put(this.getNBTKey(), compound);
    }

    default Object[] getTranslationParameters() {
        return new String[0];
    }

    default boolean canRead(CompoundTag nbt) {
        return nbt.contains(this.getNBTKey());
    }

    default String getNBTKey() {
        return this.getTranslationKey();
    }

    default MutableComponent format(boolean inverted) {
        return Component.translatable("item_attributes." + this.getTranslationKey() + (inverted ? ".inverted" : ""),
                this.getTranslationParameters());
    }

}
