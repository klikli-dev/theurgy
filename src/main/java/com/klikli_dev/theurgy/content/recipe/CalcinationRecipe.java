/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CalcinationRecipe implements Recipe<RecipeWrapper> {

    public static final int DEFAULT_CALCINATION_TIME = 200;
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final int calcinationTime;

    public CalcinationRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, int calcinationTime) {
        this.id = pId;
        this.ingredient = pIngredient;
        this.result = pResult;
        this.calcinationTime = calcinationTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.CALCINATION.get();
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeWrapper pInv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.CALCINATION_OVEN.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CALCINATION.get();
    }

    public int getCalcinationTime() {
        return this.calcinationTime;
    }

    public static class Serializer implements RecipeSerializer<CalcinationRecipe> {

        public CalcinationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var ingredientElement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
            var ingredient = Ingredient.fromJson(ingredientElement);

            var result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pJson, "result"), true, true);

            var calcinationTime = GsonHelper.getAsInt(pJson, "calcination_time", DEFAULT_CALCINATION_TIME);
            return new CalcinationRecipe(pRecipeId, ingredient, result, calcinationTime);
        }

        public CalcinationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var result = pBuffer.readItem();
            var calcinationTime = pBuffer.readVarInt();
            return new CalcinationRecipe(pRecipeId, ingredient, result, calcinationTime);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, CalcinationRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.calcinationTime);
        }
    }
}