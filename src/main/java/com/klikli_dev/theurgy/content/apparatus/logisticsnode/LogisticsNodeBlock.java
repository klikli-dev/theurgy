package com.klikli_dev.theurgy.content.apparatus.logisticsnode;

import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;

public class LogisticsNodeBlock extends Block {
    protected LogisticsNodeSelectionBehaviour selectionBehaviour;

    public LogisticsNodeBlock(Properties properties) {
        super(properties);
        this.selectionBehaviour = new LogisticsNodeSelectionBehaviour();
    }

    public LogisticsNodeSelectionBehaviour selectionBehaviour() {
        return this.selectionBehaviour;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //TODO: this whole block probably should not exist and be only for debug :)
        if(!pPlayer.getItemInHand(pHand).isEmpty())
            return InteractionResult.PASS;

        //TODO: selection behaviour should be an interface -> somehow automate tick/right/left click events
        //      e.g. lazily make list of fitting blocks and cache it

        if(!pLevel.isClientSide){
            //TODO: needs to send packet that does outlining
            //maybe a generic one?

            var connected = Logistics.get().getNetwork(GlobalPos.of(pLevel.dimension(), pPos));
            var shape = Shapes.block();
            for(var block : connected.nodes()){
                if(block.dimension().equals(pLevel.dimension())){
                    Outliner.get().showAABB(block, shape.bounds()
                                    .move(block.pos()), 20 * 5)
                            .colored(0x00FF00)
                            .lineWidth(1 / 16f);
                }
            }

        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        if(!pLevel.isClientSide){
            Logistics.get().remove(GlobalPos.of(pLevel.dimension(), pPos));
        }
    }
}
