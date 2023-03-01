/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SaltRegistry {
    public static final DeferredRegister<Item> SALTS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<Item> ALCHEMICAL_SALT_ORE =
            register("ore", () -> new Item(new Item.Properties()));

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return SALTS.register("alchemical_salt_" + name, sup);
    }

    /**
     * We add only those salts that have a recipe to the creative tab.
     * Other salts are registered, but should not be shown to players, as the related items are from mods that are not loaded
     */
    public static void onBuildCreativeModTabs(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ItemRegistry.THEURGY_TAB) {
            //event will only be called client side, but other SaltRegistry calls can come from the server, so we need to guard against dist
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
            var calcinationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get());

            SALTS.getEntries().stream()
                    .map(RegistryObject::get)
                    .forEach(sulfur -> {
                        calcinationRecipes.stream()
                                .filter(recipe -> recipe.getResultItem() != null && recipe.getResultItem().getItem() == sulfur)
                                .forEach(recipe -> event.accept(recipe.getResultItem().copy()));
                    });
        }
    }

}
