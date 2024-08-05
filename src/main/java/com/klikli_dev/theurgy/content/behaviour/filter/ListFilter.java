// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ListFilter extends Filter {

    protected List<ItemStack> filterItems;
    protected List<FluidStack> filterFluids;
    protected boolean shouldRespectDataComponents;
    protected boolean isDenyList;

    protected ListFilter(HolderLookup.Provider provider, ItemStack filter) {
        super(provider, filter);
    }

    public List<ItemStack> filterItems(){
        return this.filterItems;
    }

    public List<FluidStack> filterFluids(){
        return this.filterFluids;
    }

    public boolean shouldRespectDataComponents(){
        return this.shouldRespectDataComponents;
    }

    public boolean isDenyList(){
        return this.isDenyList;
    }

    @Override
    protected void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack) {
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
    public boolean test(Level level, FluidStack stack, boolean matchDataComponents) {
        this.resolveFluids();

        for (var filterFluidStack : this.filterFluids) {
            if (this.testFilterFluidStack(filterFluidStack, stack, this.shouldRespectDataComponents))
                return !this.isDenyList;
        }

        return this.isDenyList;
    }

    private void resolveFluids() {
        if (this.filterFluids != null)
            return;

        this.filterFluids = new ArrayList<>();

        for (var filterItem : this.filterItems) {
            var capability = filterItem.getCapability(Capabilities.FluidHandler.ITEM);
            if (capability == null)
                continue;

            this.filterFluids.add(capability.getFluidInTank(0).copyWithAmount(1));
        }
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

    private boolean testFilterFluidStack(FluidStack filterFluidStack, FluidStack stackToTest, boolean matchDataComponents) {
        if (matchDataComponents) {
            return FluidStack.matches(filterFluidStack, stackToTest);
        } else {
            return FluidStack.isSameFluid(filterFluidStack, stackToTest);
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