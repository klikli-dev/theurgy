package com.klikli_dev.theurgy.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CalcinationOvenOutputSlot extends SlotItemHandler {

    private final Player player;
    private int removeCount;

    public CalcinationOvenOutputSlot(Player player, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }


    public boolean mayPlace(ItemStack pStack) {
        return false; //output only slot
    }

    public ItemStack remove(int pAmount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(pAmount, this.getItem().getCount());
        }

        return super.remove(pAmount);
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        super.onTake(pPlayer, pStack);
    }


    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    protected void checkTakeAchievements(ItemStack pStack) {
        pStack.onCraftedBy(this.player.level, this.player, this.removeCount);

        //TODO: handle XP and achievements?
//        if (this.player instanceof ServerPlayer && this.container instanceof AbstractFurnaceBlockEntity) {
//            ((AbstractFurnaceBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
//        }
//        net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, pStack);

        this.removeCount = 0;
    }
}
