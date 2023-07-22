/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    private static final CommonConfig instance = new CommonConfig();

    public final Misc misc;

    public final ForgeConfigSpec spec;

    private CommonConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        this.misc = new Misc(builder);
        this.spec = builder.build();
    }

    public static CommonConfig get() {
        return instance;
    }

    public static class Misc {

        public Misc(ForgeConfigSpec.Builder builder) {
            builder.comment("Misc Settings").push("misc");

            builder.pop();
        }
    }
}
