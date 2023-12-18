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
        SulfurRegistry.SULFURS.getEntries().forEach(sulfur -> {
            sulfursTag.add(sulfur.get());
        });


        //add the tier tags into the material tag
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS.location());

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS.location());

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE.location())
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS.location());


        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT);
        SulfurMappings.METALS_ABUNDANT.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON);
        SulfurMappings.METALS_COMMON.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE);
        SulfurMappings.METALS_RARE.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS);
        SulfurMappings.METALS_PRECIOUS.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT);
        SulfurMappings.GEMS_ABUNDANT.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON);
        SulfurMappings.GEMS_COMMON.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE);
        SulfurMappings.GEMS_RARE.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS);
        SulfurMappings.GEMS_PRECIOUS.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT);
        SulfurMappings.OTHER_MINERALS_ABUNDANT.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON);
        SulfurMappings.OTHER_MINERALS_COMMON.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE);
        SulfurMappings.OTHER_MINERALS_RARE.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS);
        SulfurMappings.OTHER_MINERALS_PRECIOUS.forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS).add(sulfur);
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT)
                .addTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT)
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
                .addOptionalTag(this.rl("forge:ores/zinc"))
                .addOptionalTag(this.rl("forge:ores/osmium"))
                .addOptionalTag(this.rl("forge:ores/nickel"))
                .addOptionalTag(this.rl("forge:ores/lead"))
                .addOptionalTag(this.rl("forge:ores/tin"))
                .addOptionalTag(this.rl("forge:ores/cinnabar"))
                .addOptionalTag(this.rl("forge:ores/apatite"))
                .addOptionalTag(this.rl("forge:ores/sulfur"))
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_ORES)
                .addTag(Tags.Items.ORES_GOLD)
                .addOptionalTag(this.rl("forge:ores/azure_silver"))
                .addOptionalTag(this.rl("forge:ores/silver"))
                .addOptionalTag(this.rl("forge:ores/uranium"))
                .addOptionalTag(this.rl("forge:ores/iridium"))
                .addOptionalTag(this.rl("forge:ores/platinum"))
                .addOptionalTag(this.rl("forge:ores/crimson_iron"))
                .addOptionalTag(this.rl("forge:ores/ruby"))
                .addOptionalTag(this.rl("forge:ores/peridot"))
                .addOptionalTag(this.rl("forge:ores/fluorite"))
                .addOptionalTag(this.rl("forge:ores/sapphire"))
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_ORES)
                .addTag(Tags.Items.ORES_DIAMOND)
                .addTag(Tags.Items.ORES_EMERALD)
                .addTag(Tags.Items.ORES_NETHERITE_SCRAP)
                .addOptionalTag(this.rl("forge:ores/allthemodium"))
                .addOptionalTag(this.rl("forge:ores/unobtainium"))
                .addOptionalTag(this.rl("forge:ores/vibranium"))

        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_IRON)
                .addTag(Tags.Items.RAW_MATERIALS_COPPER)
                .addOptionalTag(this.rl("forge:raw_materials/zinc"))
                .addOptionalTag(this.rl("forge:raw_materials/osmium"))
                .addOptionalTag(this.rl("forge:raw_materials/nickel"))
                .addOptionalTag(this.rl("forge:raw_materials/lead"))
                .addOptionalTag(this.rl("forge:raw_materials/tin"))
                .addOptionalTag(this.rl("forge:raw_materials/cinnabar"))
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_GOLD)
                .addOptionalTag(this.rl("forge:raw_materials/uranium"))
                .addOptionalTag(this.rl("forge:raw_materials/silver"))
                .addOptionalTag(this.rl("forge:raw_materials/azure_silver"))
                .addOptionalTag(this.rl("forge:raw_materials/iridium"))
                .addOptionalTag(this.rl("forge:raw_materials/crimson_iron"))
                .addOptionalTag(this.rl("forge:raw_materials/platinum"))
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_RAW_MATERIALS)
                .addOptionalTag(this.rl("forge:raw_materials/allthemodium"))
                .addOptionalTag(this.rl("forge:raw_materials/unobtainium"))
                .addOptionalTag(this.rl("forge:raw_materials/vibranium"))
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_IRON)
                .addTag(Tags.Items.INGOTS_COPPER)
                .addOptionalTag(this.rl("forge:ingots/zinc"))
                .addOptionalTag(this.rl("forge:ingots/osmium"))
                .addOptionalTag(this.rl("forge:ingots/nickel"))
                .addOptionalTag(this.rl("forge:ingots/lead"))
                .addOptionalTag(this.rl("forge:ingots/tin"))
                .addOptionalTag(this.rl("forge:ingots/cinnabar"))
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_GOLD)
                .addOptionalTag(this.rl("forge:ingots/uranium"))
                .addOptionalTag(this.rl("forge:ingots/silver"))
                .addOptionalTag(this.rl("forge:ingots/azure_silver"))
                .addOptionalTag(this.rl("forge:ingots/iridium"))
                .addOptionalTag(this.rl("forge:ingots/crimson_iron"))
                .addOptionalTag(this.rl("forge:ingots/platinum"))
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_NETHERITE)
                .addOptionalTag(this.rl("forge:ingots/allthemodium"))
                .addOptionalTag(this.rl("forge:ingots/unobtainium"))
                .addOptionalTag(this.rl("forge:ingots/vibranium"))
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_LAPIS)
                .addTag(Tags.Items.GEMS_QUARTZ)
                .addOptionalTag(this.rl("forge:gems/cinnabar"))
                .addOptionalTag(this.rl("forge:gems/apatite"))
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_AMETHYST)
                .addTag(Tags.Items.GEMS_PRISMARINE)
                .addTag(ItemTagRegistry.SAL_AMMONIAC_GEMS)
                .addOptionalTag(this.rl("forge:gems/ruby"))
                .addOptionalTag(this.rl("forge:gems/peridot"))
                .addOptionalTag(this.rl("forge:gems/fluorite"))
                .addOptionalTag(this.rl("forge:gems/sapphire"))
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
                .addOptionalTag(this.rl("forge:gems/sulfur"))
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_OTHER_MINERALS)
        ;

        this.tag(ItemTagRegistry.OTHER_MINERALS)
                .addTag(ItemTags.COALS)
                .addTag(Tags.Items.DUSTS_REDSTONE)
        //.addOptionalTag(this.rl("forge:gems/sulfur")) sulfur is classified as a gem, so its handled in the Tags.Items.GEMS
        ;

    }

    public ResourceLocation rl(String tag) {
        return new ResourceLocation(tag);
    }
}
