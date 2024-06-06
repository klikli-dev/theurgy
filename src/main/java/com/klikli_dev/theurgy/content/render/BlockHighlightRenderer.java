/*
 * SPDX-FileCopyrightText: 2023 Aidan C. Brady
 * SPDX-FileCopyrightText: 2024 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

/**
 * Based on Mekanism RenderTickhandler#onBlockHover https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/RenderTickHandler.java#L347-L347
 */
public class BlockHighlightRenderer {

    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        BlockHitResult rayTraceResult = event.getTarget();
        if (rayTraceResult.getType() == BlockHitResult.Type.MISS)
            return;

        var stack = player.getMainHandItem();
        if (!stack.is(ItemRegistry.MERCURIAL_WAND.get()))
            return;

        var mode = stack.get(DataComponentRegistry.MERCURIAL_WAND_ITEM_MODE.get());
        if (mode == null)
            return;

        mode.renderHandler().renderBlockHighlight(event);
    }
}
