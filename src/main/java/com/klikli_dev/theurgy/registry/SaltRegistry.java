// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.recipe.TheurgyRecipeManager;
import com.klikli_dev.theurgy.util.LevelUtil;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class SaltRegistry {
    public static final DeferredRegister.Items SALTS = DeferredRegister.createItems(Theurgy.MODID);

    /**
     * Geological term for sedimentary, rock, soil, etc. Here means Stone, Dirt, Sand, Gravel, Clay, etc
     */
    public static final DeferredItem<AlchemicalSaltItem> STRATA =
            register("strata", ItemRegistry.OTHER_MINERALS_COMMON_ICON);
    public static final DeferredItem<AlchemicalSaltItem> MINERAL =
            register("mineral", ItemRegistry.METALS_COMMON_ICON);
    public static final DeferredItem<AlchemicalSaltItem> PLANT =
            register("plant", Items.OAK_LOG);
    public static final DeferredItem<AlchemicalSaltItem> CREATURE =
            register("creature", Items.BEEF);

    public static DeferredItem<AlchemicalSaltItem> register(String name, DeferredItem<?> source) {
        return register(name, (p) -> new AlchemicalSaltItem(p.component(
                DataComponentRegistry.SOURCE_ITEM,
                DeferredHolder.create(Registries.ITEM, source.getId())
        )));
    }

    public static DeferredItem<AlchemicalSaltItem> register(String name, Item source) {
        return register(name, (p) -> new AlchemicalSaltItem(p.component(
                DataComponentRegistry.SOURCE_ITEM,
                BuiltInRegistries.ITEM.wrapAsHolder(source)
        )));
    }

    public static <T extends Item> DeferredItem<T> register(String name, Function<Item.Properties, ? extends T> func) {
        return SALTS.registerItem("alchemical_salt_" + name, func);
    }

    /**
     * We add only those salts that have a recipe to the creative tab.
     * Other salts are registered, but should not be shown to players, as the related items are from mods that are not loaded
     */
    public static void onBuildCreativeModTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeModeTabRegistry.THEURGY.get()) {
            var level = LevelUtil.getLevelWithoutContext();
            if (level == null) {
                return;
            }

            var calcinationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.CALCINATION.get(), level);

            //From: EventHooks#onCreativeModeTabBuildContents
            //we need to use it here to test before inserting, because event.getEntries().contains uses a different hashing strategy and is thus not reliable
            final var searchDupes = new ObjectLinkedOpenCustomHashSet<ItemStack>(ItemStackLinkedSet.TYPE_AND_TAG);

            SALTS.getEntries().stream()
                    .map(DeferredHolder::get)
                    .forEach(sulfur -> {
                        calcinationRecipes.stream()
                                .filter(recipe -> recipe.value().getResultItem(level.registryAccess()) != null && recipe.value().getResultItem(level.registryAccess()).getItem() == sulfur)
                                .forEach(recipe -> {
                                    var stack = recipe.value().getResultItem(level.registryAccess()).copyWithCount(1);
                                    if (searchDupes.add(stack)) {
                                        event.accept(stack, event.getTabKey() == CreativeModeTabs.SEARCH ? CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                                    }
                                });
                    });
        }
    }
}
