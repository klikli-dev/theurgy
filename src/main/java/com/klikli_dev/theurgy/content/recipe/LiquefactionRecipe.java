// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;


public class LiquefactionRecipe implements Recipe<RecipeWrapperWithFluid> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<LiquefactionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    FluidIngredient.CODEC.fieldOf("solvent").forGetter((r) -> r.solvent),
                    Codec.INT.fieldOf("solvent_amount").forGetter((r) -> r.solventAmount),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, LiquefactionRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.ingredient,
            FluidIngredient.STREAM_CODEC,
            r -> r.solvent,
            ByteBufCodecs.INT,
            r -> r.solventAmount,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            LiquefactionRecipe::new
    );

    protected final Ingredient ingredient;
    protected final FluidIngredient solvent;
    protected final int solventAmount;
    protected final ItemStack result;
    protected final int time;

    public LiquefactionRecipe(Ingredient pIngredient, FluidIngredient pSolvent, int solventAmount, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.solvent = pSolvent;
        this.solventAmount = solventAmount;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.LIQUEFACTION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        return this.ingredient.test(pContainer.getItem(0)) && this.solvent.test(fluid) && fluid.getAmount() >= this.solventAmount;
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pCraftingContainer, HolderLookup.Provider pRegistries) {
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

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.LIQUEFACTION.get();
    }

    public int getTime() {
        return this.time;
    }

    public FluidIngredient getSolvent() {
        return this.solvent;
    }

    public int getSolventAmount() {
        return this.solventAmount;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public static class Serializer implements RecipeSerializer<LiquefactionRecipe> {

        @Override
        public MapCodec<LiquefactionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}