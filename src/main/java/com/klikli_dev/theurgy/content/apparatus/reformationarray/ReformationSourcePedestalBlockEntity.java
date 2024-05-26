// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ReformationSourcePedestalBlockEntity extends BlockEntity {

    public ItemStackHandler inputInventory;

    public WeakReference<SulfuricFluxEmitterBlockEntity> sulfuricFluxEmitter;

    protected boolean showParticles;

    public ReformationSourcePedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.REFORMATION_SOURCE_PEDESTAL.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
    }

    public void setSulfuricFluxEmitter(SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        this.sulfuricFluxEmitter = new WeakReference<>(sulfuricFluxEmitter);
    }

    public void tickClient() {
        if (this.showParticles && this.level.getRandom().nextFloat() < 0.07f) {
            var pos = this.getBlockPos();
            this.level.addParticle(GlowParticleProvider.createOptions(
                    ParticleColor.fromInt(0xFF00FF),
                    0.5f,
                    0.75f,
                    200), pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f, 0, 0, 0);
        }
    }

    @Override
    public void setRemoved() {
        if (this.sulfuricFluxEmitter != null && this.sulfuricFluxEmitter.get() != null) {
            this.sulfuricFluxEmitter.get().removeSourcePedestal(this);
            this.sulfuricFluxEmitter.clear();
        }

        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        this.writeNetwork(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        this.readNetwork(pTag, pRegistries);
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

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        if (pTag.contains("showParticles")) this.showParticles = pTag.getBoolean("showParticles");

        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pRegistries, pTag.getCompound("inputInventory"));
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.showParticles = !this.inputInventory.getStackInSlot(0).isEmpty();
        pTag.putBoolean("showParticles", this.showParticles);
        pTag.put("inputInventory", this.inputInventory.serializeNBT(pRegistries));
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
    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        protected SulfuricFluxEmitterBlockEntity emitter() {
            var emitter = ReformationSourcePedestalBlockEntity.this.sulfuricFluxEmitter;
            return emitter != null ? emitter.get() : null;
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            ReformationSourcePedestalBlockEntity.this.sendBlockUpdated();
            if (this.emitter() != null)
                this.emitter().craftingBehaviour.onInputItemChanged(oldStack, newStack);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ItemTagRegistry.ALCHEMICAL_SULFURS_AND_NITERS) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            ReformationSourcePedestalBlockEntity.this.setChanged();

            if (this.emitter() != null)
                this.emitter().onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity.this);
        }
    }
}
