// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import java.util.function.Supplier;

public class LiquefactionCraftingBehaviour extends CraftingBehaviour<RecipeWrapperWithFluid, LiquefactionRecipe, LiquefactionCachedCheck> {

    protected Supplier<IFluidHandler> solventTankSupplier;

    public LiquefactionCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, Supplier<IFluidHandler> solventTankSupplier) {
        super(blockEntity,
                Lazy.of(() -> new RecipeWrapperWithFluid(inputInventorySupplier.get(), solventTankSupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new LiquefactionCachedCheck(RecipeTypeRegistry.LIQUEFACTION.get()));

        this.solventTankSupplier = solventTankSupplier;
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item


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
        this.solventTankSupplier.get().drain(pRecipe.value().getSolventAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }
}
