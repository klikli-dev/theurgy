// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.result;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.CraftingHelper;


public abstract class RecipeResult {
    public static RecipeResult fromJson(JsonObject json) {
        if (json.has("item")) {
            return new ItemRecipeResult(CraftingHelper.getItemStack(json, true, true));
        } else if (json.has("tag")) {
            return TagRecipeResult.CODEC.parse(JsonOps.INSTANCE, json).result().get();
        } else {
            throw new JsonParseException("RecipeResult must have either an \"tag\" or \"item\" field.");
        }
    }

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
