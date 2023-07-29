/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MercuryCatalystBlockEntity extends BlockEntity {
    public ItemStackHandler inventory;
    public EnergyStorage mercuryFluxStorage;

    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IEnergyStorage> mercuryFluxStorageCapability;

    public MercuryCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CATALYST.get(), pPos, pBlockState);

        this.inventory = new Inventory();
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

        this.mercuryFluxStorage = new MercuryFluxStorage(1000);
        this.mercuryFluxStorageCapability = LazyOptional.of(() -> this.mercuryFluxStorage);
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
        if (tag.contains("mercuryFluxStorage")) {
            this.mercuryFluxStorage.deserializeNBT(tag.getCompound("mercuryFluxStorage"));
        }
    }

    public void writeNetwork(CompoundTag tag) {
        tag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT());
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryCapability.cast();
        }
        if (cap == CapabilityRegistry.MERCURY_FLUX) {
            return this.mercuryFluxStorageCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inventory"))
            this.inventory.deserializeNBT(pTag.getCompound("inventory"));

        if (pTag.contains("mercuryFluxStorage"))
            this.mercuryFluxStorage.deserializeNBT(pTag.getCompound("mercuryFluxStorage"));
    }

    private class Inventory extends ItemStackHandler {
        public Inventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            //TODO: only allow mercury
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            MercuryCatalystBlockEntity.this.setChanged();
        }
    }

    private class MercuryFluxStorage extends EnergyStorage {
        public MercuryFluxStorage(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                MercuryCatalystBlockEntity.this.setChanged();
                MercuryCatalystBlockEntity.this.sendBlockUpdated();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                MercuryCatalystBlockEntity.this.setChanged();
                MercuryCatalystBlockEntity.this.sendBlockUpdated();
            }

            return extracted;
        }
    }
}
