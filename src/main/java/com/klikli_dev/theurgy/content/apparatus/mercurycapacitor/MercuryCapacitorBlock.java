// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycapacitor;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;


public class MercuryCapacitorBlock extends Block implements EntityBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public MercuryCapacitorBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true));
    }

    public static int getBlockColor(@NotNull BlockState pState, @NotNull BlockAndTintGetter pLevel, @NotNull BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof MercuryCapacitorBlockEntity blockEntity) {
            var fillLevel = blockEntity.mercuryFluxStorage.getEnergyStored() / (float) blockEntity.mercuryFluxStorage.getMaxEnergyStored();

            //if empty we return white, if full we should return blue: 0x0000FF
            return getColorFromFillLevel(fillLevel);
        }

        return ARGB.opaque(0xFFFFFF);
    }

    public static int getItemColor(ItemStack pStack, int pTintIndex) {
        if (pStack.has(DataComponentRegistry.MERCURY_FLUX_STORAGE.get())) {
            var fillLevel = pStack.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get()) / (float) MercuryCapacitorBlockEntity.CAPACITY;
            return getColorFromFillLevel(fillLevel);
        }

        return ARGB.opaque(0xFFFFFF);
    }

    public static int getColorFromFillLevel(float fillLevel) {
        int r = (int) ((1 - fillLevel) * 255.0);
        int g = (int) ((1 - fillLevel) * 255.0);
        int b = (int) (255.0); // b always stays the same

        // Ensure the values are within 0-255 range
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // Combine the R, G, B values into a RGB integer
        int rgb = (r << 16) | (g << 8) | b;

        return ARGB.opaque(rgb);
    }

    public static int getParticleColorFromFillLevel(float fillLevel) {
        // Map fill level to a color gradient:
        // Empty (0%) -> White: 0xFFFFFF
        // Full (100%) -> Blue: 0x0000FF
        
        // Interpolate from white (255, 255, 255) to blue (0, 0, 255)
        int r = (int) ((1 - fillLevel) * 255.0);
        int g = (int) ((1 - fillLevel) * 255.0);
        int b = 255;
        
        // Ensure the values are within 0-255 range
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // Combine the R, G, B values into a RGB integer with full opacity
        return ARGB.opaque((r << 16) | (g << 8) | b);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        //The capacitor has no item interaction
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(ENABLED, true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, @Nullable Orientation pOrientation, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pOrientation, pIsMoving);

        this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_INVISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
        }
    }

    private void checkPoweredState(Level pLevel, BlockPos pPos, BlockState pState, int pFlags) {
        boolean enabled = !pLevel.hasNeighborSignal(pPos);
        if (enabled != pState.getValue(ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(ENABLED, enabled), pFlags);
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.MERCURY_CAPACITOR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof MercuryCapacitorBlockEntity blockEntity) {
                    blockEntity.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof MercuryCapacitorBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }

    public static class ItemTintSource implements net.minecraft.client.color.item.ItemTintSource {

        public static final ItemTintSource INSTANCE = new ItemTintSource();
        public static final MapCodec<ItemTintSource> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public int calculate(@NonNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
            return getItemColor(itemStack, 0);
        }

        @Override
        public MapCodec<? extends net.minecraft.client.color.item.ItemTintSource> type() {
            return MAP_CODEC;
        }
    }

    public static class BlockTintSource implements net.minecraft.client.color.block.BlockTintSource {

        @Override
        public int color(@NonNull BlockState blockState) {
            return ARGB.opaque(0xFFFFFF);
        }

        @Override
        public int colorInWorld(@NonNull BlockState state, @NonNull BlockAndTintGetter level, @NonNull BlockPos pos) {
            return getBlockColor(state, level, pos);
        }
    }
}
