package com.klikli_dev.theurgy.block.calcinationoven;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CalcinationOvenBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 25, 14);

    public CalcinationOvenBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof CalcinationOvenBlockEntity calcinationOven) {
            //if we click with an empty hand, first take out output, if no output is available, then input
            //if we click with an item in hand, try to insert

            ItemStack stackInHand = pPlayer.getItemInHand(pHand);
            var outputStack = calcinationOven.outputInventory.getStackInSlot(0);
            var inputStack = calcinationOven.inputInventory.getStackInSlot(0);
            if (stackInHand.isEmpty()) {
                if (!outputStack.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(outputStack);
                    calcinationOven.outputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                } else if (!inputStack.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(inputStack);
                    calcinationOven.inputInventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            } else {
                //we use inventory here because it checks for valid insert item :)
                var remainder = calcinationOven.inventory.insertItem(0, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                return remainder.getCount() != stackInHand.getCount() ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.CALCINATION_OVEN.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof CalcinationOvenBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
