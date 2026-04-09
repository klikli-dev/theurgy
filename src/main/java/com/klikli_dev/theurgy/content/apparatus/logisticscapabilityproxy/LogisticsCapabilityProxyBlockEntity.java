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
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class LogisticsCapabilityProxyBlockEntity extends BlockEntity implements LogisticsNode {

    protected int roundRobinIndex;

    public LogisticsCapabilityProxyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_CAPABILITY_PROXY.get(), pPos, pBlockState);
    }

    public List<BlockPos> getConnectedProbes() {
        if (this.level == null) {
            return List.of();
        }

        var network = Logistics.get().getNetwork(GlobalPos.of(this.level.dimension(), this.getBlockPos()));
        if (network == null) {
            return List.of();
        }

        List<BlockPos> probes = new ArrayList<>();
        for (var nodePos : network.nodes()) {
            if (nodePos.dimension().equals(this.level.dimension())) {
                var state = this.level.getBlockState(nodePos.pos());
                if (state.is(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get())) {
                    probes.add(nodePos.pos());
                }
            }
        }

        probes.sort(Comparator.comparingLong(BlockPos::asLong));
        return probes;
    }

    public <T> @Nullable T pickConnectedProbe(Function<BlockPos, @Nullable T> resolver) {
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

    public <T> @Nullable T resolveProbeCapability(BlockCapability<T, @Nullable Direction> capability) {
        if (this.level == null) {
            return null;
        }

        return this.pickConnectedProbe(probePos -> LogisticsCapabilityProbeBlock.resolveSidedCapability(this.level, probePos, capability));
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
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            var removedWires = Wires.get(this.level).removeWiresFor(pPos);
            Block.popResource(this.level, pPos, new net.minecraft.world.item.ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));
            Logistics.get().remove(GlobalPos.of(this.level.dimension(), pPos));
        }
    }
}
