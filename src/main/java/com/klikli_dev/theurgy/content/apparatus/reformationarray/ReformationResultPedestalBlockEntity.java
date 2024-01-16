// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
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
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ReformationResultPedestalBlockEntity extends BlockEntity {

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public WeakReference<SulfuricFluxEmitterBlockEntity> sulfuricFluxEmitter;

    protected boolean showParticles;

    public ReformationResultPedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.REFORMATION_RESULT_PEDESTAL.get(), pPos, pBlockState);

        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);
    }

    public void setSulfuricFluxEmitter(SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        this.sulfuricFluxEmitter = new WeakReference<>(sulfuricFluxEmitter);
    }

    public void tickClient() {
        if (this.showParticles && this.level.getRandom().nextFloat() < 0.07f) {
            var pos = this.getBlockPos();
            this.level.addParticle(GlowParticleProvider.createOptions(
                    ParticleColor.fromInt(0x008000),
                    0.5f,
                    0.75f,
                    200), pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f, 0, 0, 0);
        }
    }

    @Override
    public void setRemoved() {
        if (this.sulfuricFluxEmitter != null && this.sulfuricFluxEmitter.get() != null) {
            this.sulfuricFluxEmitter.get().removeResultPedestal(this);
            this.sulfuricFluxEmitter.clear();
        }

        super.setRemoved();
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        this.writeNetwork(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.readNetwork(pTag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.ITEM_HANDLER) {
            return this.outputInventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.outputInventoryCapability.invalidate();
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

    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("showParticles")) this.showParticles = pTag.getBoolean("showParticles");
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
    }

    public void writeNetwork(CompoundTag pTag) {
        this.showParticles = !this.outputInventory.getStackInSlot(0).isEmpty();
        pTag.putBoolean("showParticles", this.showParticles);
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    /**
     * Notification behaviour:
     * if the inventory becomes empty or non-empty or item type changes, do a network update
     * otherwise just setChanged() -> handled via onContentsChanged
     */
    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            ReformationResultPedestalBlockEntity.this.sendBlockUpdated();
        }

        @Override
        protected void onContentsChanged(int slot) {
            ReformationResultPedestalBlockEntity.this.setChanged();
        }
    }
}
