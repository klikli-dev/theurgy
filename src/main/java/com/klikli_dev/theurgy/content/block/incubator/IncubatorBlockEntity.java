/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.incubator;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.IncubatorRecipeWrapper;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlockEntity extends BlockEntity implements HeatConsumer {

    private final RecipeManager.CachedCheck<IncubatorRecipeWrapper, ? extends IncubationRecipe> recipeCachedCheck;
    public IncubatorMercuryVesselBlockEntity mercuryVessel;
    public IncubatorSulfurVesselBlockEntity sulfurVessel;
    public IncubatorSaltVesselBlockEntity saltVessel;

    public ItemStackHandler outputInventory;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public IncubatorRecipeWrapper recipeWrapper;
    protected boolean checkValidMultiblockOnNextQuery;
    public boolean isValidMultiblock;

    private boolean heatedCache;
    private int progress;
    private int totalTime;

    public IncubatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR.get(), pPos, pBlockState);

        this.outputInventory = new OutputInventory();
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.INCUBATION.get());
        this.checkValidMultiblockOnNextQuery = true;
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

        if (!this.isValidMultiblock()) {
            return;
        }

        boolean hasInput = !this.mercuryVessel.inputInventory.getStackInSlot(0).isEmpty()
                && !this.saltVessel.inputInventory.getStackInSlot(0).isEmpty()
                && !this.sulfurVessel.inputInventory.getStackInSlot(0).isEmpty();


        if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.recipeWrapper, this.level).orElse(null);

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

    private boolean canCraft(@Nullable IncubationRecipe pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.assemble(this.recipeWrapper, this.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    private boolean craft(@Nullable IncubationRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var assembledStack = pRecipe.assemble(this.recipeWrapper, this.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, false);

        this.mercuryVessel.inputInventory.extractItem(0, 1, false);
        this.saltVessel.inputInventory.extractItem(0, 1, false);
        this.sulfurVessel.inputInventory.extractItem(0, 1, false);

        return true;
    }

    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.recipeWrapper, this.level).map(IncubationRecipe::getIncubationTime).orElse(IncubationRecipe.DEFAULT_INCUBATION_TIME);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.outputInventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("progress", (short) this.progress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("progress"))
            this.progress = pTag.getShort("progress");
    }

    private void checkForVessel(BlockPos pos) {
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity instanceof IncubatorMercuryVesselBlockEntity mercury) {
            this.mercuryVessel = mercury;
            mercury.setIncubator(this);
        } else if (blockEntity instanceof IncubatorSulfurVesselBlockEntity sulfur) {
            this.sulfurVessel = sulfur;
            sulfur.setIncubator(this);
        } else if (blockEntity instanceof IncubatorSaltVesselBlockEntity salt) {
            this.saltVessel = salt;
            salt.setIncubator(this);
        }
    }

    public void validateMultiblock() {
        var oldMercuryVessel = this.mercuryVessel;
        var oldSaltVessel = this.saltVessel;
        var oldSulfurVessel = this.sulfurVessel;
        var wasValidMultiblock = this.isValidMultiblock;

        this.mercuryVessel = null;
        this.saltVessel = null;
        this.sulfurVessel = null;

        Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
            BlockPos pos = this.getBlockPos().relative(direction);
            this.checkForVessel(pos);
        });

        this.isValidMultiblock = this.mercuryVessel != null && this.sulfurVessel != null && this.saltVessel != null;
        if (wasValidMultiblock != this.isValidMultiblock) {
            if (this.isValidMultiblock) {
                this.onAssembleMultiblock();
            } else {
                this.onDisassembleMultiblock();
            }
        }

        if (oldMercuryVessel != this.mercuryVessel || oldSaltVessel != this.saltVessel || oldSulfurVessel != this.sulfurVessel) {
            this.onVesselItemChanged();
        }
    }

    public void onAssembleMultiblock() {
        this.recipeWrapper = new IncubatorRecipeWrapper(this.mercuryVessel.inputInventory, this.saltVessel.inputInventory, this.sulfurVessel.inputInventory);
    }

    public void onDisassembleMultiblock() {
        this.recipeWrapper = null;
    }

    public void onVesselItemChanged() {
        this.totalTime = this.getTotalTime();
        this.progress = 0;
    }

    @Override
    public void setRemoved() {
        if (this.mercuryVessel != null) {
            this.mercuryVessel.setIncubator(null);
            this.mercuryVessel = null;
        }
        if (this.sulfurVessel != null) {
            this.sulfurVessel.setIncubator(null);
            this.sulfurVessel = null;
        }
        if (this.saltVessel != null) {
            this.saltVessel.setIncubator(null);
            this.saltVessel = null;
        }
        super.setRemoved();
    }

    public boolean isValidMultiblock(){
        if(this.checkValidMultiblockOnNextQuery){
            this.checkValidMultiblockOnNextQuery = false;
            this.validateMultiblock();
        }
        return this.isValidMultiblock;
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
            IncubatorBlockEntity.this.setChanged();
        }
    }
}
