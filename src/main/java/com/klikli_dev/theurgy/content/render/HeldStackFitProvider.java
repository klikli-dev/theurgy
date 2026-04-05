// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import net.minecraft.world.item.ItemStack;

public interface HeldStackFitProvider {
    boolean heldStackFits(ItemStack stack);
}
