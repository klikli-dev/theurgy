// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import java.util.function.Supplier;

public class DistillationCraftingBehaviour extends CraftingBehaviour<RecipeWrapper, DistillationRecipe, DistillationCachedCheck> {
    public DistillationCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier) {
        super(blockEntity,
                Lazy.of(() -> new RecipeWrapper(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new DistillationCachedCheck(RecipeTypeRegistry.DISTILLATION.get()));
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item


        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(DistillationRecipe recipe) {
        return recipe.getIngredientCount();
    }

    @Override
    protected int getCraftingTime(DistillationRecipe recipe) {
        return recipe.getDistillationTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return DistillationRecipe.DEFAULT_DISTILLATION_TIME;
    }
}
