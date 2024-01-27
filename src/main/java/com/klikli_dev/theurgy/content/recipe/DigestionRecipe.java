// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.datagen.recipe.IngredientWithCount;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
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

import java.util.List;

public class DigestionRecipe implements Recipe<RecipeWrapperWithFluid> {
    public static final int DEFAULT_TIME = 200;

    public static final Codec<DigestionRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    Codec.INT.fieldOf("fluidAmount").forGetter((r) -> r.fluidAmount),
                    IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredientsWithCount),
                    ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DigestionRecipe::new)
    );
    protected final FluidIngredient fluid;
    protected final int fluidAmount;

    protected final List<IngredientWithCount> ingredientsWithCount;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int time;

    public DigestionRecipe(FluidIngredient fluid, int fluidAmount, List<IngredientWithCount> ingredientsWithCount, ItemStack result, int time) {
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.ingredientsWithCount = ingredientsWithCount;
        this.ingredients = ingredientsWithCount.stream().map(IngredientWithCount::ingredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        this.result = result;
        this.time = time;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.DIGESTION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        var fluidMatches = this.fluid.test(fluid) && fluid.getAmount() >= this.fluidAmount;
        if (!fluidMatches)
            return false;

        IntList visited = new IntArrayList();
        //first check for each ingredient if we have it in the container
        for(var ingredient : this.ingredientsWithCount) {
            var found = false;
            for(int i = 0; i < pContainer.getContainerSize(); i++) {
                //skip already visited slots to not "double dip"
                if(visited.contains(i))
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
        for(int i = 0; i < pContainer.getContainerSize(); i++) {
            if(visited.contains(i))
                continue;

            var stack = pContainer.getItem(i);
            if(!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pInv, RegistryAccess registryAccess) {
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
        return this.ingredients;
    }

    public List<IngredientWithCount> getIngredientsWithCount() {
        return this.ingredientsWithCount;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.DIGESTION_VAT.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DIGESTION.get();
    }

    public FluidIngredient getFluid() {
        return this.fluid;
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<DigestionRecipe> {

        @Override
        public Codec<DigestionRecipe> codec() {
            return CODEC;
        }

        @Override
        public DigestionRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            //noinspection deprecation
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DigestionRecipe pRecipe) {
            //noinspection deprecation
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
        }
    }
}
