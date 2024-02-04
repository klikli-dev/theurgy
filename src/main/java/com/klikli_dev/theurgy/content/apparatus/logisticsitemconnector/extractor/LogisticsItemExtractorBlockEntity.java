// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter.LogisticsItemInserterBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class LogisticsItemExtractorBlockEntity extends LogisticsItemConnectorBlockEntity {

    public LogisticsItemExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), pPos, pBlockState);
        this.leafNodeBehaviour =  new LogisticsItemExtractorBehaviour(this);
    }

    @Override
    public LogisticsItemExtractorBehaviour leafNode() {
        return (LogisticsItemExtractorBehaviour) this.leafNodeBehaviour;
    }

    public void tickServer(){
        this.leafNode().tickServer();
    }
}
