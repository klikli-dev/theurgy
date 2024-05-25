// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.IncubatorRecipeWrapper;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.function.Supplier;

public class IncubatorCraftingBehaviour extends CraftingBehaviour<IncubatorRecipeWrapper, IncubationRecipe, RecipeManager.CachedCheck<IncubatorRecipeWrapper, IncubationRecipe>> {
    public IncubatorCraftingBehaviour(BlockEntity blockEntity, Supplier<IncubatorRecipeWrapper> recipeWrapperSupplier, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier) {
        super(blockEntity,
                recipeWrapperSupplier,
                inputInventorySupplier,
                outputInventorySupplier,
                RecipeManager.createCheck(RecipeTypeRegistry.INCUBATION.get()));
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        return true; //not used because the vessels handle their input on their own.
    }

    @Override
    protected boolean craft(RecipeHolder<IncubationRecipe> pRecipe) {
        var recipeWrapper = this.recipeWrapperSupplier.get();
        var assembledStack = pRecipe.value().assemble(recipeWrapper, this.blockEntity.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);

        recipeWrapper.getMercuryVesselInv().extractItem(0, 1, false);
        recipeWrapper.getSaltVesselInv().extractItem(0, 1, false);
        recipeWrapper.getSulfurVesselInv().extractItem(0, 1, false);

        return true;
    }

    @Override
    protected int getIngredientCount(RecipeHolder<IncubationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<IncubationRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return IncubationRecipe.DEFAULT_TIME;
    }

    @Override
    protected int getTotalTime() {
        return this.recipeWrapperSupplier.get() != null ? super.getTotalTime() : this.getDefaultCraftingTime();
    }
}
