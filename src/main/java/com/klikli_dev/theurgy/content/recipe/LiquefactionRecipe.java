/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
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
import net.minecraftforge.registries.ForgeRegistries;

public class LiquefactionRecipe implements Recipe<RecipeWrapperWithFluid> {

    public static final int DEFAULT_LIQUEFACTION_TIME = 200;
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final FluidIngredient solvent;
    protected final ItemStack result;
    protected final int liquefactionTime;

    public LiquefactionRecipe(ResourceLocation pId, Ingredient pIngredient, FluidIngredient pSolvent, ItemStack pResult, int liquefactionTime) {
        this.id = pId;
        this.ingredient = pIngredient;
        this.solvent = pSolvent;
        this.result = pResult;
        this.liquefactionTime = liquefactionTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.LIQUEFACTION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0)) && this.solvent.test(pContainer.getTank().getFluid());
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pInv) {
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
        return new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.LIQUEFACTION.get();
    }

    public int getLiquefactionTime() {
        return this.liquefactionTime;
    }

    public FluidIngredient getSolvent() {
        return this.solvent;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public static class Serializer implements RecipeSerializer<LiquefactionRecipe> {

        public LiquefactionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var ingredientElement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
            var ingredient = Ingredient.fromJson(ingredientElement);

            var solventElement = GsonHelper.isArrayNode(pJson, "solvent") ? GsonHelper.getAsJsonArray(pJson, "solvent") : GsonHelper.getAsJsonObject(pJson, "solvent");
            var solvent = FluidIngredient.fromJson(solventElement);

            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pJson, "result"), true, true);

            int liquefactionTime = GsonHelper.getAsInt(pJson, "liquefaction_time", DEFAULT_LIQUEFACTION_TIME);

            result = fixSourceIdIfNecessary(result, ingredient);

            return new LiquefactionRecipe(pRecipeId, ingredient, solvent, result, liquefactionTime);
        }

        public ItemStack fixSourceIdIfNecessary(ItemStack resultItem, Ingredient ingredient) {
            if (resultItem.getItem() instanceof AlchemicalSulfurItem sulfur
                    && sulfur.autoGenerateSourceIdInRecipe
                    && AlchemicalSulfurItem.getSourceStack(resultItem).isEmpty()) {

                var modified = resultItem.copy();

                if(ingredient.values.length > 0){
                    if(ingredient.values[0] instanceof Ingredient.TagValue tagValue){
                        modified.getOrCreateTag().putString("#" + TheurgyConstants.Nbt.SULFUR_SOURCE_ID, tagValue.tag.location().toString());
                        return modified;
                    } else if(ingredient.values[0] instanceof Ingredient.ItemValue itemValue){
                        modified.getOrCreateTag().putString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID,
                                ForgeRegistries.ITEMS.getKey(itemValue.getItems().stream().findFirst().get().getItem()).toString());
                        return modified;
                    }
                }

                throw new IllegalArgumentException("AlchemicalSulfurItem " + resultItem + " is configured to generate source id in recipe, but Ingredient.values[0] is not a tag or item value so we don't know how to get the source.");
            }
            return resultItem;
        }

        public LiquefactionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var solvent = FluidIngredient.fromNetwork(pBuffer);
            var result = pBuffer.readItem();
            var liquefactionTime = pBuffer.readVarInt();
            return new LiquefactionRecipe(pRecipeId, ingredient, solvent, result, liquefactionTime);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, LiquefactionRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pRecipe.solvent.toNetwork(pBuffer); //Ingredient.toNetwork(pBuffer); should suffice here as it redirects to the serializer registry anyway
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.liquefactionTime);
        }
    }
}