/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.loot;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;
import java.util.function.BiConsumer;

public class TheurgyBlockLootSubProvider extends BlockLootSubProvider {
    public TheurgyBlockLootSubProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.generate();
        this.map.forEach(consumer::accept);
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.PYROMANTIC_BRAZIER.get());
        this.dropSelf(BlockRegistry.CALCINATION_OVEN.get());
        this.dropSelf(BlockRegistry.LIQUEFACTION_CAULDRON.get());
        this.dropSelf(BlockRegistry.DISTILLER.get());
    }
}
