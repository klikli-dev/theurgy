// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class IncubationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = IncubationRecipe.DEFAULT_TIME;

    public IncubationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "incubation");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        //logs from sulfurs
        this.makeRecipe(Items.OAK_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.OAK_LOG.get());
        this.makeRecipe(Items.SPRUCE_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SPRUCE_LOG.get());
        this.makeRecipe(Items.BIRCH_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BIRCH_LOG.get());
        this.makeRecipe(Items.JUNGLE_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.JUNGLE_LOG.get());
        this.makeRecipe(Items.ACACIA_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ACACIA_LOG.get());
        this.makeRecipe(Items.CHERRY_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CHERRY_LOG.get());
        this.makeRecipe(Items.DARK_OAK_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DARK_OAK_LOG.get());
        this.makeRecipe(Items.MANGROVE_LOG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MANGROVE_LOG.get());
        this.makeRecipe(Items.CRIMSON_STEM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CRIMSON_STEM.get());
        this.makeRecipe(Items.WARPED_STEM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WARPED_STEM.get());

        this.makeRecipe(ItemTagRegistry.LOGS_ROWAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ROWAN_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_FIR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.FIR_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_REDWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.REDWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_MAHOGANY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MAHOGANY_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_JACARANDA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.JACARANDA_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_PALM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PALM_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_WILLOW, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WILLOW_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_DEAD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DEAD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_MAGIC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MAGIC_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_UMBRAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.UMBRAN_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_HELLBARK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.HELLBARK_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_CINNAMON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CINNAMON_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_GLACIAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GLACIAN_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_ARCHWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ARCHWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_BLUEBRIGHT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BLUEBRIGHT_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_STARLIT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.STARLIT_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_FROSTBRIGHT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.FROSTBRIGHT_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_COMET, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.COMET_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_LUNAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LUNAR_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_DUSK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DUSK_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_MAPLE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MAPLE_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_CRYSTALLIZED, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CRYSTALLIZED_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_LIVINGWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LIVINGWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_GLIMMERING_LIVINGWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GLIMMERING_LIVINGWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_DREAMWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DREAMWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_DREAMWOOD_GLIMMERING, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GLIMMERING_DREAMWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_WALNUT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WALNUT_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_FIG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.FIG_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_WOLFBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WOLFBERRY_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_ECHO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ECHO_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_ILLWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ILLWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_UNDEAD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.UNDEAD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_AURUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.AURUM_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_MENRIL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MENRIL_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_ASHEN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ASHEN_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_AZALEA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.AZALEA_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_TRUMPET, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TRUMPET_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_NETHERWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.NETHERWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_SKYROOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SKYROOT_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_GOLDEN_OAK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GOLDEN_OAK_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_TWILIGHT_OAK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TWILIGHT_OAK_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_CANOPY_TREE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CANOPY_TREE_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_DARKWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DARKWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_TIMEWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TIMEWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_TRANSWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TRANSWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_SORTINGWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SORTINGWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_MINEWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MINEWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_SMOGSTEM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SMOGSTEM_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_WIGGLEWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WIGGLEWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_GRONGLE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GRONGLE_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_RUBBERWOOD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RUBBERWOOD_LOG.get());
        this.makeRecipe(ItemTagRegistry.LOGS_OTHERWORLD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.OTHERWORLD_LOG.get());

        //crops from sulfurs
        this.makeRecipe(Items.BEETROOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BEETROOT.get());
        this.makeRecipe(Items.CARROT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CARROT.get());
        this.makeRecipe(Items.POTATO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.POTATO.get());
        this.makeRecipe(Items.WHEAT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.WHEAT.get());
        this.makeRecipe(Items.APPLE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.APPLE.get());
        this.makeRecipe(Items.COCOA_BEANS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.COCOA.get());
        this.makeRecipe(Items.NETHER_WART, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.NETHER_WART.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ARTICHOKE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ARTICHOKE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ASPARAGUS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ASPARAGUS.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BARLEY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BARLEY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BASIL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BASIL.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BELLPEPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BELLPEPPER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BLACKBEAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BLACKBEAN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BLACKBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BLACKBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BLUEBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BLUEBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BROCCOLI, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BROCCOLI.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CABBAGE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CABBAGE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CANTALOUPE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CANTALOUPE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CAULIFLOWER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CAULIFLOWER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CELERY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CELERY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CHILE_PEPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CHILE_PEPPER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_COFFEE_BEANS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.COFFEE_BEANS.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CORN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CORN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CRANBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CRANBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CUCUMBER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CUCUMBER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CURRANT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CURRANT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_EGGPLANT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.EGGPLANT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ELDERBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ELDERBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GARLIC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GARLIC.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GINGER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GINGER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GRAPE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GRAPE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GREENBEAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GREENBEAN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GREENONION, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GREENONION.get());
        this.makeRecipe(ItemTagRegistry.CROPS_HONEYDEW, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.HONEYDEW.get());
        this.makeRecipe(ItemTagRegistry.CROPS_HOPS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.HOPS.get());
        this.makeRecipe(ItemTagRegistry.CROPS_KALE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.KALE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_KIWI, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.KIWI.get());
        this.makeRecipe(ItemTagRegistry.CROPS_LEEK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LEEK.get());
        this.makeRecipe(ItemTagRegistry.CROPS_LETTUCE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LETTUCE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_MUSTARD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MUSTARD.get());
        this.makeRecipe(ItemTagRegistry.CROPS_OAT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.OAT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_OLIVE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.OLIVE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ONION, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ONION.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PEANUT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PEANUT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PEPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PEPPER.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PINEAPPLE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PINEAPPLE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_RADISH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RADISH.get());
        this.makeRecipe(ItemTagRegistry.CROPS_RASPBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RASPBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_RHUBARB, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RHUBARB.get());
        this.makeRecipe(ItemTagRegistry.CROPS_RICE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RICE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_RUTABAGA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.RUTABAGA.get());
        this.makeRecipe(ItemTagRegistry.CROPS_SAGUARO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SAGUARO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_SOYBEAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SOYBEAN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_SPINACH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SPINACH.get());
        this.makeRecipe(ItemTagRegistry.CROPS_SQUASH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SQUASH.get());
        this.makeRecipe(ItemTagRegistry.CROPS_STRAWBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.STRAWBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_SWEETPOTATO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.SWEETPOTATO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_TEA_LEAVES, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TEA_LEAVES.get());
        this.makeRecipe(ItemTagRegistry.CROPS_TOMATILLO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TOMATILLO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_TOMATO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TOMATO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_TURMERIC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TURMERIC.get());
        this.makeRecipe(ItemTagRegistry.CROPS_TURNIP, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.TURNIP.get());
        this.makeRecipe(ItemTagRegistry.CROPS_VANILLA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.VANILLA.get());
        this.makeRecipe(ItemTagRegistry.CROPS_YAM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.YAM.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ZUCCHINI, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ZUCCHINI.get());
        this.makeRecipe(ItemTagRegistry.CROPS_FLAX, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.FLAX.get());
        this.makeRecipe(ItemTagRegistry.CROPS_JUNIPERBERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.JUNIPERBERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ALMOND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ALMOND.get());
        this.makeRecipe(ItemTagRegistry.CROPS_APRICOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.APRICOT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_AVOCADO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.AVOCADO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_BANANA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.BANANA.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CASHEW, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CASHEW.get());
        this.makeRecipe(ItemTagRegistry.CROPS_CHERRY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.CHERRY.get());
        this.makeRecipe(ItemTagRegistry.CROPS_COCONUT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.COCONUT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_DATE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DATE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_DRAGONFRUIT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.DRAGONFRUIT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_FIG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.FIG.get());
        this.makeRecipe(ItemTagRegistry.CROPS_GRAPEFRUIT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.GRAPEFRUIT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_KUMQUAT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.KUMQUAT.get());
        this.makeRecipe(ItemTagRegistry.CROPS_LEMON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LEMON.get());
        this.makeRecipe(ItemTagRegistry.CROPS_LIME, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.LIME.get());
        this.makeRecipe(ItemTagRegistry.CROPS_MANDARIN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MANDARIN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_MANGO, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.MANGO.get());
        this.makeRecipe(ItemTagRegistry.CROPS_NECTARINE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.NECTARINE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_NUTMEG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.NUTMEG.get());
        this.makeRecipe(ItemTagRegistry.CROPS_ORANGE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.ORANGE.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PEACH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PEACH.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PEAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PEAR.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PECAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PECAN.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PERSIMMON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PERSIMMON.get());
        this.makeRecipe(ItemTagRegistry.CROPS_PLUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.PLANT.get(), SulfurRegistry.PLUM.get());

        //mob drops
        this.makeRecipe(Items.ROTTEN_FLESH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.ROTTEN_FLESH.get());
        this.makeRecipe(Items.SPIDER_EYE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.SPIDER_EYE.get());
        this.makeRecipe(Items.STRING, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.STRING.get());
        this.makeRecipe(Items.GUNPOWDER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.GUNPOWDER.get());
        this.makeRecipe(Items.BONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.BONE.get());
        this.makeRecipe(Items.ARROW, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.ARROW.get());
        this.makeRecipe(Items.SLIME_BALL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.SLIMEBALL.get());
        this.makeRecipe(Items.ENDER_PEARL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.ENDER_PEARL.get());
        this.makeRecipe(Items.BLAZE_ROD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.BLAZE_ROD.get());
        this.makeRecipe(Items.PRISMARINE_SHARD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.PRISMARINE_SHARD.get());
        this.makeRecipe(Items.PHANTOM_MEMBRANE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.PHANTOM_MEMBRANE.get());
        this.makeRecipe(Items.MAGMA_CREAM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.MAGMA_CREAM.get());
        this.makeRecipe(Items.SKELETON_SKULL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.SKELETON_SKULL.get());
        this.makeRecipe(Items.WITHER_SKELETON_SKULL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.WITHER_SKELETON_SKULL.get());
        this.makeRecipe(Items.GHAST_TEAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.GHAST_TEAR.get());
        this.makeRecipe(Items.SHULKER_SHELL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.SHULKER_SHELL.get());
        this.makeRecipe(Items.ELYTRA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.ELYTRA.get());
        this.makeRecipe(Items.NETHER_STAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.NETHER_STAR.get());
        this.makeRecipe(Items.DRAGON_EGG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.DRAGON_EGG.get());
        this.makeRecipe(Items.HEART_OF_THE_SEA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.HEART_OF_THE_SEA.get());

        //creature parts
        this.makeRecipe(Items.PORKCHOP, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.PORKCHOP.get());
        this.makeRecipe(Items.BEEF, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.BEEF.get());
        this.makeRecipe(Items.MUTTON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.MUTTON.get());
        this.makeRecipe(Items.CHICKEN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.CHICKEN.get());
        this.makeRecipe(Items.EGG, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.EGG.get());
        this.makeRecipe(Items.INK_SAC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.INK_SAC.get());
        this.makeRecipe(Items.GLOW_INK_SAC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.GLOW_INK_SAC.get());
        this.makeRecipe(Items.RABBIT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.RABBIT.get());
        this.makeRecipe(Items.RABBIT_HIDE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.RABBIT_HIDE.get());
        this.makeRecipe(Items.RABBIT_FOOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.RABBIT_FOOT.get());
        this.makeRecipe(Items.LEATHER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.LEATHER.get());
        this.makeRecipe(Items.FEATHER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.FEATHER.get());
        this.makeRecipe(Items.WHITE_WOOL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.WOOL.get());
        this.makeRecipe(Items.COD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.COD.get());
        this.makeRecipe(Items.SALMON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.SALMON.get());
        this.makeRecipe(Items.TROPICAL_FISH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.TROPICAL_FISH.get());
        this.makeRecipe(Items.PUFFERFISH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.PUFFERFISH.get());
        this.makeRecipe(Items.TURTLE_SCUTE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.TURTLE_SCUTE.get());
        this.makeRecipe(Items.ARMADILLO_SCUTE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.CREATURE.get(), SulfurRegistry.ARMADILLO_SCUTE.get());
        
        this.makeRecipe(Items.DIRT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.DIRT.get());
        this.makeRecipe(Items.COARSE_DIRT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.COARSE_DIRT.get());
        this.makeRecipe(Items.PODZOL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.PODZOL.get());
        this.makeRecipe(Items.GRASS_BLOCK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.GRASS_BLOCK.get());
        this.makeRecipe(Items.ROOTED_DIRT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.ROOTED_DIRT.get());
        this.makeRecipe(Items.MOSS_BLOCK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.MOSS_BLOCK.get());
        this.makeRecipe(Items.MUD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.MUD.get());
        this.makeRecipe(Items.MUDDY_MANGROVE_ROOTS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.MUDDY_MANGROVE_ROOTS.get());
        this.makeRecipe(Items.SAND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.SAND.get());
        this.makeRecipe(Items.RED_SAND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.RED_SAND.get());
        this.makeRecipe(Items.GRAVEL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.GRAVEL.get());
        this.makeRecipe(Items.NETHERRACK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.NETHERRACK.get());
        this.makeRecipe(Items.SOUL_SAND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.SOUL_SAND.get());
        this.makeRecipe(Items.SOUL_SOIL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.SOUL_SOIL.get());
        this.makeRecipe(Items.STONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.STONE.get());
        this.makeRecipe(Items.INFESTED_STONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.INFESTED_STONE.get());
        this.makeRecipe(Items.COBBLESTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.COBBLESTONE.get());
        this.makeRecipe(Items.MOSSY_COBBLESTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.COBBLESTONE_MOSSY.get());
        this.makeRecipe(Items.INFESTED_COBBLESTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.COBBLESTONES_INFESTED.get());
        this.makeRecipe(Items.DEEPSLATE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.DEEPSLATE.get());
        this.makeRecipe(Items.COBBLED_DEEPSLATE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.COBBLESTONE_DEEPSLATE.get());
        this.makeRecipe(Items.GRANITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.GRANITE.get());
        this.makeRecipe(Items.DIORITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.DIORITE.get());
        this.makeRecipe(Items.ANDESITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.ANDESITE.get());
        this.makeRecipe(Items.BLACKSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.BLACKSTONE.get());
        this.makeRecipe(Items.BASALT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.BASALT.get());
        this.makeRecipe(Items.SANDSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.SANDSTONE.get());
        this.makeRecipe(Items.RED_SANDSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.RED_SANDSTONE.get());
        this.makeRecipe(Items.CLAY_BALL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.CLAY.get());
        this.makeRecipe(Items.TERRACOTTA, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.TERRACOTTA.get());
        this.makeRecipe(Items.CRIMSON_NYLIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.CRIMSON_NYLIUM.get());
        this.makeRecipe(Items.WARPED_NYLIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.WARPED_NYLIUM.get());
        this.makeRecipe(Items.END_STONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.END_STONE.get());
        this.makeRecipe(Items.PURPUR_BLOCK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.PURPUR_BLOCK.get());
        this.makeRecipe(Items.MYCELIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.MYCELIUM.get());
        this.makeRecipe(Items.OBSIDIAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.OBSIDIAN.get());
        this.makeRecipe(Items.CRYING_OBSIDIAN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.CRYING_OBSIDIAN.get());
        this.makeRecipe(Items.SNOWBALL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.SNOW.get());
        this.makeRecipe(Items.ICE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.ICE.get());
        this.makeRecipe(Items.MAGMA_BLOCK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.MAGMA.get());
        this.makeRecipe(ItemRegistry.CRYSTALLIZED_WATER.get(), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.WATER.get());
        this.makeRecipe(ItemRegistry.CRYSTALLIZED_LAVA.get(), ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.STRATA.get(), SulfurRegistry.LAVA.get());

        //metal ingots from sulfurs
        this.makeRecipe(Tags.Items.INGOTS_IRON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRON.get());
        this.makeRecipe(Tags.Items.INGOTS_COPPER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COPPER.get());
        this.makeRecipe(Tags.Items.INGOTS_GOLD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.GOLD.get());
        this.makeRecipe(Tags.Items.INGOTS_NETHERITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NETHERITE.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_URANIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.URANIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_URANINITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.URANINITE.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_SILVER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SILVER.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_AZURE_SILVER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.AZURE_SILVER.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_ZINC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ZINC.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_OSMIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.OSMIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_NICKEL, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NICKEL.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_LEAD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LEAD.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_ALLTHEMODIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ALLTHEMODIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_UNOBTAINIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.UNOBTAINIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_IRIDIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IRIDIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_TIN, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.TIN.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_ALUMINUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.ALUMINUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CINNABAR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CINNABAR.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CRIMSON_IRON, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CRIMSON_IRON.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_PLATINUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PLATINUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_VIBRANIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.VIBRANIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_DESH, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DESH.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_OSTRUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.OSTRUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_CALORITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CALORITE.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_IESNIUM, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.IESNIUM.get());
        this.makeRecipe(ItemTagRegistry.INGOTS_DEMONITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DEMONITE.get());

        //gems from sulfurs
        this.makeRecipe(Tags.Items.GEMS_DIAMOND, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DIAMOND.get());
        this.makeRecipe(Tags.Items.GEMS_EMERALD, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.EMERALD.get());
        this.makeRecipe(Tags.Items.GEMS_LAPIS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.LAPIS.get());
        this.makeRecipe(Tags.Items.GEMS_QUARTZ, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.QUARTZ.get());
        this.makeRecipe(Tags.Items.GEMS_AMETHYST, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.AMETHYST.get());
        this.makeRecipe(Tags.Items.GEMS_PRISMARINE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PRISMARINE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_RUBY, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.RUBY.get());
        this.makeRecipe(ItemTagRegistry.GEMS_APATITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.APATITE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_PERIDOT, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.PERIDOT.get());
        this.makeRecipe(ItemTagRegistry.GEMS_FLUORITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.FLUORITE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_SAPPHIRE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SAPPHIRE.get());
        this.makeRecipe(ItemTagRegistry.GEMS_DARK, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.DARK_GEM.get());
        this.makeRecipe(ItemTagRegistry.GEMS_SAL_AMMONIAC, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SAL_AMMONIAC.get());
        this.makeRecipe(ItemTagRegistry.GEMS_CERTUS_QUARTZ, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CERTUS_QUARTZ.get());
        this.makeRecipe(ItemTagRegistry.GEMS_FLUIX, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.FLUIX.get());
        this.makeRecipe(ItemTagRegistry.GEMS_NITER, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.NITER.get());
        this.makeRecipe(ItemTagRegistry.GEMS_CHIMERITE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.CHIMERITE.get());

        //other common minerals from sulfur
        this.makeRecipe(Tags.Items.DUSTS_REDSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.REDSTONE.get());
        this.makeRecipe(ItemTags.COALS, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.COAL.get());
        this.makeRecipe(ItemTagRegistry.GEMS_SULFUR, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.SULFUR.get());
        this.makeRecipe(Tags.Items.DUSTS_GLOWSTONE, ItemRegistry.MERCURY_SHARD.get(), SaltRegistry.MINERAL.get(), SulfurRegistry.GLOWSTONE.get());
    }

    public void makeRecipe(TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, TIME);
    }

    public void makeRecipe(TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(TagKey<Item> result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(sulfur), result, resultCount, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, TagKey<Item> result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(recipeName, result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, TagKey<Item> result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        var recipe = new Builder(RecipeResult.of(result, resultCount))
                .mercury(mercury)
                .salt(salt)
                .sulfur(sulfur)
                .time(incubationTime)
                .build();

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );
    }

    public void makeRecipe(Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, TIME);
    }

    public void makeRecipe(Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(Item result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(this.name(result), result, resultCount, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, Item result, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        this.makeRecipe(recipeName, result, 1, mercury, salt, sulfur, incubationTime);
    }

    public void makeRecipe(String recipeName, Item result, int resultCount, Item mercury, AlchemicalSaltItem salt, AlchemicalSulfurItem sulfur, int incubationTime) {
        var recipe = new Builder(RecipeResult.of(new ItemStack(result, resultCount)))
                .mercury(mercury)
                .salt(salt)
                .sulfur(sulfur)
                .time(incubationTime)
                .build();

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );
    }


    @Override
    public String getName() {
        return "Incubation Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {
        protected Builder(RecipeResult result) {
            super(RecipeTypeRegistry.INCUBATION);
            this.result(result);
            this.time(TIME);
        }

        public Builder salt(AlchemicalSaltItem item) {
            return this.ingredient("salt", item);
        }

        public Builder mercury(Item item) {
            return this.ingredient("mercury", item);
        }

        public Builder sulfur(AlchemicalSulfurItem item) {
            return this.ingredient("sulfur", item);
        }
    }
}
