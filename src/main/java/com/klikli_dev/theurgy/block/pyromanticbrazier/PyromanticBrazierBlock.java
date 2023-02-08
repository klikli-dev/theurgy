package com.klikli_dev.theurgy.block.pyromanticbrazier;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PyromanticBrazierBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public PyromanticBrazierBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof PyromanticBrazierBlockEntity brazier) {
            var stack = brazier.inventory.getStackInSlot(0);
            var stackInHand = pPlayer.getItemInHand(pHand);
            var brazierHasNonFuel = !stack.isEmpty() && brazier.getBurnDuration(stack) == 0;

            if (stackInHand.isEmpty() || brazierHasNonFuel) {
                //Click with empty hand -> remove fuel from block entity
                //Click with any hand but non fuel item in block entity, remove it -> clean out empty buckets etc
                if (!stack.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(stack);
                    brazier.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            } else {
                var remainder = brazier.inventory.insertItem(0, stackInHand, false);
                pPlayer.setItemInHand(pHand, remainder);
                return remainder.getCount() != stackInHand.getCount() ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
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

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
            }

            if (pRandom.nextInt(5) == 0) {
                for (int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                    pLevel.addParticle(ParticleTypes.LAVA, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, pRandom.nextFloat() / 2.0F, 5.0E-5D, pRandom.nextFloat() / 2.0F);
                }
            }
        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.PYROMANTIC_BRAZIER.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return pState.getValue(LIT) ?
                    (lvl, pos, blockState, t) -> {
                        if (t instanceof PyromanticBrazierBlockEntity blockEntity) {
                            blockEntity.tickClient();
                        }
                    } : null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof PyromanticBrazierBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
