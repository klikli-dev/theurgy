/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;

public class LiqueficationRecipeProvider extends JsonRecipeProvider {

    public LiqueficationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "liquefaction");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        var sulfurHelper = new JsonObject();
        sulfurHelper.addProperty(TheurgyConstants.Nbt.SULFUR_SOURCE_ID, this.locFor(Items.OAK_LOG).toString());

        //TODO: provide proper output item
        recipeConsumer.accept(
                this.modLoc("oak_log"),
                this.buildLiquificationRecipe(
                        this.makeItemIngredient(this.locFor(Items.OAK_LOG)),
                        this.makeFluidIngredient(FluidRegistry.SAL_AMMONIAC.getId(), 10),
                        this.makeResult(ItemRegistry.ALCHEMICAL_SULFUR.getId(), 1, sulfurHelper), 200));
    }

    public JsonObject buildLiquificationRecipe(JsonObject ingredient, JsonObject solvent, JsonObject result, int liquefactionTime) {
        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.LIQUEFACTION.getId().toString());
        recipe.add("solvent", solvent);
        recipe.add("ingredient", ingredient);
        recipe.add("result", result);
        recipe.addProperty("liquefaction_time", liquefactionTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Liquefaction Recipes";
    }
}
