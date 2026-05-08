// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import net.minecraft.resources.Identifier;

public final class GuiSprites {
    public static final GuiSprite MODONOMICON_ARROW_RIGHT = new GuiSprite(modonomicon("modonomicon/themes/default/content/pages/recipes/crafting_arrow"), 9, 9);
    public static final GuiSprite MODONOMICON_SLOT = new GuiSprite(modonomicon("modonomicon/themes/default/content/pages/recipes/crafting_slot"), 22, 22);

    private GuiSprites() {
    }

    private static Identifier modonomicon(String path) {
        return Identifier.fromNamespaceAndPath("modonomicon", path);
    }
}
