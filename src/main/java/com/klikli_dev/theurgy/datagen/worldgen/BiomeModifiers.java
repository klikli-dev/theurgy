// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.ForgeRegistries;


public class BiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_SAL_AMMONIAC_ORE = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Theurgy.loc("add_sal_ammoniac_ore"));


    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_SAL_AMMONIAC_ORE, new net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.SAL_AMMONIAC_ORE)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }
}
