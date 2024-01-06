// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FermentationCraftingBehaviour extends CraftingBehaviour<RecipeWrapperWithFluid, FermentationRecipe, FermentationCachedCheck> {

    protected Supplier<IFluidHandler> fluidTankSupplier;

    public FermentationCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, Supplier<IFluidHandler> fluidTankSupplier) {
        super(blockEntity,
                Lazy.of(() -> new RecipeWrapperWithFluid(inputInventorySupplier.get(), fluidTankSupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new FermentationCachedCheck(RecipeTypeRegistry.FERMENTATION.get()));

        this.fluidTankSupplier = fluidTankSupplier;
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventorySupplier.get().getStackInSlot(0)))
            return true; //early out if we are already processing this type of item


        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }


    public boolean canProcess(FluidStack stack) {
        if (this.fluidTankSupplier.get().getFluidInTank(0).isFluidEqual(stack))
            return true; //early out if we are already processing this type of fluid

        //now we use our custom cached check that checks only liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(FermentationRecipe recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(FermentationRecipe recipe) {
        return recipe.getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return FermentationRecipe.TIME;
    }

    @Override
    protected boolean craft(FermentationRecipe pRecipe) {
        var assembledStack = pRecipe.assemble(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);

        //consume the input stacks
        //the double loop may not be necessary, it may be OK to just take one from each slot (because recipe matches only if exact items match, not if more items are present)
        //however this costs almost nothing extra and is safer so we do it.
        for(var ingredient : pRecipe.getIngredients()){
            for(int i = 0; i < this.inputInventorySupplier.get().getSlots(); i++){
                if(ingredient.test(this.inputInventorySupplier.get().getStackInSlot(i))){
                    this.inputInventorySupplier.get().extractItem(i, this.getIngredientCount(pRecipe), false);
                    break;
                }
            }
        }

        //then drain the fluid
        this.fluidTankSupplier.get().drain(pRecipe.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }
}
