// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.function.Supplier;

public class CalcinationCraftingBehaviour extends CraftingBehaviour<RecipeWrapper, CalcinationRecipe, RecipeManager.CachedCheck<RecipeWrapper, CalcinationRecipe>> {
    public CalcinationCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier) {
        super(blockEntity,
                Lazy.of(() -> new RecipeWrapper(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get()));
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item

        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        RecipeWrapper tempRecipeWrapper = new RecipeWrapper(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(RecipeHolder<CalcinationRecipe> recipe) {
        return recipe.value().getIngredientCount();
    }

    @Override
    protected int getCraftingTime(RecipeHolder<CalcinationRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return CalcinationRecipe.DEFAULT_TIME;
    }
}
