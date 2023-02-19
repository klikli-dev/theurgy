/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.DivinationRodRecipe;
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

    public static void onRegisterRecipeSerializers(RegisterEvent event){
        CraftingHelper.register(FluidTagEmptyCondition.Serializer.INSTANCE);
        CraftingHelper.register(Theurgy.loc("fluid"), FluidIngredient.Serializer.INSTANCE);
    }
}
