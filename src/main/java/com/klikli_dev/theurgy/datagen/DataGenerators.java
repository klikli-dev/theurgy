/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.datagen.lang.ENUSProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class DataGenerators {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new ENUSProvider(generator));
        generator.addProvider(event.includeClient(), new ItemModelsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new RecipeProvider(generator));
    }
}