// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlockEntity;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBlock extends LogisticsItemConnectorBlock {

    public static final MapCodec<LogisticsItemExtractorBlock> CODEC = simpleCodec(LogisticsItemExtractorBlock::new);

    public LogisticsItemExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    //TODO: get targets on place using the updateCustomBlockEntityTag method
    //      might also be possible in the onplacedby
    //      the first option has the advantage of also working for future logistics blocks that use the selector system
    //      in both cases we simply place the targets in the "BlockEntityTag" and let the entity load them

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if(!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;


        if(!pLevel.isClientSide){
            //TODO: needs to send packet that does outlining
            //maybe a generic one?

            if(pLevel.getBlockEntity(pPos) instanceof LogisticsItemExtractorBlockEntity blockEntity){
                var targets = blockEntity.leafNode().insertTargets();
                for(var target : targets){
                    Outliner.get().showAABB(target, Shapes.block().bounds().move(target.pos()), 20 * 5)
                            .colored(0xFFFFF00)
                            .lineWidth(1 / 16f);
                }
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.LOGISTICS_ITEM_EXTRACTOR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            //TODO: does not tick!
            //it seems an inserter ticker is registered? o.O
            if (t instanceof LogisticsItemExtractorBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
