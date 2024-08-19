// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand;

import com.klikli_dev.theurgy.content.item.HandlesOnLeftClick;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.mode.ModeItem;
import com.klikli_dev.theurgy.content.render.itemhud.ItemHUDProvider;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MercurialWandItem extends Item implements ItemHUDProvider, ModeItem, HandlesOnLeftClick {
    public MercurialWandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void changeMode(Player player, ItemStack stack, int shift) {
        var nextMode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get()).type().shift(shift).mode();
        stack.set(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get(), nextMode);

        player.displayClientMessage(nextMode.description(stack, player.level()), true);
    }

    @Override
    public void onScroll(Player player, ItemStack stack, int shift) {
        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());
        if (mode.supportsScrollWithRightDown() && DistHelper.isRightPressed()) {
            //if right mouse button is pressed AND we are in a mode that supports this we perform a sub-scroll.
            mode.onScrollWithRightDown(player, stack, shift);
        } else {
            //otherwise we change the mode
            ModeItem.super.onScroll(player, stack, shift);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stack = pPlayer.getItemInHand(pUsedHand);
        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());

        var result = mode.use(pLevel, pPlayer, pUsedHand);
        if (result.getResult() != InteractionResult.PASS) {
            return result;
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //onItemUseFirst is called BEFORE the block so we can interrupt e.g. opening the block inv
        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());

        var result = mode.onItemUseFirst(stack, context);
        if (result != InteractionResult.PASS) {
            return result;
        }

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        pStack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get()).appendHUDText(pPlayer, pHitResult, pStack, pLevel, pTooltipComponents);
    }

    @Override
    public boolean onLeftClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        var stack = player.getItemInHand(hand);
        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());

        return mode.onLeftClickBlock(level, player, hand, pos, direction);
    }

    @Override
    public void onLeftClickEmpty(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());

        mode.onLeftClickEmpty(level, player, hand);
    }

    public static class DistHelper {

        public static boolean isRightPressed() {
            return Minecraft.getInstance().mouseHandler.isRightPressed();
        }
    }
}
