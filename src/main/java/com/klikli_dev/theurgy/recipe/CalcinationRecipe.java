package com.klikli_dev.theurgy.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;

public class CalcinationRecipe extends AbstractCookingRecipe {
    public CalcinationRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, int pCookingTime) {
        super(RecipeTypeRegistry.CALCINATION.get(), pId, "", pIngredient, pResult, 0, pCookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.CALCINATION_OVEN.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CALCINATION.get();
    }

    public static class Serializer implements RecipeSerializer<CalcinationRecipe> {

        public static final int DEFAULT_COOKING_TIME = 200;

        public CalcinationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonElement jsonelement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");

            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pJson, "result"), true, true);
            int cookingTime = GsonHelper.getAsInt(pJson, "cookingtime", DEFAULT_COOKING_TIME);
            return new CalcinationRecipe(pRecipeId, ingredient, result, cookingTime);
        }

        public CalcinationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int cookingTime = pBuffer.readVarInt();
            return new CalcinationRecipe(pRecipeId, ingredient, result, cookingTime);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, CalcinationRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }
    }
}