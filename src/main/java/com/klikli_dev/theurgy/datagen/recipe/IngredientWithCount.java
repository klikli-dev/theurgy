// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.klikli_dev.theurgy.util.TheurgyExtraStreamCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithCount(Ingredient ingredient, int count) {

    //Note: for implementation reasons count has to be above ingredient, otherwise we will get a JSON Null issue thingy
    private static final Codec<Pair<Integer, Ingredient>> PAIR_CODEC = Codec.pair(
            Codec.INT.optionalFieldOf("count", 1).codec(),
            Ingredient.CODEC
    );

    public static final Codec<IngredientWithCount> CODEC = PAIR_CODEC.xmap(pair -> new IngredientWithCount(pair.getSecond(), pair.getFirst()), iwc -> new Pair<>(iwc.count, iwc.ingredient));

    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientWithCount> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            iwc -> iwc.ingredient,
            ByteBufCodecs.INT,
            iwc -> iwc.count,
            IngredientWithCount::new
    );
}
