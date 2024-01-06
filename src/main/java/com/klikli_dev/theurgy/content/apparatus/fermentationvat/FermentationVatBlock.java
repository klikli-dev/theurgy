package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.*;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class FermentationVatBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;


    protected ItemHandlerBehaviour itemHandlerBehaviour;
    protected FluidHandlerBehaviour fluidHandlerBehaviour;

    protected InteractionBehaviour interactionBehaviour;

    public FermentationVatBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.itemHandlerBehaviour = new DynamicOneOutputSlotItemHandlerBehaviour();
        this.fluidHandlerBehaviour = new OneTankFluidHandlerBehaviour();
        this.interactionBehaviour = new FermentationVatInteractionBehaviour();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var interactionResult = this.interactionBehaviour.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (interactionResult != InteractionResult.PASS) {
            return interactionResult;
        }

        if (this.fluidHandlerBehaviour.useFluidHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        if (this.itemHandlerBehaviour.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        //Closed = is processing
        //has signal -> should be processing -> close

        boolean hasSignal = pLevel.hasNeighborSignal(pPos);
        boolean wasOpen = pState.getValue(OPEN);
        if (hasSignal && wasOpen) {

            var blockEntity = pLevel.getBlockEntity(pPos);
            if (!(blockEntity instanceof HasCraftingBehaviour<?, ?, ?>))
                return;

            @SuppressWarnings("unchecked") var vat = (HasCraftingBehaviour<?, FermentationRecipe, ?>) blockEntity;

            var craftingBehaviour = vat.craftingBehaviour();

            var recipe = craftingBehaviour.getRecipe();
            if (recipe.isPresent() && craftingBehaviour.canCraft(recipe.get())) {
                pLevel.setBlock(pPos, pState.setValue(OPEN, false), Block.UPDATE_CLIENTS);
            }
        } else if (!hasSignal && !wasOpen) {
            pLevel.setBlock(pPos, pState.setValue(OPEN, true), Block.UPDATE_CLIENTS);
        }
    }


    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof FermentationVatBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof FermentationVatBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
