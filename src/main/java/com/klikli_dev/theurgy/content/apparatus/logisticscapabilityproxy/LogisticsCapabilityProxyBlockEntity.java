// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.content.behaviour.logistics.LogisticsNode;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

public class LogisticsCapabilityProxyBlockEntity extends BlockEntity implements LogisticsNode {

    protected List<BlockPos> linkedProbes = new ArrayList<>();
    protected int roundRobinIndex;

    public LogisticsCapabilityProxyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_CAPABILITY_PROXY.get(), pPos, pBlockState);
    }

    public List<BlockPos> linkedProbes() {
        return this.linkedProbes;
    }

    public void setLinkedProbes(List<BlockPos> linkedProbes) {
        var deduplicated = new LinkedHashSet<BlockPos>();
        for (var linkedProbe : linkedProbes) {
            if (linkedProbe == null || linkedProbe.equals(this.getBlockPos())) {
                continue;
            }

            if (this.level != null && this.level.isLoaded(linkedProbe) && this.level.getBlockState(linkedProbe).is(com.klikli_dev.theurgy.registry.BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get())) {
                deduplicated.add(linkedProbe.immutable());
            }
        }

        this.linkedProbes = new ArrayList<>(deduplicated);
        this.roundRobinIndex = 0;
        this.setChanged();

        if (this.level != null && !this.level.isClientSide()) {
            this.sendBlockUpdated();
            this.level.invalidateCapabilities(this.getBlockPos());
        }
    }

    public @Nullable BlockPos selectProbeForQuery() {
        if (this.linkedProbes.isEmpty()) {
            return null;
        }

        int index = Math.floorMod(this.roundRobinIndex, this.linkedProbes.size());
        this.roundRobinIndex = (index + 1) % this.linkedProbes.size();
        return this.linkedProbes.get(index);
    }

    public <T> @Nullable T pickLinkedProbe(Function<BlockPos, @Nullable T> resolver) {
        if (this.linkedProbes.isEmpty()) {
            return null;
        }

        int startIndex = Math.floorMod(this.roundRobinIndex, this.linkedProbes.size());
        for (int offset = 0; offset < this.linkedProbes.size(); offset++) {
            int index = (startIndex + offset) % this.linkedProbes.size();
            var probePos = this.linkedProbes.get(index);
            var resolved = resolver.apply(probePos);
            if (resolved != null) {
                this.roundRobinIndex = (index + 1) % this.linkedProbes.size();
                return resolved;
            }
        }

        return null;
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
        for (var linkedProbe : this.linkedProbes) {
            result.add(Pair.of(linkedProbe, 0x00FFFF));

            var target = com.klikli_dev.theurgy.content.apparatus.logisticscapabilityprobe.LogisticsCapabilityProbeBlock.getTarget(this.level, linkedProbe);
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
        this.linkedProbes = new ArrayList<>();
        for (long linkedProbe : input.read("linkedProbes", Codec.LONG.listOf()).orElse(List.of())) {
            this.linkedProbes.add(BlockPos.of(linkedProbe));
        }
    }

    public void writeNetwork(ValueOutput output) {
        output.store("linkedProbes", Codec.LONG.listOf(), this.linkedProbes.stream().map(BlockPos::asLong).toList());
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
