// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.registry.ItemRegistry;
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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;


public class LiquefactionRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<LiquefactionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    SizedFluidIngredient.CODEC.fieldOf("solvent").forGetter((r) -> r.solvent),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, LiquefactionRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.ingredient,
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.solvent,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            LiquefactionRecipe::new
    );

    protected final Ingredient ingredient;
    protected final SizedFluidIngredient solvent;
    protected final ItemStack result;
    protected final int time;

    public LiquefactionRecipe(Ingredient pIngredient, SizedFluidIngredient pSolvent, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.solvent = pSolvent;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public @NotNull RecipeType<LiquefactionRecipe> getType() {
        return RecipeTypeRegistry.LIQUEFACTION.get();
    }

    @Override
    public boolean matches(@NotNull ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        return this.ingredient.test(pContainer.getItem(0)) && this.solvent.test(fluid);
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

//    @Override
//    public PlacementInfo placementInfo() {
//        return PlacementInfo.create(this.ingredient);
//    }

    public ItemStack assemble(ItemHandlerWithFluidRecipeInput pCraftingContainer, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public @NotNull RecipeSerializer<LiquefactionRecipe> getSerializer() {
        return (RecipeSerializer<LiquefactionRecipe>) RecipeSerializerRegistry.LIQUEFACTION.get();
    }

    public int getTime() {
        return this.time;
    }

    public SizedFluidIngredient getSolvent() {
        return this.solvent;
    }

    public int getSolventAmount() {
        return this.solvent.amount();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public static class Serializer implements RecipeSerializer<LiquefactionRecipe> {

        @Override
        public @NotNull MapCodec<LiquefactionRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}