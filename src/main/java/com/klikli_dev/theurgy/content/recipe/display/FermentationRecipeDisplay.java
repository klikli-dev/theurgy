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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public record FermentationRecipeDisplay(
        SizedFluidIngredient fluid,
        List<Ingredient> ingredients,
        ItemStack output,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<FermentationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.CODEC.fieldOf("fluid").forGetter(FermentationRecipeDisplay::fluid),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(FermentationRecipeDisplay::ingredients),
            ItemStack.CODEC.fieldOf("output").forGetter(FermentationRecipeDisplay::output),
            Codec.INT.fieldOf("time").forGetter(FermentationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(FermentationRecipeDisplay::craftingStation)
    ).apply(instance, FermentationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FermentationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            FermentationRecipeDisplay::fluid,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            FermentationRecipeDisplay::ingredients,
            ItemStack.STREAM_CODEC,
            FermentationRecipeDisplay::output,
            ByteBufCodecs.INT,
            FermentationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            FermentationRecipeDisplay::craftingStation,
            FermentationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.FERMENTATION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(this.output);
    }
}
