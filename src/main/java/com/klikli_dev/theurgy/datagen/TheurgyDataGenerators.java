// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.advancement.TheurgyAdvancementSubProvider;
import com.klikli_dev.theurgy.datagen.book.TheurgyBookProvider;
import com.klikli_dev.theurgy.datagen.lang.ENUSProvider;
import com.klikli_dev.theurgy.datagen.loot.TheurgyBlockLootSubProvider;
import com.klikli_dev.theurgy.datagen.model.TheurgyBlockStateProvider;
import com.klikli_dev.theurgy.datagen.model.TheurgyItemModelProvider;
import com.klikli_dev.theurgy.datagen.multiblock.TheurgyMultiblockProvider;
import com.klikli_dev.theurgy.datagen.recipe.*;
import com.klikli_dev.theurgy.datagen.tag.TheurgyBlockTagsProvider;
import com.klikli_dev.theurgy.datagen.tag.TheurgyFluidTagsProvider;
import com.klikli_dev.theurgy.datagen.tag.TheurgyItemTagsProvider;
import com.klikli_dev.theurgy.datagen.worldgen.TheurgyRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class TheurgyDataGenerators {

    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();

        var blockTagsProvider = new TheurgyBlockTagsProvider(generator.getPackOutput(), event.getLookupProvider());

        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new TheurgyFluidTagsProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new TheurgyItemTagsProvider(generator.getPackOutput(), event.getLookupProvider(), blockTagsProvider.contentsGetter()));

        generator.addProvider(true, new LootTableProvider(
                        generator.getPackOutput(),
                        Set.of(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(TheurgyBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                        ),
                        event.getLookupProvider()
                )
        );

        generator.addProvider(true,
                new AdvancementProvider(generator.getPackOutput(), event.getLookupProvider(), List.of(
                        new TheurgyAdvancementSubProvider()
                )));

        generator.addProvider(true, new TheurgyItemModelProvider(generator.getPackOutput()));

        generator.addProvider(true, new TheurgyBlockStateProvider(generator.getPackOutput()));
        generator.addProvider(true, new ShapedRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new ShapelessRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new SmeltingRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new CalcinationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new LiquefactionRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new DistillationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new IncubationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new AccumulationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new CatalysationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new ReformationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new FermentationRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new DigestionRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));

        generator.addProvider(true, new TheurgyMultiblockProvider(generator.getPackOutput()));

        var enUSProvider = new ENUSProvider(generator.getPackOutput());
        generator.addProvider(true,
                new BookProvider(generator.getPackOutput(), event.getLookupProvider(), Theurgy.MODID, List.of(
                        new TheurgyBookProvider(enUSProvider))
                )
        );

        //Important: Lang provider (in this case enus) needs to be added after the book provider to process the texts added by the book provider
        generator.addProvider(true, enUSProvider);

        event.getGenerator().addProvider(true,
                (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output ->
                        new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), TheurgyRegistries.BUILDER, Set.of(Theurgy.MODID)));

    }
}