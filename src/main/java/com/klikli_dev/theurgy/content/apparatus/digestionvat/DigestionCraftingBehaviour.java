// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DigestionCraftingBehaviour extends CraftingBehaviour<ItemHandlerWithFluidRecipeInput, DigestionRecipe, DigestionCachedCheck> {

    protected Supplier<IFluidHandler> fluidTankSupplier;

    public DigestionCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, Supplier<IFluidHandler> fluidTankSupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerWithFluidRecipeInput(inputInventorySupplier.get(), fluidTankSupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new DigestionCachedCheck(RecipeTypeRegistry.DIGESTION.get()));

        this.fluidTankSupplier = fluidTankSupplier;
    }

    @Override
    public boolean canProcess(ItemStack stack) {
        if (this.alreadyHasInput(stack))
            return true; //early out if we are already processing this type of item

        var currentRecipe = this.getRecipe();
        if (currentRecipe.isPresent()) {
            //if we currently have a recipe we determine process-ability based on if the item is part of the recipe
            return currentRecipe.get().value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack));
        }

        var ingredientsList = Stream.concat(
                IntStream.range(0, this.inputInventorySupplier.get().getSlots()).filter(i -> !this.inputInventorySupplier.get().getStackInSlot(i).isEmpty()).mapToObj(i -> this.inputInventorySupplier.get().getStackInSlot(i)),
                Stream.of(stack)
        ).toList();

        if (ingredientsList.size() > 1) {
            //if we have any items in the input inventory (= more simulated ingredients than one, which is the one we are checking), we can only process items that share a recipe with already existing items
            return this.recipeCachedCheck.getRecipeFor(ingredientsList, this.blockEntity.getLevel()).isPresent();
        }

        //finally if we have an empty inventory we do a simple check if the item is an ingredient of any recipe
        return this.isIngredient(stack);
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    public boolean canProcess(FluidStack stack) {
        if (FluidStack.isSameFluidSameComponents(this.fluidTankSupplier.get().getFluidInTank(0), stack))
            return true; //early out if we are already processing this type of fluid

        //now we use our custom cached check that checks only liquids:
        return this.isIngredient(stack);
    }

    @Override
    public boolean isIngredient(FluidStack stack) {
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(RecipeHolder<DigestionRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<DigestionRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return DigestionRecipe.DEFAULT_TIME;
    }

    @Override
    protected boolean craft(RecipeHolder<DigestionRecipe> pRecipe) {
        var assembledStack = pRecipe.value().assemble(this.recipeInputSupplier.get(), Objects.requireNonNull(this.blockEntity.getLevel()).registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);

        //consume the input stacks
        //the double loop may not be necessary, it may be OK to just take one from each slot (because recipe matches only if exact items match, not if more items are present)
        //however this costs almost nothing extra and is safer so we do it.
        for (var ingredient : pRecipe.value().getSizedIngredients()) {
            for (int i = 0; i < this.inputInventorySupplier.get().getSlots(); i++) {
                if (ingredient.ingredient().test(this.inputInventorySupplier.get().getStackInSlot(i))) {
                    this.inputInventorySupplier.get().extractItem(i, ingredient.count(), false);
                    break;
                }
            }
        }

        //then drain the fluid
        this.fluidTankSupplier.get().drain(pRecipe.value().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }
}
