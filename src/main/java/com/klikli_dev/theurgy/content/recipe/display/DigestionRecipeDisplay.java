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
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public record DigestionRecipeDisplay(
        SizedFluidIngredient fluid,
        List<SizedIngredient> sizedIngredients,
        ItemStack output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<DigestionRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.CODEC.fieldOf("fluid").forGetter(DigestionRecipeDisplay::fluid),
            SizedIngredient.NESTED_CODEC.listOf().fieldOf("sizedIngredients").forGetter(DigestionRecipeDisplay::sizedIngredients),
            ItemStack.CODEC.fieldOf("output").forGetter(DigestionRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(DigestionRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(DigestionRecipeDisplay::craftingStation)
    ).apply(instance, DigestionRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DigestionRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            DigestionRecipeDisplay::fluid,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            DigestionRecipeDisplay::sizedIngredients,
            ItemStack.STREAM_CODEC,
            DigestionRecipeDisplay::output,
            ByteBufCodecs.INT,
            DigestionRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            DigestionRecipeDisplay::craftingStation,
            DigestionRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.DIGESTION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(this.output));
    }
}
