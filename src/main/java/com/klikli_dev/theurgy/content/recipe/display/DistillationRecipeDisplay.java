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

public record DistillationRecipeDisplay(
        SizedIngredient ingredient,
        ItemStackTemplate output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<DistillationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(DistillationRecipeDisplay::ingredient),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(DistillationRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(DistillationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(DistillationRecipeDisplay::craftingStation)
    ).apply(instance, DistillationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DistillationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            DistillationRecipeDisplay::ingredient,
            ItemStackTemplate.STREAM_CODEC,
            DistillationRecipeDisplay::output,
            ByteBufCodecs.INT,
            DistillationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            DistillationRecipeDisplay::craftingStation,
            DistillationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.DISTILLATION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(this.output);
    }
}
