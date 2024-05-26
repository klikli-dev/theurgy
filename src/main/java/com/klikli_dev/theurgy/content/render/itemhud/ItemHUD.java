/*
 * SPDX-FileCopyrightText: 2023 Aidan C. Brady
 * SPDX-FileCopyrightText: 2024 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.render.itemhud;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

/**
 * Based on https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/HUDRenderer.java
 */
public class ItemHUD implements LayeredDraw.Layer {

    private static final ItemHUD instance = new ItemHUD();

    public static ItemHUD get() {
        return instance;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float pPartialTick) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator() || !ClientConfig.get().rendering.enableItemHUD.get())
            return;

        var stack = minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof ItemHUDProvider itemHUDProvider))
            return;

        var hudTexts = new ArrayList<Component>();
        itemHUDProvider.appendHUDText(minecraft.player, minecraft.hitResult, stack, minecraft.level, hudTexts);

        if (hudTexts.isEmpty())
            return;

        Font font = minecraft.font;

        float hudScale = ClientConfig.get().rendering.itemHUDScale.get().floatValue();

        int yScale = (int) (guiGraphics.guiHeight() / hudScale);
        int start = 2 + hudTexts.size() * 9; //where we start rendering at the bottom of the screen
        int y = yScale - start;

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(hudScale, hudScale, hudScale);

        for (Component text : hudTexts) {
            int x = 2;
            guiGraphics.drawString(font, text, x, y, 0xFFC8C8C8);
            y += 9;
        }

        pose.popPose();
    }
}
