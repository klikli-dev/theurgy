// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class DivinationRodRecipe extends ShapedRecipe {

    public DivinationRodRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, CraftingBookCategory.MISC, pWidth, pHeight, pRecipeItems, pResult);
    }

    @Override
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

            String sourceId = null;
            for (var ingredient : this.getIngredients()) {
                var json = ingredient.toJson();
                if (json instanceof JsonObject jsonObj && jsonObj.has("nbt")) {

                    var ingredientTag = CraftingHelper.getNBT(jsonObj.get("nbt"));
                    if (ingredientTag.contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
                        sourceId = ingredientTag.getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID);
                        break;
                    }
                }
            }

            if (sourceId != null) {
                var translated = this.translateToBlock(sourceId);
                if (translated != null) {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, translated);
                } else {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, sourceId);
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
            String sourceId = null;
            for (int i = 0; i < pInv.getContainerSize(); i++) {
                var stack = pInv.getItem(i);
                if (stack.hasTag()) {
                    var tag = stack.getTag();
                    if (tag.contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
                        sourceId = tag.getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID);
                        break;
                    }
                }
            }

            if (sourceId != null) {
                var translated = sourceId.startsWith("#") ? this.translateTagToBlock(sourceId) : this.translateToBlock(sourceId);
                if (translated != null) {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, translated);
                } else {
                    resultTag.putString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID, sourceId);
                }
            }
        }

        return result;
    }

    public String translateTagToBlock(String sourceTag) {
        //first we check if we have a manual override mapping
        var mapped = ServerConfig.get().recipes.sulfurSourceToBlockMapping.get().get(sourceTag);
        if (mapped != null)
            return mapped;

        //if not we use generic logic to translate ingot, storage block, nugget, raw, ore, dust to (ore)block.
        //even though likely not all of these will be used to create sulfur its good to handle them.

        //examples:
        //target is forge:ores/iron
        //forge:ingots/iron
        //forge:nuggets/iron
        //forge:raw_materials/iron
        //forge:dusts/iron
        //forge:storage_blocks/iron
        //forge:gems/iron

        //special handling for coal items as they are none of the above
        if (sourceTag.equals("#minecraft:coals"))
            return "#forge:ores/coal";

        var parts = sourceTag.split(":");
        var namespace = parts[0];
        var path = parts[1];
        var translatedPath = path;
        if (path.contains("ingots/")) {
            translatedPath = path.replace("ingots/", "ores/");
        } else if (path.contains("nuggets/")) {
            translatedPath = path.replace("nuggets/", "ores/");
        } else if (path.contains("raw_materials/")) {
            translatedPath = path.replace("raw_materials/", "ores/");
        } else if (path.contains("dusts/")) {
            translatedPath = path.replace("dusts/", "ores/");
        } else if (path.contains("storage_blocks/")) {
            translatedPath = translatedPath.replace("storage_blocks/", "ores/");
        } else if (path.contains("gems/")) {
            translatedPath = translatedPath.replace("gems/", "ores/");
        }

        var translatedTag = new ResourceLocation(namespace.substring(1) + ":" + translatedPath);
        if (ForgeRegistries.BLOCKS.tags().getTag(TagKey.create(Registries.BLOCK, translatedTag)).isBound())
            return "#" + translatedTag;

        Theurgy.LOGGER.warn("Could not find an appropriate block tag for sulfur source ttag: " + sourceTag + ", tried tag: #" + translatedTag);
        return null;
    }

    public String translateToBlock(String sourceId) {
        //first we check if we have a manual override mapping
        var mapped = ServerConfig.get().recipes.sulfurSourceToBlockMapping.get().get(sourceId);
        if (mapped != null)
            return mapped;

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
        var parts = sourceId.split(":");
        var namespace = parts[0];
        var path = parts[1];
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
        var translatedRL = new ResourceLocation(namespace + ":" + translatedPath);
        if (ForgeRegistries.BLOCKS.containsKey(translatedRL)) {
            return translatedRL.toString();
        }

        if (!ForgeRegistries.BLOCKS.containsKey(translatedRL)) {
            var fallback = ForgeRegistries.BLOCKS.getKeys().stream().filter(x -> x.getPath().equals(translatedRL.getPath())).findFirst();
            if (fallback.isPresent()) {
                return fallback.get().toString();
            }
        }

        Theurgy.LOGGER.warn("Could not find an appropriate block for sulfur source id: " + sourceId + ", tried path: " + translatedPath);
        return null;
    }

    public static class Serializer implements RecipeSerializer<DivinationRodRecipe> {

        @Override
        public DivinationRodRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromJson(pRecipeId, pJson);

            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem(RegistryAccess.EMPTY));
        }

        @Override
        public DivinationRodRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);

            return new DivinationRodRecipe(pRecipeId, shapedRecipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem(RegistryAccess.EMPTY));
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DivinationRodRecipe pRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
        }
    }
}
