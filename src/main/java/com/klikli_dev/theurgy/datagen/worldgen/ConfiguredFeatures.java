// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAL_AMMONIAC_ORE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Theurgy.loc("sal_ammoniac_ore"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        List<OreConfiguration.TargetBlockState> salAmmoniacOres = List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), BlockRegistry.SAL_AMMONIAC_ORE.get().defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get().defaultBlockState()));

        FeatureUtils.register(context, SAL_AMMONIAC_ORE, Feature.ORE, new OreConfiguration(salAmmoniacOres, 9));
    }

}
