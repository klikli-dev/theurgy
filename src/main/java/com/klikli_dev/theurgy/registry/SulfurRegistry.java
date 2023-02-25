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

    public static final RegistryObject<AlchemicalSulfurItem> LOGS =
            register("logs", () -> new AlchemicalSulfurItem(new Item.Properties()));


    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur" + name, sup);
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

            SULFURS.getEntries().stream()
                    .map(RegistryObject::get)
                    .map(AlchemicalSulfurItem.class::cast)
                    .forEach(sulfur -> {
                        liquefactionRecipes.stream()
                                .filter(recipe -> recipe.getResultItem() != null && recipe.getResultItem().getItem() == sulfur)
                                .forEach(recipe -> event.accept(recipe.getResultItem().copy()));
                    });
        }
    }

}
