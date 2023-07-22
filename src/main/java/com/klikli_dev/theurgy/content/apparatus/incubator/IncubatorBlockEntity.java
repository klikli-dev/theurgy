/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.HeatedBehaviour;
import com.klikli_dev.theurgy.content.behaviour.PreventInsertWrapper;
import com.klikli_dev.theurgy.content.recipe.wrapper.IncubatorRecipeWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlockEntity extends BlockEntity {
    public IncubatorMercuryVesselBlockEntity mercuryVessel;
    public IncubatorSulfurVesselBlockEntity sulfurVessel;
    public IncubatorSaltVesselBlockEntity saltVessel;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public IncubatorRecipeWrapper recipeWrapper;
    public boolean isValidMultiblock;

    protected CraftingBehaviour<?, ?, ?> craftingBehaviour;
    protected HeatedBehaviour heatedBehaviour;
    protected boolean checkValidMultiblockOnNextQuery;

    public IncubatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR.get(), pPos, pBlockState);

        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);
        this.checkValidMultiblockOnNextQuery = true;

        this.craftingBehaviour = new IncubatorCraftingBehaviour(this, () -> this.recipeWrapper, () -> null, () -> this.outputInventory);
        this.heatedBehaviour = new HeatedBehaviour(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        this.writeNetwork(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.readNetwork(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag);
        }
    }

    public void readNetwork(CompoundTag tag) {
        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        this.craftingBehaviour.writeNetwork(tag);
    }

    public void tickServer() {
        boolean isHeated = this.heatedBehaviour.isHeated();

        boolean hasInput = this.isValidMultiblock()
                && !this.mercuryVessel.inputInventory.getStackInSlot(0).isEmpty()
                && !this.saltVessel.inputInventory.getStackInSlot(0).isEmpty()
                && !this.sulfurVessel.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            var random = this.getLevel().getRandom();
            if (random.nextFloat() < 0.11F) {
                for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                    this.getLevel().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            this.getBlockPos().getX() + 0.5 + random.nextFloat() / 3.0 * (random.nextBoolean() ? 1 : -1),
                            this.getBlockPos().getY() + 2 + random.nextFloat(),
                            this.getBlockPos().getZ() + 0.5 + random.nextFloat() / 3.0 * (random.nextBoolean() ? 1 : -1),
                            0, 0.07, 0
                    );
                }
            }
        }
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

        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));

        this.craftingBehaviour.load(pTag);
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
        this.craftingBehaviour.onInputItemChanged(ItemStack.EMPTY, ItemStack.EMPTY);
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

    public boolean isValidMultiblock() {
        if (this.checkValidMultiblockOnNextQuery) {
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
        protected void onContentsChanged(int slot) {
            IncubatorBlockEntity.this.setChanged();
        }
    }
}