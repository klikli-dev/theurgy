/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.menu;

import com.klikli_dev.theurgy.blockentity.SteamDistillerBaseBlockEntity;
import com.klikli_dev.theurgy.registry.MenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SteamDistillerBaseMenu extends AbstractContainerMenu {

    public SteamDistillerBaseBlockEntity steamDistillerBase;
    public Inventory playerInventory;

    public SteamDistillerBaseMenu(int id, Inventory playerInventory,
                                  SteamDistillerBaseBlockEntity steamDistillerBase) {
        super(MenuRegistry.STEAM_DISTILLER_BASE.get(), id);
        this.playerInventory = playerInventory;
        this.steamDistillerBase = steamDistillerBase;

        this.setupBlockEntityInventory();
        this.setupPlayerInventorySlots(playerInventory.player);
        this.setupPlayerHotbar(playerInventory.player);
    }

    protected void setupPlayerInventorySlots(Player player) {
        int playerInventoryTop = 84;
        int playerInventoryLeft = 8;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, playerInventoryLeft + j * 18,
                        playerInventoryTop + i * 18));
    }

    protected void setupPlayerHotbar(Player player) {
        int hotbarTop = 142;
        int hotbarLeft = 8;
        for (int i = 0; i < 9; i++)
            this.addSlot(new Slot(player.getInventory(), i, hotbarLeft + i * 18, hotbarTop));
    }

    protected void setupBlockEntityInventory() {
        int outputGridTop = 17;
        int outputGridLeft = 98;
        int index = 0;

//        IItemHandler outputHandler = this.steamDistillerBase.outputHandler.orElseThrow(ItemHandlerMissingException::new);
//        IItemHandler inputHandler = this.steamDistillerBase.inputHandler.orElseThrow(ItemHandlerMissingException::new);
//
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 3; ++j) {
//                this.addSlot(
//                        new OutputSlot(outputHandler, index++, outputGridLeft + j * 18, outputGridTop + i * 18));
//            }
//        }

//        this.addSlot(new InputSlot(inputHandler, 0, 26, 35));
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
//            if (index < this.outputHandler.getSlots()) {
//                //+1 because we have the input handler slot after the output hander slots
//                if (!this.moveItemStackTo(itemstack1, this.outputHandler.getSlots() + 1, this.slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//            }
//            //input handler slot is exactly at last output handler slot + 1
//            else if (index == this.outputHandler.getSlots()) {
//                if (!this.moveItemStackTo(itemstack1, this.outputHandler.getSlots() + 1, this.slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//            }
//            //+1 because we are actually only interested in inserting in the input handler. Could even start at the end index instead of 0.
//            else if (!this.moveItemStackTo(itemstack1, 0, this.outputHandler.getSlots() + 1, false)) {
//                return ItemStack.EMPTY;
//            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(Vec3.atCenterOf(this.steamDistillerBase.getBlockPos())) <= 64.0;
    }

    //    public class InputSlot extends SlotItemHandler {
//
//        public InputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
//            super(itemHandler, index, xPosition, yPosition);
//        }
//
//        public boolean mayPlace(ItemStack stack) {
//
//        }
//    }
}
