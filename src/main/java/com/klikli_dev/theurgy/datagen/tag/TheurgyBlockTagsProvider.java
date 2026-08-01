// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class TheurgyBlockTagsProvider extends BlockTagsProvider {
    public TheurgyBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Theurgy.MODID);
    }

    private ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS)
                .add(this.key(Blocks.SUGAR_CANE))
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.SANDSTONE_BLOCKS)
                .addTag(Tags.Blocks.STONES)
                .addTag(BlockTags.LOGS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS)
                .addTag(Tags.Blocks.OBSIDIANS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.INCUBATOR_VESSELS)
                .add(this.key(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()))
                .add(this.key(BlockRegistry.INCUBATOR_SALT_VESSEL.get()))
                .add(this.key(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()));

        this.tag(BlockTagRegistry.REFORMATION_SOURCE_PEDESTALS)
                .add(this.key(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()));

        this.tag(BlockTagRegistry.REFORMATION_TARGET_PEDESTALS)
                .add(this.key(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()));

        this.tag(BlockTagRegistry.REFORMATION_RESULT_PEDESTALS)
                .add(this.key(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()));

        this.tag(BlockTagRegistry.REFORMATION_PEDESTALS)
                .addTag(BlockTagRegistry.REFORMATION_SOURCE_PEDESTALS)
                .addTag(BlockTagRegistry.REFORMATION_TARGET_PEDESTALS)
                .addTag(BlockTagRegistry.REFORMATION_RESULT_PEDESTALS);


        this.tag(BlockTagRegistry.SAL_AMMONIAC_ORES)
                .add(this.key(BlockRegistry.SAL_AMMONIAC_ORE.get()))
                .add(this.key(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));

        this.tag(Tags.Blocks.ORES).addTag(BlockTagRegistry.SAL_AMMONIAC_ORES);

        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(this.key(BlockRegistry.SAL_AMMONIAC_ORE.get()));
        this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(this.key(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTagRegistry.SAL_AMMONIAC_ORES)
                //apparatus blocks
                .add(this.key(BlockRegistry.CALCINATION_OVEN.get()))
                .add(this.key(BlockRegistry.PYROMANTIC_BRAZIER.get()))
                .add(this.key(BlockRegistry.LIQUEFACTION_CAULDRON.get()))
                .add(this.key(BlockRegistry.DISTILLER.get()))
                .add(this.key(BlockRegistry.INCUBATOR.get()))
                .add(this.key(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()))
                .add(this.key(BlockRegistry.INCUBATOR_SALT_VESSEL.get()))
                .add(this.key(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()))
                .add(this.key(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()))
                .add(this.key(BlockRegistry.SAL_AMMONIAC_TANK.get()))
                .add(this.key(BlockRegistry.LOGISTICS_NEXUS.get()))
                .add(this.key(BlockRegistry.MERCURY_CATALYST.get()))
                .add(this.key(BlockRegistry.CALORIC_FLUX_EMITTER.get()))
                .add(this.key(BlockRegistry.SULFURIC_FLUX_EMITTER.get()))
                .add(this.key(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()))
                .add(this.key(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()))
                .add(this.key(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()))
                .add(this.key(BlockRegistry.FERMENTATION_VAT.get()))
                .add(this.key(BlockRegistry.DIGESTION_VAT.get()))
                //logistics blocks
                .add(this.key(BlockRegistry.LOGISTICS_CONNECTION_NODE.get()))
                .add(this.key(BlockRegistry.LOGISTICS_ITEM_INSERTER.get()))
                .add(this.key(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get()))
                .add(this.key(BlockRegistry.LOGISTICS_FLUID_INSERTER.get()))
                .add(this.key(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get()))
                .add(this.key(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get()))
                .add(this.key(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get()))
                .add(this.key(BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()));
        this.tag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTagRegistry.SAL_AMMONIAC_ORES);
    }
}
