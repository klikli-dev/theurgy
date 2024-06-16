// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


public class IncubationRecipe implements Recipe<IncubatorRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<IncubationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("mercury").forGetter((r) -> r.mercury),
            Ingredient.CODEC.fieldOf("salt").forGetter((r) -> r.salt),
            Ingredient.CODEC.fieldOf("sulfur").forGetter((r) -> r.sulfur),
            RecipeResult.CODEC.fieldOf("result").forGetter(r -> r.result),
            Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
    ).apply(instance, IncubationRecipe::new));

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

    protected final Ingredient mercury;
    protected final Ingredient salt;
    protected final Ingredient sulfur;

    protected final RecipeResult result;
    protected final int time;

    public IncubationRecipe(Ingredient mercury, Ingredient salt, Ingredient sulfur, RecipeResult pResult, int time) {
        this.mercury = mercury;
        this.salt = salt;
        this.sulfur = sulfur;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.INCUBATION.get();
    }

    @Override
    public boolean matches(@NotNull IncubatorRecipeInput pContainer, @NotNull Level pLevel) {
        return this.mercury.test(pContainer.getMercuryVesselInv().getStackInSlot(0)) &&
                this.salt.test(pContainer.getSaltVesselInv().getStackInSlot(0)) &&
                this.sulfur.test(pContainer.getSulfurVesselInv().getStackInSlot(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull IncubatorRecipeInput pInv, @NotNull HolderLookup.Provider pRegistries) {
        return this.result.getStack().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider pRegistries) {
        return this.result.getStack();
    }

    public RecipeResult getResult() {
        return this.result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.mercury);
        nonnulllist.add(this.salt);
        nonnulllist.add(this.sulfur);
        return nonnulllist;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.INCUBATOR.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.INCUBATION.get();
    }

    public int getTime() {
        return this.time;
    }

    public Ingredient getMercury() {
        return this.mercury;
    }

    public Ingredient getSalt() {
        return this.salt;
    }

    public Ingredient getSulfur() {
        return this.sulfur;
    }

    public static class Serializer implements RecipeSerializer<IncubationRecipe> {

        @Override
        public @NotNull MapCodec<IncubationRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, IncubationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}