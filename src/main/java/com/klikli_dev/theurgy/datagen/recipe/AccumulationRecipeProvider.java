// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

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
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.BiConsumer;

public class AccumulationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = AccumulationRecipe.DEFAULT_TIME;

    public AccumulationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "accumulation");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();

        this.makeRecipe("sal_ammoniac_from_water",
                new Builder(new FluidStack(salAmmoniac, 100))
                        .evaporant(FluidTags.WATER, 1000)
                        .time(TIME));

        this.makeRecipe("sal_ammoniac_from_water_and_sal_ammoniac_crystal",
                new Builder(new FluidStack(salAmmoniac, 1000))
                        .evaporant(FluidTags.WATER, 1000)
                        .solute(ItemTagRegistry.GEMS_SAL_AMMONIAC)
                        .time(TIME));
    }

    protected void makeRecipe(String name, Builder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }


    @Override
    public String getName() {
        return "Accumulation Recipes";
    }


    protected static class Builder extends RecipeBuilder<Builder> {
        protected Builder(FluidStack result) {
            super(RecipeTypeRegistry.ACCUMULATION);
            this.result(result);
        }

        public Builder evaporant(TagKey<Fluid> tag, int amount) {
            this.recipe.addProperty("evaporantAmount", amount);
            return this.ingredient("evaporant", tag, -1);
        }

        public Builder solute(TagKey<Item> tag) {
            return this.ingredient("solute", tag, -1);
        }
    }
}
