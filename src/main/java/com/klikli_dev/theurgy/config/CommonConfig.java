// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.config;

import net.neoforged.neoforge.common.NeoForgeConfigSpec;

public class CommonConfig {

    private static final CommonConfig instance = new CommonConfig();

    public final Misc misc;

    public final NeoForgeConfigSpec spec;

    private CommonConfig() {
        NeoForgeConfigSpec.Builder builder = new NeoForgeConfigSpec.Builder();
        this.misc = new Misc(builder);
        this.spec = builder.build();
    }

    public static CommonConfig get() {
        return instance;
    }

    public static class Misc {

        public Misc(NeoForgeConfigSpec.Builder builder) {
            builder.comment("Misc Settings").push("misc");

            builder.pop();
        }
    }
}
