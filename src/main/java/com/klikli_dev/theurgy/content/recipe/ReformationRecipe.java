// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.display.ReformationRecipeDisplay;
import com.klikli_dev.theurgy.content.recipe.input.ReformationArrayRecipeInput;
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

import java.util.ArrayList;
import java.util.List;

public class ReformationRecipe implements Recipe<ReformationArrayRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<ReformationRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("sources").forGetter(r -> r.sources),
                    Ingredient.CODEC.fieldOf("target").forGetter(r -> r.target),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.fieldOf("mercuryFlux").forGetter(r -> r.mercuryFlux),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, ReformationRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ReformationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.sources,
            Ingredient.CONTENTS_STREAM_CODEC,
            r -> r.target,
            ItemStackTemplate.STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.mercuryFlux,
            ByteBufCodecs.INT,
            r -> r.time,
            ReformationRecipe::new
    );
    protected final List<SizedIngredient> sources;
    public static final RecipeSerializer<ReformationRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    protected final NonNullList<Ingredient> sourcesNonNullList;
    protected final Ingredient target;
    protected final ItemStackTemplate result;
    protected final int mercuryFlux;
    protected final int time;

    public ReformationRecipe(List<SizedIngredient> sources, Ingredient target, ItemStackTemplate result, int mercuryFlux, int time) {
        this.sources = sources;
        this.sourcesNonNullList = NonNullList.copyOf(this.sources.stream().map(SizedIngredient::ingredient).toList());
        this.target = target;
        this.result = result;
        this.mercuryFlux = mercuryFlux;
        this.time = time;
    }

    public List<SizedIngredient> getSources() {
        return this.sources;
    }

    public Ingredient getTarget() {
        return this.target;
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }

    public int getMercuryFlux() {
        return this.mercuryFlux;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean matches(ReformationArrayRecipeInput pContainer, @NotNull Level pLevel) {

        //if we do not have enough flux, exit early
        if (pContainer.getMercuryFluxStorage().getEnergyStored() < this.mercuryFlux)
            return false;

        //if the target does not match we can exit early.
        if (!this.target.test(pContainer.getTargetPedestalInv().getStackInSlot(0)))
            return false;

        //For the sources the tricky part is that the amount of source pedestals does not need to match the amount of sources in the recipe.
        //Specifically, one recipe source with a count > 1 can be satisfied by the combination of multiple source pedestals.
        //So we have to check for each source if it can be satisfied by the pedestals, while ensuring that pedestals contents are not double counted.

        var remainingSources = new ArrayList<>(this.sources);
        var pedestalsToCheck = pContainer.getSourcePedestalInvs().stream().map(p -> p.getStackInSlot(0).copy()).toList();

        //go through all sources to check if they are matched
        for (var source : remainingSources) {
            var found = false;
            //it is a n (pedestals) to n (required sources) problem, so we need to check all pedestals for each source
            for (var sourceInputStack : pedestalsToCheck) {
                if (source.test(sourceInputStack)) {
                    //we also need to prevent double-checking a pedestal
                    //so we make this (copied!) stack empty
                    sourceInputStack.setCount(0);
                    found = true;
                    break;
                }
            }

            if (!found)
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(ReformationArrayRecipeInput pCraftingContainer) {
        var result = this.result.create();
        //TODO: the tag copy should be an option in the recipe json
        var targetItem = pCraftingContainer.getTargetPedestalInv().getStackInSlot(0);

        if (!targetItem.getComponents().isEmpty())
            result.applyComponents(targetItem.getComponents());

        return result;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }


    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.sourcesNonNullList;
    }

    @Override
    public @NotNull RecipeSerializer<ReformationRecipe> getSerializer() {
        return RecipeSerializerRegistry.REFORMATION.get();
    }

    @Override
    public @NotNull RecipeType<ReformationRecipe> getType() {
        return RecipeTypeRegistry.REFORMATION.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ReformationRecipeDisplay(
                this.sources,
                this.target,
                this.result,
                this.mercuryFlux,
                this.time,
                new SlotDisplay.ItemSlotDisplay(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get().asItem())
        ));
    }

}
