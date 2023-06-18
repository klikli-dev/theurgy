/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class CraftingBehaviour<W extends RecipeWrapper, R extends Recipe<W>, C extends RecipeManager.CachedCheck<W, R>> {
    protected BlockEntity blockEntity;
    protected Supplier<W> recipeWrapperSupplier;
    protected Supplier<IItemHandlerModifiable> inputInventorySupplier;
    protected Supplier<IItemHandlerModifiable> outputInventorySupplier;
    protected C recipeCachedCheck;

    protected int progress;
    protected int totalTime;
    protected boolean isProcessing;


    public CraftingBehaviour(BlockEntity blockEntity, Supplier<W> recipeWrapper, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, C recipeCachedCheck) {
        this.blockEntity = blockEntity;
        this.recipeWrapperSupplier = recipeWrapper;
        this.inputInventorySupplier = inputInventorySupplier;
        this.outputInventorySupplier = outputInventorySupplier;
        this.recipeCachedCheck = recipeCachedCheck;
    }

    public boolean isProcessing() {
        return this.isProcessing;
    }

    public void readNetwork(CompoundTag tag) {
        this.isProcessing = tag.getBoolean("isProcessing");
    }

    public void writeNetwork(CompoundTag tag) {
        tag.putBoolean("isProcessing", this.isProcessing);
    }

    public void saveAdditional(CompoundTag pTag) {
        pTag.putShort("progress", (short) this.progress);
    }

    public void load(CompoundTag pTag) {
        if (pTag.contains("progress"))
            this.progress = pTag.getShort("progress");
    }

    public void tickServer(boolean isHeated, boolean hasInput) {
        if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel()).orElse(null);

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                this.tryStartProcessing();

                this.progress++;

                this.tryFinishProcessing(recipe);
            } else {
                this.stopProcessing();
            }
        } else {
            this.stopProcessing();
        }
    }

    public void onInputItemChanged(ItemStack oldStack, ItemStack newStack) {
        this.totalTime = this.getTotalTime();
        this.progress = 0;
    }

    /**
     * If progress has reached 100%, craft the item and reset progress.
     */
    protected void tryFinishProcessing(R pRecipe) {
        if (this.progress >= this.totalTime) {
            this.progress = 0;
            this.totalTime = this.getTotalTime();

            this.craft(pRecipe);
            this.sendBlockUpdated();
            //no need to setChanged() as the BE does that on inventory change.
        }
    }

    protected void tryStartProcessing() {
        if (this.progress == 0) {
            this.isProcessing = true;
            this.sendBlockUpdated();
            //no need to setChanged() as the BE does that on inventory change.
        }

        //we don't have to worry about total time here, as it is set when an item is put into the inventory.
    }

    protected void stopProcessing() {
        //only do state updates if we actually changed something
        if (this.progress != 0 || this.isProcessing) {
            this.isProcessing = false;
            this.progress = 0;
            this.sendBlockUpdated();
            //no need to setChanged() as the BE does that on inventory change.
        }

        //we don't have to worry about total time here, as it is set when an item is put into the inventory.
    }

    protected boolean canCraft(@Nullable R pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.assemble(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    protected boolean craft(@Nullable R pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var assembledStack = pRecipe.assemble(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);
        this.inputInventorySupplier.get().extractItem(0, this.getIngredientCount(pRecipe), false);

        return true;
    }


    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel())
                .map(this::getCraftingTime)
                .orElse(this.getDefaultCraftingTime());
    }

    protected void sendBlockUpdated() {
        if (this.blockEntity.getLevel() != null && !this.blockEntity.getLevel().isClientSide)
            this.blockEntity.getLevel().sendBlockUpdated(
                    this.blockEntity.getBlockPos(),
                    this.blockEntity.getBlockState(),
                    this.blockEntity.getBlockState(),
                    Block.UPDATE_CLIENTS
            );
    }

    protected abstract int getIngredientCount(R recipe);

    protected abstract int getCraftingTime(R recipe);

    protected abstract int getDefaultCraftingTime();

    protected abstract boolean canProcess(ItemStack stack);
}
