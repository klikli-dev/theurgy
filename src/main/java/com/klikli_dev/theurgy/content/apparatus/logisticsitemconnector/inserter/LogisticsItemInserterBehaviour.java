package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LogisticsItemInserterBehaviour extends InserterNodeBehaviour<IItemHandler, @Nullable Direction> {
    public LogisticsItemInserterBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.ItemHandler.BLOCK);
    }

    @Override
    protected @Nullable Direction getTargetContext(BlockPos targetPos) {
        //TODO: geht the direction from the blockstate or from the override
        return null;
    }
}
