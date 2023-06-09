/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Helpts to get Jei Drawables for scenarios where we don't render stuff "raw" but instead pass it to JEI.
 * E.g. Slot Backgrounds
 */
public class JeiDrawables {
    public static final IDrawable INPUT_SLOT = asDrawable(GuiTextures.JEI_INPUT_SLOT);
    public static final IDrawable OUTPUT_SLOT = asDrawable(GuiTextures.JEI_OUTPUT_SLOT);

    protected static IDrawable asDrawable(GuiTextures texture) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return texture.width;
            }

            @Override
            public int getHeight() {
                return texture.height;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                texture.render(guiGraphics, xOffset, yOffset);
            }
        };
    }

    public static IDrawableStatic asStaticDrawable(IGuiHelper helper, GuiTextures texture) {
        return helper.createDrawable(texture.location, texture.x, texture.y, texture.width, texture.height);
    }

    public static IDrawableAnimated asAnimatedDrawable(IGuiHelper helper, GuiTextures texture, int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        return helper.createAnimatedDrawable(asStaticDrawable(helper, texture), ticksPerCycle, startDirection, inverted);
    }
}
