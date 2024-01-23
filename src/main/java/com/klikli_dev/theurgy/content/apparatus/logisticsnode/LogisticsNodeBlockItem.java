// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnode;

import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageRequestCaloricFluxEmitterSelection;
import com.klikli_dev.theurgy.network.messages.MessageRequestLogisticsNodeSelection;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LogisticsNodeBlockItem extends BlockItem {
    public LogisticsNodeBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (this.getSelectionBehaviour().canCreate(level, pos, level.getBlockState(pos)))
            return InteractionResult.SUCCESS;
        return super.useOn(ctx);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
            Networking.sendTo(serverPlayer, new MessageRequestLogisticsNodeSelection(pos));

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !this.getSelectionBehaviour().canCreate(level, pos, level.getBlockState(pos));
    }

    public LogisticsNodeSelectionBehaviour getSelectionBehaviour() {
        return BlockRegistry.LOGISTICS_NODE.get().selectionBehaviour();
    }
}
