/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.datagen;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.datagen.recipe.CrucibleRecipeBuilder;
import com.github.klikli_dev.theurgy.datagen.recipe.EssentiaRecipeBuilder;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.TagRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TheurgyRecipeProvider extends RecipeProvider {

    //region Fields
    public static final ITag.INamedTag<Item> COPPER_ORE = TagRegistry.makeForgeItemTag("ores/copper");
    public static final ITag.INamedTag<Item> TIN_ORE = TagRegistry.makeForgeItemTag("ores/tin");
    public static final ITag.INamedTag<Item> SILVER_ORE = TagRegistry.makeForgeItemTag("ores/silver");

    public static final ITag.INamedTag<Item> COPPER_INGOT = TagRegistry.makeForgeItemTag("ingots/copper");
    public static final ITag.INamedTag<Item> TIN_INGOT = TagRegistry.makeForgeItemTag("ingots/tin");
    public static final ITag.INamedTag<Item> SILVER_INGOT = TagRegistry.makeForgeItemTag("ingots/silver");
    //endregion Fields

    //region Initialization
    public TheurgyRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }
    //endregion Initialization

    //region Overrides
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        Item aer = ItemRegistry.AER_ESSENTIA.get();
        Item aqua = ItemRegistry.AQUA_ESSENTIA.get();
        Item ignis = ItemRegistry.IGNIS_ESSENTIA.get();
        Item terra = ItemRegistry.TERRA_ESSENTIA.get();

        //Essentia recipes
        //Modded Ingots
        EssentiaRecipeBuilder copperIngotEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("copper_ingot")
                                                            .ingredient(COPPER_INGOT)
                                                            .essentia(ignis, 100)
                                                            .essentia(terra, 100);
        this.registerTagExistsCondition(copperIngotEssentia, COPPER_INGOT, consumer);

        EssentiaRecipeBuilder tinIngotEssentia = EssentiaRecipeBuilder.create()
                                                         .setRecipeName("tin_ingot")
                                                         .ingredient(TIN_INGOT)
                                                         .essentia(ignis, 100)
                                                         .essentia(terra, 100);
        this.registerTagExistsCondition(tinIngotEssentia, TIN_INGOT, consumer);

        EssentiaRecipeBuilder silverIngotEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("silver_ingot")
                                                            .ingredient(SILVER_INGOT)
                                                            .essentia(ignis, 150)
                                                            .essentia(terra, 150);
        this.registerTagExistsCondition(silverIngotEssentia, SILVER_INGOT, consumer);

        //Vanilla ingots
        EssentiaRecipeBuilder ironIngotEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("iron_ingot")
                                                          .ingredient(Tags.Items.INGOTS_IRON)
                                                          .essentia(ignis, 100)
                                                          .essentia(terra, 100)
                                                          .build(consumer);
        EssentiaRecipeBuilder goldIngotEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("gold_ingot")
                                                          .ingredient(Tags.Items.INGOTS_GOLD)
                                                          .essentia(aer, 50)
                                                          .essentia(ignis, 200)
                                                          .essentia(terra, 200)
                                                          .build(consumer);
        EssentiaRecipeBuilder netheriteIngotEssentia = EssentiaRecipeBuilder.create()
                                                               .setRecipeName("netherite_ingot")
                                                               .ingredient(Tags.Items.INGOTS_NETHERITE)
                                                               .essentia(aer, 2000)
                                                               .essentia(ignis, 2000)
                                                               .build(consumer);

        //Vanilla Ores
        EssentiaRecipeBuilder netheriteScrapEssentia = EssentiaRecipeBuilder.create()
                                                               .setRecipeName("netherite_scrap")
                                                               .ingredient(Tags.Items.ORES_NETHERITE_SCRAP)
                                                               .essentia(aer, 500)
                                                               .essentia(ignis, 500)
                                                               .build(consumer);

        //Vanilla Gems
        EssentiaRecipeBuilder quartzEssentia = EssentiaRecipeBuilder.create()
                                                       .setRecipeName("quartz")
                                                       .ingredient(Tags.Items.GEMS_QUARTZ)
                                                       .essentia(aer, 20)
                                                       .essentia(ignis, 20)
                                                       .build(consumer);
        EssentiaRecipeBuilder lapisEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("lapis")
                                                      .ingredient(Tags.Items.GEMS_LAPIS)
                                                      .essentia(aqua, 10)
                                                      .essentia(terra, 10)
                                                      .build(consumer);
        EssentiaRecipeBuilder diamondEssentia = EssentiaRecipeBuilder.create()
                                                        .setRecipeName("diamond")
                                                        .ingredient(Tags.Items.GEMS_DIAMOND)
                                                        .essentia(ignis, 1500)
                                                        .essentia(terra, 1000)
                                                        .build(consumer);
        EssentiaRecipeBuilder emeraldEssentia = EssentiaRecipeBuilder.create()
                                                        .setRecipeName("emerald")
                                                        .ingredient(Tags.Items.GEMS_EMERALD)
                                                        .essentia(aqua, 1000)
                                                        .essentia(ignis, 1500)
                                                        .essentia(terra, 1500)
                                                        .build(consumer);
        EssentiaRecipeBuilder prismarineCrystalEssentia = EssentiaRecipeBuilder.create()
                                                                  .setRecipeName("prismarine_crystal")
                                                                  .ingredient(Tags.Items.GEMS_PRISMARINE)
                                                                  .essentia(aqua, 500)
                                                                  .essentia(terra, 500)
                                                                  .build(consumer);

        //Vanilla Dusts
        EssentiaRecipeBuilder prismarineShardEssentia = EssentiaRecipeBuilder.create()
                                                                .setRecipeName("prismarine_shard")
                                                                .ingredient(Tags.Items.DUSTS_PRISMARINE)
                                                                .essentia(aqua, 500)
                                                                .essentia(terra, 500)
                                                                .build(consumer);
        EssentiaRecipeBuilder glowstoneDustEssentia = EssentiaRecipeBuilder.create()
                                                              .setRecipeName("glowstone_dust")
                                                              .ingredient(Tags.Items.DUSTS_GLOWSTONE)
                                                              .essentia(aer, 10)
                                                              .essentia(ignis, 10)
                                                              .build(consumer);
        EssentiaRecipeBuilder redstoneDustEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("redstone_dust")
                                                             .ingredient(Tags.Items.DUSTS_REDSTONE)
                                                             .essentia(ignis, 10)
                                                             .essentia(terra, 10)
                                                             .build(consumer);

        //Coal
        EssentiaRecipeBuilder coalEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("coal")
                                                     .ingredient(Items.COAL)
                                                     .essentia(ignis, 10)
                                                     .build(consumer);
        EssentiaRecipeBuilder charcoalEssentia = EssentiaRecipeBuilder.create()
                                                         .setRecipeName("charcoal")
                                                         .ingredient(Items.CHARCOAL)
                                                         .essentia(ignis, 10)
                                                         .build(consumer);
        //Vanilla Blocks
        EssentiaRecipeBuilder stoneEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("stone")
                                                      .ingredient(Tags.Items.STONE)
                                                      .essentia(terra, 1)
                                                      .build(consumer);
        EssentiaRecipeBuilder obsidianEssentia = EssentiaRecipeBuilder.create()
                                                         .setRecipeName("obsidian")
                                                         .ingredient(Tags.Items.OBSIDIAN)
                                                         .essentia(terra, 10)
                                                         .build(consumer);

        //Vanilla Loot
        EssentiaRecipeBuilder boneEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("bone")
                                                     .ingredient(Items.BONE)
                                                     .essentia(aer, 10)
                                                     .essentia(terra, 10)
                                                     .build(consumer);
        EssentiaRecipeBuilder slimeBallEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("slime_ball")
                                                          .ingredient(Items.SLIME_BALL)
                                                          .essentia(terra, 10)
                                                          .essentia(aqua, 10)
                                                          .build(consumer);
        //Theurgy Items
        EssentiaRecipeBuilder aerCrystalEssentia = EssentiaRecipeBuilder.create()
                                                           .setRecipeName("aer_crystal")
                                                           .ingredient(BlockRegistry.AER_CRYSTAL.get())
                                                           .essentia(aer, 1500)
                                                           .build(consumer);
        EssentiaRecipeBuilder aquaCrystalEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("aqua_crystal")
                                                            .ingredient(BlockRegistry.AQUA_CRYSTAL.get())
                                                            .essentia(aqua, 1500)
                                                            .build(consumer);
        EssentiaRecipeBuilder ignisCrystalEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("ignis_crystal")
                                                             .ingredient(BlockRegistry.IGNIS_CRYSTAL.get())
                                                             .essentia(ignis, 1500)
                                                             .build(consumer);
        EssentiaRecipeBuilder terraCrystalEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("terra_crystal")
                                                             .ingredient(BlockRegistry.TERRA_CRYSTAL.get())
                                                             .essentia(terra, 1500)
                                                             .build(consumer);

        //Purification recipes
        //Modded Ores
        CrucibleRecipeBuilder copperOrePurification = CrucibleRecipeBuilder.purification(COPPER_INGOT, 2)
                                                              .setRecipeName("copper_ore")
                                                              .ingredient(COPPER_ORE)
                                                              .essentia(terra, 10);
        this.registerTagExistsCondition(copperOrePurification, COPPER_ORE, consumer);

        CrucibleRecipeBuilder tinOrePurification = CrucibleRecipeBuilder.purification(TIN_INGOT, 2)
                                                           .setRecipeName("tin_ore")
                                                           .ingredient(TIN_ORE)
                                                           .essentia(terra, 10);
        this.registerTagExistsCondition(tinOrePurification, TIN_ORE, consumer);

        CrucibleRecipeBuilder silverOrePurification = CrucibleRecipeBuilder.purification(SILVER_INGOT, 2)
                                                              .setRecipeName("silver_ore")
                                                              .ingredient(SILVER_INGOT)
                                                              .essentia(terra, 10);
        this.registerTagExistsCondition(silverOrePurification, SILVER_INGOT, consumer);

        //Vanilla Ores
        CrucibleRecipeBuilder ironOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.INGOTS_IRON, 2)
                                                            .setRecipeName("iron_ore")
                                                            .ingredient(Tags.Items.ORES_IRON)
                                                            .essentia(terra, 10)
                                                            .build(consumer);
        CrucibleRecipeBuilder goldOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.INGOTS_GOLD, 2)
                                                            .setRecipeName("gold_ore")
                                                            .ingredient(Tags.Items.ORES_GOLD)
                                                            .essentia(terra, 10)
                                                            .build(consumer);
        CrucibleRecipeBuilder coalOrePurification = CrucibleRecipeBuilder.purification(Items.COAL, 2)
                                                            .setRecipeName("coal_ore")
                                                            .ingredient(Tags.Items.ORES_COAL)
                                                            .essentia(terra, 10)
                                                            .build(consumer);
        CrucibleRecipeBuilder diamondOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.GEMS_DIAMOND, 2)
                                                               .setRecipeName("diamond_ore")
                                                               .ingredient(Tags.Items.ORES_DIAMOND)
                                                               .essentia(terra, 10)
                                                               .build(consumer);
        CrucibleRecipeBuilder emeraldOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.GEMS_EMERALD, 2)
                                                               .setRecipeName("emerald_ore")
                                                               .ingredient(Tags.Items.ORES_EMERALD)
                                                               .essentia(terra, 10)
                                                               .build(consumer);
        CrucibleRecipeBuilder lapisOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.GEMS_LAPIS, 15)
                                                             .setRecipeName("lapis_ore")
                                                             .ingredient(Tags.Items.ORES_LAPIS)
                                                             .essentia(terra, 10)
                                                             .build(consumer);
        CrucibleRecipeBuilder quartzOrePurification = CrucibleRecipeBuilder.purification(Tags.Items.GEMS_QUARTZ, 2)
                                                              .setRecipeName("quartz_ore")
                                                              .ingredient(Tags.Items.ORES_QUARTZ)
                                                              .essentia(terra, 10)
                                                              .build(consumer);
        CrucibleRecipeBuilder redstoneOrePurification =
                CrucibleRecipeBuilder.purification(Tags.Items.DUSTS_REDSTONE, 10)
                        .setRecipeName("redstone_ore")
                        .ingredient(Tags.Items.ORES_REDSTONE)
                        .essentia(terra, 10)
                        .build(consumer);

        //Other purification
        CrucibleRecipeBuilder dirtToClayBallPurification = CrucibleRecipeBuilder.purification(Items.CLAY_BALL, 2)
                                                                   .setRecipeName("dirt_to_clay_ball")
                                                                   .ingredient(Items.DIRT)
                                                                   .essentia(aqua, 10)
                                                                   .build(consumer);

        CrucibleRecipeBuilder pureCrystalPurification =
                CrucibleRecipeBuilder.purification(ItemRegistry.PURE_CRYSTAL_STIRRER.get(), 1)
                        .setRecipeName("pure_crystal")
                        .ingredient(ItemRegistry.CRYSTAL_SUBSTRATE
                                            .get())
                        .essentia(aer, 100)
                        .essentia(aqua, 100)
                        .essentia(ignis, 100)
                        .essentia(terra, 100)
                        .build(consumer);

        //Replication recipes
        //Modded Ingots
        CrucibleRecipeBuilder copperIngotReplication = CrucibleRecipeBuilder.replication(COPPER_INGOT, 2)
                                                               .setRecipeName("copper_ingot")
                                                               .ingredient(COPPER_INGOT)
                                                               .essentia(copperIngotEssentia.essentia);
        this.registerTagExistsCondition(copperIngotReplication, COPPER_INGOT, consumer);

        CrucibleRecipeBuilder tinIngotReplication = CrucibleRecipeBuilder.replication(TIN_INGOT, 2)
                                                            .setRecipeName("tin_ingot")
                                                            .ingredient(TIN_INGOT)
                                                            .essentia(tinIngotEssentia.essentia);
        this.registerTagExistsCondition(tinIngotReplication, TIN_INGOT, consumer);

        CrucibleRecipeBuilder silverIngotReplication = CrucibleRecipeBuilder.replication(SILVER_INGOT, 2)
                                                               .setRecipeName("silver_ingot")
                                                               .ingredient(SILVER_INGOT)
                                                               .essentia(silverIngotEssentia.essentia);
        this.registerTagExistsCondition(silverIngotReplication, SILVER_INGOT, consumer);

        //Vanilla Ingots
        CrucibleRecipeBuilder ironIngotReplication = CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_IRON, 2)
                                                             .setRecipeName("iron_ingot")
                                                             .ingredient(Tags.Items.INGOTS_IRON)
                                                             .essentia(ironIngotEssentia.essentia)
                                                             .build(consumer);
        CrucibleRecipeBuilder goldIngotReplication = CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_GOLD, 2)
                                                             .setRecipeName("gold_ingot")
                                                             .ingredient(Tags.Items.INGOTS_GOLD)
                                                             .essentia(goldIngotEssentia.essentia)
                                                             .build(consumer);
        CrucibleRecipeBuilder netheriteIngotReplication =
                CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_NETHERITE, 2)
                        .setRecipeName("netherite_ingot")
                        .ingredient(Tags.Items.INGOTS_NETHERITE)
                        .essentia(netheriteIngotEssentia.essentia)
                        .build(consumer);

        //Coal
        CrucibleRecipeBuilder coalReplication = CrucibleRecipeBuilder.replication(Items.COAL, 2)
                                                        .setRecipeName("coal")
                                                        .ingredient(Items.COAL)
                                                        .essentia(coalEssentia.essentia)
                                                        .build(consumer);
        CrucibleRecipeBuilder charcoalReplication = CrucibleRecipeBuilder.replication(Items.CHARCOAL, 2)
                                                            .setRecipeName("charcoal")
                                                            .ingredient(Items.CHARCOAL)
                                                            .essentia(charcoalEssentia.essentia)
                                                            .build(consumer);

        //Vanilla Gems
        CrucibleRecipeBuilder quartzReplication = CrucibleRecipeBuilder.replication(Tags.Items.GEMS_QUARTZ, 2)
                                                          .setRecipeName("quartz")
                                                          .ingredient(Tags.Items.GEMS_QUARTZ)
                                                          .essentia(quartzEssentia.essentia)
                                                          .build(consumer);
        CrucibleRecipeBuilder lapisReplication = CrucibleRecipeBuilder.replication(Tags.Items.GEMS_LAPIS, 2)
                                                         .setRecipeName("lapis")
                                                         .ingredient(Tags.Items.GEMS_LAPIS)
                                                         .essentia(lapisEssentia.essentia)
                                                         .build(consumer);
        CrucibleRecipeBuilder prismarineCrystalReplication =
                CrucibleRecipeBuilder.replication(Tags.Items.GEMS_PRISMARINE, 2)
                        .setRecipeName("prismarine_crystal")
                        .ingredient(Tags.Items.GEMS_PRISMARINE)
                        .essentia(prismarineCrystalEssentia.essentia)
                        .build(consumer);
        CrucibleRecipeBuilder diamondReplication = CrucibleRecipeBuilder.replication(Tags.Items.GEMS_DIAMOND, 2)
                                                           .setRecipeName("diamond")
                                                           .ingredient(Tags.Items.GEMS_DIAMOND)
                                                           .essentia(diamondEssentia.essentia)
                                                           .build(consumer);
        CrucibleRecipeBuilder emeraldReplication = CrucibleRecipeBuilder.replication(Tags.Items.GEMS_EMERALD, 2)
                                                           .setRecipeName("emerald")
                                                           .ingredient(Tags.Items.GEMS_EMERALD)
                                                           .essentia(emeraldEssentia.essentia)
                                                           .build(consumer);

        //Vanilla Dusts
        CrucibleRecipeBuilder redstoneReplication = CrucibleRecipeBuilder.replication(Tags.Items.DUSTS_REDSTONE, 2)
                                                            .setRecipeName("redstone_dust")
                                                            .ingredient(Tags.Items.DUSTS_REDSTONE)
                                                            .essentia(redstoneDustEssentia.essentia)
                                                            .build(consumer);
        CrucibleRecipeBuilder glowstoneReplication = CrucibleRecipeBuilder.replication(Tags.Items.DUSTS_GLOWSTONE, 2)
                                                             .setRecipeName("glowstone_dust")
                                                             .ingredient(Tags.Items.DUSTS_GLOWSTONE)
                                                             .essentia(glowstoneDustEssentia.essentia)
                                                             .build(consumer);
        CrucibleRecipeBuilder prismarineShardReplication =
                CrucibleRecipeBuilder.replication(Tags.Items.DUSTS_PRISMARINE, 2)
                        .setRecipeName("prismarine_shard")
                        .ingredient(Tags.Items.DUSTS_PRISMARINE)
                        .essentia(prismarineShardEssentia.essentia)
                        .build(consumer);

        //Vanilla Blocks
        CrucibleRecipeBuilder obsidianReplication = CrucibleRecipeBuilder.replication(Tags.Items.OBSIDIAN, 2)
                                                            .setRecipeName("obsidian")
                                                            .ingredient(Tags.Items.OBSIDIAN)
                                                            .essentia(obsidianEssentia.essentia)
                                                            .build(consumer);

        //Other Items
        CrucibleRecipeBuilder boneReplication = CrucibleRecipeBuilder.replication(Tags.Items.BONES, 2)
                                                        .setRecipeName("bone")
                                                        .ingredient(Tags.Items.BONES)
                                                        .essentia(boneEssentia.essentia)
                                                        .build(consumer);
        CrucibleRecipeBuilder slimeBallReplication = CrucibleRecipeBuilder.replication(Tags.Items.SLIMEBALLS, 2)
                                                             .setRecipeName("slime_ball")
                                                             .ingredient(Tags.Items.SLIMEBALLS)
                                                             .essentia(slimeBallEssentia.essentia)
                                                             .build(consumer);

        //Transmutation recipes
        //Modded Ingots
        CrucibleRecipeBuilder copperIngotToGoldIngotTransmutation =
                CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_GOLD, 1)
                        .setRecipeName("copper_ingot_to_gold_ingot")
                        .ingredient(COPPER_INGOT)
                        .essentia(this.essentiaDifference(copperIngotEssentia.essentia, goldIngotEssentia.essentia));
        this.registerTagExistsCondition(copperIngotToGoldIngotTransmutation, SILVER_INGOT, consumer);

        CrucibleRecipeBuilder tinIngotToGoldIngotTransmutation =
                CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_GOLD, 1)
                        .setRecipeName("tin_ingot_to_gold_ingot")
                        .ingredient(TIN_INGOT)
                        .essentia(this.essentiaDifference(tinIngotEssentia.essentia, goldIngotEssentia.essentia));
        this.registerTagExistsCondition(tinIngotToGoldIngotTransmutation, TIN_INGOT, consumer);

        CrucibleRecipeBuilder silverIngotToGoldIngotTransmutation =
                CrucibleRecipeBuilder.replication(Tags.Items.INGOTS_GOLD, 1)
                        .setRecipeName("silver_ingot_to_gold_ingot")
                        .ingredient(SILVER_INGOT)
                        .essentia(this.essentiaDifference(silverIngotEssentia.essentia, goldIngotEssentia.essentia));
        this.registerTagExistsCondition(silverIngotToGoldIngotTransmutation, SILVER_INGOT, consumer);


        //Vanilla Ingots
        CrucibleRecipeBuilder ironIngotToGoldIngotTransmutation =
                CrucibleRecipeBuilder.transmutation(Tags.Items.INGOTS_GOLD, 1)
                        .setRecipeName("iron_ingot_to_gold_ingot")
                        .ingredient(Tags.Items.INGOTS_IRON)
                        .essentia(this.essentiaDifference(ironIngotEssentia.essentia, goldIngotEssentia.essentia))
                        .build(consumer);

        //Vanilla Gems
        CrucibleRecipeBuilder goldIngotToDiamondTransmutation =
                CrucibleRecipeBuilder.transmutation(Tags.Items.GEMS_DIAMOND, 1)
                        .setRecipeName("gold_ingot_to_diamond")
                        .ingredient(Tags.Items.INGOTS_GOLD)
                        .essentia(this.essentiaDifference(goldIngotEssentia.essentia, diamondEssentia.essentia))
                        .build(consumer);
        CrucibleRecipeBuilder diamondToEmeraldTransmutation =
                CrucibleRecipeBuilder.transmutation(Tags.Items.GEMS_EMERALD, 1)
                        .setRecipeName("diamond_to_emerald")
                        .ingredient(Tags.Items.GEMS_DIAMOND)
                        .essentia(this.essentiaDifference(diamondEssentia.essentia, emeraldEssentia.essentia))
                        .build(consumer);

        //Loot
        CrucibleRecipeBuilder slimeTransmutation = CrucibleRecipeBuilder.transmutation(Tags.Items.SLIMEBALLS, 1)
                                                           .setRecipeName("slime_ball")
                                                           .ingredient(Items.SPIDER_EYE)
                                                           .essentia(terra, 50)
                                                           .essentia(aqua, 50)
                                                           .build(consumer);
    }
    //endregion Overrides

    //region Methods
    protected void registerTagExistsCondition(EssentiaRecipeBuilder builder, ITag.INamedTag<Item> tag,
                                              Consumer<IFinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new NotCondition(new TagEmptyCondition(tag.getName())))
                .addRecipe(builder::build)
                .build(consumer, Theurgy.MODID, "essentia/" + builder.recipeName);
    }

    protected void registerTagExistsCondition(CrucibleRecipeBuilder builder, ITag.INamedTag<Item> tag,
                                              Consumer<IFinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new NotCondition(new TagEmptyCondition(tag.getName())))
                .addRecipe(builder::build)
                .build(consumer, Theurgy.MODID, builder.folder + "/" + builder.recipeName);
    }

    /**
     * Gets the remaining required essentia for an item
     *
     * @param inputEssentia the essentia contained in the input item.
     * @param inputEssentia the essentia contained in the desired output item.
     */
    protected List<ItemStack> essentiaDifference(List<ItemStack> inputEssentia, List<ItemStack> outputEssentia) {
        return essentiaDifference(inputEssentia, outputEssentia, 1.0f);
    }

    /**
     * Gets the remaining required essentia for an item
     *
     * @param inputEssentia            the essentia contained in the input item.
     * @param inputEssentia            the essentia contained in the desired output item.
     * @param outputEssentiaMultiplier a multiplier for the output essentia, usually to simulate loss / additional cost
     */
    protected List<ItemStack> essentiaDifference(List<ItemStack> inputEssentia, List<ItemStack> outputEssentia,
                                                 float outputEssentiaMultiplier) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack essentia : outputEssentia) {
            Optional<ItemStack> other = inputEssentia.stream()
                                                .filter(stack -> stack.getItem() == essentia.getItem())
                                                .findFirst();
            if (other.isPresent()) {
                //if input has matching essentia, subtract it from requirement
                ItemStack output = essentia.copy();
                int difference = Math.max(0, essentia.getCount() - other.get().getCount());
                output.setCount((int) (difference * outputEssentiaMultiplier));
                result.add(output);
            }
            else {
                //if no matching input essentia, just return the output essentia * outputEssentiaMultiplier
                ItemStack output = essentia.copy();
                output.setCount((int) (essentia.getCount() * outputEssentiaMultiplier));
                result.add(output);
            }
        }
        return result;
    }
    //endregion Methods
}
