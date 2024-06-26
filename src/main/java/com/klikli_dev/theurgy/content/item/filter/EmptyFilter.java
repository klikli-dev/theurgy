package com.klikli_dev.theurgy.content.item.filter;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmptyFilter extends Filter {
    protected EmptyFilter() {
        super(ItemStack.EMPTY);
    }

    @Override
    protected void initFromFilterItemStack(ItemStack filterItemStack) {

    }

    @Override
    public boolean test(Level level, ItemStack stack, boolean matchDataComponents) {
        return true;
    }
}
