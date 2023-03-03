/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

/**
 * A tag result for recipes that use tags as output.
 */
public class ItemRecipeResult extends RecipeResult {

    public static byte TYPE = 0;

    private final ItemStack stack;

    public ItemRecipeResult(ItemStack stack) {
        this.stack = stack;
    }

    public static ItemRecipeResult fromNetwork(FriendlyByteBuf pBuffer) {
        return new ItemRecipeResult(pBuffer.readItem());
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public byte getType() {
        return TYPE;
    }
}
