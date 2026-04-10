// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.integration.jei.recipes.*;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.klikli_dev.theurgy.recipe.TheurgyRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
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
        registration.addRecipeCategories(new ReformationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FermentationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DigestionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;

        var calcinationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.CALCINATION.get(), level);
        registration.addRecipes(JeiRecipeTypes.CALCINATION, calcinationRecipes.stream().toList());

        var liquefactionRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.LIQUEFACTION.get(), level);
        registration.addRecipes(JeiRecipeTypes.LIQUEFACTION, liquefactionRecipes.stream().toList());

        var distillationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.DISTILLATION.get(), level);
        registration.addRecipes(JeiRecipeTypes.DISTILLATION, distillationRecipes.stream().toList());

        var incubationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.INCUBATION.get(), level);
        registration.addRecipes(JeiRecipeTypes.INCUBATION, incubationRecipes.stream().toList());

        var accumulationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.ACCUMULATION.get(), level);
        registration.addRecipes(JeiRecipeTypes.ACCUMULATION, accumulationRecipes.stream().toList());

        //now remove sulfurs that have no recipe -> otherwise we see "no source" sulfurs in tag recipes
        //See also Theurgy.Client#onRecipesUpdated
        var sulfursWithoutRecipe = SulfurRegistry.SULFURS.getEntries().stream()
                .map(DeferredHolder::get)
                .map(AlchemicalSulfurItem.class::cast)
                .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.value().getResultItem(level.registryAccess()) != null && r.value().getResultItem(level.registryAccess()).getItem() == sulfur)).map(ItemStack::new).toList();
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, sulfursWithoutRecipe);

        //filter reformation recipes to exclude those that are for sulfurs without recipe
        var reformationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.REFORMATION.get(), level).stream()
                .filter(r -> r.value().getResultItem(level.registryAccess()) != null)
                .filter(r -> sulfursWithoutRecipe.stream().noneMatch(s -> s.getItem() == r.value().getResultItem(level.registryAccess()).getItem()))
                .toList();
        registration.addRecipes(JeiRecipeTypes.REFORMATION, reformationRecipes);

        //filter fermentation recipes to exclude those that use sulfurs without recipe as input
        //var fermentationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.FERMENTATION.get()).stream()
        //        .filter(r -> r.getIngredients().stream().anyMatch(i -> sulfursWithoutRecipe.stream().noneMatch(i)))
        //that filter is too naive, it would also exclude recipes that use a sulfur tag as input and only one item in that tag is unavailable
        //IF we even need that filter we instead need to check if ALL items in the tag are unavailable
        //        .toList();
        var fermentationRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.FERMENTATION.get(), level);
        registration.addRecipes(JeiRecipeTypes.FERMENTATION, fermentationRecipes.stream().toList());

        var digestionRecipes = TheurgyRecipeManager.get().getRecipesByType(RecipeTypeRegistry.DIGESTION.get(), level);
        registration.addRecipes(JeiRecipeTypes.DIGESTION, digestionRecipes.stream().toList());

        this.registerIngredientInfo(registration, ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
    }

    public void registerIngredientInfo(IRecipeRegistration registration, ItemLike ingredient) {
        registration.addIngredientInfo(new ItemStack(ingredient.asItem()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei." + Theurgy.MODID + ".ingredient." + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath() + ".description"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(JeiRecipeTypes.CALCINATION, new ItemStack(BlockRegistry.CALCINATION_OVEN.get()));

        registration.addCraftingStation(JeiRecipeTypes.LIQUEFACTION, new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()));

        registration.addCraftingStation(JeiRecipeTypes.DISTILLATION, new ItemStack(BlockRegistry.DISTILLER.get()));

        registration.addCraftingStation(JeiRecipeTypes.INCUBATION, new ItemStack(BlockRegistry.INCUBATOR.get()));

        registration.addCraftingStation(JeiRecipeTypes.ACCUMULATION, new ItemStack(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()));

        registration.addCraftingStation(JeiRecipeTypes.REFORMATION, new ItemStack(BlockRegistry.SULFURIC_FLUX_EMITTER.get()));

        registration.addCraftingStation(JeiRecipeTypes.REFORMATION, new ItemStack(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()));

        registration.addCraftingStation(JeiRecipeTypes.REFORMATION, new ItemStack(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()));

        registration.addCraftingStation(JeiRecipeTypes.REFORMATION, new ItemStack(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()));

        registration.addCraftingStation(JeiRecipeTypes.FERMENTATION, new ItemStack(BlockRegistry.FERMENTATION_VAT.get()));

        registration.addCraftingStation(JeiRecipeTypes.DIGESTION, new ItemStack(BlockRegistry.DIGESTION_VAT.get()));
    }
}
