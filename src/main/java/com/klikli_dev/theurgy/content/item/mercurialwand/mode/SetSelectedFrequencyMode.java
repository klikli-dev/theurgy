// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.FrequencySetter;
import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetSelectedFrequencyMode extends MercurialWandItemMode {

    private final Lazy<ItemModeRenderHandler<SetSelectedFrequencyMode>> renderHandler;

    protected SetSelectedFrequencyMode() {
        super();
        //We're using a lazy so that on the serverside we don't construct the client-only renderer UNLESS code actually calls it in which case we will crash which is fine as a warning.
        this.renderHandler = Lazy.of(() -> new ItemModeRenderHandler<>(this));
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_FREQUENCY;
    }

    @Override
    public MutableComponent description(ItemStack pStack, @Nullable Level pLevel) {
        return Component.translatable(this.descriptionId(),
                Component.literal(String.valueOf(pStack.getOrDefault(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0))).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        var description = this.description(pStack, pLevel);
        if (pHitResult instanceof BlockHitResult blockHitResult) {
            var blockEntity = pLevel.getBlockEntity(blockHitResult.getBlockPos());
            if (blockEntity instanceof FrequencySetter frequencySettable) {
                var currentFrequency = frequencySettable.frequency();

                var newFrequency = pStack.getOrDefault(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0);

                var component = Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION_WITH_TARGET,
                        Component.literal(String.valueOf(currentFrequency)).withStyle(currentFrequency != newFrequency ? ChatFormatting.YELLOW : ChatFormatting.GREEN),
                        Component.literal(String.valueOf(newFrequency)).withStyle(ChatFormatting.GREEN)
                );

                description = component;
            }
        }
        pTooltipComponents.add(description);
    }

    @Override
    public ItemModeRenderHandler<SetSelectedFrequencyMode> renderHandler() {
        return this.renderHandler.get();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //get the target block and move its direction to the next one

        var blockPos = context.getClickedPos();
        var level = context.getLevel();

        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof FrequencySetter frequencySettable) {
            if (!level.isClientSide) {
                var currentFrequency = frequencySettable.frequency();
                var newFrequency = stack.getOrDefault(DataComponentRegistry.SELECTED_FREQUENCY.get(), 0);
                frequencySettable.frequency(newFrequency);

                context.getPlayer().displayClientMessage(Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_FREQUENCY_SUCCESS,
                        Component.literal(String.valueOf(newFrequency)).withStyle(ChatFormatting.GREEN)
                ), true);
            }
            return InteractionResult.SUCCESS;
        }

        return super.onItemUseFirst(stack, context);
    }
}
