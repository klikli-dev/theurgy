// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AccumulationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = AccumulationRecipe.DEFAULT_TIME;

    public AccumulationRecipeProvider(PackOutput packOutput, CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Theurgy.MODID, "accumulation");
    }

    @Override
    public void buildRecipes(BiConsumer<Identifier, JsonObject> recipeConsumer) {
        var salAmmoniac = FluidRegistry.SAL_AMMONIAC.get();

        this.makeRecipe("sal_ammoniac_from_water",
                new Builder(new FluidStackTemplate(salAmmoniac, 100))
                        .evaporant(FluidTags.WATER, 1000)
                        .time(TIME));

        this.makeRecipe("sal_ammoniac_from_water_and_sal_ammoniac_crystal",
                new Builder(new FluidStackTemplate(salAmmoniac, 1000))
                        .evaporant(FluidTags.WATER, 1000)
                        .solute(ItemTagRegistry.GEMS_SAL_AMMONIAC)
                        .time(TIME));

        this.makeRecipe("water_from_crystallized_water",
                new Builder(new FluidStackTemplate(Fluids.WATER, 1000))
                        .solute(ItemRegistry.CRYSTALLIZED_WATER)
                        .time(TIME));

        this.makeRecipe("lava_from_crystallized_lava",
                new Builder(new FluidStackTemplate(Fluids.LAVA, 1000))
                        .solute(ItemRegistry.CRYSTALLIZED_LAVA)
                        .time(TIME));
    }

    protected void makeRecipe(String name, Builder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }


    @Override
    public String getName() {
        return "Accumulation Recipes";
    }


    protected class Builder extends RecipeBuilder<Builder> {
        protected Builder(FluidStackTemplate result) {
            super(RecipeTypeRegistry.ACCUMULATION);
            this.result(result);
        }

        public Builder evaporant(TagKey<Fluid> tag, int amount) {
            return this.sizedFluidIngredient("evaporant", tag, amount);
        }

        public Builder solute(TagKey<Item> tag) {
            return this.ingredient("solute", tag);
        }

        public Builder solute(ItemLike item) {
            return this.ingredient("solute", item);
        }
    }
}
