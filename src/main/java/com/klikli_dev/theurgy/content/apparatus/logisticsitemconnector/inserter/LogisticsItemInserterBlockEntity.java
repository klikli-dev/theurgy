package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LogisticsItemInserterBlockEntity extends LogisticsItemConnectorBlockEntity {

    public LogisticsItemInserterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_ITEM_INSERTER.get(), pPos, pBlockState);
        this.leafNodeBehaviour =  new LogisticsItemInserterBehaviour(this);
    }

    @Override
    public LogisticsItemInserterBehaviour leafNode() {
        return (LogisticsItemInserterBehaviour) this.leafNodeBehaviour;
    }
}
