/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.block.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SalAmmoniacAccumulatorCraftingBehaviour extends CraftingBehaviour<RecipeWrapperWithFluid, AccumulationRecipe, SalAmmoniacAccumulatorCachedCheck> {

    protected Supplier<IFluidHandler> waterTankSupplier;
    protected Supplier<IFluidHandler> outputTankSupplier;

    public SalAmmoniacAccumulatorCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, Supplier<IFluidHandler> waterTankSupplier, Supplier<IFluidHandler> outputTankSupplier) {
        super(blockEntity,
                Lazy.of(() -> new RecipeWrapperWithFluid(inputInventorySupplier.get(), waterTankSupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new SalAmmoniacAccumulatorCachedCheck(RecipeTypeRegistry.ACCUMULATION.get()));

        this.waterTankSupplier = waterTankSupplier;
        this.outputTankSupplier = outputTankSupplier;
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item


        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }


    public boolean canProcess(FluidStack stack) {
        if (this.waterTankSupplier.get().getFluidInTank(0).isFluidEqual(stack))
            return true; //early out if we are already processing this type of fluid

        //now we use our custom cached check that checks only liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(AccumulationRecipe recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(AccumulationRecipe recipe) {
        return recipe.getAccumulationTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return AccumulationRecipe.DEFAULT_ACCUMULATION_TIME;
    }

    @Override
    protected boolean canCraft(@Nullable AccumulationRecipe pRecipe) {
        if (pRecipe == null) return false;

        var assembledStack = pRecipe.assembleFluid(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var tank = this.outputTankSupplier.get();
            int fluidAccepted = tank.fill(assembledStack, IFluidHandler.FluidAction.SIMULATE);

            //initially we only allowed if we had space for the entire fluid
            //however this leads to a bug: if we first run on water and get a partially full tank,
            //and then add a sal ammoniac crystal that wants to add a full bucket -> stops running.
            //considering the low value of crystals it is OK to discard a partial fluid stack here.
            //return fluidAccepted == assembledStack.getAmount();
            return fluidAccepted > 0;
        }
    }

    @Override
    protected boolean craft(@Nullable AccumulationRecipe pRecipe) {
        if (!this.canCraft(pRecipe)) return false;

        var assembledFluid = pRecipe.assembleFluid(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());
        var outputFluidTank = this.outputTankSupplier.get();

        outputFluidTank.fill(assembledFluid, IFluidHandler.FluidAction.EXECUTE);

        //only consume the solid solute, if the recipe requires it.
        //this avoids accidentally consuming a solute when a "water only" recipe is running while the solute is added.
        if (pRecipe.hasSolute()) {
            this.inputInventorySupplier.get().extractItem(0, this.getIngredientCount(pRecipe), false);
        }

        this.waterTankSupplier.get().drain(pRecipe.getEvaporant().getAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }
}
