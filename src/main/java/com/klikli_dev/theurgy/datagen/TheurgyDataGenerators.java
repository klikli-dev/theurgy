// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.modonomicon.api.datagen.LanguageProviderCache;
import com.klikli_dev.modonomicon.api.datagen.NeoBookProvider;
import com.klikli_dev.modonomicon.api.datagen.NeoResearchProvider;
import com.klikli_dev.modonomicon.api.datagen.research.ResearchCache;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.TheurgyBookProvider;
import com.klikli_dev.theurgy.datagen.lang.ENUSProvider;
import com.klikli_dev.theurgy.datagen.model.TheurgyModelProvider;
import com.klikli_dev.theurgy.datagen.multiblock.TheurgyMultiblockProvider;
import com.klikli_dev.theurgy.datagen.recipe.*;
import com.klikli_dev.theurgy.datagen.tag.TheurgyBlockTagsProvider;
import com.klikli_dev.theurgy.datagen.tag.TheurgyFluidTagsProvider;
import com.klikli_dev.theurgy.datagen.tag.TheurgyItemTagsProvider;
import com.klikli_dev.theurgy.datagen.advancement.TheurgyAdvancementSubProvider;
import com.klikli_dev.theurgy.datagen.loot.TheurgyBlockLootSubProvider;
import com.klikli_dev.theurgy.datagen.worldgen.BiomeModifiers;
import com.klikli_dev.theurgy.datagen.worldgen.ConfiguredFeatures;
import com.klikli_dev.theurgy.datagen.worldgen.PlacedFeatures;
import com.klikli_dev.theurgy.datagen.worldgen.TheurgyRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class TheurgyDataGenerators {

    public static void onGatherDataRegistryEntries(GatherDataRegistryEntriesEvent event) {
        // World (non-reloadable) layer - layer routing is handled internally, all calls are chainable.
        // Entries mirror TheurgyRegistries.WORLD_BUILDER.
        // Note: vanilla bootstraps (e.g. LootPredicates) are already registered globally by
        // NeoForge, re-adding them causes duplicate registration errors.
        event.add(Registries.FEATURE, ConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
                .add(Registries.BIOME, TheurgyRegistries::bootstrapBiomes)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap);

        // Reloadable layer - entries mirror TheurgyRegistries.RELOADABLE_BUILDER.
        event.lootTable(
                new LootTableProvider.SubProviderEntry(TheurgyBlockLootSubProvider::new, LootContextParamSets.BLOCK)
        );

        event.advancement(TheurgyAdvancementSubProvider::new);
    }

    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();

        var blockTagsProvider = new TheurgyBlockTagsProvider(generator.getPackOutput(), event.getReloadableLookupProvider());

        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new TheurgyFluidTagsProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new TheurgyItemTagsProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));

        generator.addProvider(true, new TheurgyModelProvider(generator.getPackOutput()));
        generator.addProvider(true, new ShapedRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new ShapelessRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new SmeltingRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new CalcinationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new LiquefactionRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new DistillationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new IncubationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new AccumulationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new CatalysationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new ReformationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new FermentationRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));
        generator.addProvider(true, new DigestionRecipeProvider(generator.getPackOutput(), event.getReloadableLookupProvider()));

        generator.addProvider(true, new TheurgyMultiblockProvider(generator.getPackOutput()));

        var langCache = new LanguageProviderCache("en_us");
        var researchCache = new ResearchCache();

        generator.addProvider(true, NeoBookProvider.of(event, langCache, researchCache,
                new TheurgyBookProvider()
        ));
        generator.addProvider(true, NeoResearchProvider.of(event, langCache, researchCache,
                new TheurgyResearch()
        ));

        //Important: Lang provider (in this case enus) needs to be added after the book provider to process the texts added by the book provider
        generator.addProvider(true, new ENUSProvider(generator.getPackOutput(), langCache));

    }
}