// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister<Item> SULFURS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<AlchemicalSulfurItem> GENERIC = registerWithTagSourceNameOverride("generic");

    public static final RegistryObject<AlchemicalSulfurItem> LOGS = registerWithTagSourceNameOverride("logs");

    //Crops
    public static final RegistryObject<AlchemicalSulfurItem> WHEAT = registerDefault("wheat");


    //Common Metals
    public static final RegistryObject<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride("iron");
    public static final RegistryObject<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride("copper");

    public static final RegistryObject<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride("silver");

    public static final RegistryObject<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride("gold");

    public static final RegistryObject<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride("netherite");
    public static final RegistryObject<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride("uranium");
    public static final RegistryObject<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride("azure_silver");
    public static final RegistryObject<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride("zinc");
    public static final RegistryObject<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride("osmium");
    public static final RegistryObject<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride("nickel");
    public static final RegistryObject<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride("lead");
    public static final RegistryObject<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride("allthemodium");
    public static final RegistryObject<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride("unobtainium");
    public static final RegistryObject<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride("iridium");
    public static final RegistryObject<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride("tin");
    public static final RegistryObject<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride("cinnabar");
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride("crimson_iron");
    public static final RegistryObject<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride("platinum");
    public static final RegistryObject<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride("vibranium");

    //Common Gems

    public static final RegistryObject<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride("diamond");
    public static final RegistryObject<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride("emerald");
    public static final RegistryObject<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride("lapis");
    public static final RegistryObject<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride("quartz");
    public static final RegistryObject<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride("amethyst");
    public static final RegistryObject<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride("prismarine");
    public static final RegistryObject<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride("ruby");
    public static final RegistryObject<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride("apatite");
    public static final RegistryObject<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride("peridot");
    public static final RegistryObject<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride("fluorite");
    public static final RegistryObject<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride("sapphire");

    public static final RegistryObject<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride("sal_ammoniac");

    //Other Common Minerals
    public static final RegistryObject<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride("redstone");
    public static final RegistryObject<AlchemicalSulfurItem> COAL = registerDefault("coal");
    public static final RegistryObject<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride("sulfur");


    public static RegistryObject<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerWithSourceNameOverride(String name) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerDefault(String name) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()));
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
                liquefactionRecipes.stream().filter(recipe -> recipe.getResultItem(level.registryAccess()) != null && recipe.getResultItem(level.registryAccess()).getItem() == sulfur).forEach(recipe -> event.accept(recipe.getResultItem(level.registryAccess()).copyWithCount(1)));
            });
        }
    }
}
