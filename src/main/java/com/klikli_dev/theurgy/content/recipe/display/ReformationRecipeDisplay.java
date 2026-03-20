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
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public record ReformationRecipeDisplay(
        List<SizedIngredient> sources,
        Ingredient target,
        ItemStack output,
        int mercuryFlux,
        int time,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<ReformationRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.listOf().fieldOf("sources").forGetter(ReformationRecipeDisplay::sources),
            Ingredient.CODEC.fieldOf("target").forGetter(ReformationRecipeDisplay::target),
            ItemStack.CODEC.fieldOf("output").forGetter(ReformationRecipeDisplay::output),
            Codec.INT.fieldOf("mercuryFlux").forGetter(ReformationRecipeDisplay::mercuryFlux),
            Codec.INT.fieldOf("time").forGetter(ReformationRecipeDisplay::time),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(ReformationRecipeDisplay::craftingStation)
    ).apply(instance, ReformationRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ReformationRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ReformationRecipeDisplay::sources,
            Ingredient.CONTENTS_STREAM_CODEC,
            ReformationRecipeDisplay::target,
            ItemStack.STREAM_CODEC,
            ReformationRecipeDisplay::output,
            ByteBufCodecs.INT,
            ReformationRecipeDisplay::mercuryFlux,
            ByteBufCodecs.INT,
            ReformationRecipeDisplay::time,
            SlotDisplay.STREAM_CODEC,
            ReformationRecipeDisplay::craftingStation,
            ReformationRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return RecipeDisplayRegistry.REFORMATION.get();
    }

    @Override
    public SlotDisplay result() {
        return new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(this.output));
    }
}
