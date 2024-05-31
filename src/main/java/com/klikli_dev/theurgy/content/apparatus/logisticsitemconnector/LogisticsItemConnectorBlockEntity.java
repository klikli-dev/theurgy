// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class LogisticsItemConnectorBlockEntity extends BlockEntity implements HasLeafNodeBehaviour<IItemHandler, @Nullable Direction>, TargetDirectionSetter, EnabledSetter {

    protected LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNodeBehaviour;

    protected LogisticsItemConnectorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.level.isClientSide())
            return List.of();

        List<Pair<BlockPos, Integer>> result = new ArrayList<>();

        var targets = this.leafNode().targets();
        for (var target : targets) {
            result.add(Pair.of(target, 0x00FFFF));
        }

        //also show network?
//        var connected = Logistics.get().getNetwork(GlobalPos.of(this.level.dimension(), this.getBlockPos()));
//        if (connected != null) {
//            var shape = Shapes.block();
//            for (var block : connected.nodes()) {
//                if (block.dimension().equals(this.level.dimension())) {
//                    Outliner.get().showAABB(block, shape.bounds()
//                                    .move(block.pos()), 20 * 5)
//                            .colored(0x00FF00)
//                            .lineWidth(1 / 16f);
//                }
//            }
//        }

        return result;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (!this.level.isClientSide) {
            this.leafNode().onLoad();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (!this.level.isClientSide) {
            this.leafNode().onChunkUnload();
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.leafNode().loadAdditional(pTag, pRegistries);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        this.leafNode().saveAdditional(pTag, pRegistries);
    }

    @Override
    public LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNode() {
        return this.leafNodeBehaviour;
    }
}
