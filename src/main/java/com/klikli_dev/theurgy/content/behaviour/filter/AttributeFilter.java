// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.content.behaviour.filter.attribute.ItemAttribute;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class AttributeFilter extends Filter {

    public FilterMode filterMode;
    public List<Pair<ItemAttribute, Boolean>> attributeTests;

    protected AttributeFilter(HolderLookup.Provider provider, ItemStack filter) {
        super(provider, filter);

    }

    @Override
    protected void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack) {
        this.attributeTests = new ArrayList<>();
        this.filterMode = filterItemStack.getOrDefault(DataComponentRegistry.FILTER_MODE, FilterMode.ACCEPT_LIST_OR);

        //noinspection deprecation
        var tag = filterItemStack.getOrDefault(DataComponentRegistry.FILTER_ATTRIBUTES, CustomData.EMPTY).getUnsafe();

        ListTag attributes = tag.getList("MatchedAttributes", Tag.TAG_COMPOUND);
        for (Tag inbt : attributes) {
            CompoundTag compound = (CompoundTag) inbt;
            ItemAttribute attribute = ItemAttribute.of(provider, compound);
            if (attribute != null)
                this.attributeTests.add(Pair.of(attribute, compound.getBoolean("Inverted")));
        }
    }

    @Override
    public boolean test(Level level, ItemStack stack, boolean matchDataComponents) {
        if (this.attributeTests.isEmpty())
            return true;

        for (Pair<ItemAttribute, Boolean> test : this.attributeTests) {
            ItemAttribute attribute = test.getFirst();
            boolean inverted = test.getSecond();
            boolean matches = attribute.appliesTo(stack, level) != inverted;

            if (matches) {
                switch (this.filterMode) {
                    case DENY_LIST:
                        return false;
                    case ACCEPT_LIST_AND:
                        continue;
                    case ACCEPT_LIST_OR:
                        return true;
                }
            } else {
                switch (this.filterMode) {
                    case DENY_LIST:
                        continue;
                    case ACCEPT_LIST_AND:
                        return false;
                    case ACCEPT_LIST_OR:
                        continue;
                }
            }
        }

        switch (this.filterMode) {
            case DENY_LIST:
                return true;
            case ACCEPT_LIST_AND:
                return true;
            case ACCEPT_LIST_OR:
                return false;
        }

        return false;
    }

    @Override
    public boolean test(Level level, FluidStack stack, boolean matchDataComponents) {
        //Does not support fluids currently
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.filterItemStack.isEmpty();
    }
}