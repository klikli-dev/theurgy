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
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FermentationRecipe implements Recipe<ItemHandlerWithFluidRecipeInput> {
    public static final int DEFAULT_TIME = 200;

    public static final MapCodec<FermentationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedFluidIngredient.CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, FermentationRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FermentationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            r -> r.fluid,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.ingredients,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            FermentationRecipe::new
    );

    public static final RecipeSerializer<FermentationRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);


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
    public @NotNull RecipeType<FermentationRecipe> getType() {
        return RecipeTypeRegistry.FERMENTATION.get();
    }

    @Override
    public boolean matches(ItemHandlerWithFluidRecipeInput pContainer, @NotNull Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        var fluidMatches = this.fluid.test(fluid);
        if (!fluidMatches)
            return false;

        List<ItemStack> inputs = new ArrayList<>();
        int containerItemsCount = 0;

        for (int j = 0; j < pContainer.size(); ++j) {
            var itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                containerItemsCount++;
                inputs.add(itemstack);
            }
        }

        if (containerItemsCount != this.ingredients.size())
            return false;

        return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(ItemHandlerWithFluidRecipeInput pInv) {
        return this.result.copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
        return this.result;
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.FERMENTATION_VAT.get());
    }

    @Override
    public @NotNull RecipeSerializer<FermentationRecipe> getSerializer() {
        return (RecipeSerializer<FermentationRecipe>) RecipeSerializerRegistry.FERMENTATION.get();
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

    @Override
    public java.util.List<net.minecraft.world.item.crafting.display.RecipeDisplay> display() {
        return java.util.List.of(new com.klikli_dev.theurgy.content.recipe.display.FermentationRecipeDisplay(
                this.fluid,
                this.ingredients,
                this.result,
                this.time,
                new net.minecraft.world.item.crafting.display.SlotDisplay.ItemSlotDisplay(com.klikli_dev.theurgy.registry.BlockRegistry.FERMENTATION_VAT.get().asItem())
        ));
    }

}
