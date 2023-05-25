/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.integration.jei.recipes.*;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

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
        registration.addRecipeCategories(new DistillationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new IncubationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AccumulationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        var recipeManager = level.getRecipeManager();

        var calcinationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get());
        registration.addRecipes(JeiRecipeTypes.CALCINATION, calcinationRecipes);

        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());
        registration.addRecipes(JeiRecipeTypes.LIQUEFACTION, liquefactionRecipes);

        var distillationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.DISTILLATION.get());
        registration.addRecipes(JeiRecipeTypes.DISTILLATION, distillationRecipes);

        var incubationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.INCUBATION.get());
        registration.addRecipes(JeiRecipeTypes.INCUBATION, incubationRecipes);

        var accumulationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.ACCUMULATION.get());
        registration.addRecipes(JeiRecipeTypes.ACCUMULATION, accumulationRecipes);

        //now remove sulfurs that have no recipe -> otherwise we see "no source" sulfurs in tag recipes
        //See also Theurgy.Client#onRecipesUpdated
        var sulfursWithoutRecipe =  SulfurRegistry.SULFURS.getEntries().stream()
                .map(RegistryObject::get)
                .map(AlchemicalSulfurItem.class::cast)
                .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.getResultItem(level.registryAccess()) != null && r.getResultItem(level.registryAccess()).getItem() == sulfur)).map(ItemStack::new).toList();
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, sulfursWithoutRecipe);
    }

    public void registerIngredientInfo(IRecipeRegistration registration, ItemLike ingredient) {
        registration.addIngredientInfo(new ItemStack(ingredient.asItem()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei." + Theurgy.MODID + ".ingredient." + ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath() + ".description"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CALCINATION_OVEN.get()),
                JeiRecipeTypes.CALCINATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()),
                JeiRecipeTypes.LIQUEFACTION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.DISTILLER.get()),
                JeiRecipeTypes.DISTILLATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.INCUBATOR.get()),
                JeiRecipeTypes.INCUBATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                JeiRecipeTypes.ACCUMULATION);
    }

}
