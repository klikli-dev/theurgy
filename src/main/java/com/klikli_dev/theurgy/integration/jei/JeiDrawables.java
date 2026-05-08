// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.content.gui.GuiSprite;
import com.klikli_dev.theurgy.content.gui.GuiSprites;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Helpts to get Jei Drawables for scenarios where we don't render stuff "raw" but instead pass it to JEI.
 * E.g. Slot Backgrounds
 */
public class JeiDrawables {
    public static final IDrawable INPUT_SLOT = asDrawable(GuiSprites.JEI_INPUT_SLOT);
    public static final IDrawable OUTPUT_SLOT = asDrawable(GuiSprites.JEI_OUTPUT_SLOT);

    protected static IDrawable asDrawable(GuiSprite sprite) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return sprite.width();
            }

            @Override
            public int getHeight() {
                return sprite.height();
            }

            @Override
            public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
                sprite.extractRenderState(guiGraphics, xOffset, yOffset);
            }
        };
    }

    public static IDrawableStatic asStaticDrawable(IGuiHelper helper, GuiSprite sprite) {
        return new IDrawableStatic() {
            @Override
            public int getWidth() {
                return sprite.width();
            }

            @Override
            public int getHeight() {
                return sprite.height();
            }

            @Override
            public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
                sprite.extractRenderState(guiGraphics, xOffset, yOffset);
            }

            @Override
            public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
                int width = Math.max(0, sprite.width() - maskLeft - maskRight);
                int height = Math.max(0, sprite.height() - maskTop - maskBottom);
                if (width > 0 && height > 0) {
                    sprite.extractRenderState(guiGraphics, xOffset + maskLeft, yOffset + maskTop, width, height);
                }
            }
        };
    }

    public static IDrawableAnimated asAnimatedDrawable(IGuiHelper helper, GuiSprite sprite, int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        return helper.createAnimatedDrawable(asStaticDrawable(helper, sprite), ticksPerCycle, startDirection, inverted);
    }
}
