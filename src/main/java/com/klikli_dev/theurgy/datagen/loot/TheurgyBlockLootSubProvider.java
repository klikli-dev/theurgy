// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.loot;

import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
        this.dropMercuryCatalyst(BlockRegistry.MERCURY_CATALYST.get());
        this.dropSelf(BlockRegistry.CALORIC_FLUX_EMITTER.get());

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


    protected void dropMercuryCatalyst(Block pBlock) {
        this.add(pBlock, this.createMercuryCatalystDrop(pBlock));
    }

    protected LootTable.Builder createMercuryCatalystDrop(Block pBlock) {
        //this is copied from the shulker box with following modifications:
        // we have no custom name, so the copyName function is removed:   .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
        // we have no lock, so the copyData function for the lock is removed:   .copy("Lock", "BlockEntityTag.Lock")
        // we have no loot table, so the copyData function for the loot table and loot table seed are removed:
        //      .copy("LootTable", "BlockEntityTag.LootTable")
        //      .copy("LootTableSeed", "BlockEntityTag.LootTableSeed")
        // the container contents are set to the mercury catalyst contents: .withEntry(DynamicLoot.dynamicEntry(MercuryCatalystBlock.CONTENTS))
        // we have a bunch of other data that needs to be copied, so we add the tags to the copyData function

        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(pBlock,
                                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
                                        LootItem.lootTableItem(pBlock)
                                                .apply(
                                                        CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                                                .copy("mercuryFluxStorage", "BlockEntityTag.mercuryFluxStorage")
                                                                .copy("mercuryFluxToConvert", "BlockEntityTag.mercuryFluxToConvert")
                                                                .copy("currentMercuryFluxPerTick", "BlockEntityTag.currentMercuryFluxPerTick")
                                                )
                                                .apply(
                                                        SetContainerContents
                                                        .setContents(BlockEntityRegistry.MERCURY_CATALYST.get())
                                                        .withEntry(DynamicLoot.dynamicEntry(MercuryCatalystBlock.CONTENTS))
                                                )
                                )
                        )
                );
    }

}
