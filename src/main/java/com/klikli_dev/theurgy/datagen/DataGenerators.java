/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.lang.ENUSProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class DataGenerators {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new ItemModelsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new RecipeProvider(generator));

        var enUSProvider = new ENUSProvider(generator);
        generator.addProvider(event.includeServer(), new TheurgyBookProvider(generator, Theurgy.MODID, enUSProvider));

        //Important: Lang provider (in this case enus) needs to be added after the book provider to process the texts added by the book provider
        generator.addProvider(event.includeClient(), enUSProvider);

    }
}