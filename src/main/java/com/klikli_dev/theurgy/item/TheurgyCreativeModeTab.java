/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TheurgyCreativeModeTab extends CreativeModeTab {
    public TheurgyCreativeModeTab() {
        super(Theurgy.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.EMPTY_JAR.get());
    }
}
