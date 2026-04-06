// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ReformationArrayRecipeInput;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ReformationArrayCraftingBehaviour extends CraftingBehaviour<ReformationArrayRecipeInput, ReformationRecipe, LevelAwareCachedCheck<ReformationArrayRecipeInput, ReformationRecipe>> {

    protected final Supplier<MercuryFluxStorage> mercuryFluxStorageSupplier;

    public ReformationArrayCraftingBehaviour(BlockEntity blockEntity, Supplier<ReformationArrayRecipeInput> recipeWrapperSupplier, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier, Supplier<MercuryFluxStorage> mercuryFluxStorageSupplier) {
        super(blockEntity,
                recipeWrapperSupplier,
                inputInventorySupplier,
                outputInventorySupplier,
                new LevelAwareCachedCheck<>(RecipeTypeRegistry.REFORMATION.get()));

        this.mercuryFluxStorageSupplier = mercuryFluxStorageSupplier;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(this.recipeInputSupplier.get(), this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected boolean craft(RecipeHolder<ReformationRecipe> pRecipe) {
        var ItemHandlerRecipeInput = this.recipeInputSupplier.get();
        var assembledStack = pRecipe.value().assemble(ItemHandlerRecipeInput);

        //consume energy
        this.mercuryFluxStorageSupplier.get().extractEnergy(pRecipe.value().getMercuryFlux(), false);

        // Loop through required sources of recipe and through source inventories and extract
        Set<SettableItemStorage> usedInventories = new HashSet<>();
        for (var source : pRecipe.value().getSources()) {
            for (var sourceInventory : ItemHandlerRecipeInput.getSourcePedestalInvs()) {
                // Skip this source inventory if it has already been used
                if (usedInventories.contains(sourceInventory)) {
                    continue;
                }

                var sourceStack = sourceInventory.getStackInSlot(0);
                if (source.test(sourceStack)) {
                    // Add this source inventory to the set of used inventories
                    usedInventories.add(sourceInventory);

                    sourceInventory.extractItem(0, source.count(), false);
                    break;
                }
            }
        }

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        this.outputInventorySupplier.get().insertItemStacked(assembledStack, false);

        // Consume the target item
        ItemHandlerRecipeInput.getTargetPedestalInv().extractItem(0, 1, false);

        return true;
    }

    @Override
    protected int getIngredientCount(RecipeHolder<ReformationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<ReformationRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return ReformationRecipe.DEFAULT_TIME;
    }

    @Override
    protected int getTotalTime() {
        return this.recipeInputSupplier.get() != null ? super.getTotalTime() : this.getDefaultCraftingTime();
    }
}
