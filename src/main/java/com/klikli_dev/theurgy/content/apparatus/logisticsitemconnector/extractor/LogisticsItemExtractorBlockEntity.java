// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.datafixers.util.Pair;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LogisticsItemExtractorBlockEntity extends LogisticsItemConnectorBlockEntity {

    public LogisticsItemExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), pPos, pBlockState);
        this.leafNodeBehaviour = new LogisticsItemExtractorBehaviour(this);
    }

    @Override
    public LogisticsItemExtractorBehaviour leafNode() {
        return (LogisticsItemExtractorBehaviour) this.leafNodeBehaviour;
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.level.isClientSide())
            return List.of();

        var result = super.getStatusHighlights();

        var targets = this.leafNode().insertTargets();
        for (var target : targets) {
            result.add(Pair.of(target.pos(), 0xFFFFF00));
        }

        return result;
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
        this.leafNode().readNetwork(tag, pRegistries);
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.leafNode().writeNetwork(tag, pRegistries);;
    }

    protected void sendBlockUpdated() {
        if (this.getLevel() != null && !this.getLevel().isClientSide)
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        this.leafNode().tickServer();
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
        return this.leafNode().targets().isEmpty() ? this.getBlockPos() : this.leafNode().targets().get(0);
    }
}
