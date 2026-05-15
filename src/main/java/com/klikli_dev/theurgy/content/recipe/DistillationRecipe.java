// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.display.DistillationRecipeDisplay;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DistillationRecipe implements Recipe<ItemHandlerRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<DistillationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DistillationRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DistillationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            r -> r.ingredient,
            ItemStackTemplate.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            DistillationRecipe::new
    );
    public static final RecipeSerializer<DistillationRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    protected final SizedIngredient ingredient;
    protected final ItemStackTemplate result;
    protected final int time;

    public DistillationRecipe(SizedIngredient pIngredient, ItemStackTemplate pResult, int time) {
        this.ingredient = pIngredient;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public @NotNull RecipeType<DistillationRecipe> getType() {
        return RecipeTypeRegistry.DISTILLATION.get();
    }

    @Override
    public boolean matches(@NotNull ItemHandlerRecipeInput pContainer, @NotNull Level pLevel) {
        var stack = pContainer.getItem(0);
        return this.ingredient.test(stack);
    }

    @Override
    public ItemStack assemble(ItemHandlerRecipeInput pCraftingContainer) {
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
    public boolean isSpecial() {
        return true;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredient.ingredient());
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result.create();
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient.ingredient());
        return nonnulllist;
    }

    public SizedIngredient getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredient.count();
    }

    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.DISTILLER.get());
    }

    @Override
    public @NotNull RecipeSerializer<DistillationRecipe> getSerializer() {
        return RecipeSerializerRegistry.DISTILLATION.get();
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new DistillationRecipeDisplay(
                this.ingredient,
                this.result,
                this.time,
                new SlotDisplay.ItemSlotDisplay(BlockRegistry.DISTILLER.get().asItem())
        ));
    }

}