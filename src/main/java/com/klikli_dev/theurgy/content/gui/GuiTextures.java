// SPDX-FileCopyrightText: 2019 simibubi
// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Based on com.simibubi.create.foundation.gui.AllGuiTextures from Create
 */
public enum GuiTextures implements ScreenElement {
    JEI_INPUT_SLOT("jei/recipe_gui", 0, 0, 18, 18),
    JEI_OUTPUT_SLOT("jei/recipe_gui", 18, 0, 26, 26),

    JEI_FIRE_EMPTY("jei/recipe_gui", 0, 42, 14, 14),
    JEI_FIRE_FULL("jei/recipe_gui", 14, 42, 14, 14),
    JEI_ARROW_RIGHT_EMPTY("jei/recipe_gui", 0, 26, 22, 16),
    JEI_ARROW_RIGHT_FULL("jei/recipe_gui", 22, 26, 22, 16),

    MODONOMICON_ARROW_RIGHT(ResourceLocation.fromNamespaceAndPath("modonomicon", "textures/gui/crafting_textures.png"), 71, 205, 9, 9, 128, 256),
    MODONOMICON_SLOT(ResourceLocation.fromNamespaceAndPath("modonomicon", "textures/gui/crafting_textures.png"), 84, 198, 22, 22, 128, 256),

    BUTTON("widgets", 18, 18),
    BUTTON_HOVER("widgets", 18, 0, 18, 18),
    BUTTON_DOWN("widgets", 36, 0, 18, 18),

    INDICATOR("widgets", 0, 18, 18, 6),
    INDICATOR_WHITE("widgets", 18, 18, 18, 6),
    INDICATOR_GREEN("widgets", 36, 18, 18, 6),
    INDICATOR_YELLOW("widgets", 54, 18, 18, 6),
    INDICATOR_RED("widgets", 72, 18, 18, 6),

    PLAYER_INVENTORY("player_inventory", 176, 108),

    FILTER("filters", 214, 99);

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

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.enableBlend();
        guiGraphics.blit(this.location, x, y, 0, this.x, this.y, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
