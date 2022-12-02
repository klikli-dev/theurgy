/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.registry.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

public class DivinationRodRecipe extends ShapedRecipe {

    public DivinationRodRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, pWidth, pHeight, pRecipeItems, pResult);
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.DIVINATION_ROD.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer pInv) {
        var result = this.getResultItem().copy();

        //check pInv for ingredients with sulfur source id, if so, find the appropriate block id based on it and set it on the result item
        ResourceLocation sourceId = null;
        for (int i = 0; i < pInv.getContainerSize(); i++) {
            var stack = pInv.getItem(i);
            if (stack.hasTag()) {
                var tag = stack.getTag();
                if (tag.contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
                    sourceId = new ResourceLocation(tag.getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID));
                    break;
                }
            }
        }

        if (sourceId != null) {
            var translated = this.translateToBlock(sourceId);
            if (translated != null) {
                result.getOrCreateTag().putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, translated.toString());
            } else {
                result.getOrCreateTag().putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, sourceId.toString());
            }
        }

        return super.assemble(pInv);
    }

    public ResourceLocation translateToBlock(ResourceLocation sourceId) {
        //first we check if we have a manual override mapping
        var mapped = ServerConfig.get().recipes.sulfurSourceToBlockMapping.get().get(sourceId.toString());
        if (mapped != null)
            return new ResourceLocation(mapped);

        //if not we use generic logic to translate ingot, storage block, nugget, raw, ore, dust to (ore)block.
        //even though likely not all of these will be used to create sulfur its good to handle them.

        //examples:
        //raw_iron
        //iron_nugget
        //iron_ingot
        //iron_ore
        //deepslate_iron_ore
        //iron_dust
        //iron_block

        var path = sourceId.getPath();
        var translatedPath = path;
        if (path.contains("raw_")) {
            translatedPath = path.replace("raw_", "");
        } else if (path.contains("_nugget")) {
            translatedPath = path.replace("_nugget", "");
        } else if (path.contains("_ingot")) {
            translatedPath = path.replace("_ingot", "");
        } else if (path.contains("_ore")) {
            translatedPath = path.replace("_ore", "");
            if (path.contains("deepslate_")) {
                translatedPath = translatedPath.replace("deepslate_", "");
            }
        } else if (path.contains("_dust")) {
            translatedPath = path.replace("_dust", "");
        } else if (path.contains("_block")) {
            translatedPath = path.replace("_block", "");
        }

        translatedPath = translatedPath + "_ore";
        var translated = new ResourceLocation(sourceId.getNamespace(), translatedPath);

        if (ForgeRegistries.BLOCKS.containsKey(translated)) {
            return translated;
        }

        if (!ForgeRegistries.BLOCKS.containsKey(translated)) {
            var fallback = ForgeRegistries.BLOCKS.getKeys().stream().filter(x -> x.getPath().equals(translated.getPath())).findFirst();
            if (fallback.isPresent()) {
                return fallback.get();
            }
        }

        Theurgy.LOGGER.warn("Could not find an appropriate block for sulfur source id: " + sourceId + ", tried path: " + translatedPath);
        return null;
    }

    public static class Serializer implements RecipeSerializer<DivinationRodRecipe> {

        public DivinationRodRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromJson(pRecipeId, pJson);

            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
        }

        public DivinationRodRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);

            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
        }

        public void toNetwork(FriendlyByteBuf pBuffer, DivinationRodRecipe pRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
        }
    }
}
