// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.function.Supplier;

public class LiquefactionCraftingBehaviour extends CraftingBehaviour<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe, LiquefactionCachedCheck> {

    protected Supplier<ResourceHandler<FluidResource>> solventTankSupplier;

    public LiquefactionCraftingBehaviour(BlockEntity blockEntity, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier, Supplier<ResourceHandler<FluidResource>> solventTankSupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerWithFluidRecipeInput(inputInventorySupplier.get(), solventTankSupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new LiquefactionCachedCheck(RecipeTypeRegistry.LIQUEFACTION.get()));

        this.solventTankSupplier = solventTankSupplier;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(RecipeHolder<LiquefactionRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<LiquefactionRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return LiquefactionRecipe.DEFAULT_TIME;
    }

    @Override
    protected boolean craft(RecipeHolder<LiquefactionRecipe> pRecipe) {
        if (!super.craft(pRecipe)) //check validity and consume item ingredients
            return false;

        //then drain the solvent
        FluidStorageHelper.drain(this.solventTankSupplier.get(), pRecipe.value().getSolventAmount(), null);

        return true;
    }
}
