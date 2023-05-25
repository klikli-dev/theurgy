/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister<Item> SULFURS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<AlchemicalSulfurItem> LOGS = register("logs", () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true));

    //Crops
    public static final RegistryObject<AlchemicalSulfurItem> WHEAT = register("wheat", () -> new AlchemicalSulfurItem(new Item.Properties()));


    //Common Metals
    public static final RegistryObject<AlchemicalSulfurItem> IRON = register("iron", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> COPPER = register("copper", () -> new AlchemicalSulfurItem(new Item.Properties()));

    public static final RegistryObject<AlchemicalSulfurItem> SILVER = register("silver", () -> new AlchemicalSulfurItem(new Item.Properties()));

    public static final RegistryObject<AlchemicalSulfurItem> GOLD = register("gold", () -> new AlchemicalSulfurItem(new Item.Properties()));

    public static final RegistryObject<AlchemicalSulfurItem> NETHERITE = register("netherite", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> URANIUM = register("uranium", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> AZURE_SILVER = register("azure_silver", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> ZINC = register("zinc", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> OSMIUM = register("osmium", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> NICKEL = register("nickel", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> LEAD = register("lead", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> ALLTHEMODIUM = register("allthemodium", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> UNOBTAINIUM = register("unobtainium", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> IRIDIUM = register("iridium", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> TIN = register("tin", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> CINNABAR = register("cinnabar", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_IRON = register("crimson_iron", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> PLATINUM = register("platinum", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> VIBRANIUM = register("vibranium", () -> new AlchemicalSulfurItem(new Item.Properties()));

    //Common Gems

    public static final RegistryObject<AlchemicalSulfurItem> DIAMOND = register("diamond", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> EMERALD = register("emerald", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> LAPIS = register("lapis", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> QUARTZ = register("quartz", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> RUBY = register("ruby", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> APATITE = register("apatite", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> PERIDOT = register("peridot", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> FLUORITE = register("fluorite", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> SAPPHIRE = register("sapphire", () -> new AlchemicalSulfurItem(new Item.Properties()));

    //Other Common Minerals
    public static final RegistryObject<AlchemicalSulfurItem> REDSTONE = register("redstone", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> COAL = register("coal", () -> new AlchemicalSulfurItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> SULFUR = register("sulfur", () -> new AlchemicalSulfurItem(new Item.Properties()));

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
    }


    /**
     * We add only those sulfurs that have a recipe to the creative tab.
     * Other sulfurs are registered, but should not be shown to players, as the related items are from mods that are not loaded
     */
    public static void onBuildCreativeModTabs(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ItemRegistry.THEURGY_TAB) {
            //event will only be called client side, but other SulfurRegistry calls can come from the server, so we need to guard against dist
            DistHelper.onBuildCreativeModTabs(event);
        }
    }

    public static class DistHelper {
        public static void onBuildCreativeModTabs(CreativeModeTabEvent.BuildContents event) {
            var level = Minecraft.getInstance().level;
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
