// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class InTagAttribute implements ItemAttribute {

    public static final InTagAttribute DUMMY = new InTagAttribute(ItemTags.DIRT);

    public TagKey<Item> tag;

    public InTagAttribute(TagKey<Item> tag) {
        this.tag = tag;
    }

    @Override
    public boolean appliesTo(ItemStack stack) {
        return stack.is(this.tag);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack) {
        return stack.getTags()
                .map(InTagAttribute::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getTranslationKey() {
        return "in_tag";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{"#" + this.tag.location()};
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        nbt.putString("space", this.tag.location().getNamespace());
        nbt.putString("path", this.tag.location().getPath());
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        return new InTagAttribute(ItemTags.create(ResourceLocation.fromNamespaceAndPath(nbt.getString("space"), nbt.getString("path"))));
    }

}