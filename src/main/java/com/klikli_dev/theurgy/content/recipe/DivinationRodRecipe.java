// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;


public class DivinationRodRecipe extends ShapedRecipe {

    public DivinationRodRecipe(String pGroup, ShapedRecipePattern pPattern, ItemStack pResult, boolean pShowNotification) {
        super(pGroup, CraftingBookCategory.MISC, pPattern, pResult, pShowNotification);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DIVINATION_ROD.get();
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
        if (BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, translatedTag)).isPresent())
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
        if (BuiltInRegistries.BLOCK.containsKey(translatedRL)) {
            return translatedRL.toString();
        }

        if (!BuiltInRegistries.BLOCK.containsKey(translatedRL)) {
            var fallback = BuiltInRegistries.BLOCK.keySet().stream().filter(x -> x.getPath().equals(translatedRL.getPath())).findFirst();
            if (fallback.isPresent()) {
                return fallback.get().toString();
            }
        }

        Theurgy.LOGGER.warn("Could not find an appropriate block for sulfur source id: " + sourceId + ", tried path: " + translatedPath);
        return null;
    }

    public static class Serializer implements RecipeSerializer<DivinationRodRecipe> {

        //copied from ShapedRecipe.Serializer because xMapping it somehow causes a json null thingy error
        public static final Codec<DivinationRodRecipe> CODEC = RecordCodecBuilder.create(
                p_311728_ -> p_311728_.group(
                                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(p_311729_ -> p_311729_.getGroup()),
                                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.getResultItem(RegistryAccess.EMPTY)),
                                ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(p_311731_ -> p_311731_.showNotification())
                        )
                        .apply(p_311728_, DivinationRodRecipe::new)
        );

        @Override
        public Codec<DivinationRodRecipe> codec() {
            return CODEC;
        }

        @Override
        public DivinationRodRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            //noinspection deprecation
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DivinationRodRecipe pRecipe) {
            //noinspection deprecation
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
        }
    }
}
