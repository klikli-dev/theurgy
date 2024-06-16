// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DigestionRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {
    public static final int DEFAULT_TIME = 200;

    public static final MapCodec<DigestionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedFluidIngredient.NESTED_CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.sizedIngredients),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DigestionRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DigestionRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.fluid,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.sizedIngredients,
            ItemStack.OPTIONAL_STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            DigestionRecipe::new
    );

    protected final SizedFluidIngredient fluid;

    protected final List<SizedIngredient> sizedIngredients;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int time;

    public DigestionRecipe(SizedFluidIngredient fluid, List<SizedIngredient> sizedIngredients, ItemStack result, int time) {
        this.fluid = fluid;
        this.sizedIngredients = sizedIngredients;
        this.ingredients = sizedIngredients.stream().map(SizedIngredient::ingredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        this.result = result;
        this.time = time;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.DIGESTION.get();
    }

    @Override
    public boolean matches(ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        var fluidMatches = this.fluid.test(fluid);
        if (!fluidMatches)
            return false;

        IntList visited = new IntArrayList();
        //first check for each ingredient if we have it in the container
        for (var ingredient : this.sizedIngredients) {
            var found = false;
            for (int i = 0; i < pContainer.size(); i++) {
                //skip already visited slots to not "double dip"
                if (visited.contains(i))
                    continue;

                var stack = pContainer.getItem(i);
                if (ingredient.ingredient().test(stack) && stack.getCount() >= ingredient.count()) {
                    found = true;
                    visited.add(i);
                    break;
                }
            }
            if (!found)
                return false;
        }

        //Now make sure that we have no additional ingredients
        for (int i = 0; i < pContainer.size(); i++) {
            if (visited.contains(i))
                continue;

            var stack = pContainer.getItem(i);
            if (!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ItemHandlerWithFluidRecipeInput pInv, HolderLookup.@NotNull Provider pRegistries) {
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
        return this.ingredients;
    }

    public List<SizedIngredient> getSizedIngredients() {
        return this.sizedIngredients;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.DIGESTION_VAT.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DIGESTION.get();
    }

    public SizedFluidIngredient getFluid() {
        return this.fluid;
    }

    public int getFluidAmount() {
        return this.fluid.amount();
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<DigestionRecipe> {
        @Override
        public @NotNull MapCodec<DigestionRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DigestionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
