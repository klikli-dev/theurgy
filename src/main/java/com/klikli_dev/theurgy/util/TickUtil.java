// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.client.Minecraft;

public class TickUtil {
    public static float getPartialTicks() {
        Minecraft mc = Minecraft.getInstance();
        return (mc.isPaused() ? mc.pausePartialTick : mc.getFrameTime());
    }
}
