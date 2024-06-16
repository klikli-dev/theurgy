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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FermentationRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {
    public static final int DEFAULT_TIME = 200;

    public static final MapCodec<FermentationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedFluidIngredient.NESTED_CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, FermentationRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FermentationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.fluid,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.ingredients,
            ItemStack.OPTIONAL_STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            FermentationRecipe::new
    );


    protected final SizedFluidIngredient fluid;

    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int time;
    private final boolean hasOnlySimpleIngredients;

    public FermentationRecipe(SizedFluidIngredient fluid, List<Ingredient> ingredients, ItemStack result, int time) {
        this.fluid = fluid;
        this.ingredients = ingredients.stream().collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        this.hasOnlySimpleIngredients = ingredients.stream().allMatch(Ingredient::isSimple);
        this.result = result;
        this.time = time;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.FERMENTATION.get();
    }

    @Override
    public boolean matches(ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        var fluidMatches = this.fluid.test(fluid);
        if (!fluidMatches)
            return false;

        //logic from shapeless recipe to match ingredients without double-dipping
        var stackedcontents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int containerItemsCount = 0;

        for (int j = 0; j < pContainer.size(); ++j) {
            var itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                containerItemsCount++;
                if (this.hasOnlySimpleIngredients)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        if (containerItemsCount != this.ingredients.size())
            return false;

        return this.hasOnlySimpleIngredients ?
                stackedcontents.canCraft(this, null) :
                net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ItemHandlerWithFluidRecipeInput pInv, @NotNull HolderLookup.Provider pRegistries) {
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

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.FERMENTATION_VAT.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.FERMENTATION.get();
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

    public static class Serializer implements RecipeSerializer<FermentationRecipe> {

        @Override
        public @NotNull MapCodec<FermentationRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, FermentationRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
