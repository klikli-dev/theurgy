// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class DivinationRodRecipe extends ShapedRecipe {

    public DivinationRodRecipe(@NotNull String pGroup, @NotNull ShapedRecipePattern pPattern, @NotNull ItemStack pResult, boolean pShowNotification) {
        super(pGroup, CraftingBookCategory.MISC, pPattern, pResult, pShowNotification);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DIVINATION_ROD.get();
    }

    @SuppressWarnings({"DataFlowIssue", "OptionalGetWithoutIsPresent"})
    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput pInv, HolderLookup.@NotNull Provider pRegistries) {
        var result = this.getResultItem(pRegistries).copy();

        if (result.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK) || result.has(DataComponentRegistry.DIVINATION_LINKED_TAG))
            return result;

        //check pInv for ingredients with sulfur source id, if so, find the appropriate block id based on it and set it on the result item
        for (int i = 0; i < pInv.size(); i++) {
            var stack = pInv.getItem(i);
            if (stack.has(DataComponentRegistry.SULFUR_SOURCE_ITEM)) {
                var sourceItem = stack.get(DataComponentRegistry.SULFUR_SOURCE_ITEM);
                var sourceItemKey = sourceItem.unwrapKey();
                if (sourceItemKey.isEmpty())
                    continue;

                var sourceBlock = this.translateToBlock(sourceItem.unwrapKey().get().location().toString());

                if (sourceBlock != null) {
                    result.set(DataComponentRegistry.DIVINATION_LINKED_BLOCK, BuiltInRegistries.BLOCK.getHolder(ResourceLocation.parse(sourceBlock)).get());
                }
                break;
            }
            if (stack.has(DataComponentRegistry.SULFUR_SOURCE_TAG)) {
                var sourceItemTag = stack.get(DataComponentRegistry.SULFUR_SOURCE_TAG);
                var sourceBlockTag = this.translateTagToBlock(sourceItemTag.location().toString());

                if (sourceBlockTag != null) {
                    result.set(DataComponentRegistry.DIVINATION_LINKED_TAG, BlockTags.create(ResourceLocation.parse(sourceBlockTag)));
                }
                break;
            }
        }

        return result;
    }

    public @Nullable String translateTagToBlock(@NotNull String sourceTag) {
        //first we check if we have a manual override mapping
        var mapped = ServerConfig.get().recipes.sulfurSourceToBlockMapping.get().get(sourceTag);
        if (mapped != null)
            return mapped;

        //if not we use generic logic to translate ingot, storage block, nugget, raw, ore, dust to (ore)block.
        //even though likely not all of these will be used to create sulfur its good to handle them.

        //examples:
        //target is c:ores/iron
        //c:ingots/iron
        //c:nuggets/iron
        //c:raw_materials/iron
        //c:dusts/iron
        //c:storage_blocks/iron
        //c:gems/iron

        //special handling for coal items as they are none of the above
        if (sourceTag.equals("#minecraft:coals"))
            return "#c:ores/coal";

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

        var translatedTag = ResourceLocation.parse(namespace.substring(1) + ":" + translatedPath);
        if (BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, translatedTag)).isPresent())
            return "#" + translatedTag;

        Theurgy.LOGGER.warn("Could not find an appropriate block tag for sulfur source ttag: " + sourceTag + ", tried tag: #" + translatedTag);
        return null;
    }

    public @Nullable String translateToBlock(@NotNull String sourceId) {
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
        var translatedRL = ResourceLocation.parse(namespace + ":" + translatedPath);
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
        public static final MapCodec<DivinationRodRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340778_ -> p_340778_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.getResultItem(RegistryAccess.EMPTY)),
                                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
                        )
                        .apply(p_340778_, DivinationRodRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, DivinationRodRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                ShapedRecipe::getGroup,
                ShapedRecipePattern.STREAM_CODEC,
                r -> r.pattern,
                ItemStack.OPTIONAL_STREAM_CODEC,
                r -> r.getResultItem(RegistryAccess.EMPTY),
                ByteBufCodecs.BOOL,
                ShapedRecipe::showNotification,
                DivinationRodRecipe::new
        );


        @Override
        public @NotNull MapCodec<DivinationRodRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DivinationRodRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
