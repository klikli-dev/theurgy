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
        this.makeRecipe(SulfurRegistry.BEETROOT.get(), Items.BEETROOT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CARROT.get(), Items.CARROT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.POTATO.get(), Items.POTATO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.WHEAT.get(), Items.WHEAT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.APPLE.get(), Items.APPLE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COCOA.get(), Items.COCOA_BEANS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NETHER_WART.get(), Items.NETHER_WART, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ARTICHOKE.get(), ItemTagRegistry.CROPS_ARTICHOKE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ASPARAGUS.get(), ItemTagRegistry.CROPS_ASPARAGUS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BARLEY.get(), ItemTagRegistry.CROPS_BARLEY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BASIL.get(), ItemTagRegistry.CROPS_BASIL, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BELLPEPPER.get(), ItemTagRegistry.CROPS_BELLPEPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BLACKBEAN.get(), ItemTagRegistry.CROPS_BLACKBEAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BLACKBERRY.get(), ItemTagRegistry.CROPS_BLACKBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BLUEBERRY.get(), ItemTagRegistry.CROPS_BLUEBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BROCCOLI.get(), ItemTagRegistry.CROPS_BROCCOLI, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CABBAGE.get(), ItemTagRegistry.CROPS_CABBAGE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CANTALOUPE.get(), ItemTagRegistry.CROPS_CANTALOUPE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CAULIFLOWER.get(), ItemTagRegistry.CROPS_CAULIFLOWER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CELERY.get(), ItemTagRegistry.CROPS_CELERY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CHILE_PEPPER.get(), ItemTagRegistry.CROPS_CHILE_PEPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COFFEE_BEANS.get(), ItemTagRegistry.CROPS_COFFEE_BEANS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CORN.get(), ItemTagRegistry.CROPS_CORN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRANBERRY.get(), ItemTagRegistry.CROPS_CRANBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CUCUMBER.get(), ItemTagRegistry.CROPS_CUCUMBER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CURRANT.get(), ItemTagRegistry.CROPS_CURRANT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.EGGPLANT.get(), ItemTagRegistry.CROPS_EGGPLANT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ELDERBERRY.get(), ItemTagRegistry.CROPS_ELDERBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GARLIC.get(), ItemTagRegistry.CROPS_GARLIC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GINGER.get(), ItemTagRegistry.CROPS_GINGER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GRAPE.get(), ItemTagRegistry.CROPS_GRAPE, salAmmoniac, 10);

        this.makeRecipe(SulfurRegistry.GREENBEAN.get(), ItemTagRegistry.CROPS_GREENBEAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GREENONION.get(), ItemTagRegistry.CROPS_GREENONION, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.HONEYDEW.get(), ItemTagRegistry.CROPS_HONEYDEW, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.HOPS.get(), ItemTagRegistry.CROPS_HOPS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.KALE.get(), ItemTagRegistry.CROPS_KALE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.KIWI.get(), ItemTagRegistry.CROPS_KIWI, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEEK.get(), ItemTagRegistry.CROPS_LEEK, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LETTUCE.get(), ItemTagRegistry.CROPS_LETTUCE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MUSTARD.get(), ItemTagRegistry.CROPS_MUSTARD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OAT.get(), ItemTagRegistry.CROPS_OAT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OLIVE.get(), ItemTagRegistry.CROPS_OLIVE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ONION.get(), ItemTagRegistry.CROPS_ONION, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PEANUT.get(), ItemTagRegistry.CROPS_PEANUT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PEPPER.get(), ItemTagRegistry.CROPS_PEPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PINEAPPLE.get(), ItemTagRegistry.CROPS_PINEAPPLE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RADISH.get(), ItemTagRegistry.CROPS_RADISH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RASPBERRY.get(), ItemTagRegistry.CROPS_RASPBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RHUBARB.get(), ItemTagRegistry.CROPS_RHUBARB, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RICE.get(), ItemTagRegistry.CROPS_RICE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RUTABAGA.get(), ItemTagRegistry.CROPS_RUTABAGA, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SAGUARO.get(), ItemTagRegistry.CROPS_SAGUARO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SOYBEAN.get(), ItemTagRegistry.CROPS_SOYBEAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SPINACH.get(), ItemTagRegistry.CROPS_SPINACH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SQUASH.get(), ItemTagRegistry.CROPS_SQUASH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.STRAWBERRY.get(), ItemTagRegistry.CROPS_STRAWBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SWEETPOTATO.get(), ItemTagRegistry.CROPS_SWEETPOTATO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TEA_LEAVES.get(), ItemTagRegistry.CROPS_TEA_LEAVES, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TOMATILLO.get(), ItemTagRegistry.CROPS_TOMATILLO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TOMATO.get(), ItemTagRegistry.CROPS_TOMATO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TURMERIC.get(), ItemTagRegistry.CROPS_TURMERIC, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TURNIP.get(), ItemTagRegistry.CROPS_TURNIP, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.VANILLA.get(), ItemTagRegistry.CROPS_VANILLA, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.YAM.get(), ItemTagRegistry.CROPS_YAM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ZUCCHINI.get(), ItemTagRegistry.CROPS_ZUCCHINI, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.FLAX.get(), ItemTagRegistry.CROPS_FLAX, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.JUNIPERBERRY.get(), ItemTagRegistry.CROPS_JUNIPERBERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ALMOND.get(), ItemTagRegistry.CROPS_ALMOND, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.APRICOT.get(), ItemTagRegistry.CROPS_APRICOT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.AVOCADO.get(), ItemTagRegistry.CROPS_AVOCADO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BANANA.get(), ItemTagRegistry.CROPS_BANANA, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CASHEW.get(), ItemTagRegistry.CROPS_CASHEW, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CHERRY.get(), ItemTagRegistry.CROPS_CHERRY, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COCONUT.get(), ItemTagRegistry.CROPS_COCONUT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DATE.get(), ItemTagRegistry.CROPS_DATE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.DRAGONFRUIT.get(), ItemTagRegistry.CROPS_DRAGONFRUIT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.FIG.get(), ItemTagRegistry.CROPS_FIG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GRAPEFRUIT.get(), ItemTagRegistry.CROPS_GRAPEFRUIT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.KUMQUAT.get(), ItemTagRegistry.CROPS_KUMQUAT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LEMON.get(), ItemTagRegistry.CROPS_LEMON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.LIME.get(), ItemTagRegistry.CROPS_LIME, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MANDARIN.get(), ItemTagRegistry.CROPS_MANDARIN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MANGO.get(), ItemTagRegistry.CROPS_MANGO, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NECTARINE.get(), ItemTagRegistry.CROPS_NECTARINE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.NUTMEG.get(), ItemTagRegistry.CROPS_NUTMEG, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ORANGE.get(), ItemTagRegistry.CROPS_ORANGE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PEACH.get(), ItemTagRegistry.CROPS_PEACH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PEAR.get(), ItemTagRegistry.CROPS_PEAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PECAN.get(), ItemTagRegistry.CROPS_PECAN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PERSIMMON.get(), ItemTagRegistry.CROPS_PERSIMMON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PLUM.get(), ItemTagRegistry.CROPS_PLUM, salAmmoniac, 10);

        //Common Mob Drops
        this.makeRecipe(SulfurRegistry.ROTTEN_FLESH.get(), Items.ROTTEN_FLESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SPIDER_EYE.get(), Items.SPIDER_EYE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.STRING.get(), Items.STRING, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GUNPOWDER.get(), Items.GUNPOWDER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BONE.get(), Items.BONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.ARROW.get(), Items.ARROW, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SLIMEBALL.get(), Items.SLIME_BALL, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.ENDER_PEARL.get(), Items.ENDER_PEARL, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.BLAZE_ROD.get(), Items.BLAZE_ROD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.PRISMARINE_SHARD.get(), Items.PRISMARINE_SHARD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.PHANTOM_MEMBRANE.get(), Items.PHANTOM_MEMBRANE, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.MAGMA_CREAM.get(), Items.MAGMA_CREAM, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SKELETON_SKULL.get(), Items.SKELETON_SKULL, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.WITHER_SKELETON_SKULL.get(), Items.WITHER_SKELETON_SKULL, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.GHAST_TEAR.get(), Items.GHAST_TEAR, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.SHULKER_SHELL.get(), Items.SHULKER_SHELL, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.ELYTRA.get(), Items.ELYTRA, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.NETHER_STAR.get(), Items.NETHER_STAR, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.DRAGON_EGG.get(), Items.DRAGON_EGG, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.HEART_OF_THE_SEA.get(), Items.HEART_OF_THE_SEA, salAmmoniac, 100);

        //Common Creature Parts
        this.makeRecipe(SulfurRegistry.PORKCHOP.get(), Items.PORKCHOP, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.BEEF.get(), Items.BEEF, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.MUTTON.get(), Items.MUTTON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CHICKEN.get(), Items.CHICKEN, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.EGG.get(), Items.EGG, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.INK_SAC.get(), Items.INK_SAC, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.GLOW_INK_SAC.get(), Items.GLOW_INK_SAC, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.RABBIT.get(), Items.RABBIT, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.RABBIT_HIDE.get(), Items.RABBIT_HIDE, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.RABBIT_FOOT.get(), Items.RABBIT_FOOT, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.LEATHER.get(), Items.LEATHER, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FEATHER.get(), Items.FEATHER, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.WOOL.get(), ItemTags.WOOL, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.COD.get(), Items.COD, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SALMON.get(), Items.SALMON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TROPICAL_FISH.get(), Items.TROPICAL_FISH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.PUFFERFISH.get(), Items.PUFFERFISH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.TURTLE_SCUTE.get(), Items.TURTLE_SCUTE, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.ARMADILLO_SCUTE.get(), Items.ARMADILLO_SCUTE, salAmmoniac, 15);

        //Common Earthen Materials
        this.makeRecipe(SulfurRegistry.DIRT.get(), Items.DIRT, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.COARSE_DIRT.get(), Items.COARSE_DIRT, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.PODZOL.get(), Items.PODZOL, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.GRASS_BLOCK.get(), Items.GRASS_BLOCK, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.ROOTED_DIRT.get(), Items.ROOTED_DIRT, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.MOSS_BLOCK.get(), Items.MOSS_BLOCK, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.MUD.get(), Items.MUD, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.MUDDY_MANGROVE_ROOTS.get(), Items.MUDDY_MANGROVE_ROOTS, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.SAND.get(), Tags.Items.SANDS_COLORLESS, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.RED_SAND.get(), Tags.Items.SANDS_RED, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.GRAVEL.get(), Items.GRAVEL, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.NETHERRACK.get(), Tags.Items.NETHERRACKS, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.SOUL_SAND.get(), Items.SOUL_SAND, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.SOUL_SOIL.get(), Items.SOUL_SOIL, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.STONE.get(), Items.STONE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.INFESTED_STONE.get(), Items.INFESTED_STONE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.COBBLESTONE.get(), Tags.Items.COBBLESTONES_NORMAL, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.COBBLESTONE_MOSSY.get(), Tags.Items.COBBLESTONES_INFESTED, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.COBBLESTONES_INFESTED.get(), Tags.Items.COBBLESTONES_MOSSY, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.DEEPSLATE.get(), Items.DEEPSLATE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.COBBLESTONE_DEEPSLATE.get(), Tags.Items.COBBLESTONES_DEEPSLATE, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.GRANITE.get(), Items.GRANITE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.DIORITE.get(), Items.DIORITE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.ANDESITE.get(), Items.ANDESITE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.BLACKSTONE.get(), Items.BLACKSTONE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.BASALT.get(), Items.BASALT, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.SANDSTONE.get(), Items.SANDSTONE, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.RED_SANDSTONE.get(), Items.RED_SANDSTONE, salAmmoniac, 1);

        this.makeRecipe(SulfurRegistry.CLAY.get(), Items.CLAY_BALL, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.TERRACOTTA.get(), ItemTags.TERRACOTTA, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.CRIMSON_NYLIUM.get(), Items.CRIMSON_NYLIUM, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.WARPED_NYLIUM.get(), Items.WARPED_NYLIUM, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.END_STONE.get(), Tags.Items.END_STONES, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.PURPUR_BLOCK.get(), Items.PURPUR_BLOCK, salAmmoniac, 1);
        this.makeRecipe(SulfurRegistry.MYCELIUM.get(), Items.MYCELIUM, salAmmoniac, 1);

        //Common Metals Ore Sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 5, Tags.Items.ORES_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 5, Tags.Items.ORES_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 5, Tags.Items.ORES_GOLD, salAmmoniac, 15);
        //netherite has a custom recipe in vanilla, 4 scraps per ingot, so we only do 1 sulfur here to avoid insane duplication rates
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.ORES_NETHERITE_SCRAP, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 5, ItemTagRegistry.ORES_URANIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.URANINITE.get(), 5, ItemTagRegistry.ORES_URANINITE_POOR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.URANINITE.get(), 7, ItemTagRegistry.ORES_URANINITE_REGULAR, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.URANINITE.get(), 13, ItemTagRegistry.ORES_URANINITE_DENSE, salAmmoniac, 20);
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
        this.makeRecipe(SulfurRegistry.ALUMINUM.get(), 5, ItemTagRegistry.ORES_ALUMINUM, salAmmoniac, 10);
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
        this.makeRecipe(SulfurRegistry.DARK_GEM.get(), 6, ItemTagRegistry.ORES_DARK_GEM, salAmmoniac, 15);
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
        this.makeRecipe(SulfurRegistry.URANINITE.get(), 3, ItemTagRegistry.RAW_MATERIALS_URANINITE, salAmmoniac, 10);
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
        this.makeRecipe(SulfurRegistry.ALUMINUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_ALUMINUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 3, ItemTagRegistry.RAW_MATERIALS_CINNABAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 3, ItemTagRegistry.RAW_MATERIALS_CRIMSON_IRON, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_PLATINUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 3, ItemTagRegistry.RAW_MATERIALS_VIBRANIUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.DESH.get(), 5, ItemTagRegistry.RAW_MATERIALS_DESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSTRUM.get(), 5, ItemTagRegistry.RAW_MATERIALS_OSTRUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CALORITE.get(), 5, ItemTagRegistry.RAW_MATERIALS_CALORITE, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.IESNIUM.get(), 5, ItemTagRegistry.RAW_MATERIALS_IESNIUM, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.DEMONITE.get(), 3, ItemTagRegistry.RAW_MATERIALS_DEMONITE, salAmmoniac, 50);

        //Common Metal Ingots sulfurs
        this.makeRecipe(SulfurRegistry.IRON.get(), 1, Tags.Items.INGOTS_IRON, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COPPER.get(), 1, Tags.Items.INGOTS_COPPER, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GOLD.get(), 1, Tags.Items.INGOTS_GOLD, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NETHERITE.get(), 1, Tags.Items.INGOTS_NETHERITE, salAmmoniac, 100);
        this.makeRecipe(SulfurRegistry.URANIUM.get(), 1, ItemTagRegistry.INGOTS_URANIUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.URANINITE.get(), 1, ItemTagRegistry.INGOTS_URANINITE, salAmmoniac, 10);
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
        this.makeRecipe(SulfurRegistry.ALUMINUM.get(), 1, ItemTagRegistry.INGOTS_ALUMINUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CINNABAR.get(), 1, ItemTagRegistry.INGOTS_CINNABAR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CRIMSON_IRON.get(), 1, ItemTagRegistry.INGOTS_CRIMSON_IRON, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.PLATINUM.get(), 1, ItemTagRegistry.INGOTS_PLATINUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.VIBRANIUM.get(), 1, ItemTagRegistry.INGOTS_VIBRANIUM, salAmmoniac, 20);
        this.makeRecipe(SulfurRegistry.DESH.get(), 1, ItemTagRegistry.INGOTS_DESH, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.OSTRUM.get(), 1, ItemTagRegistry.INGOTS_OSTRUM, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.CALORITE.get(), 1, ItemTagRegistry.INGOTS_CALORITE, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.IESNIUM.get(), 1, ItemTagRegistry.INGOTS_IESNIUM, salAmmoniac, 50);
        this.makeRecipe(SulfurRegistry.DEMONITE.get(), 1, ItemTagRegistry.INGOTS_DEMONITE, salAmmoniac, 50);

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
        this.makeRecipe(SulfurRegistry.DARK_GEM.get(), 1, ItemTagRegistry.GEMS_DARK, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.SAL_AMMONIAC.get(), 1, ItemTagRegistry.GEMS_SAL_AMMONIAC, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.CERTUS_QUARTZ.get(), 1, ItemTagRegistry.GEMS_CERTUS_QUARTZ, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.FLUIX.get(), 1, ItemTagRegistry.GEMS_FLUIX, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.NITER.get(), 1, ItemTagRegistry.GEMS_NITER, salAmmoniac, 15);
        this.makeRecipe(SulfurRegistry.CHIMERITE.get(), 1, ItemTagRegistry.GEMS_CHIMERITE, salAmmoniac, 100);

        //Other Common Minerals Sulfurs
        this.makeRecipe(SulfurRegistry.REDSTONE.get(), 1, Tags.Items.DUSTS_REDSTONE, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.COAL.get(), 1, ItemTags.COALS, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.SULFUR.get(), 1, ItemTagRegistry.GEMS_SULFUR, salAmmoniac, 10);
        this.makeRecipe(SulfurRegistry.GLOWSTONE.get(), 1, Tags.Items.DUSTS_GLOWSTONE, salAmmoniac, 10);
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
