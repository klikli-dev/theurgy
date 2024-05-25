/*
 * SPDX-FileCopyrightText: 2023 Aidan C. Brady
 * SPDX-FileCopyrightText: 2024 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.util;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;


/**
 * Based on https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/lib/ScrollIncrementer.java
 */
public class ScrollHelper {

    private static long lastScrollTime = -1;
    private static double scrollDelta;

    private static long getTime() {
        ClientLevel level = Minecraft.getInstance().level;
        return level == null ? -1 : level.getGameTime();
    }

    public static int scroll(double delta) {
        long time = getTime();
        if (time - lastScrollTime > SharedConstants.TICKS_PER_SECOND) {
            scrollDelta = 0;
        }
        lastScrollTime = time;
        scrollDelta += delta;
        int shift = (int) scrollDelta;
        scrollDelta %= 1;

        shift = Mth.clamp(shift, -1, 1);

        return shift;
    }
}
