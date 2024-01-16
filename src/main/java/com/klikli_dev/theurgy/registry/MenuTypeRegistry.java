// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES, Theurgy.MODID);
}
