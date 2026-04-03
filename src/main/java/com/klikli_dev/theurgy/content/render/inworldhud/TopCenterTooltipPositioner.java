// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class TopCenterTooltipPositioner implements ClientTooltipPositioner {

    public static final TopCenterTooltipPositioner INSTANCE = new TopCenterTooltipPositioner();

    private TopCenterTooltipPositioner() {
    }

    @Override
    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        int minX = 4;
        int maxX = Math.max(minX, screenWidth - tooltipWidth - 4);
        int x = Math.max(minX, Math.min((screenWidth - tooltipWidth) / 2, maxX));

        int minY = 4;
        int maxY = Math.max(minY, screenHeight - tooltipHeight - 3);
        int y = Math.max(minY, Math.min(mouseY, maxY));

        return new Vector2i(x, y);
    }
}
