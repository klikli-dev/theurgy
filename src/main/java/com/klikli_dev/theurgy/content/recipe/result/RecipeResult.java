// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.result;

import com.klikli_dev.theurgy.registry.TheurgyRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;


public abstract class RecipeResult {

    public static final Codec<RecipeResult> CODEC = TheurgyRegistries.RECIPE_RESULT_TYPES.byNameCodec().dispatch(RecipeResult::getType, RecipeResultType::codec);

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeResult> STREAM_CODEC = ByteBufCodecs.registry(TheurgyRegistries.Keys.RECIPE_RESULT_TYPES).dispatch(RecipeResult::getType, RecipeResultType::streamCodec);

    /**
     * Get the preferred item stack this result represents.
     */
    public abstract ItemStack getStack();

    /**
     * Get all item stacks this result represents.
     */
    public abstract ItemStack[] getStacks();

    public abstract RecipeResultType<?> getType();
}
