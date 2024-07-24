// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class LiquefactionRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = LiquefactionRecipe.DEFAULT_TIME;

    public LiquefactionRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "liquefaction");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();

        //Logs
        this.makeRecipe(SulfurRegistry.OAK_LOG.get(), ItemTags.OAK_LOGS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SPRUCE_LOG.get(), Items.SPRUCE_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BIRCH_LOG.get(), Items.BIRCH_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.JUNGLE_LOG.get(), Items.JUNGLE_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ACACIA_LOG.get(), Items.ACACIA_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CHERRY_LOG.get(), Items.CHERRY_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DARK_OAK_LOG.get(), Items.DARK_OAK_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MANGROVE_LOG.get(), Items.MANGROVE_LOG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_STEM.get(), Items.CRIMSON_STEM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WARPED_STEM.get(), Items.WARPED_STEM, salAmmoniac, 10);

        this.makeRecipe(SulfurRegistry.ROWAN_LOG.get(), ItemTagRegistry.LOGS_ROWAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.FIR_LOG.get(), ItemTagRegistry.LOGS_FIR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.REDWOOD_LOG.get(), ItemTagRegistry.LOGS_REDWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MAHOGANY_LOG.get(), ItemTagRegistry.LOGS_MAHOGANY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.JACARANDA_LOG.get(), ItemTagRegistry.LOGS_JACARANDA, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PALM_LOG.get(), ItemTagRegistry.LOGS_PALM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WILLOW_LOG.get(), ItemTagRegistry.LOGS_WILLOW, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DEAD_LOG.get(), ItemTagRegistry.LOGS_DEAD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MAGIC_LOG.get(), ItemTagRegistry.LOGS_MAGIC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.UMBRAN_LOG.get(), ItemTagRegistry.LOGS_UMBRAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.HELLBARK_LOG.get(), ItemTagRegistry.LOGS_HELLBARK, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNAMON_LOG.get(), ItemTagRegistry.LOGS_CINNAMON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GLACIAN_LOG.get(), ItemTagRegistry.LOGS_GLACIAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ARCHWOOD_LOG.get(), ItemTagRegistry.LOGS_ARCHWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BLUEBRIGHT_LOG.get(), ItemTagRegistry.LOGS_BLUEBRIGHT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.STARLIT_LOG.get(), ItemTagRegistry.LOGS_STARLIT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.FROSTBRIGHT_LOG.get(), ItemTagRegistry.LOGS_FROSTBRIGHT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COMET_LOG.get(), ItemTagRegistry.LOGS_COMET, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LUNAR_LOG.get(), ItemTagRegistry.LOGS_LUNAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DUSK_LOG.get(), ItemTagRegistry.LOGS_DUSK, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MAPLE_LOG.get(), ItemTagRegistry.LOGS_MAPLE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRYSTALLIZED_LOG.get(), ItemTagRegistry.LOGS_CRYSTALLIZED, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LIVINGWOOD_LOG.get(), ItemTagRegistry.LOGS_LIVINGWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GLIMMERING_LIVINGWOOD_LOG.get(), ItemTagRegistry.LOGS_GLIMMERING_LIVINGWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DREAMWOOD_LOG.get(), ItemTagRegistry.LOGS_DREAMWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GLIMMERING_DREAMWOOD_LOG.get(), ItemTagRegistry.LOGS_DREAMWOOD_GLIMMERING, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WALNUT_LOG.get(), ItemTagRegistry.LOGS_WALNUT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.FIG_LOG.get(), ItemTagRegistry.LOGS_FIG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WOLFBERRY_LOG.get(), ItemTagRegistry.LOGS_WOLFBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ECHO_LOG.get(), ItemTagRegistry.LOGS_ECHO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ILLWOOD_LOG.get(), ItemTagRegistry.LOGS_ILLWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.UNDEAD_LOG.get(), ItemTagRegistry.LOGS_UNDEAD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AURUM_LOG.get(), ItemTagRegistry.LOGS_AURUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MENRIL_LOG.get(), ItemTagRegistry.LOGS_MENRIL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ASHEN_LOG.get(), ItemTagRegistry.LOGS_ASHEN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZALEA_LOG.get(), ItemTagRegistry.LOGS_AZALEA, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TRUMPET_LOG.get(), ItemTagRegistry.LOGS_TRUMPET, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NETHERWOOD_LOG.get(), ItemTagRegistry.LOGS_NETHERWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SKYROOT_LOG.get(), ItemTagRegistry.LOGS_SKYROOT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLDEN_OAK_LOG.get(), ItemTagRegistry.LOGS_GOLDEN_OAK, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TWILIGHT_OAK_LOG.get(), ItemTagRegistry.LOGS_TWILIGHT_OAK, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CANOPY_TREE_LOG.get(), ItemTagRegistry.LOGS_CANOPY_TREE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DARKWOOD_LOG.get(), ItemTagRegistry.LOGS_DARKWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIMEWOOD_LOG.get(), ItemTagRegistry.LOGS_TIMEWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TRANSWOOD_LOG.get(), ItemTagRegistry.LOGS_TRANSWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SORTINGWOOD_LOG.get(), ItemTagRegistry.LOGS_SORTINGWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MINEWOOD_LOG.get(), ItemTagRegistry.LOGS_MINEWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SMOGSTEM_LOG.get(), ItemTagRegistry.LOGS_SMOGSTEM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WIGGLEWOOD_LOG.get(), ItemTagRegistry.LOGS_WIGGLEWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GRONGLE_LOG.get(), ItemTagRegistry.LOGS_GRONGLE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUBBERWOOD_LOG.get(), ItemTagRegistry.LOGS_RUBBERWOOD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OTHERWORLD_LOG.get(), ItemTagRegistry.LOGS_OTHERWORLD, salAmmoniac, 10);

        //Crops
        this.makeRecipe(SulfurRegistry.WHEAT.get(), Items.WHEAT, salAmmoniac, 10);

        //Common Metals Ore Sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 5, Tags.Items.ORES_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 5, Tags.Items.ORES_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 5, Tags.Items.ORES_GOLD, salAmmoniac, 15);
        //netherite has a custom recipe in vanilla, 4 scraps per ingot, so we only do 1 sulfur here to avoid insane duplication rates
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.ORES_NETHERITE_SCRAP, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 5, ItemTagRegistry.ORES_URANIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 5, ItemTagRegistry.ORES_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 5, ItemTagRegistry.ORES_AZURE_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 5, ItemTagRegistry.ORES_ZINC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 5, ItemTagRegistry.ORES_OSMIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 5, ItemTagRegistry.ORES_NICKEL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 5, ItemTagRegistry.ORES_LEAD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 5, ItemTagRegistry.ORES_ALLTHEMODIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 5, ItemTagRegistry.ORES_UNOBTAINIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 5, ItemTagRegistry.ORES_IRIDIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 5, ItemTagRegistry.ORES_TIN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 5, ItemTagRegistry.ORES_CINNABAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 5, ItemTagRegistry.ORES_CRIMSON_IRON, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 5, ItemTagRegistry.ORES_PLATINUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 5, ItemTagRegistry.ORES_VIBRANIUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.DESH.get(), 5, ItemTagRegistry.ORES_DESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSTRUM.get(), 5, ItemTagRegistry.ORES_OSTRUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CALORITE.get(), 5, ItemTagRegistry.ORES_CALORITE, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.IESNIUM.get(), 5, ItemTagRegistry.ORES_IESNIUM, salAmmoniac, 50);

        //Common Gems Ore Sulfurs
        this.makeRecipe(SulfurRegistry.DIAMOND.get(), 4, Tags.Items.ORES_DIAMOND, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.EMERALD.get(), 4, Tags.Items.ORES_EMERALD, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.LAPIS.get(), 12, Tags.Items.ORES_LAPIS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.QUARTZ.get(), 10, Tags.Items.ORES_QUARTZ, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUBY.get(), 8, ItemTagRegistry.ORES_RUBY, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.APATITE.get(), 9, ItemTagRegistry.ORES_APATITE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PERIDOT.get(), 8, ItemTagRegistry.ORES_PERIDOT, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUORITE.get(), 8, ItemTagRegistry.ORES_FLUORITE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SAPPHIRE.get(), 8, ItemTagRegistry.ORES_SAPPHIRE, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SAL_AMMONIAC.get(), 6, ItemTagRegistry.ORES_SAL_AMMONIAC, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.CERTUS_QUARTZ.get(), 6, ItemTagRegistry.ORES_CERTUS_QUARTZ, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NITER.get(), 6, ItemTagRegistry.ORES_NITER, salAmmoniac, 15);

        //Other Common Mineral Ores
        this.makeRecipe(SulfurRegistry.REDSTONE.get(), 9, Tags.Items.ORES_REDSTONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COAL.get(), 4, Tags.Items.ORES_COAL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SULFUR.get(), 6, ItemTagRegistry.ORES_SULFUR, salAmmoniac, 10);

        //Common Raw Materials Sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 3, Tags.Items.RAW_MATERIALS_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 3, Tags.Items.RAW_MATERIALS_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 3, Tags.Items.RAW_MATERIALS_GOLD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_URANIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 3, ItemTagRegistry.RAW_MATERIALS_AZURE_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 3, ItemTagRegistry.RAW_MATERIALS_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 3, ItemTagRegistry.RAW_MATERIALS_ZINC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_OSMIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 3, ItemTagRegistry.RAW_MATERIALS_NICKEL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 3, ItemTagRegistry.RAW_MATERIALS_LEAD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_ALLTHEMODIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_UNOBTAINIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_IRIDIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 3, ItemTagRegistry.RAW_MATERIALS_TIN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 3, ItemTagRegistry.RAW_MATERIALS_CINNABAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 3, ItemTagRegistry.RAW_MATERIALS_CRIMSON_IRON, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_PLATINUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_VIBRANIUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.DESH.get(), 5, ItemTagRegistry.RAW_MATERIALS_DESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSTRUM.get(), 5, ItemTagRegistry.RAW_MATERIALS_OSTRUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CALORITE.get(), 5, ItemTagRegistry.RAW_MATERIALS_CALORITE, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.IESNIUM.get(), 5, ItemTagRegistry.RAW_MATERIALS_IESNIUM, salAmmoniac, 50);

        //Common Metal Ingots sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 1, Tags.Items.INGOTS_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 1, Tags.Items.INGOTS_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 1, Tags.Items.INGOTS_GOLD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.INGOTS_NETHERITE, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 1, ItemTagRegistry.INGOTS_URANIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SILVER.get(), 1, ItemTagRegistry.INGOTS_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AZURE_SILVER.get(), 1, ItemTagRegistry.INGOTS_AZURE_SILVER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZINC.get(), 1, ItemTagRegistry.INGOTS_ZINC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSMIUM.get(), 1, ItemTagRegistry.INGOTS_OSMIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NICKEL.get(), 1, ItemTagRegistry.INGOTS_NICKEL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEAD.get(), 1, ItemTagRegistry.INGOTS_LEAD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALLTHEMODIUM.get(), 1, ItemTagRegistry.INGOTS_ALLTHEMODIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.UNOBTAINIUM.get(), 1, ItemTagRegistry.INGOTS_UNOBTAINIUM, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.IRIDIUM.get(), 1, ItemTagRegistry.INGOTS_IRIDIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TIN.get(), 1, ItemTagRegistry.INGOTS_TIN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 1, ItemTagRegistry.INGOTS_CINNABAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 1, ItemTagRegistry.INGOTS_CRIMSON_IRON, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 1, ItemTagRegistry.INGOTS_PLATINUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 1, ItemTagRegistry.INGOTS_VIBRANIUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.DESH.get(), 5, ItemTagRegistry.INGOTS_DESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSTRUM.get(), 5, ItemTagRegistry.INGOTS_OSTRUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CALORITE.get(), 5, ItemTagRegistry.INGOTS_CALORITE, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.IESNIUM.get(), 5, ItemTagRegistry.INGOTS_IESNIUM, salAmmoniac, 50);

        //Common Gems Sulfurs
        this.makeRecipe(SulfurRegistry.DIAMOND.get(), 1, Tags.Items.GEMS_DIAMOND, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.EMERALD.get(), 1, Tags.Items.GEMS_EMERALD, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.LAPIS.get(), 1, Tags.Items.GEMS_LAPIS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.QUARTZ.get(), 1, Tags.Items.GEMS_QUARTZ, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AMETHYST.get(), 1, Tags.Items.GEMS_AMETHYST, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PRISMARINE.get(), 1, Tags.Items.GEMS_PRISMARINE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUBY.get(), 1, ItemTagRegistry.GEMS_RUBY, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.APATITE.get(), 1, ItemTagRegistry.GEMS_APATITE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PERIDOT.get(), 1, ItemTagRegistry.GEMS_PERIDOT, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUORITE.get(), 1, ItemTagRegistry.GEMS_FLUORITE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SAPPHIRE.get(), 1, ItemTagRegistry.GEMS_SAPPHIRE, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SAL_AMMONIAC.get(), 1, ItemTagRegistry.GEMS_SAL_AMMONIAC, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.CERTUS_QUARTZ.get(), 6, ItemTagRegistry.GEMS_CERTUS_QUARTZ, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUIX.get(), 6, ItemTagRegistry.GEMS_FLUIX, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NITER.get(), 6, ItemTagRegistry.GEMS_NITER, salAmmoniac, 15);

        //Other Common Minerals Sulfurs
        this.makeRecipe(SulfurRegistry.REDSTONE.get(), 1, Tags.Items.DUSTS_REDSTONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COAL.get(), 1, ItemTags.COALS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SULFUR.get(), 1, ItemTagRegistry.GEMS_SULFUR, salAmmoniac, 10);
    }


    public void makeRecipe(Item sulfurName, Item ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, TIME);
    }

    public void makeRecipe(Item sulfur, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        this.makeRecipe(sulfur, 1, ingredient, solvent, solventAmount, liquefactionTime);
    }

    public void makeRecipe(Item sulfur, int resultCount, Item ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfur, resultCount, ingredient, solvent, solventAmount, TIME);
    }

    public void makeRecipe(Item sulfur, int resultCount, Item ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        var name = this.name(sulfur);

        var recipe = new Builder(RecipeResult.of(new ItemStack(sulfur, resultCount)))
                .solvent(solvent, solventAmount)
                .ingredient(ingredient)
                .time(liquefactionTime)
                .build();

        this.recipeConsumer.accept(
                this.modLoc(name),
                recipe
        );
    }

    public void makeRecipe(Item sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_TIME);
    }

    public void makeRecipe(Item sulfurName, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        this.makeRecipe(sulfurName, 1, ingredient, solvent, solventAmount, liquefactionTime);
    }

    public void makeRecipe(Item sulfur, int resultCount, TagKey<Item> ingredient, Fluid solvent, int solventAmount) {
        this.makeRecipe(sulfur, resultCount, ingredient, solvent, solventAmount, LiquefactionRecipe.DEFAULT_TIME);
    }

    public void makeRecipe(Item sulfur, int resultCount, TagKey<Item> ingredient, Fluid solvent, int solventAmount, int liquefactionTime) {
        var name = this.name(sulfur) + "_from_" + this.name(ingredient);

        var recipe = new Builder(RecipeResult.of(new ItemStack(sulfur, resultCount)))
                .solvent(solvent, solventAmount)
                .ingredient(ingredient)
                .time(liquefactionTime)
                .build();

        this.recipeConsumer.accept(
                this.modLoc(name),
                recipe
        );
    }

    @Override
    public String getName() {
        return "Liquefaction Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {
        protected Builder(RecipeResult result) {
            super(RecipeTypeRegistry.LIQUEFACTION);
            this.result(result);
            this.time(TIME);
        }

        public Builder solvent(Fluid fluid, int amount) {
            return this.sizedFluidIngredient("solvent", fluid, amount);
        }
    }
}
