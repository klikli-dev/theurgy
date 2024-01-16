// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.result;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;


public abstract class RecipeResult {
    public static final Codec<RecipeResult> CODEC = ExtraCodecs.xor(ItemRecipeResult.CODEC, TagRecipeResult.CODEC)
            .xmap(first -> first.map(l -> l, r -> r), second -> {
                if (second instanceof TagRecipeResult tag) {
                    return Either.right(tag);
                } else if (second instanceof ItemRecipeResult item) {
                    return Either.left(item);
                } else {
                    throw new UnsupportedOperationException("This is neither an ItemRecipeResult nor a TagRecipeResult.");
                }
            });

    public static RecipeResult fromNetwork(FriendlyByteBuf pBuffer) {
        var type = pBuffer.readByte();
        if (type == ItemRecipeResult.TYPE) {
            return ItemRecipeResult.fromNetwork(pBuffer);
        } else if (type == TagRecipeResult.TYPE) {
            return TagRecipeResult.fromNetwork(pBuffer);
        } else {
            throw new IllegalArgumentException("Unknown recipe result type: " + type);
        }
    }

    /**
     * Get the preferred item stack this result represents.
     */
    public abstract ItemStack getStack();

    /**
     * Get all item stacks this result represents.
     */
    public abstract ItemStack[] getStacks();

    public abstract byte getType();

    public void toNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeByte(this.getType());
    }
}
