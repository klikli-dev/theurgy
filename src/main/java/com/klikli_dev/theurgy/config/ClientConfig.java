// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.config;

import com.klikli_dev.theurgy.content.render.HeldStackFitRenderMode;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

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
        public final BooleanValue enableInWorldHUD;
        public final BooleanValue enableHeldStackFitOutline;
        public final ModConfigSpec.EnumValue<HeldStackFitRenderMode> heldStackFitOutlineRenderMode;
        public final ModConfigSpec.DoubleValue itemHUDScale;
        public final IntValue wireLineWidth;
        public final BooleanValue useSimpleWireRenderer;

        public Rendering(ModConfigSpec.Builder builder) {
            builder.comment("Rendering Settings").push("rendering");

            this.renderSulfurSourceItem = builder
                    .comment("True to show the icon of the source item on top of the sulfur jar icon, false to hide it (source item will still be shown on hover in item name).",
                            "Disabling this setting may improve performance in inventories with lots of sulfur item stacks.")
                    .define("renderSulfurSourceItem", true);

            this.enableItemHUD = builder
                    .comment("True to enable the item HUD, false to disable it.")
                    .define("enableItemHUD", true);

            this.enableInWorldHUD = builder
                    .comment("True to enable the in-world HUD when looking at supported machine blocks, false to disable it.",
                            "Disable this if you use another overlay mod such as Jade and want it to provide the in-world block information instead.")
                    .define("enableInWorldHUD", true);

            this.enableHeldStackFitOutline = builder
                    .comment("True to enable held-stack fit outlines on supported apparatuses, false to disable them.")
                    .define("enableHeldStackFitOutline", true);

            this.heldStackFitOutlineRenderMode = builder
                    .comment("Controls whether held-stack fit outlines show only green for accepted items or also red for non-accepted items.")
                    .defineEnum("heldStackFitOutlineRenderMode", HeldStackFitRenderMode.GREEN_ONLY);

            this.itemHUDScale = builder
                    .comment("The scale of the Item HUD text (e.g. for the mercurial wand).")
                    .defineInRange("hudScale", 0.7, 0.25, 3);

            this.wireLineWidth = builder
                    .comment("The line width of wires rendered in-world. Higher values produce thicker wires.",
                            "Note: Line width rendering is platform-dependent and may be limited to 1 on some GPUs.")
                    .defineInRange("wireLineWidth", 2, 1, 64);

            this.useSimpleWireRenderer = builder
                    .comment("True to render logistics wires with RenderType.lines() instead of distanceLines().",
                            "If shaders are used, this type might allow wires to show when they otherwise do not render.")
                    .define("useSimpleWireRenderer", false);

            builder.pop();
        }
    }
}
