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

public class IncubationRecipe implements Recipe<IncubatorRecipeWrapper> {

    public static final int DEFAULT_INCUBATION_TIME = 200;

    protected final ResourceLocation id;
    protected final Ingredient mercury;
    protected final Ingredient salt;
    protected final Ingredient sulfur;

    protected final RecipeResult result;
    protected final int incubationTime;

    public IncubationRecipe(ResourceLocation pId, Ingredient mercury, Ingredient salt, Ingredient sulfur, RecipeResult pResult, int incubationTime) {
        this.id = pId;
        this.mercury = mercury;
        this.salt = salt;
        this.sulfur = sulfur;
        this.result = pResult;
        this.incubationTime = incubationTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.INCUBATION.get();
    }

    @Override
    public boolean matches(IncubatorRecipeWrapper pContainer, Level pLevel) {
        return this.mercury.test(pContainer.getMercuryVesselInv().getStackInSlot(0)) &&
                this.salt.test(pContainer.getSaltVesselInv().getStackInSlot(0)) &&
                this.sulfur.test(pContainer.getSulfurVesselInv().getStackInSlot(0));
    }

    @Override
    public ItemStack assemble(IncubatorRecipeWrapper pInv) {
        return this.result.getStack().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.getStack();
    }

    public RecipeResult getResult() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.mercury);
        nonnulllist.add(this.salt);
        nonnulllist.add(this.sulfur);
        return nonnulllist;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.INCUBATOR.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.INCUBATION.get();
    }

    public int getIncubationTime() {
        return this.incubationTime;
    }

    public Ingredient getMercury() {
        return this.mercury;
    }

    public Ingredient getSalt() {
        return this.salt;
    }

    public Ingredient getSulfur() {
        return this.sulfur;
    }

    public static class Serializer implements RecipeSerializer<IncubationRecipe> {

        public IncubationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var mercuryElement = GsonHelper.isArrayNode(pJson, "mercury") ?
                    GsonHelper.getAsJsonArray(pJson, "mercury") : GsonHelper.getAsJsonObject(pJson, "mercury");
            var mercury = Ingredient.fromJson(mercuryElement);

            var saltElement = GsonHelper.isArrayNode(pJson, "salt") ?
                    GsonHelper.getAsJsonArray(pJson, "salt") : GsonHelper.getAsJsonObject(pJson, "salt");
            var salt = Ingredient.fromJson(saltElement);

            var sulfurElement = GsonHelper.isArrayNode(pJson, "sulfur") ?
                    GsonHelper.getAsJsonArray(pJson, "sulfur") : GsonHelper.getAsJsonObject(pJson, "sulfur");
            var sulfur = Ingredient.fromJson(sulfurElement);

            var result = RecipeResult.fromJson(GsonHelper.getAsJsonObject(pJson, "result"));

            var incubationTime = GsonHelper.getAsInt(pJson, "incubation_time", DEFAULT_INCUBATION_TIME);
            return new IncubationRecipe(pRecipeId, mercury, salt, sulfur, result, incubationTime);
        }

        public IncubationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var mercury = Ingredient.fromNetwork(pBuffer);
            var salt = Ingredient.fromNetwork(pBuffer);
            var sulfur = Ingredient.fromNetwork(pBuffer);
            var result = RecipeResult.fromNetwork(pBuffer);
            var incubationTime = pBuffer.readVarInt();
            return new IncubationRecipe(pRecipeId, mercury, salt, sulfur, result, incubationTime);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, IncubationRecipe pRecipe) {
            pRecipe.mercury.toNetwork(pBuffer);
            pRecipe.salt.toNetwork(pBuffer);
            pRecipe.sulfur.toNetwork(pBuffer);
            pRecipe.result.toNetwork(pBuffer);
            pBuffer.writeVarInt(pRecipe.incubationTime);
        }
    }
}