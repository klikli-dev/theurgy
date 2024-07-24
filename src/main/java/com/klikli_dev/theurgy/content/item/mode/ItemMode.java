// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import com.klikli_dev.theurgy.content.render.itemhud.ItemHUDProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ItemMode implements ItemHUDProvider {

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    public boolean onLeftClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        return false;
    }

    public void onLeftClickEmpty(Level level, Player player, InteractionHand hand) {

    }

    public void onScrollWithRightDown(Player player, ItemStack stack, int shift) {

    }

    public boolean supportsScrollWithRightDown() {
        return false;
    }

    protected abstract String typeName();

    public abstract String descriptionId();

    @Override
    public int hashCode() {
        return this.typeName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemMode && ((ItemMode) obj).typeName().equals(this.typeName());
    }

    public MutableComponent description(ItemStack pStack, @Nullable Level pLevel) {
        return Component.translatable(this.descriptionId());
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        pTooltipComponents.add(this.description(pStack, pLevel));
    }

    public abstract ItemModeRenderHandler<?> renderHandler();
}
