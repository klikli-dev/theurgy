// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.content.render.itemhud.ItemHUDProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MercurialWandItem extends Item implements ItemHUDProvider, ModeItem {
    public MercurialWandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void changeMode(Player player, ItemStack stack, int shift) {
        //TODO: when we change mode we should run  player.displayClientMessage() to show the new mode above the hotbar

        player.displayClientMessage(Component.translatable("test %s", shift), true);
    }

    @Override
    public void onScroll(Player player, ItemStack stack, int shift) {

        if(!DistHelper.isRightPressed()){
            ModeItem.super.onScroll(player, stack, shift); //handle mode change
            return;
        }
        //if right mouse button is pressed AND we are in a mode that supports this we perform a sub-scroll.
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //TODO: get mode, mode should be a neo attachment
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }

    @Override
    public void appendHUDText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        pTooltipComponents.add(Component.translatable("test"));
    }

    public static class DistHelper{

        public static boolean isRightPressed(){
            return Minecraft.getInstance().mouseHandler.isRightPressed();
        }
    }
}
