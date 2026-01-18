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
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;


public class CalcinationRecipe implements Recipe<ItemHandlerRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<CalcinationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, CalcinationRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CalcinationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            r -> r.ingredient,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            CalcinationRecipe::new
    );

    protected final SizedIngredient ingredient;
    protected final ItemStack result;
    protected final int time;

    public CalcinationRecipe(SizedIngredient pIngredient, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.result = pResult;
        this.time = time;
    }

    public SizedIngredient sizedIngredient(){
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredient.count();
    }


    @Override
    public @NotNull RecipeType<CalcinationRecipe> getType() {
        return RecipeTypeRegistry.CALCINATION.get();
    }

    @Override
    public boolean matches(ItemHandlerRecipeInput input, @NotNull Level pLevel) {
        var stack = input.getItem(0);
        return this.ingredient.test(stack);
    }

    public ItemStack assemble(ItemHandlerRecipeInput input, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient.ingredient());
        return nonnulllist;
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.CALCINATION_OVEN.get());
    }

    @Override
    public @NotNull RecipeSerializer<CalcinationRecipe> getSerializer() {
        return (RecipeSerializer<CalcinationRecipe>) RecipeSerializerRegistry.CALCINATION.get();
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public java.util.List<net.minecraft.world.item.crafting.display.RecipeDisplay> display() {
        return java.util.List.of(new com.klikli_dev.theurgy.content.recipe.display.CalcinationRecipeDisplay(
                this.ingredient,
                this.result,
                this.time,
                new net.minecraft.world.item.crafting.display.SlotDisplay.ItemSlotDisplay(com.klikli_dev.theurgy.registry.BlockRegistry.CALCINATION_OVEN.get().asItem())
        ));
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