package com.klikli_dev.theurgy.menu;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CalcinationOvenFuelSlot extends SlotItemHandler {

    CalcinationOvenMenu menu;
    public CalcinationOvenFuelSlot(CalcinationOvenMenu menu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return this.menu.isFuel(pStack);
    }
}
