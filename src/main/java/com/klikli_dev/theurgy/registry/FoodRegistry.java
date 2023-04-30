/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties ALCHEMICAL_SALT = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1), 0.5f).build();

}
