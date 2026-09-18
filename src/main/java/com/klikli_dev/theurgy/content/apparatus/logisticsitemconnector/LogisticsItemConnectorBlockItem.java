// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class LogisticsItemConnectorBlockItem extends BlockItem {
    public LogisticsItemConnectorBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        var result = super.place(context);
        if (result == InteractionResult.SUCCESS) {
            var level = context.getLevel();
            var pos = context.getClickedPos();
            var state = level.getBlockState(pos);
            if (state.is(this.getBlock()) && level.getBlockEntity(pos) instanceof LogisticsItemConnectorBlockEntity connector) {
                var direction = state.getValue(BlockStateProperties.FACING).getOpposite();
                connector.leafNode().targets().clear();
                connector.leafNode().targets().add(pos.relative(direction));
                connector.setChanged();
            }
        }
        return result;
    }
}
