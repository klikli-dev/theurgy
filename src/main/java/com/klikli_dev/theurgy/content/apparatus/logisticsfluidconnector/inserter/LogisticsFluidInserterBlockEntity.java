// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter;

import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.LogisticsFluidConnectorBlockEntity;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogisticsFluidInserterBlockEntity extends LogisticsFluidConnectorBlockEntity {

    public LogisticsFluidInserterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_FLUID_INSERTER.get(), pPos, pBlockState);
        this.leafNodeBehaviour = new LogisticsFluidInserterBehaviour(this);
    }

    @Override
    public LogisticsFluidInserterBehaviour leafNode() {
        return (LogisticsFluidInserterBehaviour) this.leafNodeBehaviour;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        this.readNetwork(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ValueInput input) {
        this.readNetwork(input);
    }

    public void readNetwork(ValueInput input) {
        this.leafNode().readNetwork(input);
        this.filter().readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        this.leafNode().writeNetwork(output);
        this.filter().writeNetwork(output);
    }

    protected void sendBlockUpdated() {
        if (this.getLevel() != null && !this.getLevel().isClientSide)
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void enabled(boolean enabled) {
        this.leafNode().enabled(enabled);
        this.setChanged();
        this.sendBlockUpdated();
    }

    @Override
    public boolean enabled() {
        return this.leafNode().enabled();
    }

    @Override
    public void targetDirection(Direction direction) {
        this.leafNode().directionOverride(direction);
        this.setChanged();
        this.sendBlockUpdated();
    }

    @Override
    public Direction targetDirection() {
        return this.leafNode().getTargetContext(this.targetPos());
    }

    @Override
    public BlockPos targetPos() {
        return this.leafNode().targets().isEmpty() ? this.getBlockPos() : this.leafNode().targets().getFirst();
    }

    @Override
    public void frequency(int frequency) {
        this.leafNode().frequency(frequency);
        this.setChanged();
        this.sendBlockUpdated();
    }

    @Override
    public int frequency() {
        return this.leafNode().frequency();
    }
}
