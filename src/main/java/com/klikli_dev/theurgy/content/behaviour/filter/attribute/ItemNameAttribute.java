// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemNameAttribute implements ItemAttribute {
    String itemName;

    public ItemNameAttribute(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return extractCustomName(itemStack).equals(itemName);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack itemStack) {
        String name = extractCustomName(itemStack);

        List<ItemAttribute> atts = new ArrayList<>();
        if(name.length() > 0) {
            atts.add(new ItemNameAttribute(name));
        }
        return atts;
    }

    @Override
    public String getTranslationKey() {
        return "has_name";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[] { itemName };
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        nbt.putString("name", this.itemName);
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        return new ItemNameAttribute(nbt.getString("name"));
    }

    private String extractCustomName(ItemStack stack) {
        var name = stack.get(DataComponents.ITEM_NAME);
        return name == null ? "" : name.getString();
    }
}
