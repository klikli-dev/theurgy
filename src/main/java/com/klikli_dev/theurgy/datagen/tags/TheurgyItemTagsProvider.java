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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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


        //add the tier tags into the mai tag
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

        //TODO: Fill the tier tags
        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT)
                .add(SulfurRegistry.COPPER.get())
        ;

        this.tag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON)
                .add(SulfurRegistry.IRON.get())
                .add(SulfurRegistry.TIN.get())
                .add(SulfurRegistry.ZINC.get())
                .add(SulfurRegistry.LEAD.get())
                .add(SulfurRegistry.NICKEL.get())
                .add(SulfurRegistry.CINNABAR.get())
                .add(SulfurRegistry.OSMIUM.get())
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

    }

    public ResourceLocation rl(String tag) {
        return new ResourceLocation(tag);
    }
}
