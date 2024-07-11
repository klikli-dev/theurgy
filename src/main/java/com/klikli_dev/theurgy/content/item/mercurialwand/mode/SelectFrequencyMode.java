// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public class SelectFrequencyMode extends MercurialWandItemMode {

    private final Lazy<ItemModeRenderHandler<SelectFrequencyMode>> renderHandler;

    protected SelectFrequencyMode() {
        super();
        //We're using a lazy so that on the serverside we don't construct the client-only renderer UNLESS code actually calls it in which case we will crash which is fine as a warning.
        this.renderHandler = Lazy.of(() -> new ItemModeRenderHandler<>(this));
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_FREQUENCY;
    }

    @Override
    public MutableComponent description(ItemStack pStack, @Nullable Level pLevel) {
        var frequency = pStack.getOrDefault(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0);
        return Component.translatable(this.descriptionId(), Component.literal(frequency.toString()).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public ItemModeRenderHandler<?> renderHandler() {
        return this.renderHandler.get();
    }

    @Override
    public boolean onLeftClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        return this.onLeftClick(level, player, hand);
    }

    @Override
    public void onLeftClickEmpty(Level level, Player player, InteractionHand hand) {
        this.onLeftClick(level, player, hand);
    }

    protected boolean onLeftClick(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND)
            return false;

        if (level.isClientSide)
            return false;

        this.changeFrequency(player.getMainHandItem(), -1);
        return true;
    }

    protected void changeFrequency(ItemStack stack, int delta) {
        stack.update(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0, (frequency) -> frequency + delta);
    }


    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        this.changeFrequency(stack, 1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand != InteractionHand.MAIN_HAND)
            return super.use(pLevel, pPlayer, pUsedHand);

        if (pLevel.isClientSide)
            return super.use(pLevel, pPlayer, pUsedHand);

        this.changeFrequency(pPlayer.getItemInHand(pUsedHand), 1);

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
