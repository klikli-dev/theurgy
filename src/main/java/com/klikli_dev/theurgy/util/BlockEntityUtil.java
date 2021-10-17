/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlockEntityUtil {

    /**
     * Creates the item entity with nbt from the block entity.
     * Default pickup delay is set.
     *
     * @param itemStack   the stack to drop.
     * @param blockEntity the block entity to get nbt from.
     * @return the item entity.
     */
    public static ItemEntity getDroppedItemWithNbt(ItemStack itemStack, BlockEntity blockEntity) {
        CompoundTag CompoundTag = blockEntity.serializeNBT();
        if (!CompoundTag.isEmpty()) {
            itemStack.addTagElement("BlockEntityTag", CompoundTag);
        }
        ItemEntity itementity =
                new ItemEntity(blockEntity.getLevel(), blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(),
                        blockEntity.getBlockPos().getZ(), itemStack);
        itementity.setDefaultPickUpDelay();
        return itementity;
    }

    /**
     * Handles the common use case of dropping self with block entity nbt on block change during replace.
     *
     * @param block    the current block.
     * @param state    the old state.
     * @param level    the level
     * @param pos      the position.
     * @param newState the new state
     */
    public static void onBlockChangeDropWithNbt(Block block, BlockState state, Level level, BlockPos pos,
                                                BlockState newState) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    level.addFreshEntity(BlockEntityUtil.getDroppedItemWithNbt(new ItemStack(block), blockEntity));
                }
            }
            level.updateNeighbourForOutputSignal(pos, block);
        }
    }

    /**
     * Handles the common use case of giving self as item with block entity nbt.
     *
     * @param block the current block.
     * @param level the level
     * @param pos   the position.
     */
    public static ItemStack getItemWithNbt(Block block, BlockGetter level, BlockPos pos) {
        ItemStack itemStack = new ItemStack(block);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        CompoundTag CompoundTag = blockEntity.serializeNBT();
        if (!CompoundTag.isEmpty()) {
            itemStack.addTagElement("BlockEntityTag", CompoundTag);
        }

        return itemStack;
    }
}
