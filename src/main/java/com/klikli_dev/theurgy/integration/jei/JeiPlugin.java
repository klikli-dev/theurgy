/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

//TODO: Enable
//@mezz.jei.api.JeiPlugin
//public class JeiPlugin implements IModPlugin {
//    @Override
//    public @NotNull ResourceLocation getPluginUid() {
//        return Theurgy.loc("jei_plugin");
//    }
//
//    @Override
//    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
//
//        ItemRegistry.ITEMS.getEntries().stream().filter(item -> item.get() instanceof DivinationRodItem).forEach(item -> {
//            registration.registerSubtypeInterpreter(item.get(), DivinationRodSubtypeInterpreter.get());
//            Theurgy.LOGGER.debug("Registered Divination Rod JEI Subtype Interpreter for: {}", item.getKey());
//        });
//    }
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registration) {
//        registration.addRecipeCategories(new CalcinationCategory(registration.getJeiHelpers().getGuiHelper()));
//        registration.addRecipeCategories(new LiquefactionCategory(registration.getJeiHelpers().getGuiHelper()));
//        registration.addRecipeCategories(new DistillationCategory(registration.getJeiHelpers().getGuiHelper()));
//        registration.addRecipeCategories(new IncubationCategory(registration.getJeiHelpers().getGuiHelper()));
//    }
//
//    @Override
//    public void registerRecipes(IRecipeRegistration registration) {
//        var level = Minecraft.getInstance().level;
//        var recipeManager = level.getRecipeManager();
//
//        var calcinationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.CALCINATION.get());
//        registration.addRecipes(JeiRecipeTypes.CALCINATION, calcinationRecipes);
//
//        var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());
//        registration.addRecipes(JeiRecipeTypes.LIQUEFACTION, liquefactionRecipes);
//
//        var distillationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.DISTILLATION.get());
//        registration.addRecipes(JeiRecipeTypes.DISTILLATION, distillationRecipes);
//
//        var incubationRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.INCUBATION.get());
//        registration.addRecipes(JeiRecipeTypes.INCUBATION, incubationRecipes);
//    }
//
//    @Override
//    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CALCINATION_OVEN.get()),
//                JeiRecipeTypes.CALCINATION);
//
//        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()),
//                JeiRecipeTypes.LIQUEFACTION);
//
//        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.DISTILLER.get()),
//                JeiRecipeTypes.DISTILLATION);
//
//        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.INCUBATOR.get()),
//                JeiRecipeTypes.INCUBATION);
//    }
//}
