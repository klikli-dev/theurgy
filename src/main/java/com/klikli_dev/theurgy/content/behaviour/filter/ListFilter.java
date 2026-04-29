// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.codedefinedgui.filter.core.FilterMatchContext;
import com.klikli_dev.codedefinedgui.filter.list.ListFilterDefinition;
import com.klikli_dev.codedefinedgui.filter.list.ListFilterMode;
import com.klikli_dev.codedefinedgui.filter.list.ListFilterState;
import com.klikli_dev.codedefinedgui.filter.list.ListFilterStateAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.ArrayList;
import java.util.List;

public class ListFilter extends Filter {

    protected ListFilterState state;
    protected List<ItemStack> filterItems;
    protected List<FluidStack> filterFluids;
    protected boolean shouldRespectDataComponents;
    protected boolean isDenyList;

    protected ListFilter(HolderLookup.Provider provider, ItemStack filter) {
        super(provider, filter);
    }

    public List<ItemStack> filterItems() {
        return this.filterItems;
    }

    public List<FluidStack> filterFluids() {
        return this.filterFluids;
    }

    public boolean shouldRespectDataComponents() {
        return this.shouldRespectDataComponents;
    }

    public boolean isDenyList() {
        return this.isDenyList;
    }

    @Override
    protected void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack) {
        this.state = ListFilterStateAccessor.INSTANCE.read(filterItemStack);
        this.filterItems = this.getFilterItems(this.state.entries());
        this.shouldRespectDataComponents = this.state.respectDataComponents();
        this.isDenyList = this.state.mode() == ListFilterMode.DENY;
        this.filterFluids = null;
    }

    @Override
    public boolean test(Level world, ItemStack stack, boolean matchDataComponents) {
        return ListFilterDefinition.INSTANCE.matches(stack, this.state, new FilterMatchContext(world));
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
            var capability = ItemAccess.forStack(filterItem).oneByOne().getCapability(Capabilities.Fluid.ITEM);
            if (capability == null)
                continue;

            this.filterFluids.add(FluidUtil.getStack(capability, 0).copyWithAmount(1));
        }
    }

    @Override
    public boolean isEmpty() {
        return this.filterItems.isEmpty();
    }

    private boolean testFilterFluidStack(FluidStack filterFluidStack, FluidStack stackToTest, boolean matchDataComponents) {
        if (matchDataComponents) {
            return FluidStack.matches(filterFluidStack, stackToTest);
        } else {
            return FluidStack.isSameFluid(filterFluidStack, stackToTest);
        }
    }

    private List<ItemStack> getFilterItems(ItemContainerContents entries) {
        var result = new ArrayList<ItemStack>();

        NonNullList<ItemStack> items = NonNullList.withSize(entries.getSlots(), ItemStack.EMPTY);
        entries.copyInto(items);
        for (ItemStack stackInSlot : items) {
            if (!stackInSlot.isEmpty()) {
                result.add(stackInSlot);
            }
        }

        return result;
    }

}
