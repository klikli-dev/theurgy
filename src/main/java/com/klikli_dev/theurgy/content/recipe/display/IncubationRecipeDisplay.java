// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.display;

import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.registry.RecipeDisplayRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record IncubationRecipeDisplay(
        Ingredient mercury,
        Ingredient salt,
        Ingredient sulfur,
        RecipeResult output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<IncubationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("mercury").forGetter(IncubationRecipeDisplay::mercury),
            Ingredient.CODEC.fieldOf("salt").forGetter(IncubationRecipeDisplay::salt),
            Ingredient.CODEC.fieldOf("sulfur").forGetter(IncubationRecipeDisplay::sulfur),
            RecipeResult.CODEC.fieldOf("output").forGetter(IncubationRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(IncubationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(IncubationRecipeDisplay::craftingStation)
    ).apply(instance, IncubationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IncubationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            IncubationRecipeDisplay::mercury,
            Ingredient.CONTENTS_STREAM_CODEC,
            IncubationRecipeDisplay::salt,
            Ingredient.CONTENTS_STREAM_CODEC,
            IncubationRecipeDisplay::sulfur,
            RecipeResult.STREAM_CODEC,
            IncubationRecipeDisplay::output,
            ByteBufCodecs.INT,
            IncubationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            IncubationRecipeDisplay::craftingStation,
            IncubationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.INCUBATION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(this.output.getStack()));
    }
}
