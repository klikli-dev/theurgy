// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.recipe;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TheurgyRecipeManagerClient {
    private TheurgyRecipeManagerClient() {
    }

    public static void onRecipesReceived(RecipesReceivedEvent event) {
        var manager = TheurgyRecipeManager.get();
        manager.clearClientCache();

        for (var type : event.getRecipeTypes()) {
            manager.storeClientRecipesUnchecked(event.getRecipeMap(), type);
        }

        hideSulfursWithoutLiquefactionRecipe(event.getRecipeMap());
    }

    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        TheurgyRecipeManager.get().clearClientCache();
    }

    private static void hideSulfursWithoutLiquefactionRecipe(RecipeMap recipeMap) {
        var liquefactionRecipes = recipeMap.byType(RecipeTypeRegistry.LIQUEFACTION.get());

        SulfurRegistry.SULFURS.getEntries().stream()
                .map(DeferredHolder::get)
                .map(AlchemicalSulfurItem.class::cast)
                .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> {
                    var resultItem = r.value().getResultItem(RegistryAccess.EMPTY);
                    return resultItem != null && resultItem.getItem() == sulfur;
                }))
                .map(ItemStack::new)
                .forEach(PageRendererRegistry::registerItemStackNotToRender);
    }
}
