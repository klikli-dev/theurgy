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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;


public class LiquefactionRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<LiquefactionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    SizedFluidIngredient.CODEC.fieldOf("solvent").forGetter((r) -> r.solvent),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, LiquefactionRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LiquefactionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.ingredient,
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.solvent,
            ItemStackTemplate.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            LiquefactionRecipe::new
    );
    protected final Ingredient ingredient;
    public static final RecipeSerializer<LiquefactionRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    protected final SizedFluidIngredient solvent;
    protected final ItemStackTemplate result;
    protected final int time;

    public LiquefactionRecipe(Ingredient pIngredient, SizedFluidIngredient pSolvent, ItemStackTemplate pResult, int time) {
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
        var fluid = FluidStorageHelper.getFluidInTank(pContainer.getTank(), 0);
        return this.ingredient.test(pContainer.getItem(0)) && this.solvent.test(fluid);
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result.create();
    }

//    @Override
//    public PlacementInfo placementInfo() {
//        return PlacementInfo.create(this.ingredient);
//    }

    @Override
    public ItemStack assemble(ItemHandlerWithFluidRecipeInput pCraftingContainer) {
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
        return RecipeSerializerRegistry.LIQUEFACTION.get();
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

    @Override
    public java.util.List<net.minecraft.world.item.crafting.display.RecipeDisplay> display() {
        return java.util.List.of(new com.klikli_dev.theurgy.content.recipe.display.LiquefactionRecipeDisplay(
                this.ingredient,
                this.solvent,
                this.result,
                this.time,
                new net.minecraft.world.item.crafting.display.SlotDisplay.ItemSlotDisplay(com.klikli_dev.theurgy.registry.BlockRegistry.LIQUEFACTION_CAULDRON.get().asItem())
        ));
    }

}