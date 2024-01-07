package com.klikli_dev.theurgy.datagen.recipe;

import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithCount(Ingredient ingredient, int count) {

    private static final Codec<Pair<Ingredient, Integer>> PAIR_CODEC = Codec.pair(
            TheurgyExtraCodecs.INGREDIENT,
            Codec.INT.fieldOf("count").codec()
    );

    public static final Codec<IngredientWithCount> CODEC = PAIR_CODEC.xmap(pair -> new IngredientWithCount(pair.getFirst(), pair.getSecond()), iwc -> new Pair<>(iwc.ingredient, iwc.count));
}
