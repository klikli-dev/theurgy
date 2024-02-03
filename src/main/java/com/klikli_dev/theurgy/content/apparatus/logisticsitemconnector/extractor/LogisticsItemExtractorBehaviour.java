// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBehaviour extends ExtractorNodeBehaviour<IItemHandler, @Nullable Direction> {
    public LogisticsItemExtractorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.ItemHandler.BLOCK);
    }

    @Override
    protected boolean isValidInsertTarget(LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNode, BlockCapabilityCache<IItemHandler, @Nullable Direction> capability) {
        //any target we get is guaranteed to exist and have an item capability, so we just return true here.
        return true;
    }

    public void tickServer(){
        //TODO: extraction logic
    }
}
