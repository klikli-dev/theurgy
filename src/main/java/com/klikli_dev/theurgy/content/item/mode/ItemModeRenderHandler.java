// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.phys.BlockHitResult;

public class ItemModeRenderHandler<T extends ItemMode> {

    protected final T mode;

    public ItemModeRenderHandler(T mode) {
        this.mode = mode;
    }


    public void renderBlockHighlight(BlockHitResult hitResult, PoseStack poseStack, SubmitNodeCollector bufferSource, Camera camera) {

    }
}
