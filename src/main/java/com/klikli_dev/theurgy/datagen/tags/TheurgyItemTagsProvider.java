/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.tags;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyItemTagsProvider extends ItemTagsProvider {
    public TheurgyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider, Theurgy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.copy(BlockTagRegistry.SAL_AMMONIAC_ORES, ItemTagRegistry.SAL_AMMONIAC_ORES);
        this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
        this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);

        this.tag(ItemTagRegistry.SAL_AMMONIAC_GEMS)
                .add(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
        this.tag(Tags.Items.GEMS).addTag(ItemTagRegistry.SAL_AMMONIAC_GEMS);

        var mercuryTag = this.tag(ItemTagRegistry.ALCHEMICAL_MERCURIES);
        ItemRegistry.ITEMS.getEntries().forEach(item -> {
            //theoretically this loop is unnecessary, but allows us to apply additional logic in the future
            if (item.get() == ItemRegistry.MERCURY_SHARD.get() || item.get() == ItemRegistry.MERCURY_CRYSTAL.get())
                mercuryTag.add(item.get());
        });

        var saltTag = this.tag(ItemTagRegistry.ALCHEMICAL_SALTS);
        SaltRegistry.SALTS.getEntries().forEach(salt -> {
            saltTag.add(salt.get());
        });

        var sulfurTag = this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS);
        SulfurRegistry.SULFURS.getEntries().forEach(sulfur -> {
            sulfurTag.add(sulfur.get());
        });
    }
}
