// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.loot;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Arrays;
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
        this.dropSelfWithNbt(BlockRegistry.MERCURY_CATALYST.get(),
                "mercuryFluxStorage",
                "mercuryFluxToConvert",
                "currentMercuryFluxPerTick",
                "inventory"
        );
        this.dropSelfWithNbt(BlockRegistry.CALORIC_FLUX_EMITTER.get(),
                "mercuryFluxStorage"
        );

        this.dropSelf(BlockRegistry.SULFURIC_FLUX_EMITTER.get());
        this.dropSelf(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());
        this.dropSelf(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get());
        this.dropSelf(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());

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


    @SuppressWarnings("unchecked")
    protected final void dropSelfWithNbt(Block pBlock, String... sourcePaths) {
        this.dropSelfWithNbt(pBlock, Arrays.stream(sourcePaths).map(s -> Pair.of(s, "BlockEntityTag." + s)).toArray(Pair[]::new));
    }

    @SafeVarargs
    protected final void dropSelfWithNbt(Block pBlock, Pair<String, String>... sourceTargetPathPairs) {
        this.add(pBlock, this.createSelfWithNbtDrop(pBlock, this.copyData(sourceTargetPathPairs)));
    }

    protected void dropSelfWithNbt(Block pBlock, CopyNbtFunction.Builder data) {
        this.add(pBlock, this.createSelfWithNbtDrop(pBlock, data));
    }

    protected LootTable.Builder createSelfWithNbtDrop(Block pBlock, CopyNbtFunction.Builder data) {
        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(pBlock,
                                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
                                        LootItem.lootTableItem(pBlock)
                                                .apply(
                                                        data
                                                )
                                )
                        )
                );
    }

    protected CopyNbtFunction.Builder copyData(Pair<String, String>... sourceTargetPathPairs) {
        var builder = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
        for (var pair : sourceTargetPathPairs) {
            builder.copy(pair.getFirst(), pair.getSecond());
        }
        return builder;
    }
}



