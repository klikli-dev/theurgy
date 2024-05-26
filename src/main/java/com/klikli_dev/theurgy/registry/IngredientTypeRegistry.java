// SPDX-FileCopyrightText: 2024 klikli_dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IngredientTypeRegistry {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, Theurgy.MODID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<FluidIngredient>> FLUID_INGREDIENT = INGREDIENT_TYPES.register("fluid", () -> new IngredientType<>(FluidIngredient.CODEC, FluidIngredient.CODEC_NONEMPTY));
}
