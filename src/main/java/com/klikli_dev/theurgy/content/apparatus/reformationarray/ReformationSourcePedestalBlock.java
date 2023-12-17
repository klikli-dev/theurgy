package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.content.behaviour.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.OneSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class ReformationSourcePedestalBlock extends Block implements EntityBlock {
    private final ItemHandlerBehaviour itemHandlerBehaviour;

    public ReformationSourcePedestalBlock(Properties pProperties) {
        super(pProperties);

        this.itemHandlerBehaviour = new OneSlotItemHandlerBehaviour();
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (this.itemHandlerBehaviour.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof ReformationSourcePedestalBlockEntity blockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(blockEntity.inputInventory));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
//                if(lvl.getGameTime() % 40 == 0)
                if(lvl.getRandom().nextFloat() < 0.07f)
                    lvl.addParticle(GlowParticleProvider.createOptions(
                            ParticleColor.fromInt(0xFF00FF),
                            0.5f,
                            0.75f,
                            200), pos.getX() + 0.5f, pos.getY() + 1, pos.getZ()+ 0.5f, 0, 0, 0);
            };
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.REFORMATION_SOURCE_PEDESTAL.get().create(pPos, pState);
    }
}
