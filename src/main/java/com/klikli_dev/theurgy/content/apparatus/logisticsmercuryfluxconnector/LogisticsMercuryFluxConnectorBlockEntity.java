// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.FrequencySetter;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.NetworkTagHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LogisticsMercuryFluxConnectorBlockEntity extends BlockEntity implements HasLeafNodeBehaviour<MercuryFluxHandler, @Nullable Direction>, TargetDirectionSetter, EnabledSetter, FrequencySetter {

    protected LogisticsMercuryFluxConnectorBehaviour leafNodeBehaviour;

    public LogisticsMercuryFluxConnectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get(), pPos, pBlockState);
        this.leafNodeBehaviour = new LogisticsMercuryFluxConnectorBehaviour(this);
    }

    @Override
    public LogisticsMercuryFluxConnectorBehaviour leafNode() {
        return this.leafNodeBehaviour;
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return List.of();

        List<Pair<BlockPos, Integer>> result = new ArrayList<>();

        var targets = this.leafNode().targets();
        for (var target : targets) {
            result.add(Pair.of(target, 0x00FFFF));
        }

        return result;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.ensureAttachedTarget();
            this.leafNode().onLoad();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.leafNode().onChunkUnload();
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.leafNode().loadAdditional(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.leafNode().saveAdditional(output);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return NetworkTagHelper.write(pRegistries, this::writeNetwork);
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
    }

    public void writeNetwork(ValueOutput output) {
        this.leafNode().writeNetwork(output);
    }

    protected void ensureAttachedTarget() {
        if (!this.leafNode().targets().isEmpty()) {
            return;
        }

        var attachedPos = this.getBlockPos().relative(this.getBlockState().getValue(LogisticsItemConnectorBlock.FACING).getOpposite());
        this.leafNode().targets().add(attachedPos);
    }

    protected void sendBlockUpdated() {
        if (this.getLevel() != null && !this.getLevel().isClientSide())
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        var level = this.getLevel();
        if (level != null) {
            var removedWires = Wires.get(level).removeWiresFor(pPos);
            Block.popResource(level, pPos, new ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));

            this.leafNode().onDestroyed();
        }
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
