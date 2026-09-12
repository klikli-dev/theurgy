// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.tag;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.SulfurMappings;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class TheurgyItemTagsProvider extends TagsProvider<Item> {
    public TheurgyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider);
    }

    private ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }

    private ResourceKey<Item> key(Block block) {
        return BuiltInRegistries.ITEM.getResourceKey(block.asItem()).orElseThrow();
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(this.key(ItemRegistry.THE_HERMETICA.get()));
        this.tag(ItemTags.LECTERN_BOOKS).add(this.key(ItemRegistry.THE_HERMETICA.get()));

        //Note: we cannot use this.copy() here because our custom copy converts the block tag to an item
        //tag with the same location and adds it as a tag reference - which creates a self-reference
        //for tags that share the same path (e.g. c:ores/sal_ammoniac exists as both block and item tag).
        //Instead, we directly add the items to mirror what the block tag provider does.
        this.tag(ItemTagRegistry.ORES_SAL_AMMONIAC)
                .add(this.key(BlockRegistry.SAL_AMMONIAC_ORE.get()))
                .add(this.key(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));
        this.tag(Tags.Items.ORES_IN_GROUND_STONE)
                .add(this.key(BlockRegistry.SAL_AMMONIAC_ORE.get()));
        this.tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE)
                .add(this.key(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));

        this.tag(Tags.Items.ORES).addTag(ItemTagRegistry.ORES_SAL_AMMONIAC);

        this.tag(ItemTagRegistry.GEMS_SAL_AMMONIAC)
                .add(this.key(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get()));
        this.tag(Tags.Items.GEMS).addTag(ItemTagRegistry.GEMS_SAL_AMMONIAC);

        var mercuriesTag = this.tag(ItemTagRegistry.ALCHEMICAL_MERCURIES);
        ItemRegistry.ITEMS.getEntries().forEach(item -> {
            //theoretically this loop is unnecessary, but allows us to apply additional logic in the future
            if (item.get() == ItemRegistry.MERCURY_SHARD.get() || item.get() == ItemRegistry.MERCURY_CRYSTAL.get())
                mercuriesTag.add(this.key(item.get()));
        });

        var saltsTag = this.tag(ItemTagRegistry.ALCHEMICAL_SALTS);
        SaltRegistry.SALTS.getEntries().forEach(salt -> {
            saltsTag.add(this.key(salt.get()));
        });

        var sulfursTag = this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS);
        SulfurRegistry.SULFURS.getEntries().forEach(sulfur -> sulfursTag.add(this.key(sulfur.get())));

        var nitersTag = this.tag(ItemTagRegistry.ALCHEMICAL_NITERS);
        NiterRegistry.NITERS.getEntries().forEach(niter -> nitersTag.add(this.key(niter.get())));

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_AND_NITERS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_NITERS);

        this.tag(ItemTagRegistry.SUGARS)
                .add(this.key(Items.SUGAR));

        this.tag(ItemTagRegistry.FERMENTATION_STARTERS)
                .add(this.key(ItemRegistry.FERMENTATION_STARTER.get()));

        // Populate the berry foods tag with vanilla berry items
        this.tag(ItemTagRegistry.FOODS_BERRY)
                .add(this.key(Items.SWEET_BERRIES))
                .add(this.key(Items.GLOW_BERRIES));

        //add the tier tags into the material tag
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_PRECIOUS);

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
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT);
        SulfurMappings.earthenMattersAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON);
        SulfurMappings.earthenMattersCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT);
        SulfurMappings.metalsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON);
        SulfurMappings.metalsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE);
        SulfurMappings.metalsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS);
        SulfurMappings.metalsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT);
        SulfurMappings.gemsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON);
        SulfurMappings.gemsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE);
        SulfurMappings.gemsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS);
        SulfurMappings.gemsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT);
        SulfurMappings.otherMineralsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON);
        SulfurMappings.otherMineralsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE);
        SulfurMappings.otherMineralsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS);
        SulfurMappings.otherMineralsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT);
        SulfurMappings.logsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT);
        SulfurMappings.cropsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT);
        SulfurMappings.animalsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON);
        SulfurMappings.animalsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE);
        SulfurMappings.animalsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE).add(this.key(sulfur));
        });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT);
        SulfurMappings.mobsAbundant().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON);
        SulfurMappings.mobsCommon().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON_FOR_AUTOMATIC_RECIPES);
        SulfurMappings.mobsCommon()
                .stream()
                .filter(sulfur -> !SulfurMappings.noAutomaticRecipesFor().contains(sulfur))
                .forEach(sulfur -> {
                    this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON_FOR_AUTOMATIC_RECIPES).add(this.key(sulfur));
                });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE);
        SulfurMappings.mobsRare().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE_FOR_AUTOMATIC_RECIPES);
        SulfurMappings.mobsRare()
                .stream()
                .filter(sulfur -> !SulfurMappings.noAutomaticRecipesFor().contains(sulfur))
                .forEach(sulfur -> {
                    this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE_FOR_AUTOMATIC_RECIPES).add(this.key(sulfur));
                });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS);
        SulfurMappings.mobsPrecious().forEach(sulfur -> {
            this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS).add(this.key(sulfur));
        });
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS_FOR_AUTOMATIC_RECIPES);
        SulfurMappings.mobsPrecious()
                .stream()
                .filter(sulfur -> !SulfurMappings.noAutomaticRecipesFor().contains(sulfur))
                .forEach(sulfur -> {
                    this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS_FOR_AUTOMATIC_RECIPES).add(this.key(sulfur));
                });

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE)
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_HERBS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_PRECIOUS)
                .addOptionalTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS)
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
                .addOptionalTag(ItemTagRegistry.ORES_URANINITE_POOR)
                .addOptionalTag(ItemTagRegistry.ORES_LIGNITE_COAL)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_ORES)
                .addTag(Tags.Items.ORES_GOLD)
                .addOptionalTag(ItemTagRegistry.ORES_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.ORES_SILVER)
                .addOptionalTag(ItemTagRegistry.ORES_URANIUM)
                .addOptionalTag(ItemTagRegistry.ORES_URANINITE_REGULAR)
                .addOptionalTag(ItemTagRegistry.ORES_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.ORES_PLATINUM)
                .addOptionalTag(ItemTagRegistry.ORES_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.ORES_RUBY)
                .addOptionalTag(ItemTagRegistry.ORES_PERIDOT)
                .addOptionalTag(ItemTagRegistry.ORES_FLUORITE)
                .addOptionalTag(ItemTagRegistry.ORES_SAPPHIRE)
                .addOptionalTag(ItemTagRegistry.ORES_TITANIUM)
                .addOptionalTag(ItemTagRegistry.ORES_TUNGSTEN)
                .addOptionalTag(ItemTagRegistry.ORES_BAUXITE)
                .addOptionalTag(ItemTagRegistry.ORES_ANTIMONY)
                .addOptionalTag(ItemTagRegistry.ORES_MONAZITE)
                .addOptionalTag(ItemTagRegistry.ORES_MITHRIL)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_ORES)
                .addTag(Tags.Items.ORES_DIAMOND)
                .addTag(Tags.Items.ORES_EMERALD)
                .addTag(Tags.Items.ORES_NETHERITE_SCRAP)
                .addOptionalTag(ItemTagRegistry.ORES_URANINITE_DENSE)
                .addOptionalTag(ItemTagRegistry.ORES_ALLTHEMODIUM)
                .addOptionalTag(ItemTagRegistry.ORES_UNOBTAINIUM)
                .addOptionalTag(ItemTagRegistry.ORES_VIBRANIUM)
                .addOptionalTag(ItemTagRegistry.ORES_STELLARITE)

        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_IRON)
                .addTag(Tags.Items.RAW_MATERIALS_COPPER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_ZINC)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_OSMIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_NICKEL)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_LEAD)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_TIN)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_ALUMINUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_CINNABAR)
        ;

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS)
                .addTag(Tags.Items.RAW_MATERIALS_GOLD)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_URANIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_URANINITE)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_SILVER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_PLATINUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_TITANIUM)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_TUNGSTEN)
                .addOptionalTag(ItemTagRegistry.RAW_MATERIALS_MITHRIL)
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
                .addOptionalTag(ItemTagRegistry.INGOTS_ALUMINUM);

        this.tag(ItemTagRegistry.MEDIUM_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_GOLD)
                .addOptionalTag(ItemTagRegistry.INGOTS_URANIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_URANINITE)
                .addOptionalTag(ItemTagRegistry.INGOTS_SILVER)
                .addOptionalTag(ItemTagRegistry.INGOTS_AZURE_SILVER)
                .addOptionalTag(ItemTagRegistry.INGOTS_IRIDIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_CRIMSON_IRON)
                .addOptionalTag(ItemTagRegistry.INGOTS_PLATINUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_MITHRIL)
        ;

        this.tag(ItemTagRegistry.HIGH_MERCURY_METALS)
                .addTag(Tags.Items.INGOTS_NETHERITE)
                .addOptionalTag(ItemTagRegistry.INGOTS_ALLTHEMODIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_UNOBTAINIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_VIBRANIUM)
                .addOptionalTag(ItemTagRegistry.INGOTS_STELLARITE)
        ;

        this.tag(ItemTagRegistry.LOW_MERCURY_GEMS)
                .addTag(Tags.Items.GEMS_LAPIS)
                .addTag(Tags.Items.GEMS_QUARTZ)
                .addOptionalTag(ItemTagRegistry.GEMS_APATITE)
                .addOptionalTag(ItemTagRegistry.GEMS_CINNABAR)
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

        //Creature items that can be calcinated into creature salt
        this.tag(ItemTagRegistry.CREATURE_ITEMS_ANIMALS)
                .add(this.key(Items.PORKCHOP))
                .add(this.key(Items.BEEF))
                .add(this.key(Items.MUTTON))
                .add(this.key(Items.CHICKEN))
                .add(this.key(Items.COOKED_PORKCHOP))
                .add(this.key(Items.COOKED_BEEF))
                .add(this.key(Items.COOKED_MUTTON))
                .add(this.key(Items.COOKED_CHICKEN))
                .add(this.key(Items.RABBIT))
                .add(this.key(Items.COOKED_RABBIT))
                .add(this.key(Items.LEATHER))
                .add(this.key(Items.FEATHER))
                .add(this.key(Items.EGG))
                .add(this.key(Items.RABBIT_HIDE))
                .add(this.key(Items.INK_SAC))
                .add(this.key(Items.GLOW_INK_SAC))
                .add(this.key(Items.ARMADILLO_SCUTE))
                .addTag(ItemTags.WOOL);

        this.tag(ItemTagRegistry.CREATURE_ITEMS_FISH)
                .add(this.key(Items.COD))
                .add(this.key(Items.COOKED_COD))
                .add(this.key(Items.SALMON))
                .add(this.key(Items.COOKED_SALMON))
                .add(this.key(Items.TROPICAL_FISH))
                .add(this.key(Items.PUFFERFISH));

        this.tag(ItemTagRegistry.CREATURE_ITEMS_MOBS)
                .add(this.key(Items.ROTTEN_FLESH))
                .add(this.key(Items.SPIDER_EYE))
                .add(this.key(Items.STRING))
                .add(this.key(Items.GUNPOWDER))
                .add(this.key(Items.BONE))
                .add(this.key(Items.BONE_MEAL))
                .add(this.key(Items.ARROW))
                .add(this.key(Items.SLIME_BALL))
                .add(this.key(Items.BLAZE_ROD))
                .add(this.key(Items.PHANTOM_MEMBRANE))
                .add(this.key(Items.MAGMA_CREAM))
                .add(this.key(Items.TURTLE_SCUTE))
                .add(this.key(Items.SHULKER_SHELL))
                .add(this.key(Items.ENDER_PEARL))
                .add(this.key(Items.PRISMARINE_SHARD))
                .add(this.key(Items.PRISMARINE_CRYSTALS))
                .add(this.key(Items.SKELETON_SKULL))
                .add(this.key(Items.WITHER_SKELETON_SKULL))
                .add(this.key(Items.GHAST_TEAR))
                .add(this.key(Items.ELYTRA))
                .add(this.key(Items.NETHER_STAR))
                .add(this.key(Items.DRAGON_EGG))
                .add(this.key(Items.HEART_OF_THE_SEA));

        this.tag(ItemTagRegistry.CREATURE_ITEMS)
                .addOptionalTag(ItemTagRegistry.CREATURE_ITEMS_ANIMALS)
                .addOptionalTag(ItemTagRegistry.CREATURE_ITEMS_FISH)
                .addOptionalTag(ItemTagRegistry.CREATURE_ITEMS_MOBS);


        //Set up tags for other mods that may not properly tag their mats
        this.tag(ItemTagRegistry.INGOTS_URANINITE)
                .addOptional(ResourceKey.create(Registries.ITEM, this.rl("powah:uraninite"))); //powah adds ore tags and raw material tags but not ingot tags

        this.tag(ItemTagRegistry.ORES_DARK_GEM)
                .addOptionalTag(TagKey.create(Registries.ITEM, this.rl("evilcraft:dark_ores")))
                .addOptionalTag(TagKey.create(Registries.ITEM, this.rl("evilcraft:ores/dark_gem"))); //does not exist as of 1.21, but if they unify the pattern it will

        this.tag(ItemTagRegistry.GEMS_DARK)
                .addOptionalTag(TagKey.create(Registries.ITEM, this.rl("evilcraft:gems/dark")));

        this.tag(ItemTagRegistry.RAW_MATERIALS_DEMONITE)
                .addOptional(ResourceKey.create(Registries.ITEM, this.rl("bloodmagic:rawdemonite")));
        this.tag(ItemTagRegistry.INGOTS_DEMONITE)
                .addOptional(ResourceKey.create(Registries.ITEM, this.rl("bloodmagic:ingot_hellforged")));

        this.tag(ItemTagRegistry.INGOTS_STELLARITE)
                .addOptional(ResourceKey.create(Registries.ITEM, this.rl("forbidden_arcanus:stellarite_piece")));

        this.tag(ItemTagRegistry.GEMS_CHIMERITE)
                .addOptional(ResourceKey.create(Registries.ITEM, this.rl("mna:chimerite_gem")));
    }

    public Identifier rl(String tag) {
        return Identifier.parse(tag);
    }
}
