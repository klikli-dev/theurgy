/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SalAmmoniacAccumulatorBlockEntity extends BlockEntity {

    private final CachedCheck recipeCachedCheck;

    public ItemStackHandler inventory;
    public LazyOptional<IItemHandler> inventoryCapability;

    public FluidTank waterTank;
    public LazyOptional<IFluidHandler> waterTankCapability;

    public RecipeWrapperWithFluid recipeWrapper;

    protected boolean checkOutputTankOnNextQuery;
    protected boolean hasOutputTank;
    protected int progress;
    protected int totalTime;

    public SalAmmoniacAccumulatorBlockEntity(BlockPos pPos, BlockState pState) {
        super(BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), pPos, pState);

        this.inventory = new Inventory();
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

        //fluid tank has a validator that can use our canProcess, our inventory instead has to override isItemValid
        this.waterTank = new FluidTank(FluidType.BUCKET_VOLUME, (this::canProcess)) {
            @Override
            protected void onContentsChanged() {
                SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            }
        };
        this.waterTankCapability = LazyOptional.of(() -> this.waterTank);

        this.recipeCachedCheck = new CachedCheck(RecipeTypeRegistry.ACCUMULATION.get());

        this.recipeWrapper = new RecipeWrapperWithFluid(this.inventory, this.waterTank);

        this.checkOutputTankOnNextQuery = true;
    }

    public void tickServer() {
        if (!this.hasOutputTank()) {
            return;
        }

        boolean hasInput = !this.waterTank.isEmpty();

        if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.recipeWrapper, this.level).orElse(null);

            //if we are lit and have a recipe, update progress
            if (this.canCraft(recipe)) {
                ++this.progress;

                //if we hit max progress, craft the item and reset progress
                if (this.progress >= this.totalTime) {
                    this.progress = 0;
                    this.totalTime = this.getTotalTime();
                    this.craft(recipe);
                    //TODO: advancement?
                }
            } else {
                this.progress = 0;
            }
        }
    }


    private boolean canCraft(@Nullable AccumulationRecipe pRecipe) {
        if (pRecipe == null) return false;

        var assembledStack = pRecipe.assembleFluid(this.recipeWrapper, this.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var tank = this.getOutputTank();
            int fluidAccepted = tank.fill(assembledStack, IFluidHandler.FluidAction.SIMULATE);

            return fluidAccepted == assembledStack.getAmount(); // only allow if we have space for the entire fluid
        }

    }

    private boolean craft(@Nullable AccumulationRecipe pRecipe) {
        if (!this.canCraft(pRecipe)) return false;

        FluidStack assembledFluid = pRecipe.assembleFluid(this.recipeWrapper, this.getLevel().registryAccess());
        IFluidHandler outputFluidTank = this.getOutputTank();

        outputFluidTank.fill(assembledFluid, IFluidHandler.FluidAction.EXECUTE);

        //only consume the solid solute, if the recipe requires it.
        //this avoids accidentally consuming a solute when a "water only" recipe is running while the solute is added.
        if (pRecipe.hasSolute()) {
            this.inventory.extractItem(0, 1, false);
        }

        this.waterTank.drain(pRecipe.getEvaporant().getAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }

    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.recipeWrapper, this.level).map(AccumulationRecipe::getAccumulationTime).orElse(AccumulationRecipe.DEFAULT_ACCUMULATION_TIME);
    }

    public IFluidHandler getOutputTank() {
        var below = this.getBlockPos().below();
        var blockEntity = this.level.getBlockEntity(below);
        return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElseThrow(() -> new IllegalStateException("No fluid handler capability found on block entity below at location: " + below));
    }

    public void validateOutputTank() {
        var below = this.getBlockPos().below();
        var blockEntity = this.level.getBlockEntity(below);
        this.hasOutputTank = blockEntity != null && blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent();
    }

    public boolean hasOutputTank(){
        if(this.checkOutputTankOnNextQuery){
            this.checkOutputTankOnNextQuery = false;
            this.validateOutputTank();
        }
        return this.hasOutputTank;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return this.waterTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.putShort("progress", (short) this.progress);
        var waterTankTag = new CompoundTag();
        this.waterTank.writeToNBT(waterTankTag);
        pTag.put("waterTank", waterTankTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inventory")) this.inventory.deserializeNBT(pTag.getCompound("inventory"));
        if (pTag.contains("progress")) this.progress = pTag.getShort("progress");

        if (pTag.contains("waterTank")) {
            var waterTankTag = pTag.getCompound("waterTank");
            this.waterTank.readFromNBT(waterTankTag);
        }
    }

    private boolean canProcess(ItemStack stack) {
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inventory.getStackInSlot(0)))
            return true; //early out if we are already processing this type of item

        //now we use our custom cached check that ignores liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.level).isPresent();
    }

    private boolean canProcess(FluidStack stack) {
        if (this.waterTank.getFluid().isFluidEqual(stack))
            return true; //early out if we are already processing this type of fluid

        //now we use our custom cached check that checks only liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.level).isPresent();
    }

    /**
     * A custom cached check
     */
    private static class CachedCheck implements RecipeManager.CachedCheck<RecipeWrapperWithFluid, AccumulationRecipe> {

        private final RecipeType<AccumulationRecipe> type;
        private final RecipeManager.CachedCheck<RecipeWrapperWithFluid, AccumulationRecipe> internal;
        @Nullable
        private ResourceLocation lastRecipe;

        public CachedCheck(RecipeType<AccumulationRecipe> type) {
            this.type = type;
            this.internal = RecipeManager.createCheck(type);
        }

        private Optional<Pair<ResourceLocation, AccumulationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

            var recipeManager = level.getRecipeManager();
            var map = recipeManager.byType(this.type);
            if (lastRecipe != null) {
                var recipe = map.get(lastRecipe);
                //test only the ingredient without the (separate) evaporate fluid ingredient check that the recipe.matches() would.
                if (recipe != null && recipe.hasSolute() && recipe.getSolute().test(stack)) {
                    return Optional.of(Pair.of(lastRecipe, recipe));
                }
            }

            return map.entrySet().stream().filter((entry) -> entry.getValue().hasSolute() && entry.getValue().getSolute().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
        }

        private Optional<Pair<ResourceLocation, AccumulationRecipe>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

            var recipeManager = level.getRecipeManager();
            var map = recipeManager.byType(this.type);
            if (lastRecipe != null) {
                var recipe = map.get(lastRecipe);
                //test only the fluid without the (separate) solute item ingredient check that the recipe.matches() would.
                if (recipe != null && recipe.getEvaporant().test(stack)) {
                    return Optional.of(Pair.of(lastRecipe, recipe));
                }
            }

            return map.entrySet().stream().filter((entry) -> entry.getValue().getEvaporant().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
        }

        /**
         * This only checks ingredients, not fluids
         */
        public Optional<AccumulationRecipe> getRecipeFor(ItemStack stack, Level level) {
            var optional = this.getRecipeFor(stack, level, this.lastRecipe);
            if (optional.isPresent()) {
                var pair = optional.get();
                this.lastRecipe = pair.getFirst();
                return Optional.of(pair.getSecond());
            } else {
                return Optional.empty();
            }
        }

        /**
         * This only checks fluids, not ingredients
         */
        public Optional<AccumulationRecipe> getRecipeFor(FluidStack stack, Level level) {
            var optional = this.getRecipeFor(stack, level, this.lastRecipe);
            if (optional.isPresent()) {
                var pair = optional.get();
                this.lastRecipe = pair.getFirst();
                return Optional.of(pair.getSecond());
            } else {
                return Optional.empty();
            }
        }

        /**
         * This checks full recipe validity: ingredients + fluids
         */
        @Override
        public Optional<AccumulationRecipe> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
            var recipe = this.internal.getRecipeFor(container, level);
            if (recipe.isPresent()) {
                this.lastRecipe = recipe.get().getId();
            }

            return recipe;
        }
    }


    public class Inventory extends ItemStackHandler {

        public Inventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && newStack.sameItem(oldStack) && ItemStack.tagMatches(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                SalAmmoniacAccumulatorBlockEntity.this.totalTime = SalAmmoniacAccumulatorBlockEntity.this.getTotalTime();
                SalAmmoniacAccumulatorBlockEntity.this.progress = 0;
                SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return SalAmmoniacAccumulatorBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
        }
    }

}
