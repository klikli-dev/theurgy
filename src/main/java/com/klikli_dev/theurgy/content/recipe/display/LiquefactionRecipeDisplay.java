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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public record LiquefactionRecipeDisplay(
        Ingredient ingredient,
        SizedFluidIngredient solvent,
        ItemStack output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<LiquefactionRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(LiquefactionRecipeDisplay::ingredient),
            SizedFluidIngredient.CODEC.fieldOf("solvent").forGetter(LiquefactionRecipeDisplay::solvent),
            ItemStack.CODEC.fieldOf("output").forGetter(LiquefactionRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(LiquefactionRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(LiquefactionRecipeDisplay::craftingStation)
    ).apply(instance, LiquefactionRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            LiquefactionRecipeDisplay::ingredient,
            SizedFluidIngredient.STREAM_CODEC,
            LiquefactionRecipeDisplay::solvent,
            ItemStack.STREAM_CODEC,
            LiquefactionRecipeDisplay::output,
            ByteBufCodecs.INT,
            LiquefactionRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            LiquefactionRecipeDisplay::craftingStation,
            LiquefactionRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.LIQUEFACTION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(this.output));
    }
}
