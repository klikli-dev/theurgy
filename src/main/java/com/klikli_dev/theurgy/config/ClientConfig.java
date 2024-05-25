// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class ClientConfig {

    private static final ClientConfig instance = new ClientConfig();

    public final Rendering rendering;

    public final ModConfigSpec spec;

    private ClientConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        this.rendering = new Rendering(builder);
        this.spec = builder.build();
    }

    public static ClientConfig get() {
        return instance;
    }

    public static class Rendering {

        public final BooleanValue renderSulfurSourceItem;
        public final BooleanValue enableItemHUD;
        public final ModConfigSpec.DoubleValue itemHUDScale;

        public Rendering(ModConfigSpec.Builder builder) {
            builder.comment("Rendering Settings").push("rendering");

            this.renderSulfurSourceItem = builder
                    .comment("True to show the icon of the source item on top of the sulfur jar icon, false to hide it (source item will still be shown on hover in item name).",
                            "Disabling this setting may improve performance in inventories with lots of sulfur item stacks.")
                    .define("renderSulfurSourceItem", true);

            this.enableItemHUD = builder
                    .comment("True to enable the item HUD, false to disable it.")
                    .define("enableItemHUD", true);

            this.itemHUDScale = builder
                    .comment("The scale of the Item HUD text (e.g. for the mercurial wand).")
                    .defineInRange("hudScale", 0.7, 0.25, 3);

            builder.pop();
        }
    }
}
