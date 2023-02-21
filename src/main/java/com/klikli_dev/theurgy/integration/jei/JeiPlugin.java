/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.jei.recipes.CalcinationCategory;
import com.klikli_dev.theurgy.integration.jei.recipes.JeiRecipeTypes;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.integration.jei.recipes.LiquefactionCategory;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Theurgy.loc("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {

        ItemRegistry.ITEMS.getEntries().stream().filter(item -> item.get() instanceof DivinationRodItem).forEach(item -> {
            registration.registerSubtypeInterpreter(item.get(), DivinationRodSubtypeInterpreter.get());
            Theurgy.LOGGER.debug("Registered Divination Rod JEI Subtype Interpreter for: {}", item.getKey());
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CalcinationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new LiquefactionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        var recipeManager = level.getRecipeManager();

        var calcinationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get());
        registration.addRecipes(JeiRecipeTypes.CALCINATION, calcinationRecipes);

        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());
        registration.addRecipes(JeiRecipeTypes.LIQUEFACTION, liquefactionRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CALCINATION_OVEN.get()),
                JeiRecipeTypes.CALCINATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()),
                JeiRecipeTypes.LIQUEFACTION);
    }
}
