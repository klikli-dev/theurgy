package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemInserterBlock extends LogisticsItemConnectorBlock implements EntityBlock {

    public static final MapCodec<LogisticsItemInserterBlock> CODEC = simpleCodec(LogisticsItemInserterBlock::new);

    public LogisticsItemInserterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.LOGISTICS_ITEM_INSERTER.get().create(pPos, pState);
    }
}
