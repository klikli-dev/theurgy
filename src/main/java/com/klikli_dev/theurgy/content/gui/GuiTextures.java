/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Based on com.simibubi.create.foundation.gui.AllGuiTextures from Create
 */
public enum GuiTextures {
    JEI_INPUT_SLOT("jei/recipe_gui", 0, 0, 18, 18),
    JEI_OUTPUT_SLOT("jei/recipe_gui", 18, 0, 26, 26),

    JEI_FIRE_EMPTY("jei/recipe_gui", 0, 42, 14, 14),
    JEI_FIRE_FULL("jei/recipe_gui", 14, 42, 14, 14),
    JEI_ARROW_RIGHT_EMPTY("jei/recipe_gui", 0, 26, 22, 16),
    JEI_ARROW_RIGHT_FULL("jei/recipe_gui", 22, 26, 22, 16),

    MODONOMICON_ARROW_RIGHT(new ResourceLocation("modonomicon", "textures/gui/crafting_textures.png"), 71, 205, 9, 9, 128, 256),
    MODONOMICON_SLOT(new ResourceLocation("modonomicon", "textures/gui/crafting_textures.png"), 84, 198, 22, 22, 128, 256);

    public final ResourceLocation location;
    public final int textureHeight;
    public final int textureWidth;
    public final int width;
    public final int height;
    public final int x;
    public final int y;

    GuiTextures(String name, int width, int height) {
        this(name, 0, 0, width, height);
    }

    GuiTextures(String name, int x, int y, int width, int height) {
        this(Theurgy.loc("textures/gui/" + name + ".png"), x, y, width, height, 256, 256);
    }

    GuiTextures(ResourceLocation location, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        this.location = location;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.enableBlend();
        //seems tex width and height are switched here, but that is just a misnomer in the mapping.
        //I think pre 1.19.4 it was actually switched, but now when providing the size corresponding to the mapping name it is distorted - so we switch it back.
        guiGraphics.blit(this.location, x, y, 0, this.x, this.y, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
