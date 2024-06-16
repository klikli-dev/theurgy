// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.loot;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;
import java.util.function.BiConsumer;

public class TheurgyBlockLootSubProvider extends BlockLootSubProvider {
    public TheurgyBlockLootSubProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> pGenerator) {
        this.generate();
        this.map.forEach(pGenerator);
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
        this.dropSelfWithComponents(BlockRegistry.MERCURY_CATALYST.get(),
                DataComponentRegistry.MERCURY_FLUX_STORAGE.get(),
                DataComponentRegistry.MERCURY_FLUX_TO_CONVERT.get(),
                DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK.get(),
                DataComponentRegistry.MERCURY_CATALYST_INVENTORY.get()
        );
        this.dropSelfWithComponents(BlockRegistry.CALORIC_FLUX_EMITTER.get(),
                DataComponentRegistry.MERCURY_FLUX_STORAGE.get()
        );

        this.dropSelfWithComponents(BlockRegistry.SULFURIC_FLUX_EMITTER.get(),
                DataComponentRegistry.MERCURY_FLUX_STORAGE.get()
        );
        this.dropSelf(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());
        this.dropSelf(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get());
        this.dropSelf(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());

        this.dropSelf(BlockRegistry.FERMENTATION_VAT.get());
        this.dropSelf(BlockRegistry.DIGESTION_VAT.get());

        this.dropSelf(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get());
        this.dropSelf(BlockRegistry.LOGISTICS_ITEM_INSERTER.get());
        this.dropSelf(BlockRegistry.LOGISTICS_CONNECTION_NODE.get());

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

    protected final void dropSelfWithComponents(Block pBlock, DataComponentType<?>... pIncludes) {
        this.add(pBlock, this.createSelfWithComponentsDrop(pBlock, this.copyComponents(pIncludes)));
    }

    protected void dropSelfWithComponents(Block pBlock, CopyComponentsFunction.Builder data) {
        this.add(pBlock, this.createSelfWithComponentsDrop(pBlock, data));
    }

    protected LootTable.Builder createSelfWithComponentsDrop(Block pBlock, CopyComponentsFunction.Builder data) {
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

    protected CopyComponentsFunction.Builder copyComponents(DataComponentType<?>... pIncludes) {
        var builder = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);
        for (var include : pIncludes) {
            builder.include(include);
        }
        return builder;
    }
}



