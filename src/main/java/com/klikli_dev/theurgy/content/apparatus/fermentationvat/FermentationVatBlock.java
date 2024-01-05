package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.*;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FermentationVatBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    protected ItemHandlerBehaviour itemHandlerBehaviour;
    protected FluidHandlerBehaviour fluidHandlerBehaviour;

    public FermentationVatBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.itemHandlerBehaviour = new DynamicOneOutputSlotItemHandlerBehaviour();
        //TODO: we need an item handler behaviour that supports one output and multiple input slots
        this.fluidHandlerBehaviour = new OneTankFluidHandlerBehaviour();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (this.fluidHandlerBehaviour.useFluidHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        if (this.itemHandlerBehaviour.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        //TODO: move that in an interaction handler
        //interaction with shift and empty hand opens/closes the vat
        if(pPlayer.isShiftKeyDown() && pPlayer.getMainHandItem().isEmpty()){

            var blockEntity = pLevel.getBlockEntity(pPos);
            if(!(blockEntity instanceof FermentationVatBlockEntity vat)){
                return InteractionResult.PASS;
            }

            var craftingBehaviour = vat.getCraftingBehaviour();

            var isOpen = pState.getValue(OPEN);

            if(isOpen){
                //we can only close if we have a valid recipe and can craft it
                var recipe = craftingBehaviour.getRecipe();
                if(recipe.isPresent() && craftingBehaviour.canCraft(recipe.get())){
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(OPEN, Boolean.FALSE));
                    return InteractionResult.SUCCESS;
                }
                //TODO: show message that we cannot close because we are missing ingredients
            } else {
                //when opening we stop processing (because we want to interrupt the crafting process)
                //we also set changed so that gets saved.
                pLevel.setBlockAndUpdate(pPos, pState.setValue(OPEN, Boolean.TRUE));
                craftingBehaviour.stopProcessing();
                vat.setChanged();
            }
        }


        return InteractionResult.PASS;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.FERMENTATION_VAT.get().create(pPos, pState);
    }
}
