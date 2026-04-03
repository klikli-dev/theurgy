// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ClientItemStacksTooltip implements ClientTooltipComponent {

    private static final int SLOT_SIZE = 18;
    private static final int HEIGHT = 20;

    private final List<ItemStack> items;

    public ClientItemStacksTooltip(ItemStacksTooltip tooltip) {
        this.items = tooltip.items();
    }

    @Override
    public int getHeight(Font font) {
        return this.items.isEmpty() ? 0 : HEIGHT;
    }

    @Override
    public int getWidth(Font font) {
        return this.items.size() * SLOT_SIZE;
    }

    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor guiGraphics) {
        for (int i = 0; i < this.items.size(); i++) {
            ItemStack stack = this.items.get(i);
            int itemX = x + i * SLOT_SIZE;
            guiGraphics.item(stack, itemX, y + 1);
            guiGraphics.itemDecorations(font, stack, itemX, y + 1);
        }
    }
}
