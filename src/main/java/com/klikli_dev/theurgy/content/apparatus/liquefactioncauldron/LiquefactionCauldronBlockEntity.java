// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultHeatReceiver;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class LiquefactionCauldronBlockEntity extends BlockEntity {

    public DefaultHeatReceiver heatReceiver;

    public LiquefactionStorageBehaviour storageBehaviour;

    protected LiquefactionCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);


        this.storageBehaviour = new LiquefactionStorageBehaviour(this, () -> this.craftingBehaviour);

        this.heatReceiver = new DefaultHeatReceiver();

        this.craftingBehaviour = new LiquefactionCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.solventTank);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
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
        this.storageBehaviour.readNetwork(tag);
        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        this.storageBehaviour.writeNetwork(tag);
        this.craftingBehaviour.writeNetwork(tag);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            if (this.getLevel().getGameTime() % 2 == 0) { // only spawn particles every 2 ticks
                this.getLevel().addParticle(
                        ColoredBubbleParticleProvider.createOptions(new ParticleColor(255, 0, 255)),
                        this.getBlockPos().getX() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),

                        this.getBlockPos().getY() + 1.1,
                        this.getBlockPos().getZ() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),
                        0.0D, 0.015D, 0.0D

                );
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("heatReceiver", this.heatReceiver.serializeNBT());

        this.storageBehaviour.saveAdditional(pTag);
        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("heatReceiver"))
            this.heatReceiver.deserializeNBT(pTag.get("heatReceiver"));

        this.storageBehaviour.load(pTag);
        this.craftingBehaviour.load(pTag);
    }
}
