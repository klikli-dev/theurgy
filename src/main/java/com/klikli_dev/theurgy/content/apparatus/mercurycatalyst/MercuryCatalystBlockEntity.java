// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.content.capability.SimpleMercuryFluxHandler;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MercuryCatalystBlockEntity extends BlockEntity implements HeldStackFitProvider {

    public static final int CAPACITY = 50000;

    public static final int PUSH_TICK_INTERVAL = 20;
    public static final int PUSH_RATE_PER_SIDE_PER_TICK = 2;

    public MonitoredItemStackHandler inventory;
    public MercuryCatalystMercuryFluxHandler mercuryFluxHandler;

    protected MercuryCatalystCraftingBehaviour craftingBehaviour;

    public MercuryCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CATALYST.get(), pPos, pBlockState);

        this.inventory = new Inventory();

        this.mercuryFluxHandler = new MercuryCatalystMercuryFluxHandler(CAPACITY);


        this.craftingBehaviour = new MercuryCatalystCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> (MercuryFluxHandler) this.mercuryFluxHandler);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.readNetwork(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input) {
        this.readNetwork(input);
    }

    public void readNetwork(ValueInput input) {
        input.child("MercuryFluxHandler").ifPresent(value -> {
            this.mercuryFluxHandler.deserialize(value);
            if (this.level != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_IMMEDIATE);
            }
        });
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput fluxOutput = output.child("MercuryFluxHandler");
        this.mercuryFluxHandler.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("MercuryFluxHandler");
        }
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        boolean hasInput = !this.inventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(true, hasInput);

        if (this.getLevel().getGameTime() % PUSH_TICK_INTERVAL == 0) {
            if (this.getBlockState().getValue(BlockStateProperties.ENABLED)) {
                this.pushMercuryFlux();
            }
        }
    }

    protected void pushMercuryFlux() {
        // Collect all valid flux handlers first
        var directions = Direction.allShuffled(this.getLevel().getRandom());
        var targets = new ArrayList<MercuryFluxHandler>();
        
        for (var direction : directions) {
            var fluxStorage = this.level.getCapability(CapabilityRegistry.MERCURY_FLUX_HANDLER, this.getBlockPos().relative(direction), direction.getOpposite());
            if (fluxStorage != null) {
                targets.add(fluxStorage);
            }
        }
        
        if (targets.isEmpty()) {
            return;
        }
        
        // Calculate how much to push to each target (scale by number of targets to maintain throughput)
        int totalToPush = Math.min(this.mercuryFluxHandler.getAmountAsInt(), PUSH_RATE_PER_SIDE_PER_TICK * PUSH_TICK_INTERVAL * targets.size());
        if (totalToPush <= 0) {
            return;
        }
        
        int perTarget = totalToPush / targets.size();
        int remainder = totalToPush % targets.size();
        
        // Distribute evenly to all targets
        for (int i = 0; i < targets.size(); i++) {
            int amount = perTarget + (i < remainder ? 1 : 0);
            if (amount <= 0) continue;
            
            try (net.neoforged.neoforge.transfer.transaction.Transaction tx = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                var received = targets.get(i).insert(amount, tx);
                if (received > 0) {
                    this.mercuryFluxHandler.extract(received, tx);
                    tx.commit();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput inventoryOutput = output.child("inventory");
        this.inventory.serialize(inventoryOutput);
        if (inventoryOutput.isEmpty()) {
            output.discard("inventory");
        }

        ValueOutput fluxOutput = output.child("MercuryFluxHandler");
        this.mercuryFluxHandler.serialize(fluxOutput);
        if (fluxOutput.isEmpty()) {
            output.discard("MercuryFluxHandler");
        }

        this.craftingBehaviour.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("inventory").ifPresent(this.inventory::deserialize);
        input.child("MercuryFluxHandler").ifPresent(value -> this.mercuryFluxHandler.deserialize(value));

        this.craftingBehaviour.loadAdditional(input);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxHandler.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()));

        if (pComponentInput.get(DataComponentRegistry.MERCURY_CATALYST_INVENTORY.get()) != null)
            ValueIOUtils.deserialize(this.level.registryAccess(), this.inventory, pComponentInput.get(DataComponentRegistry.MERCURY_CATALYST_INVENTORY.get()).copyTag());

        this.craftingBehaviour.applyImplicitComponents(pComponentInput);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

            pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxHandler.getAmountAsInt());

        pComponents.set(DataComponentRegistry.MERCURY_CATALYST_INVENTORY, CustomData.of(ValueIOUtils.serialize(this.level.registryAccess(), this.inventory)));

        this.craftingBehaviour.collectImplicitComponents(pComponents);
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
        public boolean isValid(int index, ItemResource resource) {
            if (resource.isEmpty()) return true;
            return MercuryCatalystBlockEntity.this.craftingBehaviour.canProcess(resource.toStack(1));
        }

        @Override
        protected void onContentsChanged(int slot) {
            MercuryCatalystBlockEntity.this.setChanged();
        }
    }

    public class MercuryCatalystMercuryFluxHandler extends SimpleMercuryFluxHandler {

        public static final int UPDATE_THRESHOLD = 100;
        private int lastUpdateLevel;

        public MercuryCatalystMercuryFluxHandler(int capacity) {
            // Non-receiving: only internal flux generation can fill this storage
            super(capacity, 0, capacity);
        }

        @Override
        public int insert(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            // Do not receive any external flux - only internal generation
            return 0;
        }

        /**
         * Internal method for the crafting behaviour to add flux generated from processing.
         * This bypasses the non-receiving restriction by directly modifying energy.
         */
        public int addInternalFlux(int amount) {
            int spaceAvailable = this.capacity - this.energy;
            int energyReceived = Math.min(spaceAvailable, amount);
            
            if (energyReceived > 0) {
                this.energy += energyReceived;
                MercuryCatalystBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }
            
            return energyReceived;
        }

        @Override
        public int extract(int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
            var extracted = super.extract(amount, transaction);

            if (extracted > 0) {
                MercuryCatalystBlockEntity.this.setChanged();
                this.trySendBlockUpdated();
            }

            return extracted;
        }

        public void trySendBlockUpdated() {
            var currentLevel = this.getAmountAsInt();
            if (Math.abs(this.lastUpdateLevel - currentLevel) > UPDATE_THRESHOLD) {
                this.lastUpdateLevel = currentLevel;
                MercuryCatalystBlockEntity.this.sendBlockUpdated();
            }
        }
    }

    @Override
    public List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of(this.inventory);
    }
}
