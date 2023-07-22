/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.IncubatorRecipeWrapper;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

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
    protected boolean craft(@Nullable IncubationRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var recipeWrapper = this.recipeWrapperSupplier.get();
        var assembledStack = pRecipe.assemble(recipeWrapper, this.blockEntity.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);

        recipeWrapper.getMercuryVesselInv().extractItem(0, 1, false);
        recipeWrapper.getSaltVesselInv().extractItem(0, 1, false);
        recipeWrapper.getSulfurVesselInv().extractItem(0, 1, false);

        return true;
    }

    @Override
    protected int getIngredientCount(IncubationRecipe recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(IncubationRecipe recipe) {
        return recipe.getIncubationTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return IncubationRecipe.DEFAULT_INCUBATION_TIME;
    }

    @Override
    protected int getTotalTime() {
        return this.recipeWrapperSupplier.get() != null ? super.getTotalTime() : this.getDefaultCraftingTime();
    }
}
