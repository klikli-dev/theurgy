// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import mezz.jei.api.constants.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Collectors;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {

    public static final EmiStack ACCUMULATION_ICON = EmiStack.of(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    public static final EmiRecipeCategory ACCUMULATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.ACCUMULATION.getId(), ACCUMULATION_ICON);

    public static final EmiStack CALCINATION_ICON = EmiStack.of(ItemRegistry.CALCINATION_OVEN.get());
    public static final EmiRecipeCategory CALCINATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.CALCINATION.getId(), CALCINATION_ICON);

    public static final EmiStack DIGESTION_ICON = EmiStack.of(ItemRegistry.DIGESTION_VAT.get());
    public static final EmiRecipeCategory DIGESTION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.DIGESTION.getId(), DIGESTION_ICON);

    public static final EmiStack DISTILLATION_ICON = EmiStack.of(ItemRegistry.DISTILLER.get());
    public static final EmiRecipeCategory DISTILLATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.DISTILLATION.getId(), DISTILLATION_ICON);

    public static final EmiStack FERMENTATION_ICON = EmiStack.of(ItemRegistry.FERMENTATION_VAT.get());
    public static final EmiRecipeCategory FERMENTATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.FERMENTATION.getId(), FERMENTATION_ICON);

    public static final EmiStack INCUBATION_ICON = EmiStack.of(ItemRegistry.INCUBATOR.get());
    public static final EmiRecipeCategory INCUBATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.INCUBATION.getId(), INCUBATION_ICON);

    public static final EmiStack LIQUEFACTION_ICON = EmiStack.of(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    public static final EmiRecipeCategory LIQUEFACTION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.LIQUEFACTION.getId(), LIQUEFACTION_ICON);

    public static final EmiStack REFORMATION_ICON = EmiStack.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
    public static final EmiRecipeCategory REFORMATION_CATEGORY = new EmiRecipeCategory(RecipeTypeRegistry.REFORMATION.getId(), REFORMATION_ICON);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ACCUMULATION_CATEGORY);
        registry.addWorkstation(ACCUMULATION_CATEGORY, ACCUMULATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.ACCUMULATION.get())) {
            registry.addRecipe(new AccumulationEmiRecipe(recipe));
        }

        registry.addCategory(CALCINATION_CATEGORY);
        registry.addWorkstation(CALCINATION_CATEGORY, CALCINATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get())) {
            registry.addRecipe(new CalcinationEmiRecipe(recipe));
        }

        registry.addCategory(DIGESTION_CATEGORY);
        registry.addWorkstation(DIGESTION_CATEGORY, DIGESTION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.DIGESTION.get())) {
            registry.addRecipe(new DigestionEmiRecipe(recipe));
        }

        registry.addCategory(DISTILLATION_CATEGORY);
        registry.addWorkstation(DISTILLATION_CATEGORY, DISTILLATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.DISTILLATION.get())) {
            registry.addRecipe(new DistillationEmiRecipe(recipe));
        }

        registry.addCategory(FERMENTATION_CATEGORY);
        registry.addWorkstation(FERMENTATION_CATEGORY, FERMENTATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FERMENTATION.get())) {
            registry.addRecipe(new FermentationEmiRecipe(recipe));
        }

        registry.addCategory(INCUBATION_CATEGORY);
        registry.addWorkstation(INCUBATION_CATEGORY, INCUBATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.INCUBATION.get())) {
            registry.addRecipe(new IncubationEmiRecipe(recipe));
        }

        registry.addCategory(LIQUEFACTION_CATEGORY);
        registry.addWorkstation(LIQUEFACTION_CATEGORY, LIQUEFACTION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get())) {
            registry.addRecipe(new LiquefactionEmiRecipe(recipe));
        }

        registry.addCategory(REFORMATION_CATEGORY);
        registry.addWorkstation(REFORMATION_CATEGORY, REFORMATION_ICON);
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.REFORMATION.get())) {
            registry.addRecipe(new ReformationEmiRecipe(recipe));
        }
    }

    @Override
    public void initialize(EmiInitRegistry registry) {
        var recipeManager = getRecipeManager();

        //now remove sulfurs that have no recipe -> otherwise we see "no source" sulfurs in tag recipes
        //See also Theurgy.Client#onRecipesUpdated
        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());
        var sulfursWithoutRecipe = SulfurRegistry.SULFURS.getEntries().stream()
                .map(DeferredHolder::get)
                .map(AlchemicalSulfurItem.class::cast)
                .filter(sulfur -> liquefactionRecipes.stream().noneMatch(r -> r.value().getResultItem(RegistryAccess.EMPTY) != null && r.value().getResultItem(RegistryAccess.EMPTY).getItem() == sulfur)).map(EmiStack::of).toList();

        sulfursWithoutRecipe.forEach(registry::disableStack);
    }

    public static RecipeManager getRecipeManager(){
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return DistHelper.getRecipeManager();
        }
        return null;
    }

    public static class DistHelper{
        public static RecipeManager getRecipeManager(){
            return Minecraft.getInstance().level.getRecipeManager();
        }
    }
}
