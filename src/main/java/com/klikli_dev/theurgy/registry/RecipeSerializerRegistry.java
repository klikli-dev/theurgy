// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.*;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidTagEmptyCondition;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, Theurgy.MODID);

    public static final RegistryObject<RecipeSerializer<DivinationRodRecipe>> DIVINATION_ROD = RECIPE_SERIALIZERS.register("divination_rod",
            DivinationRodRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CalcinationRecipe>> CALCINATION = RECIPE_SERIALIZERS.register("calcination",
            CalcinationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<LiquefactionRecipe>> LIQUEFACTION = RECIPE_SERIALIZERS.register("liquefaction",
            LiquefactionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<DistillationRecipe>> DISTILLATION = RECIPE_SERIALIZERS.register("distillation",
            DistillationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<IncubationRecipe>> INCUBATION = RECIPE_SERIALIZERS.register("incubation",
            IncubationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<AccumulationRecipe>> ACCUMULATION = RECIPE_SERIALIZERS.register("accumulation",
            AccumulationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CatalysationRecipe>> CATALYSATION = RECIPE_SERIALIZERS.register("catalysation",
            CatalysationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> REFORMATION = RECIPE_SERIALIZERS.register("reformation", ReformationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> FERMENTATION = RECIPE_SERIALIZERS.register("fermentation", FermentationRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> DIGESTION = RECIPE_SERIALIZERS.register("digestion", DigestionRecipe.Serializer::new);


    public static void onRegisterRecipeSerializers(RegisterEvent event) {
        //restrict to one event type otherwise it is called multiple times
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            // the actual recipe serializers are registered by the deferred register, but we register related serializers that do not have a Registry here
            CraftingHelper.register(FluidTagEmptyCondition.Serializer.INSTANCE);
            CraftingHelper.register(Theurgy.loc("fluid"), FluidIngredient.Serializer.INSTANCE);
        }
    }
}
