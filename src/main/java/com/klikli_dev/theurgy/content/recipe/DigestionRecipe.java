// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DigestionRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {
    public static final int DEFAULT_TIME = 200;

    public static final MapCodec<DigestionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedFluidIngredient.CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.sizedIngredients),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DigestionRecipe::new)
    );
    public static final RecipeSerializer<DigestionRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, DigestionRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.fluid,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.sizedIngredients,
            ItemStackTemplate.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            DigestionRecipe::new
    );
    protected final SizedFluidIngredient fluid;

    protected final List<SizedIngredient> sizedIngredients;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStackTemplate result;
    protected final int time;

    public DigestionRecipe(SizedFluidIngredient fluid, List<SizedIngredient> sizedIngredients, ItemStackTemplate result, int time) {
        this.fluid = fluid;
        this.sizedIngredients = sizedIngredients;
        this.ingredients = sizedIngredients.stream().map(SizedIngredient::ingredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        this.result = result;
        this.time = time;
    }


    @Override
    public @NotNull RecipeType<DigestionRecipe> getType() {
        return RecipeTypeRegistry.DIGESTION.get();
    }

    @Override
    public boolean matches(ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = FluidStorageHelper.getFluidInTank(pContainer.getTank(), 0);
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
    public ItemStack assemble(ItemHandlerWithFluidRecipeInput pInv) {
        return this.result.create();
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
        return PlacementInfo.NOT_PLACEABLE;
    }
//    @Override
//    public RecipeBookCategory recipeBookCategory() {
//        return RecipeBookCategories.CRAFTING_MISC;
//    }

//    @Override
//    public PlacementInfo placementInfo() {
//        return PlacementInfo.create(this.ingredients);
//    }

    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
        return this.result.create();
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<SizedIngredient> getSizedIngredients() {
        return this.sizedIngredients;
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.DIGESTION_VAT.get());
    }

    @Override
    public @NotNull RecipeSerializer<DigestionRecipe> getSerializer() {
        return RecipeSerializerRegistry.DIGESTION.get();
    }

    public SizedFluidIngredient getFluid() {
        return this.fluid;
    }

    public int getFluidAmount() {
        return this.fluid.amount();
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public java.util.List<net.minecraft.world.item.crafting.display.RecipeDisplay> display() {
        return java.util.List.of(new com.klikli_dev.theurgy.content.recipe.display.DigestionRecipeDisplay(
                this.fluid,
                this.sizedIngredients,
                this.result,
                this.time,
                new net.minecraft.world.item.crafting.display.SlotDisplay.ItemSlotDisplay(com.klikli_dev.theurgy.registry.BlockRegistry.DIGESTION_VAT.get().asItem())
        ));
    }

}
