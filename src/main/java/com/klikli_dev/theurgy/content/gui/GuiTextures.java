/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.Theurgy;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
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
    JEI_ARROW_RIGHT_FULL("jei/recipe_gui", 22, 26, 22, 16);

    public final ResourceLocation location;
    public final int width;
    public final int height;
    public final int x;
    public final int y;

    GuiTextures(String name, int width, int height) {
        this(name, 0, 0, width, height);
    }

    GuiTextures(String name, int x, int y, int width, int height) {
        this(Theurgy.loc("textures/gui/" + name + ".png"), x, y, width, height);
    }

    GuiTextures(ResourceLocation location, int x, int y, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, this.location);
    }

    public void render(PoseStack ms, int x, int y) {
        this.bind();
        GuiComponent.blit(ms, x, y, 0, this.x, this.y, this.width, this.height, 256, 256);
    }

    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        this.bind();
        component.blit(ms, x, y, this.x, this.y, this.width, this.height);
    }
}
