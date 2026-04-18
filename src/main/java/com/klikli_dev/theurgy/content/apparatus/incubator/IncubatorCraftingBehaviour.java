// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.IncubatorRecipeInput;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Supplier;

public class IncubatorCraftingBehaviour extends CraftingBehaviour<IncubatorRecipeInput, IncubationRecipe, LevelAwareCachedCheck<IncubatorRecipeInput, IncubationRecipe>> {
    public IncubatorCraftingBehaviour(BlockEntity blockEntity, Supplier<IncubatorRecipeInput> recipeWrapperSupplier, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier) {
        super(blockEntity,
                recipeWrapperSupplier,
                inputInventorySupplier,
                outputInventorySupplier,
                new LevelAwareCachedCheck<>(RecipeTypeRegistry.INCUBATION.get()));
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        return true; //not used because the vessels handle their input on their own.
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return true; //not used because the vessels handle their input on their own.
    }

    @Override
    protected boolean craft(RecipeHolder<IncubationRecipe> pRecipe) {
        var ItemHandlerRecipeInput = this.recipeInputSupplier.get();
        var assembledStack = pRecipe.value().assemble(ItemHandlerRecipeInput);

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        try (var tx = Transaction.openRoot()) {
            this.outputInventorySupplier.get().insert(ItemResource.of(assembledStack), assembledStack.getCount(), tx);
            tx.commit();
        }

        try (var tx = Transaction.openRoot()) {
            ItemHandlerRecipeInput.getMercuryVesselInv().extract(ItemResource.of(ItemUtil.getStack(ItemHandlerRecipeInput.getMercuryVesselInv(), 0)), 1, tx);
            ItemHandlerRecipeInput.getSaltVesselInv().extract(ItemResource.of(ItemUtil.getStack(ItemHandlerRecipeInput.getSaltVesselInv(), 0)), 1, tx);
            ItemHandlerRecipeInput.getSulfurVesselInv().extract(ItemResource.of(ItemUtil.getStack(ItemHandlerRecipeInput.getSulfurVesselInv(), 0)), 1, tx);
            tx.commit();
        }

        return true;
    }

    @Override
    protected int getIngredientCount(RecipeHolder<IncubationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<IncubationRecipe> recipe) {
        return recipe.value().time();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return IncubationRecipe.DEFAULT_TIME;
    }

    @Override
    protected int getTotalTime() {
        return this.recipeInputSupplier.get() != null ? super.getTotalTime() : this.getDefaultCraftingTime();
    }
}
