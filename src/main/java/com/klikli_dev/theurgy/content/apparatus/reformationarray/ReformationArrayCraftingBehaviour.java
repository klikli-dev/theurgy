// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ReformationArrayRecipeInput;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ReformationArrayCraftingBehaviour extends CraftingBehaviour<ReformationArrayRecipeInput, ReformationRecipe, LevelAwareCachedCheck<ReformationArrayRecipeInput, ReformationRecipe>> {

    protected final Supplier<MercuryFluxHandler> mercuryFluxHandlerSupplier;

    public ReformationArrayCraftingBehaviour(BlockEntity blockEntity, Supplier<ReformationArrayRecipeInput> recipeWrapperSupplier, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier, Supplier<MercuryFluxHandler> mercuryFluxHandlerSupplier) {
        super(blockEntity,
                recipeWrapperSupplier,
                inputInventorySupplier,
                outputInventorySupplier,
                new LevelAwareCachedCheck<>(RecipeTypeRegistry.REFORMATION.get()));

        this.mercuryFluxHandlerSupplier = mercuryFluxHandlerSupplier;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(this.recipeInputSupplier.get(), this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected boolean craft(RecipeHolder<ReformationRecipe> pRecipe) {
        var ItemHandlerRecipeInput = this.recipeInputSupplier.get();
        var assembledStack = pRecipe.value().assemble(ItemHandlerRecipeInput);

        try (var tx = Transaction.openRoot()) {
            if (this.mercuryFluxHandlerSupplier.get().extract(pRecipe.value().getMercuryFlux(), tx) < pRecipe.value().getMercuryFlux()) {
                return false;
            }

            Set<SettableItemStorage> usedInventories = new HashSet<>();
            for (var source : pRecipe.value().getSources()) {
                boolean extracted = false;
                for (var sourceInventory : ItemHandlerRecipeInput.getSourcePedestalInvs()) {
                    if (usedInventories.contains(sourceInventory)) {
                        continue;
                    }

                    var sourceStack = ItemUtil.getStack(sourceInventory, 0);
                    if (source.test(sourceStack)) {
                        if (sourceInventory.extract(ItemResource.of(sourceStack), source.count(), tx) < source.count()) {
                            return false;
                        }
                        usedInventories.add(sourceInventory);
                        extracted = true;
                        break;
                    }
                }
                if (!extracted) {
                    return false;
                }
            }

            if (this.outputInventorySupplier.get().insert(ItemResource.of(assembledStack), assembledStack.getCount(), tx) < assembledStack.getCount()) {
                return false;
            }

            tx.commit();
        }

        return true;
    }

    @Override
    protected int getIngredientCount(RecipeHolder<ReformationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<ReformationRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return ReformationRecipe.DEFAULT_TIME;
    }

    @Override
    protected int getTotalTime() {
        return this.recipeInputSupplier.get() != null ? super.getTotalTime() : this.getDefaultCraftingTime();
    }
}
