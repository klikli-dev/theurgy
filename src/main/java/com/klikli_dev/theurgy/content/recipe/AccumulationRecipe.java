// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AccumulationRecipe implements Recipe<RecipeWrapperWithFluid> {
    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<AccumulationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("evaporant").forGetter((r) -> r.evaporant),
                    Codec.INT.fieldOf("evaporantAmount").forGetter((r) -> r.evaporantAmount),
                    Ingredient.CODEC.optionalFieldOf("solute").forGetter(r -> Optional.ofNullable(r.solute)),
                    FluidStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, (evaporant, evaporantAmount, solute, result, accumulation_time) -> new AccumulationRecipe(evaporant, evaporantAmount, solute.orElse(null), result, accumulation_time))
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AccumulationRecipe> STREAM_CODEC = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC,
            r -> r.evaporant,
            ByteBufCodecs.INT,
            r -> r.evaporantAmount,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            r -> Optional.ofNullable(r.solute),
            FluidStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            (evaporant, evaporantAmount, solute, result, accumulation_time) -> new AccumulationRecipe(evaporant, evaporantAmount, solute.orElse(null), result, accumulation_time)
    );

    /**
     * The fluid to evaporate to obtain the result.
     */
    protected final FluidIngredient evaporant;
    protected final int evaporantAmount;
    /**
     * The (optional) item to dissolve in the evaporant to obtain the result.
     */
    @Nullable
    protected final Ingredient solute;
    /**
     * The result of the recipe.
     */
    protected final FluidStack result;
    protected final int time;

    public AccumulationRecipe(FluidIngredient evaporant, int evaporantAmount, @Nullable Ingredient solute, FluidStack result, int time) {
        this.evaporant = evaporant;
        this.evaporantAmount = evaporantAmount;
        this.solute = solute;
        this.result = result;
        this.time = time;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.ACCUMULATION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        boolean evaporantMatches = this.evaporant.test(fluid) && fluid.getAmount() >= this.evaporantAmount;
        boolean soluteMatches =
                pContainer.getItem(0).isEmpty() && !this.hasSolute() || //if recipe requires no solute and container does not have one we're ok
                        this.hasSolute() && this.solute.test(pContainer.getItem(0)); // if recipe requires solute we check if the container has it

        //note: it is important that if the container HAS a solute but the recipe does not require one, we do not match -> otherwise water -> sal ammoniac recipes would always match, even if the faster water + sal ammoniac crystal -> sal ammoniac recipe is available

        return soluteMatches && evaporantMatches;
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pInv, HolderLookup.Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    public FluidStack assembleFluid(RecipeWrapperWithFluid pInv, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        if (this.solute != null)
            nonnulllist.add(this.solute);
        return nonnulllist;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.ACCUMULATION.get();
    }

    public int getTime() {
        return this.time;
    }

    public FluidIngredient getEvaporant() {
        return this.evaporant;
    }

    public int getEvaporantAmount() {
        return this.evaporantAmount;
    }

    @Nullable
    public Ingredient getSolute() {
        return this.solute;
    }

    public boolean hasSolute() {
        return this.solute != null;
    }

    public FluidStack getResult() {
        return this.result;
    }

    public static class Serializer implements RecipeSerializer<AccumulationRecipe> {

        @Override
        public MapCodec<AccumulationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AccumulationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
