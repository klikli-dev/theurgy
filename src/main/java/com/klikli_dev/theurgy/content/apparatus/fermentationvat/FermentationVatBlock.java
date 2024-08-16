// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.fluidhandler.FluidHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.fluidhandler.OneTankFluidHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.interaction.InteractionBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.DynamicOneOutputSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.HasItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.redstone.VatAnalogSignalOutputBehaviour;
import com.klikli_dev.theurgy.content.behaviour.redstone.VatRedstoneChangeOpenCloseLidBehaviour;
import com.klikli_dev.theurgy.content.behaviour.redstone.VatRedstoneHasOutputBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class FermentationVatBlock extends Block implements EntityBlock, HasItemHandlerBehaviour<DynamicOneOutputSlotItemHandlerBehaviour> {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty HAS_OUTPUT = BooleanProperty.create("has_output");

    protected DynamicOneOutputSlotItemHandlerBehaviour itemHandlerBehaviour;
    protected FluidHandlerBehaviour fluidHandlerBehaviour;
    protected InteractionBehaviour interactionBehaviour;

    protected VatRedstoneChangeOpenCloseLidBehaviour<FermentationRecipe> redstoneInputBehaviour;
    protected VatRedstoneHasOutputBehaviour redstoneOutputBehaviour;
    protected VatAnalogSignalOutputBehaviour analogSignalOutputBehaviour;

    public FermentationVatBlock(Properties pProperties) {
        super(pProperties);

        this.itemHandlerBehaviour = new DynamicOneOutputSlotItemHandlerBehaviour();
        this.fluidHandlerBehaviour = new OneTankFluidHandlerBehaviour();
        this.interactionBehaviour = new FermentationVatInteractionBehaviour();
        this.redstoneInputBehaviour = new VatRedstoneChangeOpenCloseLidBehaviour<>();
        this.redstoneOutputBehaviour = new VatRedstoneHasOutputBehaviour();
        this.analogSignalOutputBehaviour = new VatAnalogSignalOutputBehaviour();

        this.registerDefaultState(this.stateDefinition.any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, true).setValue(HAS_OUTPUT, false));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        //We do not check for client side because
        // a) returning success causes https://github.com/klikli-dev/theurgy/issues/158
        // b) client side BEs are separate objects even in SP, so modification in our behaviours is safe

        var interactionResult = this.interactionBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        if (interactionResult != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) {
            return interactionResult;
        }

        if (this.fluidHandlerBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult) == ItemInteractionResult.SUCCESS) {
            return ItemInteractionResult.SUCCESS;
        }

        if (this.itemHandlerBehaviour.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult) == ItemInteractionResult.SUCCESS) {
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        this.redstoneInputBehaviour.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean isSignalSource(@NotNull BlockState pState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getSignal(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull Direction pDirection) {
        return this.redstoneOutputBehaviour.getSignal(pState, pLevel, pPos, pDirection);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState pBlockState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        return this.analogSignalOutputBehaviour.getAnalogOutputSignal(pBlockState, pLevel, pPos);
    }

    @Override
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof FermentationVatBlockEntity blockEntity) {
                for (int i = 0; i < blockEntity.storageBehaviour.inventory.getSlots(); i++) {
                    Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), blockEntity.storageBehaviour.inventory.getStackInSlot(i));
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState pState, @NotNull PathComputationType pPathComputationType) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(HORIZONTAL_FACING, pRotation.rotate(pState.getValue(HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING, OPEN, HAS_OUTPUT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, pContext.getHorizontalDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityRegistry.FERMENTATION_VAT.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof FermentationVatBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }

    @Override
    public DynamicOneOutputSlotItemHandlerBehaviour itemHandlerBehaviour() {
        return this.itemHandlerBehaviour;
    }
}
