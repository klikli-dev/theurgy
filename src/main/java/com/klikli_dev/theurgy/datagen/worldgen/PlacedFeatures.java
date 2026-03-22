// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PlacedFeatures {
    public static final ResourceKey<PlacedFeature> SAL_AMMONIAC_ORE = ResourceKey.create(Registries.PLACED_FEATURE, Theurgy.loc("sal_ammoniac_ore"));


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        PlacementUtils.register(context, SAL_AMMONIAC_ORE, configuredFeatures.getOrThrow(ConfiguredFeatures.SAL_AMMONIAC_ORE),
                commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(200))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier rarity, PlacementModifier height) {
        return List.of(rarity, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
}
