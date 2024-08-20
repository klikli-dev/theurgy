// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.content.recipe.CatalysationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MercuryCatalystCraftingBehaviour extends CraftingBehaviour<ItemHandlerRecipeInput, CatalysationRecipe, RecipeManager.CachedCheck<ItemHandlerRecipeInput, CatalysationRecipe>> {

    private final Supplier<MercuryFluxStorage> mercuryFluxStorageSupplier;

    protected int mercuryFluxToConvert;
    protected int currentMercuryFluxPerTick;


    public MercuryCatalystCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, Supplier<MercuryFluxStorage> mercuryFluxStorageSupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerRecipeInput(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                RecipeManager.createCheck(RecipeTypeRegistry.CATALYSATION.get()));

        this.mercuryFluxStorageSupplier = mercuryFluxStorageSupplier;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        var tempInv = new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, stack));
        var tempRecipeWrapper = new ItemHandlerRecipeInput(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("mercuryFluxToConvert", this.mercuryFluxToConvert);
        pTag.putInt("currentMercuryFluxPerTick", this.currentMercuryFluxPerTick);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        if (pTag.contains("mercuryFluxToConvert"))
            this.mercuryFluxToConvert = pTag.getInt("mercuryFluxToConvert");

        if (pTag.contains("currentMercuryFluxPerTick"))
            this.currentMercuryFluxPerTick = pTag.getInt("currentMercuryFluxPerTick");
    }

    @Override
    public void applyImplicitComponents(BlockEntity.DataComponentInput pComponentInput) {
        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_TO_CONVERT) != null)
            this.mercuryFluxToConvert = pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_TO_CONVERT);

        if (pComponentInput.get(DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK) != null)
            this.currentMercuryFluxPerTick = pComponentInput.get(DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK);
    }

    @Override
    public void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        pComponents.set(DataComponentRegistry.MERCURY_FLUX_TO_CONVERT, this.mercuryFluxToConvert);
        pComponents.set(DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK, this.currentMercuryFluxPerTick);
    }

    @Override
    protected int getIngredientCount(RecipeHolder<CatalysationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<CatalysationRecipe> recipe) {
        return -1;
    }

    @Override
    protected int getDefaultCraftingTime() {
        return -1;
    }

    @Override
    public boolean canCraft(@Nullable RecipeHolder<CatalysationRecipe> pRecipe) {
        if (pRecipe == null) return false;

        var storage = this.mercuryFluxStorageSupplier.get();
        int fluxAccepted = storage.receiveEnergy(pRecipe.value().getTotalMercuryFlux(), true);

        return fluxAccepted > 0;
    }

    @Override
    public void tickServer(boolean canProcess, boolean hasInput) {
        //first see if we have leftover flux to convert
        if (this.mercuryFluxToConvert > 0) {
            if (canProcess) {
                var storage = this.mercuryFluxStorageSupplier.get();
                var maxFluxToConvert = Math.min(this.mercuryFluxToConvert, this.currentMercuryFluxPerTick);
                int fluxAccepted = storage.receiveEnergy(maxFluxToConvert, false);
                this.mercuryFluxToConvert -= fluxAccepted;
            }
        } else if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups

            //if we have no flux available, consume more mercury
            var recipe = this.recipeCachedCheck.getRecipeFor(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel()).orElse(null);


            this.couldCraftLastTick = this.canCraft(recipe);

            //if we are lit and have a recipe, update progress
            if (canProcess && this.couldCraftLastTick) {
                this.craft(recipe);
            }
        }
    }

    @Override
    protected boolean craft(@Nullable RecipeHolder<CatalysationRecipe> pRecipe) {
        this.mercuryFluxToConvert = pRecipe.value().getTotalMercuryFlux();
        this.currentMercuryFluxPerTick = pRecipe.value().getMercuryFluxPerTick();

        this.inputInventorySupplier.get().extractItem(0, this.getIngredientCount(pRecipe), false);

        return true;
    }
}
