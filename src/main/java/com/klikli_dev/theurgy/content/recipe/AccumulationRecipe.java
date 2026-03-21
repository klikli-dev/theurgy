// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;


import com.klikli_dev.theurgy.content.recipe.display.AccumulationRecipeDisplay;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.registry.BlockRegistry;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @param evaporant The (optional) fluid to evaporate to obtain the result.
 * @param solute    The (optional) item to dissolve in the evaporant to obtain the result.
 * @param result    The result of the recipe.
 */
public record AccumulationRecipe(@Nullable SizedFluidIngredient evaporant, @Nullable Ingredient solute,
                                 FluidStackTemplate result,
                                 int time) implements Recipe<ItemHandlerWithFluidRecipeInput> {
    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<AccumulationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedFluidIngredient.CODEC.optionalFieldOf("evaporant").forGetter((r) -> Optional.ofNullable(r.evaporant)),
                    Ingredient.CODEC.optionalFieldOf("solute").forGetter(r -> Optional.ofNullable(r.solute)),
                    FluidStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, (evaporant, solute, result, accumulation_time) -> new AccumulationRecipe(evaporant.orElse(null), solute.orElse(null), result, accumulation_time))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AccumulationRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(SizedFluidIngredient.STREAM_CODEC),
            r -> Optional.ofNullable(r.evaporant),
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            r -> Optional.ofNullable(r.solute),
            FluidStackTemplate.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            (evaporant, solute, result, accumulation_time) -> new AccumulationRecipe(evaporant.orElse(null), solute.orElse(null), result, accumulation_time)
    );

    public static final RecipeSerializer<AccumulationRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    @Override
    public @NotNull RecipeType<AccumulationRecipe> getType() {
        return RecipeTypeRegistry.ACCUMULATION.get();
    }

    @Override
    public boolean matches(@NotNull ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = FluidStorageHelper.getFluidInTank(pContainer.getTank(), 0);
        boolean evaporantMatches = !this.hasEvaporant() || this.evaporant.test(fluid);
        //noinspection DataFlowIssue: we are checking this.hasSolute so solute is not null!
        boolean soluteMatches =
                pContainer.getItem(0).isEmpty() && !this.hasSolute() || //if recipe requires no solute and container does not have one we're ok
                        this.hasSolute() && this.solute.test(pContainer.getItem(0)); // if recipe requires solute we check if the container has it

        //note: it is important that if the container HAS a solute but the recipe does not require one, we do not match -> otherwise water -> sal ammoniac recipes would always match, even if the faster water + sal ammoniac crystal -> sal ammoniac recipe is available

        return soluteMatches && evaporantMatches;
    }

    @Override
    public ItemStack assemble(ItemHandlerWithFluidRecipeInput pInv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

//    @Override
//    public boolean canCraftInDimensions(int pWidth, int pHeight) {
//        return true;
//    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public @NotNull FluidStack assembleFluid(@NotNull ItemHandlerWithFluidRecipeInput pInv, @NotNull HolderLookup.Provider pRegistries) {
        return this.result.create();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        if (this.solute != null)
            nonnulllist.add(this.solute);
        return nonnulllist;
    }

    @Override
    public @NotNull RecipeSerializer<AccumulationRecipe> getSerializer() {
        return RecipeSerializerRegistry.ACCUMULATION.get();
    }

    public int getEvaporantAmount() {
        return this.hasEvaporant() ? this.evaporant.amount() : 0;
    }

    public boolean hasSolute() {
        return this.solute != null;
    }

    public boolean hasEvaporant() {
        return this.evaporant != null;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new AccumulationRecipeDisplay(
                Optional.ofNullable(this.evaporant),
                Optional.ofNullable(this.solute).map(Ingredient::display),
                this.result.create(),
                this.time,
                new SlotDisplay.ItemSlotDisplay(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get().asItem())
        ));
    }

}
