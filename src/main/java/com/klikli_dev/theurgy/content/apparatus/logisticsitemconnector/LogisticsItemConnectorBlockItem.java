// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemConnectorBlockItem extends BlockItem {
    public LogisticsItemConnectorBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        var direction = pState.getValue(BlockStateProperties.FACING).getOpposite();

        var blockEntityTag = new CompoundTag();

        var list = new ListTag();
        var target = pPos.relative(direction);
        list.add(LongTag.valueOf(target.asLong()));
        blockEntityTag.put("targets", list);

        pStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));

        //super will handle copying the nbt to the block entity and loading it there
        return super.updateCustomBlockEntityTag(pPos, pLevel, pPlayer, pStack, pState);
    }
}
