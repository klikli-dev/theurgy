// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatures {
    public static final ResourceKey<Feature> SAL_AMMONIAC_ORE = ResourceKey.create(Registries.FEATURE, Theurgy.loc("sal_ammoniac_ore"));

    public static void bootstrap(BootstrapContext<Feature> context) {
        List<BlockReplacement> salAmmoniacOres = List.of(
                BlockReplacement.replace(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), BlockRegistry.SAL_AMMONIAC_ORE.get().defaultBlockState()),
                BlockReplacement.replace(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get().defaultBlockState()));

        context.register(SAL_AMMONIAC_ORE, new OreFeature(salAmmoniacOres, 9));
    }

}
