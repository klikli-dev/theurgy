// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.display;

import com.klikli_dev.theurgy.registry.RecipeDisplayRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record CatalysationRecipeDisplay(
        Ingredient ingredient,
        int totalMercuryFlux,
        int mercuryFluxPerTick,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<CatalysationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CatalysationRecipeDisplay::ingredient),
            Codec.INT.fieldOf("totalMercuryFlux").forGetter(CatalysationRecipeDisplay::totalMercuryFlux),
            Codec.INT.fieldOf("mercuryFluxPerTick").forGetter(CatalysationRecipeDisplay::mercuryFluxPerTick),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(CatalysationRecipeDisplay::craftingStation)
    ).apply(instance, CatalysationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CatalysationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            CatalysationRecipeDisplay::ingredient,
            ByteBufCodecs.INT,
            CatalysationRecipeDisplay::totalMercuryFlux,
            ByteBufCodecs.INT,
            CatalysationRecipeDisplay::mercuryFluxPerTick,
            SlotDisplay.STREAM_CODEC,
            CatalysationRecipeDisplay::craftingStation,
            CatalysationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.CATALYSATION.get();
    }

    @Override
    public SlotDisplay result() {
        return SlotDisplay.Empty.INSTANCE;
    }
}
