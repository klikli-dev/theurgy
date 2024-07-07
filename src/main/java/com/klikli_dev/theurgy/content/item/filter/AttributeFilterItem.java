// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.filter.FilterMode;
import com.klikli_dev.theurgy.content.behaviour.filter.attribute.ItemAttribute;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeFilterItem extends FilterItem {
    public AttributeFilterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected List<Component> makeSummary(ItemStack filter, HolderLookup.Provider provider) {
        List<Component> list = new ArrayList<>();

        if (!filter.has(DataComponentRegistry.FILTER_ATTRIBUTES))
            return list;

        var filterMode = filter.getOrDefault(DataComponentRegistry.FILTER_MODE, FilterMode.ACCEPT_LIST_OR);

        list.add((filterMode == FilterMode.ACCEPT_LIST_AND
                ? Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP)
                : filterMode == FilterMode.ACCEPT_LIST_OR
                ? Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP)
                : Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP)).withStyle(ChatFormatting.GOLD));

        int count = 0;
        //noinspection deprecation
        var tag = filter.getOrDefault(DataComponentRegistry.FILTER_ATTRIBUTES, CustomData.EMPTY).getUnsafe();
        ListTag attributes = tag.getList("MatchedAttributes", Tag.TAG_COMPOUND);

        for (Tag inbt : attributes) {
            CompoundTag compound = (CompoundTag) inbt;
            ItemAttribute attribute = ItemAttribute.of(provider, compound);
            if (attribute == null)
                continue;
            boolean inverted = compound.getBoolean("Inverted");
            if (count > 3) {
                list.add(Component.literal("- ...")
                        .withStyle(ChatFormatting.DARK_GRAY));
                break;
            }
            list.add(Component.literal("- ")
                    .append(attribute.format(inverted)));
            count++;
        }

        if (count == 0)
            return Collections.emptyList();

        return list;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return AttributeFilterMenu.create(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }
}
