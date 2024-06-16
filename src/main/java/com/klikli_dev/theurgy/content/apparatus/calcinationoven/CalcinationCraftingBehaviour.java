// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Objects;
import java.util.function.Supplier;

public class CalcinationCraftingBehaviour extends CraftingBehaviour<ItemHandlerRecipeInput, CalcinationRecipe, RecipeManager.CachedCheck<ItemHandlerRecipeInput, CalcinationRecipe>> {
    public CalcinationCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerRecipeInput(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get()));
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemStack.isSameItemSameComponents(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item

        var tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        var tempInput = new ItemHandlerRecipeInput(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempInput, Objects.requireNonNull(this.blockEntity.getLevel())).isPresent();
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
