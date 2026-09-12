// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DigestionCraftingBehaviour extends CraftingBehaviour<ItemHandlerWithFluidRecipeInput, DigestionRecipe, DigestionCachedCheck> {

    protected Supplier<ResourceHandler<FluidResource>> fluidTankSupplier;

    public DigestionCraftingBehaviour(BlockEntity blockEntity, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier, Supplier<ResourceHandler<FluidResource>> fluidTankSupplier) {
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
                IntStream.range(0, this.inputInventorySupplier.get().size()).filter(i -> !ItemUtil.getStack(this.inputInventorySupplier.get(), i).isEmpty()).mapToObj(i -> ItemUtil.getStack(this.inputInventorySupplier.get(), i)),
                Stream.of(stack)
        ).toList();

        if (ingredientsList.size() > 1) {
            //if we have any items in the input inventory (= more simulated ingredients than one, which is the one we are checking), we can only process items that share a recipe with already existing items
            return this.recipeCachedCheck.getRecipeFor(ingredientsList, this.blockEntity.getLevel()).isPresent();
        }

        //finally if we have an empty inventory we do a simple check if the item is an ingredient of any recipe
        return this.isIngredient(stack);
    }

    public void onInputChanged() {
        this.recipeCachedCheck.resetNoRecipeForLastItemHandlerInput();
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    public boolean canProcess(FluidStack stack) {
        if (FluidStack.isSameFluidSameComponents(FluidStorageHelper.getFluidInTank(this.fluidTankSupplier.get(), 0), stack))
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
        var assembledStack = pRecipe.value().assemble(this.recipeInputSupplier.get());

        try (var tx = Transaction.openRoot()) {
            if (this.outputInventorySupplier.get().insert(ItemResource.of(assembledStack), assembledStack.getCount(), tx) < assembledStack.getCount()) {
                return false;
            }

            for (var ingredient : pRecipe.value().getSizedIngredients()) {
                boolean extracted = false;
                for (int i = 0; i < this.inputInventorySupplier.get().size(); i++) {
                    var stack = ItemUtil.getStack(this.inputInventorySupplier.get(), i);
                    if (ingredient.ingredient().test(stack)) {
                        if (this.inputInventorySupplier.get().extract(ItemResource.of(stack), ingredient.count(), tx) < ingredient.count()) {
                            return false;
                        }
                        extracted = true;
                        break;
                    }
                }
                if (!extracted) {
                    return false;
                }
            }

            if (FluidStorageHelper.drain(this.fluidTankSupplier.get(), pRecipe.value().getFluidAmount(), tx).getAmount() < pRecipe.value().getFluidAmount()) {
                return false;
            }

            tx.commit();
        }

        return true;
    }
}
