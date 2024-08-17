// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.recipe.CatalysationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


public class MercuryCatalystBlockEntity extends BlockEntity implements HasCraftingBehaviour<ItemHandlerRecipeInput, CatalysationRecipe, RecipeManager.CachedCheck<ItemHandlerRecipeInput, CatalysationRecipe>> {

    public static final int CAPACITY = 50000;

    public static final int PUSH_TICK_INTERVAL = 20;
    public static final int PUSH_RATE_PER_TICK = 2;

    public ItemStackHandler inventory;
    public MercuryCatalystMercuryFluxStorage mercuryFluxStorage;

    protected MercuryCatalystCraftingBehaviour craftingBehaviour;

    public MercuryCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.MERCURY_CATALYST.get(), pPos, pBlockState);

        this.inventory = new Inventory();

        this.mercuryFluxStorage = new MercuryCatalystMercuryFluxStorage(CAPACITY);


        this.craftingBehaviour = new MercuryCatalystCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> this.mercuryFluxStorage);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        var tag = new CompoundTag();
        this.writeNetwork(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.readNetwork(tag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider pRegistries) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag, pRegistries);
        }
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("mercuryFluxStorage")) {
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pRegistries, tag.get("mercuryFluxStorage"));
            if (this.level != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_IMMEDIATE);
            }
        }
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT(pRegistries));
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("inventory", this.inventory.serializeNBT(pRegistries));
        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT(pRegistries));

        this.craftingBehaviour.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("inventory"))
            this.inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pRegistries, pTag.get("mercuryFluxStorage"));

        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);

        if (pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE) != null)
            //noinspection DataFlowIssue
            this.mercuryFluxStorage.setEnergyStored(pComponentInput.get(DataComponentRegistry.MERCURY_FLUX_STORAGE));

        if (pComponentInput.get(DataComponentRegistry.MERCURY_CATALYST_INVENTORY) != null)
            this.inventory.deserializeNBT(this.level.registryAccess(), pComponentInput.get(DataComponentRegistry.MERCURY_CATALYST_INVENTORY).getUnsafe());

        this.craftingBehaviour.applyImplicitComponents(pComponentInput);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);

        pComponents.set(DataComponentRegistry.MERCURY_FLUX_STORAGE, this.mercuryFluxStorage.getEnergyStored());

        pComponents.set(DataComponentRegistry.MERCURY_CATALYST_INVENTORY, CustomData.of(this.inventory.serializeNBT(this.level.registryAccess())));

        this.craftingBehaviour.collectImplicitComponents(pComponents);
    }

    @Override
    public CraftingBehaviour<ItemHandlerRecipeInput, CatalysationRecipe, RecipeManager.CachedCheck<ItemHandlerRecipeInput, CatalysationRecipe>> craftingBehaviour() {
        return this.craftingBehaviour;
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
