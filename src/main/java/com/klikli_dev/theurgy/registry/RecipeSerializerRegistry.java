/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.DivinationRodRecipe;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidTagEmptyCondition;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
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

    public static void onRegisterRecipeSerializers(RegisterEvent event){
        //restrict to one event type otherwise it is called multiple times
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            // the actual recipe serializers are registered by the deferred register, but we register related serializers that do not have a Registry here
            CraftingHelper.register(FluidTagEmptyCondition.Serializer.INSTANCE);
            CraftingHelper.register(Theurgy.loc("fluid"), FluidIngredient.Serializer.INSTANCE);
        }
    }
}
