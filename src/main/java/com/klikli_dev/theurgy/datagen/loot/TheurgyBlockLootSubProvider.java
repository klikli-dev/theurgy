/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.loot;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
        this.dropDoubleBlockOnce(BlockRegistry.CALCINATION_OVEN.get());
        this.dropDoubleBlockOnce(BlockRegistry.LIQUEFACTION_CAULDRON.get());
        this.dropDoubleBlockOnce(BlockRegistry.DISTILLER.get());
        this.dropDoubleBlockOnce(BlockRegistry.INCUBATOR.get());
        this.dropSelf(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get());
        this.dropSelf(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get());
        this.dropSelf(BlockRegistry.INCUBATOR_SALT_VESSEL.get());
        this.dropSelf(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
        this.dropSelf(BlockRegistry.SAL_AMMONIAC_TANK.get());

        this.add(BlockRegistry.SAL_AMMONIAC_ORE.get(), (block) -> {
            return this.createOreDrop(block, ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
        });
        this.add(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), (block) -> {
            return this.createOreDrop(block, ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
        });
    }

    protected void dropDoubleBlockOnce(Block pBlock) {
        this.add(pBlock, this.createSinglePropConditionTable(pBlock, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
    }

}
