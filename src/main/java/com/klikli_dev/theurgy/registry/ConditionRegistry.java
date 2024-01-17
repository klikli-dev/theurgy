// SPDX-FileCopyrightText: 2024 klikli_dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.condition.FluidTagEmptyCondition;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ConditionRegistry {
    public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, Theurgy.MODID);

    public static final DeferredHolder<Codec<? extends ICondition>, Codec<FluidTagEmptyCondition>> FLUID_TAG_EMPTY = CONDITION_SERIALIZERS.register("fluid_tag_empty", () -> FluidTagEmptyCondition.CODEC);
}
