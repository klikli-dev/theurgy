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
import net.minecraft.tags.ItemTags;
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

    public static final ITag.INamedTag<Item> COPPER_NUGGET = TagRegistry.makeForgeItemTag("nuggets/copper");
    public static final ITag.INamedTag<Item> TIN_NUGGET = TagRegistry.makeForgeItemTag("nuggets/tin");
    public static final ITag.INamedTag<Item> SILVER_NUGGET = TagRegistry.makeForgeItemTag("nuggets/silver");
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

        //Modded nuggets
        EssentiaRecipeBuilder copperNuggetEssentia = EssentiaRecipeBuilder.create()
                                                           .setRecipeName("copper_nugget")
                                                           .ingredient(COPPER_NUGGET)
                                                           .essentia(ignis, 10)
                                                           .essentia(terra, 10);
        this.registerTagExistsCondition(copperNuggetEssentia, COPPER_NUGGET, consumer);

        EssentiaRecipeBuilder tinNuggetEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("tin_nugget")
                                                             .ingredient(TIN_NUGGET)
                                                             .essentia(ignis, 10)
                                                             .essentia(terra, 10);
        this.registerTagExistsCondition(tinNuggetEssentia, TIN_NUGGET, consumer);

        EssentiaRecipeBuilder silverNuggetEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("silver_nugget")
                                                             .ingredient(SILVER_NUGGET)
                                                             .essentia(ignis, 15)
                                                             .essentia(terra, 15);
        this.registerTagExistsCondition(silverNuggetEssentia, SILVER_NUGGET, consumer);


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

        //Vanilla nuggets
        EssentiaRecipeBuilder ironNuggetEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("iron_nugget")
                                                          .ingredient(Tags.Items.NUGGETS_IRON)
                                                          .essentia(ignis, 10)
                                                          .essentia(terra, 10)
                                                          .build(consumer);
        EssentiaRecipeBuilder goldNuggetEssentia = EssentiaRecipeBuilder.create()
                                                           .setRecipeName("gold_nugget")
                                                           .ingredient(Tags.Items.NUGGETS_GOLD)
                                                           .essentia(aer, 5)
                                                           .essentia(ignis, 20)
                                                           .essentia(terra, 20)
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

        //Storage Blocks
        EssentiaRecipeBuilder coalBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("coal_block")
                                                          .ingredient(Tags.Items.STORAGE_BLOCKS_COAL)
                                                          .essentia(coalEssentia.essentia, 9)
                                                          .build(consumer);
        EssentiaRecipeBuilder ironBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("iron_block")
                                                          .ingredient(Tags.Items.STORAGE_BLOCKS_IRON)
                                                          .essentia(ironIngotEssentia.essentia, 9)
                                                          .build(consumer);
        EssentiaRecipeBuilder goldBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("gold_block")
                                                          .ingredient(Tags.Items.STORAGE_BLOCKS_GOLD)
                                                          .essentia(goldIngotEssentia.essentia, 9)
                                                          .build(consumer);
        EssentiaRecipeBuilder netheriteBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("netherite_block")
                                                          .ingredient(Tags.Items.STORAGE_BLOCKS_NETHERITE)
                                                          .essentia(netheriteIngotEssentia.essentia, 9)
                                                          .build(consumer);
        EssentiaRecipeBuilder quartzBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("quartz_block")
                                                          .ingredient(Tags.Items.STORAGE_BLOCKS_QUARTZ)
                                                          .essentia(quartzEssentia.essentia, 9)
                                                          .build(consumer);
        EssentiaRecipeBuilder lapisBlockEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("lapis_block")
                                                            .ingredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
                                                            .essentia(lapisEssentia.essentia, 9)
                                                            .build(consumer);
        EssentiaRecipeBuilder redstoneBlockEssentia = EssentiaRecipeBuilder.create()
                                                           .setRecipeName("redstone_block")
                                                           .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                                                           .essentia(redstoneDustEssentia.essentia, 9)
                                                           .build(consumer);
        EssentiaRecipeBuilder glowstoneBlockEssentia = EssentiaRecipeBuilder.create()
                                                              .setRecipeName("glowstone_block")
                                                              .ingredient(Items.GLOWSTONE)
                                                              .essentia(glowstoneDustEssentia.essentia, 4)
                                                              .build(consumer);
        EssentiaRecipeBuilder diamondBlockEssentia = EssentiaRecipeBuilder.create()
                                                               .setRecipeName("diamond_block")
                                                               .ingredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
                                                               .essentia(diamondEssentia.essentia, 9)
                                                               .build(consumer);
        EssentiaRecipeBuilder emeraldBlockEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("emerald_block")
                                                             .ingredient(Tags.Items.STORAGE_BLOCKS_EMERALD)
                                                             .essentia(emeraldEssentia.essentia, 9)
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
        EssentiaRecipeBuilder sandEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("sand")
                                                      .ingredient(Tags.Items.SAND)
                                                      .essentia(terra, 1)
                                                      .build(consumer);
        EssentiaRecipeBuilder dirtEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("dirt")
                                                     .ingredient(Items.DIRT)
                                                     .essentia(terra, 1)
                                                     .build(consumer);
        EssentiaRecipeBuilder coarseDirtEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("coarse_dirt")
                                                     .ingredient(Items.COARSE_DIRT)
                                                     .essentia(terra, 1)
                                                     .build(consumer);
        EssentiaRecipeBuilder sandstoneEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("sandstone")
                                                     .ingredient(Tags.Items.SANDSTONE)
                                                     .essentia(terra, 1)
                                                     .build(consumer);
        EssentiaRecipeBuilder clayBlockEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("clay_block")
                                                          .ingredient(Items.CLAY)
                                                          .essentia(terra, 4)
                                                          .essentia(aqua, 4)
                                                          .build(consumer);
        EssentiaRecipeBuilder cobblestoneEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("cobblestone")
                                                     .ingredient(Items.COBBLESTONE)
                                                     .essentia(terra, 1)
                                                     .build(consumer);
        EssentiaRecipeBuilder stoneBricksEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("stone_bricks")
                                                            .ingredient(Items.STONE_BRICKS)
                                                            .essentia(terra, 1)
                                                            .build(consumer);
        EssentiaRecipeBuilder mossyCobblestoneEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("mossy_cobblestone")
                                                            .ingredient(Items.MOSSY_COBBLESTONE)
                                                            .essentia(terra, 4)
                                                            .essentia(aer, 4)
                                                            .build(consumer);
        EssentiaRecipeBuilder mossyStoneBricksEssentia = EssentiaRecipeBuilder.create()
                                                                 .setRecipeName("mossy_stone_bricks")
                                                                 .ingredient(Items.MOSSY_STONE_BRICKS)
                                                                 .essentia(terra, 4)
                                                                 .essentia(aer, 4)
                                                                 .build(consumer);
        EssentiaRecipeBuilder netherrackEssentia = EssentiaRecipeBuilder.create()
                                                            .setRecipeName("netherrack")
                                                            .ingredient(Tags.Items.NETHERRACK)
                                                            .essentia(ignis, 1)
                                                            .build(consumer);
        EssentiaRecipeBuilder gravelEssentia = EssentiaRecipeBuilder.create()
                                                           .setRecipeName("gravel")
                                                           .ingredient(Tags.Items.GRAVEL)
                                                           .essentia(terra, 1)
                                                           .build(consumer);
        EssentiaRecipeBuilder glassEssentia = EssentiaRecipeBuilder.create()
                                                       .setRecipeName("glass")
                                                       .ingredient(Tags.Items.GLASS)
                                                       .essentia(terra, 1)
                                                       .essentia(ignis, 1)
                                                       .build(consumer);
        EssentiaRecipeBuilder glassPaneEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("glass_pane")
                                                      .ingredient(Tags.Items.GLASS)
                                                      .essentia(terra, 1)
                                                      .essentia(ignis, 1)
                                                      .build(consumer);
        EssentiaRecipeBuilder logEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("log")
                                                          .ingredient(ItemTags.LOGS)
                                                          .essentia(terra, 1)
                                                          .build(consumer);
        EssentiaRecipeBuilder plankEssentia = EssentiaRecipeBuilder.create()
                                                    .setRecipeName("plank")
                                                    .ingredient(ItemTags.PLANKS)
                                                    .essentia(terra, 1)
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
        EssentiaRecipeBuilder gunpowderEssentia = EssentiaRecipeBuilder.create()
                                                          .setRecipeName("gunpowder")
                                                          .ingredient(Items.GUNPOWDER)
                                                          .essentia(ignis, 20)
                                                          .build(consumer);
        //Vanilla Plants
        EssentiaRecipeBuilder saplingEssentia = EssentiaRecipeBuilder.create()
                                                             .setRecipeName("sapling")
                                                             .ingredient(ItemTags.SAPLINGS)
                                                             .essentia(terra, 2)
                                                             .build(consumer);
        EssentiaRecipeBuilder leavesEssentia = EssentiaRecipeBuilder.create()
                                                        .setRecipeName("leaves")
                                                        .ingredient(ItemTags.LEAVES)
                                                        .essentia(terra, 2)
                                                        .essentia(aer, 2)
                                                        .build(consumer);
        EssentiaRecipeBuilder seedsEssentia = EssentiaRecipeBuilder.create()
                                                       .setRecipeName("seeds")
                                                       .ingredient(Tags.Items.SEEDS)
                                                       .essentia(terra, 2)
                                                       .build(consumer);
        EssentiaRecipeBuilder cropsEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("crops")
                                                      .ingredient(Tags.Items.CROPS)
                                                      .essentia(terra, 2)
                                                      .build(consumer);

        //Misc Items
        EssentiaRecipeBuilder clayBallEssentia = EssentiaRecipeBuilder.create()
                                                         .setRecipeName("clay_ball")
                                                         .ingredient(Items.CLAY)
                                                         .essentia(terra, 1)
                                                         .essentia(aqua, 1)
                                                         .build(consumer);
        EssentiaRecipeBuilder brickEssentia = EssentiaRecipeBuilder.create()
                                                         .setRecipeName("brick")
                                                         .ingredient(Tags.Items.INGOTS_BRICK)
                                                         .essentia(terra, 1)
                                                         .essentia(ignis, 1)
                                                         .build(consumer);
        EssentiaRecipeBuilder netherBrickEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("nether_brick")
                                                      .ingredient(Tags.Items.INGOTS_NETHER_BRICK)
                                                      .essentia(ignis, 2)
                                                      .build(consumer);
        EssentiaRecipeBuilder featherEssentia = EssentiaRecipeBuilder.create()
                                                      .setRecipeName("feather")
                                                      .ingredient(Tags.Items.FEATHERS)
                                                      .essentia(aer, 30)
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

        //Vanilla Blocks
        CrucibleRecipeBuilder sandToClayBlockTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.CLAY, 1)
                        .setRecipeName("sand_to_clay_block")
                        .ingredient(Tags.Items.SAND)
                        .essentia(this.essentiaDifference(sandEssentia.essentia, clayBlockEssentia.essentia))
                        .build(consumer);

        CrucibleRecipeBuilder cobblestoneToMossyCobblestone =
                CrucibleRecipeBuilder.transmutation(Items.MOSSY_COBBLESTONE, 1)
                        .setRecipeName("cobblestone_to_mossy_cobblestone")
                        .ingredient(Items.COBBLESTONE)
                        .essentia(this.essentiaDifference(cobblestoneEssentia.essentia, mossyCobblestoneEssentia.essentia))
                        .build(consumer);

        CrucibleRecipeBuilder stoneBricksToMossyStoneBricksCobblestone =
                CrucibleRecipeBuilder.transmutation(Items.MOSSY_STONE_BRICKS, 1)
                        .setRecipeName("stone_bricks_to_mossy_stone_bricks")
                        .ingredient(Items.STONE_BRICKS)
                        .essentia(this.essentiaDifference(stoneBricksEssentia.essentia, mossyStoneBricksEssentia.essentia))
                        .build(consumer);


        //Vanilla Plants
        CrucibleRecipeBuilder oakToDarkOarkSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.DARK_OAK_SAPLING, 1)
                        .setRecipeName("oak_sapling_to_dark_oak_sapling")
                        .ingredient(Items.OAK_SAPLING)
                        .build(consumer);
        CrucibleRecipeBuilder darkOakToSpruceSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.SPRUCE_SAPLING, 1)
                        .setRecipeName("dark_oak_sapling_to_spruce_sapling")
                        .ingredient(Items.DARK_OAK_SAPLING)
                        .build(consumer);
        CrucibleRecipeBuilder spruceToJungleSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.JUNGLE_SAPLING, 1)
                        .setRecipeName("spruce_sapling_to_jungle_sapling")
                        .ingredient(Items.SPRUCE_SAPLING)
                        .build(consumer);
        CrucibleRecipeBuilder jungleToAcaciaSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.ACACIA_SAPLING, 1)
                        .setRecipeName("jungle_sapling_to_acacia_sapling")
                        .ingredient(Items.JUNGLE_SAPLING)
                        .build(consumer);
        CrucibleRecipeBuilder acaciaToBirchSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.BIRCH_SAPLING, 1)
                        .setRecipeName("acacia_sapling_to_birch_sapling")
                        .ingredient(Items.ACACIA_SAPLING)
                        .build(consumer);
        CrucibleRecipeBuilder birchToOakSaplingTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.OAK_SAPLING, 1)
                        .setRecipeName("birch_sapling_to_oak_sapling")
                        .ingredient(Items.BIRCH_SAPLING)
                        .build(consumer);

        CrucibleRecipeBuilder wheatSeedsToBeetrootSeedsTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.BEETROOT_SEEDS, 1)
                        .setRecipeName("wheat_seeds_to_beetroot_seeds")
                        .ingredient(Items.WHEAT_SEEDS)
                        .build(consumer);
        CrucibleRecipeBuilder beetrootSeedsToPumpkinSeedsTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.PUMPKIN_SEEDS, 1)
                        .setRecipeName("beetroot_seeds_to_pumpkin_seeds")
                        .ingredient(Items.BEETROOT_SEEDS)
                        .build(consumer);
        CrucibleRecipeBuilder pumpkinSeedsToMelonSeedsTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.MELON_SEEDS, 1)
                        .setRecipeName("pumpkin_seeds_to_melon_seeds")
                        .ingredient(Items.PUMPKIN_SEEDS)
                        .build(consumer);
        CrucibleRecipeBuilder melonSeedsToWheatSeedsTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.WHEAT_SEEDS, 1)
                        .setRecipeName("melon_seeds_to_wheat_seeds")
                        .ingredient(Items.MELON_SEEDS)
                        .build(consumer);

        CrucibleRecipeBuilder wheatToPotatoTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.POTATO, 1)
                        .setRecipeName("wheat_to_potato")
                        .ingredient(Items.WHEAT)
                        .build(consumer);
        CrucibleRecipeBuilder potatoToCarrotTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.CARROT, 1)
                        .setRecipeName("potato_to_carrot")
                        .ingredient(Items.POTATO)
                        .build(consumer);
        CrucibleRecipeBuilder carrotToWheatSeedsTransmutation =
                CrucibleRecipeBuilder.transmutation(Items.WHEAT_SEEDS, 1)
                        .setRecipeName("carrot_to_wheat_seeds")
                        .ingredient(Items.CARROT)
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
