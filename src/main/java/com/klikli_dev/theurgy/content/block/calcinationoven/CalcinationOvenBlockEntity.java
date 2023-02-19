/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.calcinationoven;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalcinationOvenBlockEntity extends BlockEntity implements HeatConsumer {

    private final RecipeManager.CachedCheck<RecipeWrapper, ? extends CalcinationRecipe> recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;
    public RecipeWrapper inputRecipeWrapper;
    int calcinationProgress;
    int calcinationTotalTime;

    boolean heatedCache;

    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventory);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.inputRecipeWrapper = new RecipeWrapper(this.inputInventory);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get());
    }

    @Override
    public boolean getHeatedCache() {
        return this.heatedCache;
    }

    @Override
    public void setHeatedCache(boolean heated) {
        this.heatedCache = heated;
    }

    public void tickServer() {
        boolean isHeated = this.isHeated();
        boolean hasInput = !this.inputInventory.getStackInSlot(0).isEmpty();

        if (hasInput) {
            //only even check for recipe if we have fuel and input or are currently burning to avoid unnecessary lookups
            Recipe<?> recipe;
            if (hasInput) {
                recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);
            } else {
                recipe = null;
            }

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                ++this.calcinationProgress;

                //if we hit max progress, craft the item and reset progress
                if (this.calcinationProgress == this.calcinationTotalTime) {
                    this.calcinationProgress = 0;
                    this.calcinationTotalTime = this.getTotalCalcinationTime();
                    this.craft(recipe);
                    //TODO: advancement?
                }
            } else {
                this.calcinationProgress = 0;
            }
        }
    }

    private boolean canCraft(@Nullable Recipe<?> pRecipe) {
        if (pRecipe == null)
            return false;

        if (this.inputInventory.getStackInSlot(0).isEmpty())
            return false;

        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inputRecipeWrapper);
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var outputStack = this.outputInventory.getStackInSlot(0);
            if (outputStack.isEmpty()) {
                return true;
            } else if (!outputStack.sameItem(assembledStack)) {
                return false;
            } else if (outputStack.getCount() + assembledStack.getCount() <= this.outputInventory.getSlotLimit(0)
                    && outputStack.getCount() + assembledStack.getCount() <= outputStack.getMaxStackSize()) {
                return true;
            } else {
                return outputStack.getCount() + assembledStack.getCount() <= assembledStack.getMaxStackSize();
            }
        }

    }

    private boolean craft(@Nullable Recipe<?> pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var inputStack = this.inputInventory.getStackInSlot(0);
        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inputRecipeWrapper);
        var outputStack = this.outputInventory.getStackInSlot(0);
        if (outputStack.isEmpty()) {
            this.inputInventory.setStackInSlot(0, assembledStack.copy());
        } else if (outputStack.is(assembledStack.getItem())) {
            outputStack.grow(assembledStack.getCount());
        }

        inputStack.shrink(1);
        return true;

    }

    protected int getTotalCalcinationTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(CalcinationRecipe::getCalcinationTime).orElse(CalcinationRecipe.DEFAULT_CALCINATION_TIME);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("calcinationProgress", (short) this.calcinationProgress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("calcinationProgress"))
            this.calcinationProgress = pTag.getShort("calcinationProgress");
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        RecipeWrapper tempRecipeWrapper = new RecipeWrapper(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.level).isPresent();
    }

    public class InputInventory extends ItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && newStack.sameItem(oldStack) && ItemStack.tagMatches(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                CalcinationOvenBlockEntity.this.calcinationTotalTime = CalcinationOvenBlockEntity.this.getTotalCalcinationTime();
                CalcinationOvenBlockEntity.this.calcinationProgress = 0;
                CalcinationOvenBlockEntity.this.setChanged();
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CalcinationOvenBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    }
}
