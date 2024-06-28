// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ListFilter extends Filter {

    public List<ItemStack> filterItems;
    public boolean shouldRespectDataComponents;
    public boolean isDenyList;

    protected ListFilter(ItemStack filter) {
        super(filter);
    }

    @Override
    protected void initFromFilterItemStack(ItemStack filterItemStack) {
        this.filterItems = this.getFilterItems(filterItemStack);

        this.shouldRespectDataComponents = filterItemStack.getOrDefault(DataComponentRegistry.FILTER_RESPECTS_DATA_COMPONENTS, false);
        this.isDenyList = filterItemStack.getOrDefault(DataComponentRegistry.FILTER_IS_DENY_LIST, false);
    }

    @Override
    public boolean test(Level world, ItemStack stack, boolean matchDataComponents) {
        for (var filterItemStack : this.filterItems) {
            if (this.testFilterItemStack(filterItemStack, stack, this.shouldRespectDataComponents))
                return !this.isDenyList;
        }

        return this.isDenyList;
    }

    @Override
    public boolean isEmpty() {
        return this.filterItems.isEmpty();
    }

    private boolean testFilterItemStack(ItemStack filterItemStack, ItemStack stackToTest, boolean matchDataComponents) {
        if (matchDataComponents) {
            return ItemStack.isSameItemSameComponents(filterItemStack, stackToTest);
        } else {
            return ItemStack.isSameItem(filterItemStack, stackToTest);
        }
    }

    private List<ItemStack> getFilterItems(ItemStack filter) {
        var result = new ArrayList<ItemStack>();

        if (!filter.has(DataComponentRegistry.FILTER_ITEMS))
            return result;

        var items = filter.get(DataComponentRegistry.FILTER_ITEMS);

        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (!stackInSlot.isEmpty())
                result.add(stackInSlot);
        }

        return result;
    }

}