// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.result;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * A tag result for recipes that use tags as output.
 */
public class ItemRecipeResult extends RecipeResult {

    public static final Codec<ItemRecipeResult> CODEC = ItemStack.ITEM_WITH_COUNT_CODEC.xmap(ItemRecipeResult::new, ItemRecipeResult::getStack);

    public static byte TYPE = 0;

    private final ItemStack stack;

    @Nullable
    private ItemStack[] cachedStacks;

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
    public ItemStack[] getStacks() {
        if (this.cachedStacks == null) {
            this.cachedStacks = new ItemStack[]{this.stack};
        }
        return this.cachedStacks;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer) {
        super.toNetwork(pBuffer); //write type

        pBuffer.writeItem(this.stack);
    }
}
