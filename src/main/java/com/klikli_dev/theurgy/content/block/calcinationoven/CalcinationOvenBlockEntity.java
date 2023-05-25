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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
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
    int progress;
    int totalTime;

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
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
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

    private boolean canCraft(@Nullable CalcinationRecipe pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    private boolean craft(@Nullable CalcinationRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, false);
        this.inputInventory.extractItem(0, pRecipe.getIngredientCount(), false);

        return true;
    }

    protected int getTotalTime() {
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
        pTag.putShort("progress", (short) this.progress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("progress"))
            this.progress = pTag.getShort("progress");
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
                CalcinationOvenBlockEntity.this.totalTime = CalcinationOvenBlockEntity.this.getTotalTime();
                CalcinationOvenBlockEntity.this.progress = 0;
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CalcinationOvenBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationOvenBlockEntity.this.setChanged();
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

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationOvenBlockEntity.this.setChanged();
        }
    }
}
