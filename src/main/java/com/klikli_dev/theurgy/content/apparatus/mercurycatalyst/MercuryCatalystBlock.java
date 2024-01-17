// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.itemhandler.ItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.itemhandler.OneSlotItemHandlerBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class MercuryCatalystBlock extends Block implements EntityBlock {

    protected ItemHandlerBehaviour itemHandlerBehaviour;

    public MercuryCatalystBlock(Properties pProperties) {
        super(pProperties);

        this.itemHandlerBehaviour = new OneSlotItemHandlerBehaviour();
    }

    public static int getBlockColor(BlockState pState, BlockAndTintGetter pLevel, BlockPos pPos, int pTintIndex) {
        //tint index 0 is default and thus used by particles
        //our model has 1
        if (pTintIndex != 0 && pLevel != null && pPos != null && pLevel.getBlockEntity(pPos) instanceof MercuryCatalystBlockEntity blockEntity) {
            var fillLevel = blockEntity.mercuryFluxStorage.getEnergyStored() / (float) blockEntity.mercuryFluxStorage.getMaxEnergyStored();

            //if empty we return white, if full we should return blue: 0x0000FF
            return getColorFromFillLevel(fillLevel);
        }

        return 0xFFFFFF;
    }

    public static int getItemColor(ItemStack pStack, int pTintIndex) {
        if (pStack.hasTag() && pStack.getTag().contains("BlockEntityTag")) {
            var fillLevel = pStack.getTag().getCompound("BlockEntityTag").getInt("mercuryFluxStorage") / (float) MercuryCatalystBlockEntity.CAPACITY;
            return getColorFromFillLevel(fillLevel);
        }

        return 0xFFFFFF;
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

        return rgb;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //We do not check for client side because
        // a) returning success causes https://github.com/klikli-dev/theurgy/issues/158
        // b) client side BEs are separate objects even in SP, so modification in our behaviours is safe

        if (this.itemHandlerBehaviour.useItemHandler(pState, pLevel, pPos, pPlayer, pHand, pHit) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.MERCURY_CATALYST.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof MercuryCatalystBlockEntity blockEntity) {
                blockEntity.tickServer();
            }
        };
    }
}
