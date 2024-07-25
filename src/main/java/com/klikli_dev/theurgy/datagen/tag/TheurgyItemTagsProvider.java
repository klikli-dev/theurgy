// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.SulfurMappings;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyItemTagsProvider extends ItemTagsProvider {
    public TheurgyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider, Theurgy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.copy(BlockTagRegistry.SAL_AMMONIAC_ORES, ItemTagRegistry.ORES_SAL_AMMONIAC);
        this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
        this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);

        this.tag(Tags.Items.ORES).addTag(ItemTagRegistry.ORES_SAL_AMMONIAC);

        this.tag(ItemTagRegistry.GEMS_SAL_AMMONIAC)
                .add(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
        this.tag(Tags.Items.GEMS).addTag(ItemTagRegistry.GEMS_SAL_AMMONIAC);

        var mercuriesTag = this.tag(ItemTagRegistry.ALCHEMICAL_MERCURIES);
        ItemRegistry.ITEMS.getEntries().forEach(item -> {
            //theoretically this loop is unnecessary, but allows us to apply additional logic in the future
            if (item.get() == ItemRegistry.MERCURY_SHARD.get() || item.get() == ItemRegistry.MERCURY_CRYSTAL.get())
                mercuriesTag.add(item.get());
        });

        var saltsTag = this.tag(ItemTagRegistry.ALCHEMICAL_SALTS);
        SaltRegistry.SALTS.getEntries().forEach(salt -> {
            saltsTag.add(salt.get());
        });

        var sulfursTag = this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS);
        SulfurRegistry.SULFURS.getEntries().forEach(sulfur -> sulfursTag.add(sulfur.get()));

        var nitersTag = this.tag(ItemTagRegistry.ALCHEMICAL_NITERS);
        NiterRegistry.NITERS.getEntries().forEach(niter -> nitersTag.add(niter.get()));

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_AND_NITERS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_NITERS);

        this.tag(ItemTagRegistry.SUGARS)
                .add(Items.SUGAR);

        //add the tier tags into the material tag
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS);

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS);

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS);

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT);

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT);

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT);
        SulfurMappings.metalsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON);
        SulfurMappings.metalsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE);
        SulfurMappings.metalsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS);
        SulfurMappings.metalsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT);
        SulfurMappings.gemsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON);
        SulfurMappings.gemsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE);
        SulfurMappings.gemsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS);
        SulfurMappings.gemsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT);
        SulfurMappings.otherMineralsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON);
        SulfurMappings.otherMineralsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE);
        SulfurMappings.otherMineralsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS);
        SulfurMappings.otherMineralsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT);
        SulfurMappings.logsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT);
        SulfurMappings.cropsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_COMMON)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_RARE)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_PRECIOUS)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_ORES)
                .addTag(Tags.Items.ORES_IRON)
                .addTag(Tags.Items.ORES_COPPER)
                .addTag(Tags.Items.ORES_LAPIS)
                .addTag(Tags.Items.ORES_QUARTZ)
                .addTag(Tags.Items.ORES_REDSTONE)
                .addTag(Tags.Items.ORES_COAL)
                .addOptionalTag(ItemTagRegistry.ORES_ZINC)
                .addOptionalTag(ItemTagRegistry.ORES_OSMIUM)
                .addOptionalTag(ItemTagRegistry.ORES_NICKEL)
                .addOptionalTag(ItemTagRegistry.ORES_LEAD)
                .addOptionalTag(ItemTagRegistry.ORES_TIN)
                .addOptionalTag(ItemTagRegistry.ORES_CINNABAR)
                .addOptionalTag(ItemTagRegistry.ORES_APATITE)
                .addOptionalTag(ItemTagRegistry.ORES_SULFUR)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_ORES)
                .addTag(Tags.Items.ORES_GOLD)
                .addOptionalTag(ItemTagRegistry.ORES_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.ORES_SILVER)
                .addOptionalTag(ItemTagRegistry.ORES_URANIUM)
                .addOptionalTag(ItemTagRegistry.ORES_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.ORES_PLATINUM)
                .addOptionalTag(ItemTagRegistry.ORES_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.ORES_RUBY)
                .addOptionalTag(ItemTagRegistry.ORES_PERIDOT)
                .addOptionalTag(ItemTagRegistry.ORES_FLUORITE)
                .addOptionalTag(ItemTagRegistry.ORES_SAPPHIRE)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_ORES)
                .addTag(Tags.Items.ORES_DIAMOND)
                .addTag(Tags.Items.ORES_EMERALD)
                .addTag(Tags.Items.ORES_NETHERITE_SCRAP)
                .addOptionalTag(ItemTagRegistry.ORES_ALLTHEMODIUM)
                .addOptionalTag(ItemTagRegistry.ORES_UNOBTAINIUM)
                .addOptionalTag(ItemTagRegistry.ORES_VIBRANIUM)

        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_IRON)
                .addTag(Tags.Items.RAW_MATERIALS_COPPER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_ZINC)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_OSMIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_NICKEL)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_LEAD)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_TIN)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_CINNABAR)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_GOLD)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_URANIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_SILVER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_PLATINUM)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_RAW_MATERIALS)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_ALLTHEMODIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_UNOBTAINIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_VIBRANIUM)
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_IRON)
                .addTag(Tags.Items.INGOTS_COPPER)
                .addOptionalTag(ItemTagRegistry.INGOTS_ZINC)
                .addOptionalTag(ItemTagRegistry.INGOTS_OSMIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_NICKEL)
                .addOptionalTag(ItemTagRegistry.INGOTS_LEAD)
                .addOptionalTag(ItemTagRegistry.INGOTS_TIN)
                .addOptionalTag(ItemTagRegistry.INGOTS_CINNABAR)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_GOLD)
                .addOptionalTag(ItemTagRegistry.INGOTS_URANIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_SILVER)
                .addOptionalTag(ItemTagRegistry.INGOTS_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.INGOTS_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.INGOTS_PLATINUM)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_NETHERITE)
                .addOptionalTag(ItemTagRegistry.INGOTS_ALLTHEMODIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_UNOBTAINIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_VIBRANIUM)
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_LAPIS)
                .addTag(Tags.Items.GEMS_QUARTZ)
                .addOptionalTag(ItemTagRegistry.GEMS_APATITE)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_AMETHYST)
                .addTag(Tags.Items.GEMS_PRISMARINE)
                .addTag(ItemTagRegistry.GEMS_SAL_AMMONIAC)
                .addOptionalTag(ItemTagRegistry.GEMS_RUBY)
                .addOptionalTag(ItemTagRegistry.GEMS_PERIDOT)
                .addOptionalTag(ItemTagRegistry.GEMS_FLUORITE)
                .addOptionalTag(ItemTagRegistry.GEMS_SAPPHIRE)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_DIAMOND)
                .addTag(Tags.Items.GEMS_EMERALD)
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_OTHER_MINERALS)
                .addTag(ItemTags.COALS)
                .addTag(Tags.Items.DUSTS_REDSTONE)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_OTHER_MINERALS)
                .addOptionalTag(ItemTagRegistry.GEMS_SULFUR)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_OTHER_MINERALS)
        ;

        this.tag(ItemTagRegistry.OTHER_MINERALS)
                .addTag(ItemTags.COALS)
                .addTag(Tags.Items.DUSTS_REDSTONE)
        //  .addOptionalTag(ItemTagRegistry.GEMS_SULFUR) sulfur is classified as a gem, so its handled in the Tags.Items.GEMS
        ;

    }

    public ResourceLocation rl(String tag) {
        return ResourceLocation.parse(tag);
    }
}
