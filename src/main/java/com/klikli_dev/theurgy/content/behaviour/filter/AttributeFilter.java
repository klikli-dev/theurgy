// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterDefinition;
import com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterState;
import com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterStateAccessor;
import com.klikli_dev.codedefinedgui.filter.core.FilterMatchContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class AttributeFilter extends Filter {
    protected AttributeFilterState state;

    protected AttributeFilter(HolderLookup.Provider provider, ItemStack filter) {
        super(provider, filter);
    }

    @Override
    protected void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack) {
        this.state = AttributeFilterStateAccessor.INSTANCE.read(filterItemStack);
    }

    @Override
    public boolean test(Level level, ItemStack stack, boolean matchDataComponents) {
        return AttributeFilterDefinition.INSTANCE.matches(stack, this.state, new FilterMatchContext(level));
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
