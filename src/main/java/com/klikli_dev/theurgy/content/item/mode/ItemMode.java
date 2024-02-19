package com.klikli_dev.theurgy.content.item.mode;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public abstract class ItemMode {

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    public void onScrollWithRightDown(Player player, ItemStack stack, int shift) {

    }

    public boolean supportsScrollWithRightDown() {
        return false;
    }

    protected abstract String typeName();

    public abstract String descriptionId();

    protected CompoundTag getModeTag(ItemStack stack) {
        //an item will end up with one tag per mode type.
        return stack.getOrCreateTagElement("theurgy:mode." + this.typeName());
    }
}
