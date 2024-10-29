// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Lazy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerConfig {

    private static final ServerConfig instance = new ServerConfig();
    public final Recipes recipes;
    public final TooltipHandler tooltipHandler;
    public final ForgeConfigSpec spec;

    private ServerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        this.recipes = new Recipes(builder);
        this.tooltipHandler = new TooltipHandler(builder);
        this.spec = builder.build();
    }

    public static ServerConfig get() {
        return instance;
    }

    public static class TooltipHandler {
        public ForgeConfigSpec.ConfigValue<List<? extends String>> additionalTooltipHandlerNamespaces;

        public TooltipHandler(ForgeConfigSpec.Builder builder) {
            builder.comment("Tooltip Handler Settings").push("tooltipHandler");

            this.additionalTooltipHandlerNamespaces = builder
                    .comment(
                            "Theurgy automatically adds tooltips for sulfur items.",
                            "When adding a sulfur that is not in the 'theurgy' or 'kubejs' namespace, the namespace needs to be added to this list in order for the tooltip to show.",
                            "Format is: [\"<my_mod_namespace>\", \"<my_modpack_namespace>\", ...]"
                    )
                    .defineList("additionalTooltipHandlerNamespaces", List.of(), e -> true);

            builder.pop();
        }
    }

    public static class Recipes {

        protected ForgeConfigSpec.ConfigValue<List<? extends String>> sulfurSourceToBlockMappingList;

        public final Lazy<Map<String, String>> sulfurSourceToBlockMapping = Lazy.of(() -> this.sulfurSourceToBlockMappingList.get().stream()
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(s -> s[0], s -> s[1])));


        public Recipes(ForgeConfigSpec.Builder builder) {
            builder.comment("Recipe Settings").push("recipes");

            this.sulfurSourceToBlockMappingList = builder
                    .comment(
                            "A mapping of sulfur source to origin block. The key is the sulfur source, the value is the block.",
                            "This is used by divination rod recipes to determine which (ore-)block to scan for, if e.g. a raw metal or ingot is used to craft the sulfur used in the rod. This also works for tags, prefixed with #.",
                            "Format is: [\"source=block\", \"#sourcetag=#blocktag\", ...]"
                    )
                    .defineList("sulfurSourceToBlockMapping", List.of(), e -> ((String) e).contains("="));

            builder.pop();
        }
    }
}
