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

public class DistillationRecipe implements Recipe<RecipeWrapper> {

    public static final int DEFAULT_DISTILLATION_TIME = 200;
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final int distillationTime;

    public DistillationRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, int distillationTime) {
        this.id = pId;
        this.ingredient = pIngredient;
        this.result = pResult;
        this.distillationTime = distillationTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.DISTILLATION.get();
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeWrapper pInv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.DISTILLER.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DISTILLATION.get();
    }

    public int getDistillationTime() {
        return this.distillationTime;
    }

    public static class Serializer implements RecipeSerializer<DistillationRecipe> {

        public DistillationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var ingredientElement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
            var ingredient = Ingredient.fromJson(ingredientElement);

            var result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pJson, "result"), true, true);

            var distillationTime = GsonHelper.getAsInt(pJson, "distillation_time", DEFAULT_DISTILLATION_TIME);
            return new DistillationRecipe(pRecipeId, ingredient, result, distillationTime);
        }

        public DistillationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var result = pBuffer.readItem();
            var distillationTime = pBuffer.readVarInt();
            return new DistillationRecipe(pRecipeId, ingredient, result, distillationTime);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, DistillationRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.distillationTime);
        }
    }
}