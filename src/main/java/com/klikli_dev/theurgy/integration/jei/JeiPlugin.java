// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

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

        SulfurRegistry.SULFURS.getEntries().stream().forEach(sulfur -> {
            registration.registerSubtypeInterpreter(sulfur.get(), AlchemicalSulfurSubtypeInterpreter.get());
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
        var sulfursWithoutRecipe = SulfurRegistry.SULFURS.getEntries().stream()
                .map(RegistryObject::get)
                .map(AlchemicalSulfurItem.class::cast)
                .filter(sulfur -> !SulfurRegistry.keepInItemLists(sulfur))
                .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.getResultItem(level.registryAccess()) != null && r.getResultItem(level.registryAccess()).getItem() == sulfur)).map(ItemStack::new).toList();
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, sulfursWithoutRecipe);

        //filter reformation recipes to exclude those that are for sulfurs without recipe
        var reformationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.REFORMATION.get()).stream()
                .filter(r -> r.getResultItem(level.registryAccess()) != null)
                .filter(r -> sulfursWithoutRecipe.stream().noneMatch(s -> s.getItem() == r.getResultItem(level.registryAccess()).getItem()))
                .toList();
        registration.addRecipes(JeiRecipeTypes.REFORMATION, reformationRecipes);

        //filter fermentation recipes to exclude those that use sulfurs without recipe as input
//        var fermentationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.FERMENTATION.get()).stream()
//                .filter(r -> r.getIngredients().stream().anyMatch(i -> sulfursWithoutRecipe.stream().noneMatch(i)))
        //that filter is too naive, it would also exclude recipes that use a sulfur tag as input and only one item in that tag is unavailable
        //IF we even need that filter we instead need to check if ALL items in the tag are unavailable
//                .toList();
        var fermentationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.FERMENTATION.get());
        registration.addRecipes(JeiRecipeTypes.FERMENTATION, fermentationRecipes);

        var digestionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.DIGESTION.get());
        registration.addRecipes(JeiRecipeTypes.DIGESTION, digestionRecipes);

        this.registerIngredientInfo(registration, ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
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

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SULFURIC_FLUX_EMITTER.get()),
                JeiRecipeTypes.REFORMATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                JeiRecipeTypes.REFORMATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                JeiRecipeTypes.REFORMATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()),
                JeiRecipeTypes.REFORMATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.FERMENTATION_VAT.get()),
                JeiRecipeTypes.FERMENTATION);

        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.DIGESTION_VAT.get()),
                JeiRecipeTypes.DIGESTION);
    }
}
