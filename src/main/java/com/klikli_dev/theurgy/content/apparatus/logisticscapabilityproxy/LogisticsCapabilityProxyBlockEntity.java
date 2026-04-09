// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock;
import com.klikli_dev.theurgy.content.behaviour.logistics.LogisticsNode;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LogisticsCapabilityProxyBlockEntity extends BlockEntity implements LogisticsNode {

    protected int roundRobinIndex;

    public LogisticsCapabilityProxyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_CAPABILITY_PROXY.get(), pPos, pBlockState);
    }

    public List<BlockPos> getConnectedProbes() {
        if (this.level == null || this.level.isClientSide()) {
            return List.of();
        }

        var network = Logistics.get().getNetwork(GlobalPos.of(this.level.dimension(), this.getBlockPos()));
        if (network == null) {
            return List.of();
        }

        List<BlockPos> probes = new ArrayList<>();
        for (var nodePos : network.nodes()) {
            if (nodePos.dimension() == this.level.dimension()) {
                var state = this.level.getBlockState(nodePos.pos());
                if (state.is(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get())) {
                    probes.add(nodePos.pos());
                }
            }
        }
        return probes;
    }

    public <T> @Nullable T pickLinkedProbe(Function<BlockPos, @Nullable T> resolver) {
        var probes = this.getConnectedProbes();
        if (probes.isEmpty()) {
            return null;
        }

        int startIndex = Math.floorMod(this.roundRobinIndex, probes.size());
        for (int offset = 0; offset < probes.size(); offset++) {
            int index = (startIndex + offset) % probes.size();
            var probePos = probes.get(index);
            var resolved = resolver.apply(probePos);
            if (resolved != null) {
                this.roundRobinIndex = (index + 1) % probes.size();
                return resolved;
            }
        }

        return null;
    }

    public <T, C> @Nullable T resolveSidedCapability(BlockCapability<T, C> capability) {
        return this.pickLinkedProbe(probePos -> {
            if (this.level == null) return null;
            return LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, probePos, (BlockCapability<T, @Nullable Direction>) capability);
        });
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.level == null || this.level.isClientSide()) {
            return List.of();
        }

        List<Pair<BlockPos, Integer>> result = new ArrayList<>();
        for (var probe : this.getConnectedProbes()) {
            result.add(Pair.of(probe, 0x00FFFF));

            var target = LogisticsCapabilityProbeBlock.getTarget(this.level, probe);
            if (target != null) {
                result.add(Pair.of(target.pos(), 0x00FF00));
            }
        }

        return result;
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
    }

    public void writeNetwork(ValueOutput output) {
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        this.writeNetwork(output);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.readNetwork(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            var removedWires = Wires.get(this.level).removeWiresFor(pPos);
            Block.popResource(this.level, pPos, new net.minecraft.world.item.ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));
            Logistics.get().remove(GlobalPos.of(this.level.dimension(), pPos));
        }
    }
}
