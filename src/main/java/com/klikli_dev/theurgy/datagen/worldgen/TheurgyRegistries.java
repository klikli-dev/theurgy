// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import com.klikli_dev.theurgy.datagen.advancement.TheurgyAdvancementSubProvider;
import com.klikli_dev.theurgy.datagen.loot.TheurgyBlockLootSubProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;

public class TheurgyRegistries {
    public static final RegistrySetBuilder WORLD_BUILDER = (new RegistrySetBuilder())
            .add(Registries.FEATURE, ConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
            .add(Registries.BIOME, TheurgyRegistries::bootstrapBiomes)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap);

    public static final RegistrySetBuilder RELOADABLE_BUILDER = (new RegistrySetBuilder())
            .add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(TheurgyBlockLootSubProvider::new, LootContextParamSets.BLOCK)
            )))
            .add(Registries.ADVANCEMENT, new AdvancementProvider(List.of(
                    TheurgyAdvancementSubProvider::new
            )));

    public static void bootstrapBiomes(BootstrapContext<Biome> context) {
        //doesn't need to do anything, just gives us access to a biome registry with empty tag lookup in our other boopstrap contexts
    }
}
