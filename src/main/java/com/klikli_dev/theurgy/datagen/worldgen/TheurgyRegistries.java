// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class TheurgyRegistries {
    public static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
            .add(Registries.BIOME, TheurgyRegistries::bootstrapBiomes)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap);

    public static void bootstrapBiomes(BootstapContext<Biome> context) {
        //doesn't need to do anything, just gives us access to a biome registry with empty tag lookup in our other boopstrap contexts
    }
}
