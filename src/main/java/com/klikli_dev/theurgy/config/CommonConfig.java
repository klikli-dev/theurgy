// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {

    private static final CommonConfig instance = new CommonConfig();

    public final Misc misc;

    public final ModConfigSpec spec;

    private CommonConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        this.misc = new Misc(builder);
        this.spec = builder.build();
    }

    public static CommonConfig get() {
        return instance;
    }

    public static class Misc {

        public Misc(ModConfigSpec.Builder builder) {
            builder.comment("Misc Settings").push("misc");

            builder.pop();
        }
    }
}
