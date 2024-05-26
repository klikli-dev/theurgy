// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

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
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;


public class DistillationRecipe implements Recipe<RecipeWrapper> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<DistillationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    Codec.INT.fieldOf("ingredientCount").forGetter((r) -> r.ingredientCount),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DistillationRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DistillationRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.ingredient,
            ByteBufCodecs.INT,
            r -> r.ingredientCount,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            DistillationRecipe::new
    );


    protected final Ingredient ingredient;
    protected final int ingredientCount;
    protected final ItemStack result;
    protected final int time;

    public DistillationRecipe(Ingredient pIngredient, int ingredientCount, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.ingredientCount = ingredientCount;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.DISTILLATION.get();
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        var stack = pContainer.getItem(0);
        return this.ingredient.test(stack) && stack.getCount() >= this.ingredientCount;
    }

    @Override
    public ItemStack assemble(RecipeWrapper pInv, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.DISTILLER.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DISTILLATION.get();
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<DistillationRecipe> {

        @Override
        public MapCodec<DistillationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DistillationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}