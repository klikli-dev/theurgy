/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class AccumulationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = AccumulationRecipe.DEFAULT_ACCUMULATION_TIME;

    public AccumulationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "accumulation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();

        this.makeRecipe("sal_ammoniac_from_water", salAmmoniac, 100, (Item) null, FluidTags.WATER, 1000, TIME);
        this.makeRecipe("sal_ammoniac_from_water_and_sal_ammoniac_crystal", salAmmoniac, 1000, ItemTagRegistry.SAL_AMMONIAC_GEMS, FluidTags.WATER, 1000, TIME);
    }

    public void makeRecipe(String name, Fluid result, int resultAmount, @Nullable Item solute, TagKey<Fluid> evaporant, int evaporantAmount, int accumulationTime) {
        this.recipeConsumer.accept(
                this.modLoc(name),
                this.makeRecipeJson(
                        this.makeFluidTagIngredient(evaporant.location(), evaporantAmount),
                        solute != null ? this.makeItemIngredient(this.locFor(solute)) : null,
                        this.makeFluidResult(new FluidStack(result, resultAmount)), accumulationTime));
    }

    public void makeRecipe(String name, Fluid result, int resultAmount, @Nullable TagKey<Item> solute, TagKey<Fluid> evaporant, int evaporantAmount, int accumulationTime) {
        this.recipeConsumer.accept(
                this.modLoc(name),
                this.makeRecipeJson(
                        this.makeFluidTagIngredient(evaporant.location(), evaporantAmount),
                        solute != null ? this.makeTagIngredient(solute.location()) : null,
                        this.makeFluidResult(new FluidStack(result, resultAmount)), accumulationTime));
    }

    public JsonObject makeRecipeJson(JsonObject evaporant, @Nullable JsonObject solute, JsonObject result, int accumulationTime) {

        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.ACCUMULATION.getId().toString());
        recipe.add("evaporant", evaporant);
        if (solute != null)
            recipe.add("solute", solute);
        recipe.add("result", result);
        recipe.addProperty("accumulation_time", accumulationTime);
        return recipe;
    }


    @Override
    public String getName() {
        return "Accumulation Recipes";
    }
}
