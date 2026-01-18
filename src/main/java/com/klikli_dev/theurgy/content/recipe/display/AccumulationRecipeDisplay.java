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
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;

public record AccumulationRecipeDisplay(
        Optional<SizedFluidIngredient> evaporant,
        Optional<SlotDisplay> solute,
        FluidStack result,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<AccumulationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.CODEC.optionalFieldOf("evaporant").forGetter(AccumulationRecipeDisplay::evaporant),
            SlotDisplay.CODEC.optionalFieldOf("solute").forGetter(AccumulationRecipeDisplay::solute),
            FluidStack.CODEC.fieldOf("result").forGetter(AccumulationRecipeDisplay::result),
            Codec.INT.fieldOf("time").forGetter(AccumulationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(AccumulationRecipeDisplay::craftingStation)
    ).apply(instance, AccumulationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AccumulationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(SizedFluidIngredient.STREAM_CODEC),
            AccumulationRecipeDisplay::evaporant,
            ByteBufCodecs.optional(SlotDisplay.STREAM_CODEC),
            AccumulationRecipeDisplay::solute,
            FluidStack.STREAM_CODEC,
            AccumulationRecipeDisplay::result,
            ByteBufCodecs.INT,
            AccumulationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            AccumulationRecipeDisplay::craftingStation,
            AccumulationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.ACCUMULATION.get();
    }

    @Override
    public SlotDisplay result() {
        return SlotDisplay.Empty.INSTANCE;
    }
}
