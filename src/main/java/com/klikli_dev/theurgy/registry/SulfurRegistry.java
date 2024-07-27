// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister.Items SULFURS = DeferredRegister.createItems(Theurgy.MODID);

    //Crops
    public static final DeferredItem<AlchemicalSulfurItem> BEETROOT = registerForSourceItem(Items.BEETROOT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CARROT = registerForSourceItem(Items.CARROT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> POTATO = registerForSourceItem(Items.POTATO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> WHEAT = registerForSourceItem(Items.WHEAT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> APPLE = registerForSourceItem(Items.APPLE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> COCOA = registerForSourceItem(Items.COCOA_BEANS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> NETHER_WART = registerForSourceItem(Items.NETHER_WART, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ARTICHOKE = registerForSourceTag(ItemTagRegistry.CROPS_ARTICHOKE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ASPARAGUS = registerForSourceTag(ItemTagRegistry.CROPS_ASPARAGUS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BARLEY = registerForSourceTag(ItemTagRegistry.CROPS_BARLEY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BASIL = registerForSourceTag(ItemTagRegistry.CROPS_BASIL, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BELLPEPPER = registerForSourceTag(ItemTagRegistry.CROPS_BELLPEPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BLACKBEAN = registerForSourceTag(ItemTagRegistry.CROPS_BLACKBEAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BLACKBERRY = registerForSourceTag(ItemTagRegistry.CROPS_BLACKBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BLUEBERRY = registerForSourceTag(ItemTagRegistry.CROPS_BLUEBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BROCCOLI = registerForSourceTag(ItemTagRegistry.CROPS_BROCCOLI, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CABBAGE = registerForSourceTag(ItemTagRegistry.CROPS_CABBAGE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CANTALOUPE = registerForSourceTag(ItemTagRegistry.CROPS_CANTALOUPE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CAULIFLOWER = registerForSourceTag(ItemTagRegistry.CROPS_CAULIFLOWER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CELERY = registerForSourceTag(ItemTagRegistry.CROPS_CELERY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CHILE_PEPPER = registerForSourceTag(ItemTagRegistry.CROPS_CHILE_PEPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> COFFEE_BEANS = registerForSourceTag(ItemTagRegistry.CROPS_COFFEE_BEANS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CORN = registerForSourceTag(ItemTagRegistry.CROPS_CORN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CRANBERRY = registerForSourceTag(ItemTagRegistry.CROPS_CRANBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CUCUMBER = registerForSourceTag(ItemTagRegistry.CROPS_CUCUMBER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CURRANT = registerForSourceTag(ItemTagRegistry.CROPS_CURRANT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> EGGPLANT = registerForSourceTag(ItemTagRegistry.CROPS_EGGPLANT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ELDERBERRY = registerForSourceTag(ItemTagRegistry.CROPS_ELDERBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> GARLIC = registerForSourceTag(ItemTagRegistry.CROPS_GARLIC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> GINGER = registerForSourceTag(ItemTagRegistry.CROPS_GINGER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> GRAPE = registerForSourceTag(ItemTagRegistry.CROPS_GRAPE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> GREENBEAN = registerForSourceTag(ItemTagRegistry.CROPS_GREENBEAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> GREENONION = registerForSourceTag(ItemTagRegistry.CROPS_GREENONION, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> HONEYDEW = registerForSourceTag(ItemTagRegistry.CROPS_HONEYDEW, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> HOPS = registerForSourceTag(ItemTagRegistry.CROPS_HOPS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> KALE = registerForSourceTag(ItemTagRegistry.CROPS_KALE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> KIWI = registerForSourceTag(ItemTagRegistry.CROPS_KIWI, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> LEEK = registerForSourceTag(ItemTagRegistry.CROPS_LEEK, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> LETTUCE = registerForSourceTag(ItemTagRegistry.CROPS_LETTUCE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> MUSTARD = registerForSourceTag(ItemTagRegistry.CROPS_MUSTARD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> OAT = registerForSourceTag(ItemTagRegistry.CROPS_OAT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> OLIVE = registerForSourceTag(ItemTagRegistry.CROPS_OLIVE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ONION = registerForSourceTag(ItemTagRegistry.CROPS_ONION, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PEANUT = registerForSourceTag(ItemTagRegistry.CROPS_PEANUT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PEPPER = registerForSourceTag(ItemTagRegistry.CROPS_PEPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PINEAPPLE = registerForSourceTag(ItemTagRegistry.CROPS_PINEAPPLE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> RADISH = registerForSourceTag(ItemTagRegistry.CROPS_RADISH, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> RASPBERRY = registerForSourceTag(ItemTagRegistry.CROPS_RASPBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> RHUBARB = registerForSourceTag(ItemTagRegistry.CROPS_RHUBARB, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> RICE = registerForSourceTag(ItemTagRegistry.CROPS_RICE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> RUTABAGA = registerForSourceTag(ItemTagRegistry.CROPS_RUTABAGA, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> SAGUARO = registerForSourceTag(ItemTagRegistry.CROPS_SAGUARO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> SOYBEAN = registerForSourceTag(ItemTagRegistry.CROPS_SOYBEAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> SPINACH = registerForSourceTag(ItemTagRegistry.CROPS_SPINACH, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> SQUASH = registerForSourceTag(ItemTagRegistry.CROPS_SQUASH, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> STRAWBERRY = registerForSourceTag(ItemTagRegistry.CROPS_STRAWBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> SWEETPOTATO = registerForSourceTag(ItemTagRegistry.CROPS_SWEETPOTATO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> TEA_LEAVES = registerForSourceTag(ItemTagRegistry.CROPS_TEA_LEAVES, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> TOMATILLO = registerForSourceTag(ItemTagRegistry.CROPS_TOMATILLO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> TOMATO = registerForSourceTag(ItemTagRegistry.CROPS_TOMATO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> TURMERIC = registerForSourceTag(ItemTagRegistry.CROPS_TURMERIC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> TURNIP = registerForSourceTag(ItemTagRegistry.CROPS_TURNIP, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> VANILLA = registerForSourceTag(ItemTagRegistry.CROPS_VANILLA, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> YAM = registerForSourceTag(ItemTagRegistry.CROPS_YAM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ZUCCHINI = registerForSourceTag(ItemTagRegistry.CROPS_ZUCCHINI, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> FLAX = registerForSourceTag(ItemTagRegistry.CROPS_FLAX, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> JUNIPERBERRY = registerForSourceTag(ItemTagRegistry.CROPS_JUNIPERBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ALMOND = registerForSourceTag(ItemTagRegistry.CROPS_ALMOND, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> APRICOT = registerForSourceTag(ItemTagRegistry.CROPS_APRICOT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> AVOCADO = registerForSourceTag(ItemTagRegistry.CROPS_AVOCADO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> BANANA = registerForSourceTag(ItemTagRegistry.CROPS_BANANA, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CASHEW = registerForSourceTag(ItemTagRegistry.CROPS_CASHEW, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> CHERRY = registerForSourceTag(ItemTagRegistry.CROPS_CHERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> COCONUT = registerForSourceTag(ItemTagRegistry.CROPS_COCONUT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> DATE = registerForSourceTag(ItemTagRegistry.CROPS_DATE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> DRAGONFRUIT = registerForSourceTag(ItemTagRegistry.CROPS_DRAGONFRUIT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> FIG = register("crops_fig", () -> AlchemicalSulfurItem.ofSource(ItemTagRegistry.CROPS_FIG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS));
    public static final DeferredItem<AlchemicalSulfurItem> GRAPEFRUIT = registerForSourceTag(ItemTagRegistry.CROPS_GRAPEFRUIT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> KUMQUAT = registerForSourceTag(ItemTagRegistry.CROPS_KUMQUAT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> LEMON = registerForSourceTag(ItemTagRegistry.CROPS_LEMON, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> LIME = registerForSourceTag(ItemTagRegistry.CROPS_LIME, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> MANDARIN = registerForSourceTag(ItemTagRegistry.CROPS_MANDARIN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> MANGO = registerForSourceTag(ItemTagRegistry.CROPS_MANGO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> NECTARINE = registerForSourceTag(ItemTagRegistry.CROPS_NECTARINE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> NUTMEG = registerForSourceTag(ItemTagRegistry.CROPS_NUTMEG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> ORANGE = registerForSourceTag(ItemTagRegistry.CROPS_ORANGE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PEACH = registerForSourceTag(ItemTagRegistry.CROPS_PEACH, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PEAR = registerForSourceTag(ItemTagRegistry.CROPS_PEAR, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PECAN = registerForSourceTag(ItemTagRegistry.CROPS_PECAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PERSIMMON = registerForSourceTag(ItemTagRegistry.CROPS_PERSIMMON, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final DeferredItem<AlchemicalSulfurItem> PLUM = registerForSourceTag(ItemTagRegistry.CROPS_PLUM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.CROPS);

    //Logs
    public static final DeferredItem<AlchemicalSulfurItem> OAK_LOG = registerForSourceItem(Items.OAK_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> SPRUCE_LOG = registerForSourceItem(Items.SPRUCE_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> BIRCH_LOG = registerForSourceItem(Items.BIRCH_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> JUNGLE_LOG = registerForSourceItem(Items.JUNGLE_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> ACACIA_LOG = registerForSourceItem(Items.ACACIA_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> CHERRY_LOG = registerForSourceItem(Items.CHERRY_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> DARK_OAK_LOG = registerForSourceItem(Items.DARK_OAK_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MANGROVE_LOG = registerForSourceItem(Items.MANGROVE_LOG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> CRIMSON_STEM = registerForSourceItem(Items.CRIMSON_STEM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> WARPED_STEM = registerForSourceItem(Items.WARPED_STEM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);

    public static final DeferredItem<AlchemicalSulfurItem> ROWAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ROWAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> FIR_LOG = registerForSourceTag(ItemTagRegistry.LOGS_FIR, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> REDWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_REDWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MAHOGANY_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAHOGANY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> JACARANDA_LOG = registerForSourceTag(ItemTagRegistry.LOGS_JACARANDA, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> PALM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_PALM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> WILLOW_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WILLOW, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> DEAD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DEAD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MAGIC_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAGIC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> UMBRAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_UMBRAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> HELLBARK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_HELLBARK, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> CINNAMON_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CINNAMON, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> GLACIAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GLACIAN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> ARCHWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ARCHWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> BLUEBRIGHT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_BLUEBRIGHT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> STARLIT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_STARLIT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> FROSTBRIGHT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_FROSTBRIGHT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> COMET_LOG = registerForSourceTag(ItemTagRegistry.LOGS_COMET, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> LUNAR_LOG = registerForSourceTag(ItemTagRegistry.LOGS_LUNAR, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> DUSK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DUSK, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MAPLE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAPLE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> CRYSTALLIZED_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CRYSTALLIZED, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> LIVINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_LIVINGWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> GLIMMERING_LIVINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GLIMMERING_LIVINGWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> DREAMWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DREAMWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> GLIMMERING_DREAMWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DREAMWOOD_GLIMMERING, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> WALNUT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WALNUT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> FIG_LOG = register("logs_fig", () -> AlchemicalSulfurItem.ofSource(ItemTagRegistry.LOGS_FIG, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS));
    public static final DeferredItem<AlchemicalSulfurItem> WOLFBERRY_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WOLFBERRY, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> ECHO_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ECHO, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> ILLWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ILLWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> UNDEAD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_UNDEAD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> AURUM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_AURUM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MENRIL_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MENRIL, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> ASHEN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ASHEN, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> AZALEA_LOG = registerForSourceTag(ItemTagRegistry.LOGS_AZALEA, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> TRUMPET_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TRUMPET, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> NETHERWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_NETHERWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> SKYROOT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SKYROOT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> GOLDEN_OAK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GOLDEN_OAK, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> TWILIGHT_OAK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TWILIGHT_OAK, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> CANOPY_TREE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CANOPY_TREE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> DARKWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DARKWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> TIMEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TIMEWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> TRANSWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TRANSWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> SORTINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SORTINGWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> MINEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MINEWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> SMOGSTEM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SMOGSTEM, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> WIGGLEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WIGGLEWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> GRONGLE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GRONGLE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> RUBBERWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_RUBBERWOOD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final DeferredItem<AlchemicalSulfurItem> OTHERWORLD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_OTHERWORLD, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.LOGS);

    //Common Metals
    public static final DeferredItem<AlchemicalSulfurItem> IRON = registerForSourceTag(Tags.Items.INGOTS_IRON, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> COPPER = registerForSourceTag(Tags.Items.INGOTS_COPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> SILVER = registerForSourceTag(ItemTagRegistry.INGOTS_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> GOLD = registerForSourceTag(Tags.Items.INGOTS_GOLD, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> NETHERITE = registerForSourceTag(Tags.Items.INGOTS_NETHERITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> URANIUM = registerForSourceTag(ItemTagRegistry.INGOTS_URANIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> AZURE_SILVER = registerForSourceTag(ItemTagRegistry.INGOTS_AZURE_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ZINC = registerForSourceTag(ItemTagRegistry.INGOTS_ZINC, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSMIUM = registerForSourceTag(ItemTagRegistry.INGOTS_OSMIUM, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> NICKEL = registerForSourceTag(ItemTagRegistry.INGOTS_NICKEL, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> LEAD = registerForSourceTag(ItemTagRegistry.INGOTS_LEAD, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ALLTHEMODIUM = registerForSourceTag(ItemTagRegistry.INGOTS_ALLTHEMODIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> UNOBTAINIUM = registerForSourceTag(ItemTagRegistry.INGOTS_UNOBTAINIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IRIDIUM = registerForSourceTag(ItemTagRegistry.INGOTS_IRIDIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> TIN = registerForSourceTag(ItemTagRegistry.INGOTS_TIN, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CINNABAR = registerForSourceTag(ItemTagRegistry.INGOTS_CINNABAR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CRIMSON_IRON = registerForSourceTag(ItemTagRegistry.INGOTS_CRIMSON_IRON, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> PLATINUM = registerForSourceTag(ItemTagRegistry.INGOTS_PLATINUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> VIBRANIUM = registerForSourceTag(ItemTagRegistry.INGOTS_VIBRANIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> DESH = registerForSourceTag(ItemTagRegistry.INGOTS_DESH, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSTRUM = registerForSourceTag(ItemTagRegistry.INGOTS_OSTRUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CALORITE = registerForSourceTag(ItemTagRegistry.INGOTS_CALORITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IESNIUM = registerForSourceTag(ItemTagRegistry.INGOTS_IESNIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final DeferredItem<AlchemicalSulfurItem> DIAMOND = registerForSourceTag(Tags.Items.GEMS_DIAMOND, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> EMERALD = registerForSourceTag(Tags.Items.GEMS_EMERALD, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> LAPIS = registerForSourceTag(Tags.Items.GEMS_LAPIS, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> QUARTZ = registerForSourceTag(Tags.Items.GEMS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> AMETHYST = registerForSourceTag(Tags.Items.GEMS_AMETHYST, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PRISMARINE = registerForSourceTag(Tags.Items.GEMS_PRISMARINE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> RUBY = registerForSourceTag(ItemTagRegistry.GEMS_RUBY, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> APATITE = registerForSourceTag(ItemTagRegistry.GEMS_APATITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PERIDOT = registerForSourceTag(ItemTagRegistry.GEMS_PERIDOT, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUORITE = registerForSourceTag(ItemTagRegistry.GEMS_FLUORITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAPPHIRE = registerForSourceTag(ItemTagRegistry.GEMS_SAPPHIRE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAL_AMMONIAC = registerForSourceTag(ItemTagRegistry.GEMS_SAL_AMMONIAC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> CERTUS_QUARTZ = registerForSourceTag(ItemTagRegistry.GEMS_CERTUS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUIX = registerForSourceTag(ItemTagRegistry.GEMS_FLUIX, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> NITER = registerForSourceTag(ItemTagRegistry.GEMS_NITER, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final DeferredItem<AlchemicalSulfurItem> REDSTONE = registerForSourceTag(Tags.Items.DUSTS_REDSTONE, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> COAL = registerForSourceTag(ItemTags.COALS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> SULFUR = registerForSourceTag(ItemTagRegistry.GEMS_SULFUR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);


    /**
     * The source tag does not need to cover all possible sources (e.g. a "uranium" tag that covers ore, raw metal, ingot, ...) but rather one possible source that should be used to get the icon from.
     */
    public static DeferredItem<AlchemicalSulfurItem> registerForSourceTag(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> AlchemicalSulfurItem.ofSource(source, tier, type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerForSourceItem(Item source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> AlchemicalSulfurItem.ofSource(source, tier, type));
    }

    public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
    }

    private static String name(TagKey<Item> source) {
        var slashIndex = source.location().getPath().lastIndexOf("/");
        return source.location().getPath().substring(slashIndex + 1);
    }

    private static String name(Item source) {
        //noinspection deprecation
        var namePath = source.builtInRegistryHolder().key().location().getPath();
        var slashIndex = namePath.lastIndexOf("/");
        return namePath.substring(slashIndex + 1);
    }

    /**
     * We add only those sulfurs that have a recipe to the creative tab.
     * Other sulfurs are registered, but should not be shown to players, as the related items are from mods that are not loaded
     */
    public static void onBuildCreativeModTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeModeTabRegistry.THEURGY.get()) {
            var level = LevelUtil.getLevelWithoutContext();
            if (level == null) {
                return;
            }

            var recipeManager = level.getRecipeManager();
            var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());

            //Register only sulfurs that have a liquefaction recipe
            liquefactionRecipes.forEach(r -> {
                var result = r.value().getResultItem(level.registryAccess());
                if (result != null && !result.isEmpty() && result.getItem() instanceof AlchemicalSulfurItem && !event.getParentEntries().contains(result)) {
                    var stack = result.copyWithCount(1);

                    if (!event.getParentEntries().contains(stack)) {
                        event.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
                }
            });
        }
    }
}
