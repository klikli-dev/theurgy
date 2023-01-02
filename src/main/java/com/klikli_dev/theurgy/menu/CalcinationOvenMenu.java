package com.klikli_dev.theurgy.menu;

import com.klikli_dev.theurgy.registry.MenuTypeRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CalcinationOvenMenu extends AbstractContainerMenu {

    Player player;
    IItemHandlerModifiable playerInventory;
    IItemHandlerModifiable calcinationOvenInventory;
    ContainerData dataAccess;

    /**
     * Constructor for MenuType
     */
    public CalcinationOvenMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new ItemStackHandler(3), new SimpleContainerData(4));

    }

    /**
     * Constructor for createMenu
     */
    public CalcinationOvenMenu(int pContainerId, Inventory pPlayerInventory, IItemHandlerModifiable calcinationOvenInventory, ContainerData dataAccess) {
        super(MenuTypeRegistry.CALCINATION_OVEN.get(), pContainerId);

        this.player = pPlayerInventory.player;
        this.playerInventory = new InvWrapper(pPlayerInventory);
        this.calcinationOvenInventory = calcinationOvenInventory;
        this.dataAccess = dataAccess;

        this.addSlot(new SlotItemHandler(calcinationOvenInventory, 0, 56, 17));
        this.addSlot(new CalcinationOvenFuelSlot(this, calcinationOvenInventory, 1, 56, 53));
        this.addSlot(new CalcinationOvenOutputSlot(pPlayerInventory.player, calcinationOvenInventory, 2, 116, 35));

        this.addPlayerInventorySlots(10, 70);

        this.addDataSlots(this.dataAccess);
    }

    protected boolean isFuel(ItemStack pStack) {
        return ForgeHooks.getBurnTime(pStack, RecipeTypeRegistry.CALCINATION.get()) > 0;
    }

    protected boolean canCraft(ItemStack pStack) {
        return this.player.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CALCINATION.get(), new SimpleContainer(pStack), this.player.level).isPresent();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack newStackInSlot = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            newStackInSlot = stackInSlot.copy();
            if (slotIndex == 2) {
                if (!this.moveItemStackTo(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, newStackInSlot);
            } else if (slotIndex != 1 && slotIndex != 0) {
                if (this.canCraft(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 3 && slotIndex < 30) {
                    if (!this.moveItemStackTo(stackInSlot, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 30 && slotIndex < 39 && !this.moveItemStackTo(stackInSlot, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == newStackInSlot.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return newStackInSlot;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        //TODO: stillValid
        return false;
    }

    public int getCalcinationProgress() {
        int i = this.dataAccess.get(2);
        int j = this.dataAccess.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitDuration() {
        int i = this.dataAccess.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.dataAccess.get(0) * 13 / i;
    }

    public boolean isLit() {
        return this.dataAccess.get(0) > 0;
    }

    private int addSlotRow(IItemHandler handler, int index, int x, int y, int columns, int dx) {
        for (int i = 0; i < columns; i++) {
            this.addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotTable(IItemHandler handler, int index, int x, int y, int columns, int dx, int rows, int dy) {
        for (int j = 0; j < rows; j++) {
            index = this.addSlotRow(handler, index, x, y, columns, dx);
            y += dy;
        }
        return index;
    }

    private void addPlayerInventorySlots(int leftCol, int topRow) {
        this.addSlotTable(this.playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        this.addSlotRow(this.playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
