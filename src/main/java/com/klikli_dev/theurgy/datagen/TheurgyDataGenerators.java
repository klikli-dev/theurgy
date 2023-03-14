/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.TheurgyBookProvider;
import com.klikli_dev.theurgy.datagen.lang.ENUSProvider;
import com.klikli_dev.theurgy.datagen.loot.TheurgyBlockLootSubProvider;
import com.klikli_dev.theurgy.datagen.models.TheurgyBlockStateProvider;
import com.klikli_dev.theurgy.datagen.models.TheurgyItemModelProvider;
import com.klikli_dev.theurgy.datagen.multiblock.TheurgyMultiblockProvider;
import com.klikli_dev.theurgy.datagen.recipe.*;
import com.klikli_dev.theurgy.datagen.tags.TheurgyBlockTagsProvider;
import com.klikli_dev.theurgy.datagen.tags.TheurgyFluidTagsProvider;
import com.klikli_dev.theurgy.datagen.tags.TheurgyItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class TheurgyDataGenerators {

    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        var blockTagsProvider = new TheurgyBlockTagsProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new TheurgyFluidTagsProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TheurgyItemTagsProvider(generator.getPackOutput(), event.getLookupProvider(), blockTagsProvider, event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(),
                new LootTableProvider(generator.getPackOutput(), Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(TheurgyBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                )));

        generator.addProvider(event.includeClient(), new TheurgyItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TheurgyBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ShapedRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new ShapelessRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new CalcinationRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new LiqueficationRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new DistillationRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new IncubationRecipeProvider(generator.getPackOutput()));

        generator.addProvider(event.includeServer(), new TheurgyMultiblockProvider(generator.getPackOutput()));

        var enUSProvider = new ENUSProvider(generator.getPackOutput());
        generator.addProvider(event.includeServer(), new TheurgyBookProvider(generator.getPackOutput(), enUSProvider));

        //Important: Lang provider (in this case enus) needs to be added after the book provider to process the texts added by the book provider
        generator.addProvider(event.includeClient(), enUSProvider);
    }
}