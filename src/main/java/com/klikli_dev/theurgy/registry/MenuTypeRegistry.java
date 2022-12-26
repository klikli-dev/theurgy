/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.menu.CalcinationOvenMenu;
import com.klikli_dev.theurgy.recipe.CalcinationRecipe;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES, Theurgy.MODID);
    public static final RegistryObject<MenuType<CalcinationOvenMenu>> CALCINATION_OVEN = MENU_TYPES.register(
            "calcination_oven", () -> new MenuType<>(CalcinationOvenMenu::new));
}
