// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.SideMode;
import com.klikli_dev.theurgy.content.item.mode.SideModeSetter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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

public class CycleCapacitorSideMode extends MercurialWandItemMode {

    private final Lazy<CycleCapacitorSideModeRenderHandler> renderHandler;

    protected CycleCapacitorSideMode() {
        super();
        this.renderHandler = Lazy.of(() -> new CycleCapacitorSideModeRenderHandler(this));
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_CAPACITOR_SIDE;
    }

    @Override
    public CycleCapacitorSideModeRenderHandler renderHandler() {
        return this.renderHandler.get();
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        var description = this.description(pStack, pLevel);
        if (pHitResult instanceof BlockHitResult blockHitResult && pLevel != null) {
            var blockEntity = pLevel.getBlockEntity(blockHitResult.getBlockPos());
            if (blockEntity instanceof SideModeSetter sideModeSetter) {
                var side = blockHitResult.getDirection();
                var currentMode = sideModeSetter.getSideMode(side);
                var newMode = this.nextMode(currentMode);

                var component = Component.translatable(
                        TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_CAPACITOR_SIDE_WITH_TARGET,
                        Component.translatable("theurgy.side." + side.getName()).withStyle(ChatFormatting.AQUA),
                        Component.translatable("theurgy.side_mode." + currentMode.name().toLowerCase()).withStyle(currentMode != newMode ? ChatFormatting.YELLOW : ChatFormatting.GREEN),
                        Component.translatable("theurgy.side_mode." + newMode.name().toLowerCase()).withStyle(ChatFormatting.GREEN)
                );

                description = component;
            }
        }
        pTooltipComponents.add(description);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var blockPos = context.getClickedPos();
        var level = context.getLevel();

        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SideModeSetter sideModeSetter) {
            if (!level.isClientSide()) {
                var side = context.getClickedFace();
                var newMode = sideModeSetter.cycleSideMode(side);

                context.getPlayer().sendOverlayMessage(Component.translatable(
                        TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_CAPACITOR_SIDE_SUCCESS,
                        Component.translatable("theurgy.side." + side.getName()).withStyle(ChatFormatting.AQUA),
                        Component.translatable("theurgy.side_mode." + newMode.name().toLowerCase()).withStyle(ChatFormatting.GREEN)
                ));
            }
            return InteractionResult.SUCCESS;
        }

        return super.onItemUseFirst(stack, context);
    }

    protected SideMode nextMode(SideMode mode) {
        return switch (mode) {
            case NONE -> SideMode.INPUT;
            case INPUT -> SideMode.OUTPUT;
            case OUTPUT -> SideMode.BOTH;
            case BOTH -> SideMode.NONE;
        };
    }
}