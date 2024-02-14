package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.network.messages.MessageSetMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemMode {


    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    public InteractionResult useOn(UseOnContext pContext) {
        return InteractionResult.PASS;
    }

    public void onScrollWithRightDown(Player player, ItemStack stack, int shift) {

    }
}
