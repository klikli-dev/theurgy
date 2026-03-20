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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record CalcinationRecipeDisplay(
        SizedIngredient ingredient,
        ItemStack output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<CalcinationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(CalcinationRecipeDisplay::ingredient),
            ItemStack.CODEC.fieldOf("output").forGetter(CalcinationRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(CalcinationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(CalcinationRecipeDisplay::craftingStation)
    ).apply(instance, CalcinationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CalcinationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            CalcinationRecipeDisplay::ingredient,
            ItemStack.STREAM_CODEC,
            CalcinationRecipeDisplay::output,
            ByteBufCodecs.INT,
            CalcinationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            CalcinationRecipeDisplay::craftingStation,
            CalcinationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.CALCINATION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(this.output));
    }
}
