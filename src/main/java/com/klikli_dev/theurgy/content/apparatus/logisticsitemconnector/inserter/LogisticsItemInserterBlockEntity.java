// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LogisticsItemInserterBlockEntity extends LogisticsItemConnectorBlockEntity {

    public LogisticsItemInserterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_ITEM_INSERTER.get(), pPos, pBlockState);
        this.leafNodeBehaviour = new LogisticsItemInserterBehaviour(this);
    }

    @Override
    public LogisticsItemInserterBehaviour leafNode() {
        return (LogisticsItemInserterBehaviour) this.leafNodeBehaviour;
    }

    @Override
    public void enabled(boolean enabled) {
        this.leafNode().enabled(enabled);
    }

    @Override
    public boolean enabled() {
        return this.leafNode().enabled();
    }

    @Override
    public void targetDirection(Direction direction) {
        this.leafNode().directionOverride(direction);
    }

    @Override
    public Direction targetDirection() {
        return this.leafNode().directionOverride();
    }

    @Override
    public BlockPos targetPos() {
        return this.leafNode().targets().isEmpty() ? this.getBlockPos() : this.leafNode().targets().get(0);
    }
}
