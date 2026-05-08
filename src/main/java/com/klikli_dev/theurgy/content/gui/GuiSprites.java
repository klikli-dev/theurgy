// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;

public final class GuiSprites {
    public static final GuiSprite JEI_INPUT_SLOT = new GuiSprite(Theurgy.loc("jei/input_slot"), 18, 18);
    public static final GuiSprite JEI_OUTPUT_SLOT = new GuiSprite(Theurgy.loc("jei/output_slot"), 26, 26);
    public static final GuiSprite JEI_FIRE_EMPTY = new GuiSprite(Theurgy.loc("jei/fire_empty"), 14, 14);
    public static final GuiSprite JEI_FIRE_FULL = new GuiSprite(Theurgy.loc("jei/fire_full"), 14, 14);
    public static final GuiSprite JEI_ARROW_RIGHT_EMPTY = new GuiSprite(Theurgy.loc("jei/arrow_right_empty"), 22, 16);
    public static final GuiSprite JEI_ARROW_RIGHT_FULL = new GuiSprite(Theurgy.loc("jei/arrow_right_full"), 22, 16);

    public static final GuiSprite MODONOMICON_ARROW_RIGHT = new GuiSprite(modonomicon("modonomicon/themes/default/content/pages/recipes/crafting_arrow"), 9, 9);
    public static final GuiSprite MODONOMICON_SLOT = new GuiSprite(modonomicon("modonomicon/themes/default/content/pages/recipes/crafting_slot"), 22, 22);

    private GuiSprites() {
    }

    private static Identifier modonomicon(String path) {
        return Identifier.fromNamespaceAndPath("modonomicon", path);
    }
}
