// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class CraftingBehaviour<W extends RecipeInput, R extends Recipe<W>, C extends RecipeManager.CachedCheck<W, R>> {
    protected BlockEntity blockEntity;
    protected Supplier<W> recipeWrapperSupplier;
    protected Supplier<IItemHandlerModifiable> inputInventorySupplier;
    protected Supplier<IItemHandlerModifiable> outputInventorySupplier;
    protected C recipeCachedCheck;

    protected int progress;
    protected int totalTime;
    protected boolean isProcessing;
    protected boolean couldCraftLastTick;


    public CraftingBehaviour(BlockEntity blockEntity, Supplier<W> ItemHandlerRecipeInput, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier, C recipeCachedCheck) {
        this.blockEntity = blockEntity;
        this.recipeWrapperSupplier = ItemHandlerRecipeInput;
        this.inputInventorySupplier = inputInventorySupplier;
        this.outputInventorySupplier = outputInventorySupplier;
        this.recipeCachedCheck = recipeCachedCheck;
    }

    public boolean isProcessing() {
        return this.isProcessing;
    }

    public boolean couldCraftLastTick() {
        return this.couldCraftLastTick;
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.isProcessing = tag.getBoolean("isProcessing");
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.putBoolean("isProcessing", this.isProcessing);
    }

    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putShort("progress", (short) this.progress);
    }

    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        if (pTag.contains("progress"))
            this.progress = pTag.getShort("progress");
    }

    public void applyImplicitComponents(BlockEntity.DataComponentInput pComponentInput) {

    }

    public void collectImplicitComponents(DataComponentMap.Builder pComponents) {

    }

    public Optional<RecipeHolder<R>> getRecipe() {
        return this.recipeCachedCheck.getRecipeFor(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel());
    }

    /**
     * Advances the crafting process by one tick.
     *
     * @param canProcess indicates if "fuel" is available (energy, or usually: heat). If false, will stop processing and reset progress.
     * @param hasInput   indicates if input ingredients are available. If false, will stop processing and reset progress.
     */
    public void tickServer(boolean canProcess, boolean hasInput) {
        if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.getRecipe().orElse(null);

            this.couldCraftLastTick = this.canCraft(recipe);

            //if we are lit and have a recipe, update progress
            if (canProcess && this.couldCraftLastTick) {
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

    public abstract boolean canProcess(ItemStack stack);

    public boolean canProcess(FluidStack stack) {
        return false;
    }

    /**
     * Returns true if the input inventory already contains a stack matching the given stack.
     * This allows us to early out of canProcess.
     */
    protected boolean alreadyHasInput(ItemStack stack) {
        return IntStream.range(0, this.inputInventorySupplier.get().getSlots()).anyMatch(i -> ItemStack.isSameItemSameComponents(stack, this.inputInventorySupplier.get().getStackInSlot(i)));
    }

    /**
     * If progress has reached 100%, craft the item and reset progress.
     */
    protected void tryFinishProcessing(RecipeHolder<R> pRecipe) {
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

    public void stopProcessing() {
        //only do state updates if we actually changed something
        if (this.progress != 0 || this.isProcessing) {
            this.isProcessing = false;
            this.couldCraftLastTick = false;
            this.progress = 0;
            this.sendBlockUpdated();
            //no need to setChanged() as the BE does that on inventory change.
        }

        //we don't have to worry about total time here, as it is set when an item is put into the inventory.
    }

    public boolean canCraft(@Nullable RecipeHolder<R> pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.value().assemble(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    protected boolean craft(RecipeHolder<R> pRecipe) {
        var assembledStack = pRecipe.value().assemble(this.recipeWrapperSupplier.get(), this.blockEntity.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventorySupplier.get(), assembledStack, false);

        //consume the input stack
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
            this.blockEntity.getLevel().sendBlockUpdated(this.blockEntity.getBlockPos(), this.blockEntity.getBlockState(), this.blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
    }

    protected abstract int getIngredientCount(RecipeHolder<R> recipe);

    protected abstract int getCraftingTime(RecipeHolder<R> recipe);

    protected abstract int getDefaultCraftingTime();

}
