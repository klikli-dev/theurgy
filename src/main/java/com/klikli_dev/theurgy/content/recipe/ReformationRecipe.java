// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.wrapper.ReformationArrayRecipeWrapper;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

import java.util.ArrayList;
import java.util.List;

public class ReformationRecipe implements Recipe<ReformationArrayRecipeWrapper> {

    public static final int DEFAULT_TIME = 100;

    public static final Codec<ReformationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC.listOf().fieldOf("sources").forGetter(r -> r.sources),
                    Ingredient.CODEC.fieldOf("target").forGetter(r -> r.target),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.fieldOf("mercury_flux").forGetter(r -> r.mercuryFlux),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, ReformationRecipe::new)
    );
    protected final List<Ingredient> sources;
    protected final Ingredient target;
    protected final ItemStack result;
    protected final int mercuryFlux;
    protected final int time;

    public ReformationRecipe(List<Ingredient> sources, Ingredient target, ItemStack result, int mercuryFlux, int time) {
        this.sources = sources;
        this.target = target;
        this.result = result;
        this.mercuryFlux = mercuryFlux;
        this.time = time;
    }

    public List<Ingredient> getSources() {
        return this.sources;
    }

    public Ingredient getTarget() {
        return this.target;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getMercuryFlux() {
        return this.mercuryFlux;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean matches(ReformationArrayRecipeWrapper pContainer, Level pLevel) {

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
    public ItemStack assemble(ReformationArrayRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
        var result = this.result.copy();
        //TODO: the tag copy should be an option in the recipe json
        var targetItem = pContainer.getTargetPedestalInv().getStackInSlot(0);
        if (targetItem.hasTag())
            result.setTag(targetItem.getTag().copy());
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(this.sources);
        return nonnulllist;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.REFORMATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.REFORMATION.get();
    }

    public record IngedientWithCount(Ingredient ingredient, int count) {
        public static final Codec<IngedientWithCount> CODEC = Codec.pair(
                Codec.INT.optionalFieldOf("count", 1).codec(),
                Ingredient.CODEC
        ).xmap(s -> new IngedientWithCount(s.getSecond(), s.getFirst()), s -> Pair.of(s.count, s.ingredient));
    }

    public static class Serializer implements RecipeSerializer<ReformationRecipe> {

        @Override
        public Codec<ReformationRecipe> codec() {
            return CODEC;
        }

        @Override
        public ReformationRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            //noinspection deprecation
            return pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ReformationRecipe pRecipe) {
            //noinspection deprecation
            pBuffer.writeWithCodec(NbtOps.INSTANCE, CODEC, pRecipe);
        }
    }
}
