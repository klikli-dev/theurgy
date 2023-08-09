// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyBlockTagsProvider extends BlockTagsProvider {
    public TheurgyBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Theurgy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS)
                .add(Blocks.SUGAR_CANE)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.SAND)
                .addTag(Tags.Blocks.STONE)
                .addTag(BlockTags.LOGS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS)
                .addTag(Tags.Blocks.OBSIDIAN);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS)
                .addTag(BlockTagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS);
        this.tag(BlockTagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS);

        this.tag(BlockTagRegistry.INCUBATOR_VESSELS)
                .add(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get())
                .add(BlockRegistry.INCUBATOR_SALT_VESSEL.get())
                .add(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get());

        this.tag(BlockTagRegistry.SAL_AMMONIAC_ORES)
                .add(BlockRegistry.SAL_AMMONIAC_ORE.get())
                .add(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get());

        this.tag(Tags.Blocks.ORES).addTag(BlockTagRegistry.SAL_AMMONIAC_ORES);
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(BlockRegistry.SAL_AMMONIAC_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTagRegistry.SAL_AMMONIAC_ORES);
        this.tag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTagRegistry.SAL_AMMONIAC_ORES);
    }
}
