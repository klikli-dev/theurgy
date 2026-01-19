// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurType;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister<Item> SULFURS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<AlchemicalSulfurItem> GENERIC = registerWithTagSourceNameOverride("generic", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);

    public static final RegistryObject<AlchemicalSulfurItem> EARTHEN_MATTERS_ABUNDANT = registerNiter("earthen_matters_abundant", Items.DIRT, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> EARTHEN_MATTERS_COMMON = registerNiter("earthen_matters_common", Items.CLAY_BALL, AlchemicalSulfurTier.COMMON);

    public static final RegistryObject<AlchemicalSulfurItem> GEMS_ABUNDANT = registerNiter("gems_abundant", ItemRegistry.GEMS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_COMMON = registerNiter("gems_common", ItemRegistry.GEMS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_RARE = registerNiter("gems_rare", ItemRegistry.GEMS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_PRECIOUS = registerNiter("gems_precious", ItemRegistry.GEMS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> METALS_ABUNDANT = registerNiter("metals_abundant", ItemRegistry.METALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_COMMON = registerNiter("metals_common", ItemRegistry.METALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_RARE = registerNiter("metals_rare", ItemRegistry.METALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_PRECIOUS = registerNiter("metals_precious", ItemRegistry.METALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT = registerNiter("other_minerals_abundant", ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_COMMON = registerNiter("other_minerals_common", ItemRegistry.OTHER_MINERALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_RARE = registerNiter("other_minerals_rare", ItemRegistry.OTHER_MINERALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS = registerNiter("other_minerals_precious", ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> LOGS_ABUNDANT = registerNiter("logs_abundant", Items.OAK_LOG, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> CROPS_ABUNDANT = registerNiter("crops_abundant", Items.WHEAT, AlchemicalSulfurTier.ABUNDANT);

    public static final RegistryObject<AlchemicalSulfurItem> ANIMALS_ABUNDANT = registerNiter("animals_abundant", Items.BEEF, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> ANIMALS_COMMON = registerNiter("animals_common", Items.LEATHER, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> ANIMALS_RARE = registerNiter("animals_rare", Items.RABBIT_FOOT, AlchemicalSulfurTier.COMMON);

    public static final RegistryObject<AlchemicalSulfurItem> MOBS_ABUNDANT = registerNiter("mobs_abundant", Items.ROTTEN_FLESH, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> MOBS_COMMON = registerNiter("mobs_common", Items.BLAZE_ROD, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> MOBS_RARE = registerNiter("mobs_rare", Items.GHAST_TEAR, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> MOBS_PRECIOUS = registerNiter("mobs_precious", Items.NETHER_STAR, AlchemicalSulfurTier.PRECIOUS);


    public static final RegistryObject<AlchemicalSulfurItem> ROTTEN_FLESH = registerForSourceItem(Items.ROTTEN_FLESH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> SPIDER_EYE = registerForSourceItem(Items.SPIDER_EYE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> STRING = registerForSourceItem(Items.STRING, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> GUNPOWDER = registerForSourceItem(Items.GUNPOWDER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> BONE = registerForSourceItem(Items.BONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MOBS);

    public static final RegistryObject<AlchemicalSulfurItem> ARROW = registerForSourceItem(Items.ARROW, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> SLIMEBALL = registerForSourceItem(Items.SLIME_BALL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> ENDER_PEARL = registerForSourceItem(Items.ENDER_PEARL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> BLAZE_ROD = registerForSourceItem(Items.BLAZE_ROD, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> PRISMARINE_SHARD = registerForSourceItem(Items.PRISMARINE_SHARD, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> PHANTOM_MEMBRANE = registerForSourceItem(Items.PHANTOM_MEMBRANE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> MAGMA_CREAM = registerForSourceItem(Items.MAGMA_CREAM, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> SKELETON_SKULL = registerForSourceItem(Items.SKELETON_SKULL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.MOBS);

    public static final RegistryObject<AlchemicalSulfurItem> WITHER_SKELETON_SKULL = registerForSourceItem(Items.WITHER_SKELETON_SKULL, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> GHAST_TEAR = registerForSourceItem(Items.GHAST_TEAR, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> SHULKER_SHELL = registerForSourceItem(Items.SHULKER_SHELL, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> ELYTRA = registerForSourceItem(Items.ELYTRA, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.MOBS);

    public static final RegistryObject<AlchemicalSulfurItem> NETHER_STAR = registerForSourceItem(Items.NETHER_STAR, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> DRAGON_EGG = registerForSourceItem(Items.DRAGON_EGG, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.MOBS);
    public static final RegistryObject<AlchemicalSulfurItem> HEART_OF_THE_SEA = registerForSourceItem(Items.HEART_OF_THE_SEA, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.MOBS);

    //Animal stuff
    public static final RegistryObject<AlchemicalSulfurItem> PORKCHOP = registerForSourceItem(Items.PORKCHOP, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> BEEF = registerForSourceItem(Items.BEEF, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> MUTTON = registerForSourceItem(Items.MUTTON, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> CHICKEN = registerForSourceItem(Items.CHICKEN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> EGG = registerForSourceItem(Items.EGG, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> INK_SAC = registerForSourceItem(Items.INK_SAC, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> GLOW_INK_SAC = registerForSourceItem(Items.GLOW_INK_SAC, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> RABBIT = registerForSourceItem(Items.RABBIT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> RABBIT_HIDE = registerForSourceItem(Items.RABBIT_HIDE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> RABBIT_FOOT = registerForSourceItem(Items.RABBIT_FOOT, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> LEATHER = registerForSourceItem(Items.LEATHER, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> FEATHER = registerForSourceItem(Items.FEATHER, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> WOOL = registerForSourceItem(Items.WHITE_WOOL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> COD = registerForSourceItem(Items.COD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> SALMON = registerForSourceItem(Items.SALMON, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> TROPICAL_FISH = registerForSourceItem(Items.TROPICAL_FISH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> PUFFERFISH = registerForSourceItem(Items.PUFFERFISH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.ANIMALS);
    public static final RegistryObject<AlchemicalSulfurItem> TURTLE_SCUTE = register("turtle_scute", () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(AlchemicalSulfurTier.RARE).type(AlchemicalSulfurType.ANIMALS));

    //Crops
    public static final RegistryObject<AlchemicalSulfurItem> BEETROOT = registerForSourceItem(Items.BEETROOT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CARROT = registerForSourceItem(Items.CARROT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> POTATO = registerForSourceItem(Items.POTATO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> WHEAT = registerForSourceItem(Items.WHEAT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> APPLE = registerForSourceItem(Items.APPLE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> COCOA = registerForSourceItem(Items.COCOA_BEANS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> NETHER_WART = registerForSourceItem(Items.NETHER_WART, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ARTICHOKE = registerForSourceTag(ItemTagRegistry.CROPS_ARTICHOKE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ASPARAGUS = registerForSourceTag(ItemTagRegistry.CROPS_ASPARAGUS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BARLEY = registerForSourceTag(ItemTagRegistry.CROPS_BARLEY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BASIL = registerForSourceTag(ItemTagRegistry.CROPS_BASIL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BELLPEPPER = registerForSourceTag(ItemTagRegistry.CROPS_BELLPEPPER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BLACKBEAN = registerForSourceTag(ItemTagRegistry.CROPS_BLACKBEAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BLACKBERRY = registerForSourceTag(ItemTagRegistry.CROPS_BLACKBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BLUEBERRY = registerForSourceTag(ItemTagRegistry.CROPS_BLUEBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BROCCOLI = registerForSourceTag(ItemTagRegistry.CROPS_BROCCOLI, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CABBAGE = registerForSourceTag(ItemTagRegistry.CROPS_CABBAGE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CANTALOUPE = registerForSourceTag(ItemTagRegistry.CROPS_CANTALOUPE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CAULIFLOWER = registerForSourceTag(ItemTagRegistry.CROPS_CAULIFLOWER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CELERY = registerForSourceTag(ItemTagRegistry.CROPS_CELERY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CHILE_PEPPER = registerForSourceTag(ItemTagRegistry.CROPS_CHILE_PEPPER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> COFFEE_BEANS = registerForSourceTag(ItemTagRegistry.CROPS_COFFEE_BEANS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CORN = registerForSourceTag(ItemTagRegistry.CROPS_CORN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CRANBERRY = registerForSourceTag(ItemTagRegistry.CROPS_CRANBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CUCUMBER = registerForSourceTag(ItemTagRegistry.CROPS_CUCUMBER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CURRANT = registerForSourceTag(ItemTagRegistry.CROPS_CURRANT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> EGGPLANT = registerForSourceTag(ItemTagRegistry.CROPS_EGGPLANT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ELDERBERRY = registerForSourceTag(ItemTagRegistry.CROPS_ELDERBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> GARLIC = registerForSourceTag(ItemTagRegistry.CROPS_GARLIC, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> GINGER = registerForSourceTag(ItemTagRegistry.CROPS_GINGER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> GRAPE = registerForSourceTag(ItemTagRegistry.CROPS_GRAPE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> GREENBEAN = registerForSourceTag(ItemTagRegistry.CROPS_GREENBEAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> GREENONION = registerForSourceTag(ItemTagRegistry.CROPS_GREENONION, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> HONEYDEW = registerForSourceTag(ItemTagRegistry.CROPS_HONEYDEW, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> HOPS = registerForSourceTag(ItemTagRegistry.CROPS_HOPS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> KALE = registerForSourceTag(ItemTagRegistry.CROPS_KALE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> KIWI = registerForSourceTag(ItemTagRegistry.CROPS_KIWI, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> LEEK = registerForSourceTag(ItemTagRegistry.CROPS_LEEK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> LETTUCE = registerForSourceTag(ItemTagRegistry.CROPS_LETTUCE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> MUSTARD = registerForSourceTag(ItemTagRegistry.CROPS_MUSTARD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> OAT = registerForSourceTag(ItemTagRegistry.CROPS_OAT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> OLIVE = registerForSourceTag(ItemTagRegistry.CROPS_OLIVE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ONION = registerForSourceTag(ItemTagRegistry.CROPS_ONION, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PEANUT = registerForSourceTag(ItemTagRegistry.CROPS_PEANUT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PEPPER = registerForSourceTag(ItemTagRegistry.CROPS_PEPPER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PINEAPPLE = registerForSourceTag(ItemTagRegistry.CROPS_PINEAPPLE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> RADISH = registerForSourceTag(ItemTagRegistry.CROPS_RADISH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> RASPBERRY = registerForSourceTag(ItemTagRegistry.CROPS_RASPBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> RHUBARB = registerForSourceTag(ItemTagRegistry.CROPS_RHUBARB, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> RICE = registerForSourceTag(ItemTagRegistry.CROPS_RICE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> RUTABAGA = registerForSourceTag(ItemTagRegistry.CROPS_RUTABAGA, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> SAGUARO = registerForSourceTag(ItemTagRegistry.CROPS_SAGUARO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> SOYBEAN = registerForSourceTag(ItemTagRegistry.CROPS_SOYBEAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> SPINACH = registerForSourceTag(ItemTagRegistry.CROPS_SPINACH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> SQUASH = registerForSourceTag(ItemTagRegistry.CROPS_SQUASH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> STRAWBERRY = registerForSourceTag(ItemTagRegistry.CROPS_STRAWBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> SWEETPOTATO = registerForSourceTag(ItemTagRegistry.CROPS_SWEETPOTATO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> TEA_LEAVES = registerForSourceTag(ItemTagRegistry.CROPS_TEA_LEAVES, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> TOMATILLO = registerForSourceTag(ItemTagRegistry.CROPS_TOMATILLO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> TOMATO = registerForSourceTag(ItemTagRegistry.CROPS_TOMATO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> TURMERIC = registerForSourceTag(ItemTagRegistry.CROPS_TURMERIC, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> TURNIP = registerForSourceTag(ItemTagRegistry.CROPS_TURNIP, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> VANILLA = registerForSourceTag(ItemTagRegistry.CROPS_VANILLA, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> YAM = registerForSourceTag(ItemTagRegistry.CROPS_YAM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ZUCCHINI = registerForSourceTag(ItemTagRegistry.CROPS_ZUCCHINI, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> FLAX = registerForSourceTag(ItemTagRegistry.CROPS_FLAX, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> JUNIPERBERRY = registerForSourceTag(ItemTagRegistry.CROPS_JUNIPERBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ALMOND = registerForSourceTag(ItemTagRegistry.CROPS_ALMOND, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> APRICOT = registerForSourceTag(ItemTagRegistry.CROPS_APRICOT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> AVOCADO = registerForSourceTag(ItemTagRegistry.CROPS_AVOCADO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> BANANA = registerForSourceTag(ItemTagRegistry.CROPS_BANANA, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CASHEW = registerForSourceTag(ItemTagRegistry.CROPS_CASHEW, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> CHERRY = registerForSourceTag(ItemTagRegistry.CROPS_CHERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> COCONUT = registerForSourceTag(ItemTagRegistry.CROPS_COCONUT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> DATE = registerForSourceTag(ItemTagRegistry.CROPS_DATE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> DRAGONFRUIT = registerForSourceTag(ItemTagRegistry.CROPS_DRAGONFRUIT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> FIG = register("crops_fig", () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(AlchemicalSulfurTier.ABUNDANT).type(AlchemicalSulfurType.CROPS));
    public static final RegistryObject<AlchemicalSulfurItem> GRAPEFRUIT = registerForSourceTag(ItemTagRegistry.CROPS_GRAPEFRUIT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> KUMQUAT = registerForSourceTag(ItemTagRegistry.CROPS_KUMQUAT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> LEMON = registerForSourceTag(ItemTagRegistry.CROPS_LEMON, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> LIME = registerForSourceTag(ItemTagRegistry.CROPS_LIME, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> MANDARIN = registerForSourceTag(ItemTagRegistry.CROPS_MANDARIN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> MANGO = registerForSourceTag(ItemTagRegistry.CROPS_MANGO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> NECTARINE = registerForSourceTag(ItemTagRegistry.CROPS_NECTARINE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> NUTMEG = registerForSourceTag(ItemTagRegistry.CROPS_NUTMEG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> ORANGE = registerForSourceTag(ItemTagRegistry.CROPS_ORANGE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PEACH = registerForSourceTag(ItemTagRegistry.CROPS_PEACH, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PEAR = registerForSourceTag(ItemTagRegistry.CROPS_PEAR, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PECAN = registerForSourceTag(ItemTagRegistry.CROPS_PECAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PERSIMMON = registerForSourceTag(ItemTagRegistry.CROPS_PERSIMMON, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);
    public static final RegistryObject<AlchemicalSulfurItem> PLUM = registerForSourceTag(ItemTagRegistry.CROPS_PLUM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.CROPS);

    //Logs
    public static final RegistryObject<AlchemicalSulfurItem> OAK_LOG = registerForSourceItem(Items.OAK_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> SPRUCE_LOG = registerForSourceItem(Items.SPRUCE_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> BIRCH_LOG = registerForSourceItem(Items.BIRCH_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> JUNGLE_LOG = registerForSourceItem(Items.JUNGLE_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> ACACIA_LOG = registerForSourceItem(Items.ACACIA_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> CHERRY_LOG = registerForSourceItem(Items.CHERRY_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> DARK_OAK_LOG = registerForSourceItem(Items.DARK_OAK_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MANGROVE_LOG = registerForSourceItem(Items.MANGROVE_LOG, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_STEM = registerForSourceItem(Items.CRIMSON_STEM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> WARPED_STEM = registerForSourceItem(Items.WARPED_STEM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);

    public static final RegistryObject<AlchemicalSulfurItem> ROWAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ROWAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> FIR_LOG = registerForSourceTag(ItemTagRegistry.LOGS_FIR, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> REDWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_REDWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MAHOGANY_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAHOGANY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> JACARANDA_LOG = registerForSourceTag(ItemTagRegistry.LOGS_JACARANDA, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> PALM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_PALM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> WILLOW_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WILLOW, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> DEAD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DEAD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MAGIC_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAGIC, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> UMBRAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_UMBRAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> HELLBARK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_HELLBARK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> CINNAMON_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CINNAMON, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> GLACIAN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GLACIAN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> ARCHWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ARCHWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> BLUEBRIGHT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_BLUEBRIGHT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> STARLIT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_STARLIT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> FROSTBRIGHT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_FROSTBRIGHT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> COMET_LOG = registerForSourceTag(ItemTagRegistry.LOGS_COMET, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> LUNAR_LOG = registerForSourceTag(ItemTagRegistry.LOGS_LUNAR, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> DUSK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DUSK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MAPLE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MAPLE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> CRYSTALLIZED_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CRYSTALLIZED, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> LIVINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_LIVINGWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> GLIMMERING_LIVINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GLIMMERING_LIVINGWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> DREAMWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DREAMWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> GLIMMERING_DREAMWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DREAMWOOD_GLIMMERING, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> WALNUT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WALNUT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> FIG_LOG = register("logs_fig", () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(AlchemicalSulfurTier.ABUNDANT).type(AlchemicalSulfurType.LOGS));
    public static final RegistryObject<AlchemicalSulfurItem> WOLFBERRY_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WOLFBERRY, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> ECHO_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ECHO, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> ILLWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ILLWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> UNDEAD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_UNDEAD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> AURUM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_AURUM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MENRIL_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MENRIL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> ASHEN_LOG = registerForSourceTag(ItemTagRegistry.LOGS_ASHEN, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> AZALEA_LOG = registerForSourceTag(ItemTagRegistry.LOGS_AZALEA, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> TRUMPET_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TRUMPET, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> NETHERWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_NETHERWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> SKYROOT_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SKYROOT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> GOLDEN_OAK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GOLDEN_OAK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> TWILIGHT_OAK_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TWILIGHT_OAK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> CANOPY_TREE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_CANOPY_TREE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> DARKWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_DARKWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> TIMEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TIMEWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> TRANSWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_TRANSWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> SORTINGWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SORTINGWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> MINEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_MINEWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> SMOGSTEM_LOG = registerForSourceTag(ItemTagRegistry.LOGS_SMOGSTEM, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> WIGGLEWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_WIGGLEWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> GRONGLE_LOG = registerForSourceTag(ItemTagRegistry.LOGS_GRONGLE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> RUBBERWOOD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_RUBBERWOOD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);
    public static final RegistryObject<AlchemicalSulfurItem> OTHERWORLD_LOG = registerForSourceTag(ItemTagRegistry.LOGS_OTHERWORLD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.LOGS);

    //Common Earthen Matter
    public static final RegistryObject<AlchemicalSulfurItem> DIRT = registerForSourceItem(Items.DIRT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> COARSE_DIRT = registerForSourceItem(Items.COARSE_DIRT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> PODZOL = registerForSourceItem(Items.PODZOL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> GRASS_BLOCK = registerForSourceItem(Items.GRASS_BLOCK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> ROOTED_DIRT = registerForSourceItem(Items.ROOTED_DIRT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> MOSS_BLOCK = registerForSourceItem(Items.MOSS_BLOCK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> MUD = registerForSourceItem(Items.MUD, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> MUDDY_MANGROVE_ROOTS = registerForSourceItem(Items.MUDDY_MANGROVE_ROOTS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> SAND = registerWithTagSourceNameOverride("sand", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> RED_SAND = registerWithTagSourceNameOverride("red_sand", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> GRAVEL = registerForSourceItem(Items.GRAVEL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> NETHERRACK = registerForSourceItem(Items.NETHERRACK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> SOUL_SAND = registerForSourceItem(Items.SOUL_SAND, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> SOUL_SOIL = registerForSourceItem(Items.SOUL_SOIL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> STONE = registerForSourceItem(Items.STONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> INFESTED_STONE = registerForSourceItem(Items.INFESTED_STONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> COBBLESTONE = registerWithTagSourceNameOverride("cobblestone", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> COBBLESTONE_INFESTED = registerWithTagSourceNameOverride("infested_cobblestone", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> COBBLESTONE_MOSSY = registerWithTagSourceNameOverride("mossy_cobblestone", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> DEEPSLATE = registerForSourceItem(Items.DEEPSLATE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> COBBLESTONE_DEEPSLATE = registerForSourceItem(Items.COBBLED_DEEPSLATE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> GRANITE = registerForSourceItem(Items.GRANITE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> DIORITE = registerForSourceItem(Items.DIORITE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> ANDESITE = registerForSourceItem(Items.ANDESITE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> BLACKSTONE = registerForSourceItem(Items.BLACKSTONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> BASALT = registerForSourceItem(Items.BASALT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> SANDSTONE = registerForSourceItem(Items.SANDSTONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> RED_SANDSTONE = registerForSourceItem(Items.RED_SANDSTONE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> CLAY = registerForSourceItem(Items.CLAY_BALL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> TERRACOTTA = registerForSourceItem(Items.TERRACOTTA, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_NYLIUM = registerForSourceItem(Items.CRIMSON_NYLIUM, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> WARPED_NYLIUM = registerForSourceItem(Items.WARPED_NYLIUM, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> END_STONE = registerForSourceItem(Items.END_STONE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> PURPUR_BLOCK = registerForSourceItem(Items.PURPUR_BLOCK, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> MYCELIUM = registerForSourceItem(Items.MYCELIUM, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> OBSIDIAN = registerForSourceItem(Items.OBSIDIAN, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> CRYING_OBSIDIAN = registerForSourceItem(Items.CRYING_OBSIDIAN, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);


    public static final RegistryObject<AlchemicalSulfurItem> SNOW = registerForSourceItem(Items.SNOWBALL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> ICE = registerForSourceItem(Items.ICE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> MAGMA = registerForSourceItem(Items.MAGMA_BLOCK, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);

    public static final RegistryObject<AlchemicalSulfurItem> WATER = registerForSourceItem(Items.WATER_BUCKET /*ItemRegistry.CRYSTALLIZED_WATER.get()*/, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.EARTHEN_MATTERS);
    public static final RegistryObject<AlchemicalSulfurItem> LAVA = registerForSourceItem(Items.LAVA_BUCKET /*ItemRegistry.CRYSTALLIZED_LAVA.get()*/, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.EARTHEN_MATTERS);

    //Common Metals
    public static final RegistryObject<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride("iron", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride("copper", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride("silver", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride("gold", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride("netherite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride("uranium", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> URANINITE = registerWithSourceNameOverride("uraninite", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride("azure_silver", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride("zinc", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride("osmium", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride("nickel", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride("lead", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride("allthemodium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride("unobtainium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride("iridium", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride("tin", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ALUMINUM = registerWithSourceNameOverride("aluminum", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride("cinnabar", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride("crimson_iron", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride("platinum", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride("vibranium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> DESH = registerWithSourceNameOverride("desh", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> OSTRUM = registerWithSourceNameOverride("ostrum", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CALORITE = registerWithSourceNameOverride("calorite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> IESNIUM = registerWithSourceNameOverride("iesnium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> DEMONITE = registerWithSourceNameOverride("demonite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ANTIMONY = registerForSourceTag(ItemTagRegistry.INGOTS_ANTIMONY, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> TITANIUM = registerForSourceTag(ItemTagRegistry.INGOTS_TITANIUM, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> TUNGSTEN = registerForSourceTag(ItemTagRegistry.INGOTS_TUNGSTEN, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final RegistryObject<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride("diamond", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride("emerald", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride("lapis", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride("quartz", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride("amethyst", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride("prismarine", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride("ruby", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride("apatite", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride("peridot", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride("fluorite", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride("sapphire", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> DARK_GEM = registerForSourceTag(ItemTagRegistry.GEMS_DARK, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);

    public static final RegistryObject<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride("sal_ammoniac", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> CERTUS_QUARTZ = registerWithSourceNameOverride("certus_quartz", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> FLUIX = registerWithSourceNameOverride("fluix", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> NITER = registerWithSourceNameOverride("niter", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);

    public static final RegistryObject<AlchemicalSulfurItem> CHIMERITE = registerWithSourceNameOverride("chimerite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final RegistryObject<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride("redstone", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> COAL = registerDefault("coal", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride("sulfur", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> GLOWSTONE = registerForSourceTag(Tags.Items.DUSTS_GLOWSTONE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> LIGNITE_COAL = registerForSourceTag(ItemTagRegistry.GEMS_LIGNITE_COAL, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> MONAZITE = registerForSourceTag(ItemTagRegistry.DUSTS_MONAZITE, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> BAUXITE = registerForSourceTag(ItemTagRegistry.DUSTS_BAUXITE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);

    /**
     * Sulfurs for which we return true will not be exlcuded from jei/modonomicon renderers despite not having a liquefaction recipe
     */
    public static boolean keepInItemLists(AlchemicalSulfurItem sulfur) {
        return sulfur.type() == AlchemicalSulfurType.NITER;
    }

    public static RegistryObject<AlchemicalSulfurItem> registerForSourceTag(TagKey<Item> source, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true).tier(tier).type(type));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerForSourceItem(Item source, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(tier).type(type));
    }

    private static String name(TagKey<Item> source) {
        var slashIndex = source.location().getPath().lastIndexOf("/");
        return source.location().getPath().substring(slashIndex + 1);
    }

    private static String name(Item source) {
        //noinspection deprecation
        return name(source.builtInRegistryHolder());
    }

    private static String name(Holder<Item> source) {
        var namePath = source.unwrapKey().get().location().getPath();
        var slashIndex = namePath.lastIndexOf("/");
        return namePath.substring(slashIndex + 1);
    }


    public static RegistryObject<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true).tier(tier).type(type));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerWithSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(tier).type(type));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerNiter(String name, Supplier<Item> sourceStackSupplier, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties(), Suppliers.memoize(() -> new ItemStack(sourceStackSupplier.get()))).overrideSourceName(true).autoTooltip(true, false).autoName(true, false).withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()))).tier(tier).type(AlchemicalSulfurType.NITER));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerNiter(String name, Item sourceStackSupplier, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties(), Suppliers.memoize(() -> new ItemStack(sourceStackSupplier))).overrideSourceName(true).autoTooltip(true, false).autoName(true, false).withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()))).tier(tier).type(AlchemicalSulfurType.NITER));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerDefault(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).tier(tier).type(type));
    }

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
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

            SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
                var preferred = getPreferredSulfurVariant(sulfur, liquefactionRecipes, level);
                preferred.ifPresent(itemStack -> event.accept(itemStack.copyWithCount(1)));
            });

            event.accept(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get());
            event.accept(SulfurRegistry.EARTHEN_MATTERS_COMMON.get());

            event.accept(SulfurRegistry.GEMS_ABUNDANT.get());
            event.accept(SulfurRegistry.GEMS_COMMON.get());
            event.accept(SulfurRegistry.GEMS_RARE.get());
            event.accept(SulfurRegistry.GEMS_PRECIOUS.get());

            event.accept(SulfurRegistry.METALS_ABUNDANT.get());
            event.accept(SulfurRegistry.METALS_COMMON.get());
            event.accept(SulfurRegistry.METALS_RARE.get());
            event.accept(SulfurRegistry.METALS_PRECIOUS.get());

            event.accept(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_COMMON.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_RARE.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get());

            event.accept(SulfurRegistry.LOGS_ABUNDANT.get());
            event.accept(SulfurRegistry.CROPS_ABUNDANT.get());

            event.accept(SulfurRegistry.ANIMALS_ABUNDANT.get());
            event.accept(SulfurRegistry.ANIMALS_COMMON.get());
            event.accept(SulfurRegistry.ANIMALS_RARE.get());

            event.accept(SulfurRegistry.MOBS_ABUNDANT.get());
            event.accept(SulfurRegistry.MOBS_COMMON.get());
            event.accept(SulfurRegistry.MOBS_RARE.get());
            event.accept(SulfurRegistry.MOBS_PRECIOUS.get());
        }
    }

    /**
     * We want sulfurs to display with the most recognizable source items: ingots, gems, dusts.
     * This method selects these sulfurs, and otherwise gets the first matching one.
     */
    public static Optional<ItemStack> getPreferredSulfurVariant(AlchemicalSulfurItem sulfur, List<LiquefactionRecipe> liquefactionRecipes, Level level) {
        var matchingRecipes = liquefactionRecipes.stream()
                .filter(recipe -> recipe.getResultItem(level.registryAccess()) != null && recipe.getResultItem(level.registryAccess()).getItem() == sulfur).toList();

        //prefer ingot/gems
        var sulfurWithNbt = matchingRecipes.stream().filter(r -> Arrays.stream(r.getIngredient().getItems()).anyMatch(i -> i.is(Tags.Items.INGOTS) || i.is(Tags.Items.GEMS))).findFirst().map(r -> r.getResultItem(level.registryAccess()));

        //second choice: dusts (e.g redstone, glowstone)
        if (sulfurWithNbt.isEmpty())
            sulfurWithNbt = matchingRecipes.stream().filter(r -> Arrays.stream(r.getIngredient().getItems()).anyMatch(i -> i.is(Tags.Items.DUSTS))).findFirst().map(r -> r.getResultItem(level.registryAccess()));

        //but fall back to any other
        if (sulfurWithNbt.isEmpty())
            sulfurWithNbt = matchingRecipes.stream().findFirst().map(r -> r.getResultItem(level.registryAccess()));

        return sulfurWithNbt;
    }
}
