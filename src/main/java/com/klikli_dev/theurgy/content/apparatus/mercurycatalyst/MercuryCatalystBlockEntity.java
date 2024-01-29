// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


public class MercuryCatalystBlockEntity extends BlockEntity {

    public static final int CAPACITY = 50000;

    public static final int PUSH_TICK_INTERVAL = 20;
    public static final int PUSH_RATE_PER_TICK = 2;

    public ItemStackHandler inventory;
    public MercuryCatalystMercuryFluxStorage mercuryFluxStorage;

    protected CraftingBehaviour<?, ?, ?> craftingBehaviour;

    public MercuryCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CATALYST.get(), pPos, pBlockState);

        this.inventory = new Inventory();

        this.mercuryFluxStorage = new MercuryCatalystMercuryFluxStorage(CAPACITY);


        this.craftingBehaviour = new MercuryCatalystCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> this.mercuryFluxStorage);
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
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(tag.get("mercuryFluxStorage"));
            if (this.level != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_IMMEDIATE);
            }
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
        boolean hasInput = !this.inventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(true, hasInput);

        if (this.getLevel().getGameTime() % PUSH_TICK_INTERVAL == 0) {
            if(this.getBlockState().getValue(BlockStateProperties.ENABLED)){
                this.pushMercuryFlux();
            }
        }
    }

    protected void pushMercuryFlux() {
        var directions = Direction.allShuffled(this.getLevel().getRandom());
        for (var direction : directions) {
            if (this.mercuryFluxStorage.getEnergyStored() <= 0)
                break;

            var blockEntity = this.getLevel().getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity == null)
                continue;

            var fluxStorage = this.level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, this.getBlockPos().relative(direction), null);
            if (fluxStorage == null)
                continue;

            var energy = this.mercuryFluxStorage.extractEnergy(PUSH_RATE_PER_TICK * PUSH_TICK_INTERVAL, true);
            var received = fluxStorage.receiveEnergy(energy, false);
            this.mercuryFluxStorage.extractEnergy(received, false);
        }
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
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pTag.get("mercuryFluxStorage"));
    }

    private class Inventory extends MonitoredItemStackHandler {
        public Inventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            MercuryCatalystBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            MercuryCatalystBlockEntity.this.sendBlockUpdated();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return MercuryCatalystBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            MercuryCatalystBlockEntity.this.setChanged();
        }
    }

    public class MercuryCatalystMercuryFluxStorage extends DefaultMercuryFluxStorage {

        public static final int UPDATE_THRESHOLD = 100;
        private int lastUpdateLevel;

        public MercuryCatalystMercuryFluxStorage(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                MercuryCatalystBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                MercuryCatalystBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }

            return extracted;
        }

        public void trySendBlockUpdated() {
            var currentLevel = this.getEnergyStored();
            if (Math.abs(this.lastUpdateLevel - currentLevel) > UPDATE_THRESHOLD) {
                this.lastUpdateLevel = currentLevel;
                MercuryCatalystBlockEntity.this.sendBlockUpdated();
            }
        }
    }
}
