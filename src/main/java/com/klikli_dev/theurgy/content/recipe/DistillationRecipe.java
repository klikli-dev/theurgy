// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;


public class DistillationRecipe implements Recipe<RecipeWrapper> {

    public static final int DEFAULT_TIME = 100;

    public static final Codec<DistillationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    Codec.INT.fieldOf("ingredientCount").forGetter((r) -> r.ingredientCount),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DistillationRecipe::new
            )
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
    public ItemStack assemble(RecipeWrapper pInv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
        public Codec<DistillationRecipe> codec() {
            return CODEC;
        }

        @Override
        public DistillationRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            //noinspection deprecation
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DistillationRecipe pRecipe) {
            //noinspection deprecation
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
        }
    }
}