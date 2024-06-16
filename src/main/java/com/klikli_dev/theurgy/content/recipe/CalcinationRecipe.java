// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
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


public class CalcinationRecipe implements Recipe<ItemHandlerRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<CalcinationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    Codec.INT.fieldOf("ingredientCount").forGetter((r) -> r.ingredientCount),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, CalcinationRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CalcinationRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.ingredient,
            ByteBufCodecs.INT,
            r -> r.ingredientCount,
            ItemStack.OPTIONAL_STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            CalcinationRecipe::new
    );

    protected final Ingredient ingredient;
    protected final int ingredientCount;
    protected final ItemStack result;
    protected final int time;

    public CalcinationRecipe(Ingredient pIngredient, int ingredientCount, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.ingredientCount = ingredientCount;
        this.result = pResult;
        this.time = time;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.CALCINATION.get();
    }

    @Override
    public boolean matches(ItemHandlerRecipeInput input, @NotNull Level pLevel) {
        var stack = input.getItem(0);
        return this.ingredient.test(stack) && stack.getCount() >= this.ingredientCount;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ItemHandlerRecipeInput input, HolderLookup.@NotNull Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
        return this.result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.CALCINATION_OVEN.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CALCINATION.get();
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<CalcinationRecipe> {
        @Override
        public @NotNull MapCodec<CalcinationRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CalcinationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}