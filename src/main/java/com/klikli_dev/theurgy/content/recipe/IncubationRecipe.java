// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.display.IncubationRecipeDisplay;
import com.klikli_dev.theurgy.content.recipe.input.IncubatorRecipeInput;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResult;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public record IncubationRecipe(Ingredient mercury, Ingredient salt, Ingredient sulfur, RecipeResult result,
                               int time) implements Recipe<IncubatorRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<IncubationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("mercury").forGetter((r) -> r.mercury),
            Ingredient.CODEC.fieldOf("salt").forGetter((r) -> r.salt),
            Ingredient.CODEC.fieldOf("sulfur").forGetter((r) -> r.sulfur),
            RecipeResult.CODEC.fieldOf("result").forGetter(r -> r.result),
            Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
    ).apply(instance, IncubationRecipe::new));
    public static final RecipeSerializer<IncubationRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, IncubationRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.mercury,
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.salt,
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.sulfur,
            RecipeResult.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            IncubationRecipe::new
    );

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<IncubationRecipe> getType() {
        return RecipeTypeRegistry.INCUBATION.get();
    }

    @Override
    public boolean matches(@NotNull IncubatorRecipeInput pContainer, @NotNull Level pLevel) {
        return this.mercury.test(ItemUtil.getStack(pContainer.getMercuryVesselInv(), 0)) &&
                this.salt.test(ItemUtil.getStack(pContainer.getSaltVesselInv(), 0)) &&
                this.sulfur.test(ItemUtil.getStack(pContainer.getSulfurVesselInv(), 0));
    }

    @Override
    public ItemStack assemble(IncubatorRecipeInput pInv) {
        return this.result.getStack().copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.getIngredients());
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result.getStack();
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.mercury);
        nonnulllist.add(this.salt);
        nonnulllist.add(this.sulfur);
        return nonnulllist;
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.INCUBATOR.get());
    }

    @Override
    public @NotNull RecipeSerializer<IncubationRecipe> getSerializer() {
        return RecipeSerializerRegistry.INCUBATION.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new IncubationRecipeDisplay(
                this.mercury,
                this.salt,
                this.sulfur,
                this.result,
                this.time,
                new SlotDisplay.ItemSlotDisplay(BlockRegistry.INCUBATOR.get().asItem())
        ));
    }

}
