/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class DivinationRodRecipe extends ShapedRecipe {

    public DivinationRodRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, CraftingBookCategory.MISC, pWidth, pHeight, pRecipeItems, pResult);
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DIVINATION_ROD.get();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {

        var result = super.getResultItem(registryAccess);

        var resultTag = result.getOrCreateTag();

        //if we do not have a linked block id in the recipe output, we try to get it from the ingredients.
        //we check if any ingredient supplies a source id for the linked block id.
        //this allows JEI/creative menu to show the correct linked block id effects (altered item name, tooltip, etc)
        if (!resultTag.contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID)) {

            ResourceLocation sourceId = null;
            for (var ingredient : this.getIngredients()) {
                var json = ingredient.toJson();
                if (json instanceof JsonObject jsonObj && jsonObj.has("nbt")) {

                    var ingredientTag = CraftingHelper.getNBT(jsonObj.get("nbt"));
                    if (ingredientTag.contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
                        sourceId = new ResourceLocation(ingredientTag.getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID));
                        break;
                    }
                }
            }

            if (sourceId != null) {
                var translated = this.translateToBlock(sourceId);
                if (translated != null) {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, translated.toString());
                } else {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, sourceId.toString());
                }
                //we also set the preview mode, to allow the assemble() method to override based on the actual input.
                resultTag.putBoolean(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID_PREVIEW_MODE, true);
            }
        }

        return result;
    }

    @Override
    public ItemStack assemble(CraftingContainer pInv, RegistryAccess registryAccess) {
        var result = this.getResultItem(registryAccess).copy();


        var resultTag = result.getOrCreateTag();


        //if the recipe already has a linked block, we don't need to do the translation stuff.
        //if that linked block is only a preview (coming from getResultItem(), used mainly for correct display in JEI),
        // we have to ignore it and translate after all
        if (!resultTag.contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID) ||
                resultTag.getBoolean(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID_PREVIEW_MODE)
        ) {
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
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, translated.toString());
                } else {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, sourceId.toString());
                }
            }
        }

        return super.assemble(pInv, registryAccess);
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

            //we can use null here because the shaped recipe does not use the registry access
            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem(RegistryAccess.EMPTY));
        }

        public DivinationRodRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);

            //we can use null here because the shaped recipe does not use the registry access
            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem(RegistryAccess.EMPTY));
        }

        public void toNetwork(FriendlyByteBuf pBuffer, DivinationRodRecipe pRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
        }
    }
}
