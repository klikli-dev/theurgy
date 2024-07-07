// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmptyFilter extends Filter {
    protected EmptyFilter() {
        super(RegistryAccess.EMPTY, ItemStack.EMPTY);
    }

    @Override
    protected void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack) {

    }

    @Override
    public boolean test(Level level, ItemStack stack, boolean matchDataComponents) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
