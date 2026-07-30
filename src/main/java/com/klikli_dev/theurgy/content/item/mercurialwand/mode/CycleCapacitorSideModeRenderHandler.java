// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.content.item.mode.SideModeSetter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class CycleCapacitorSideModeRenderHandler extends ItemModeRenderHandler<CycleCapacitorSideMode> {

    public CycleCapacitorSideModeRenderHandler(CycleCapacitorSideMode mode) {
        super(mode);
    }

    @Override
    public void renderBlockHighlight(BlockHitResult rayTraceResult, PoseStack ps, SubmitNodeCollector bufferSource, Camera camera) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        if (rayTraceResult.getType() != BlockHitResult.Type.BLOCK)
            return;

        var blockEntity = player.level().getBlockEntity(rayTraceResult.getBlockPos());
        if (blockEntity instanceof SideModeSetter) {
            // For now, highlight the face that will be changed
            // Could add more sophisticated rendering later
            // For now, no visual highlight - just HUD text is enough
        }
    }
}