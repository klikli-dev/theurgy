// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand;

import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.SelectDirectionMode;
import com.klikli_dev.theurgy.content.item.mode.ModeItem;
import com.klikli_dev.theurgy.content.render.itemhud.ItemHUDProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MercurialWandItem extends Item implements ItemHUDProvider, ModeItem {
    public MercurialWandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void changeMode(Player player, ItemStack stack, int shift) {
        var nextMode = MercurialWandItemMode.getMode(stack).type().next().mode();
        MercurialWandItemMode.setMode(stack, nextMode);

        player.displayClientMessage(Component.translatable(nextMode.descriptionId()), true);
    }

    @Override
    public void onScroll(Player player, ItemStack stack, int shift) {
        if (MercurialWandItemMode.getMode(stack).supportsScrollWithRightDown() && DistHelper.isRightPressed()) {
            //if right mouse button is pressed AND we are in a mode that supports this we perform a sub-scroll.
            MercurialWandItemMode.getMode(stack).onScrollWithRightDown(player, stack, shift);
        } else {
            //otherwise we change the mode
            ModeItem.super.onScroll(player, stack, shift);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stack = pPlayer.getItemInHand(pUsedHand);
        var mode = MercurialWandItemMode.getMode(stack);

        if(mode instanceof SelectDirectionMode) {
            //TODO without a block target this mode should probably not do anything
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //onItemUseFirst is called BEFORE the block so we can interrupt e.g. opening the block inv
        var mode = MercurialWandItemMode.getMode(stack);

        if(mode instanceof SelectDirectionMode) {
            return InteractionResult.CONSUME; //we need to consume because SUCCESS causes a swing animation
        }

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHUDText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        var mode = MercurialWandItemMode.getMode(pStack);
        pTooltipComponents.add(Component.translatable(mode.descriptionId()));
    }

    public static class DistHelper {

        public static boolean isRightPressed() {
            return Minecraft.getInstance().mouseHandler.isRightPressed();
        }
    }
}
