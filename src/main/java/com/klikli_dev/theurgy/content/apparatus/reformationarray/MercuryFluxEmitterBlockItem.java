// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MercuryFluxEmitterBlockItem extends BlockItem {
    public MercuryFluxEmitterBlockItem(Block pBlock, Properties pProperties) {
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
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    public MercuryFluxEmitterSelectionBehaviour getSelectionBehaviour() {
        return (MercuryFluxEmitterSelectionBehaviour) BlockRegistry.MERCURY_FLUX_EMITTER.get().selectionBehaviour();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, tooltipFlag);

        if (stack.has(DataComponentRegistry.MERCURY_FLUX_STORAGE.get())) {
            int stored = stack.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get());
            int capacity = MercuryFluxEmitterBlockEntity.CAPACITY;

            tooltipAdder.accept(Component.translatable(
                    TheurgyConstants.I18n.JEI.MERCURY_FLUX,
                    stored + " / " + capacity
            ).withStyle(ChatFormatting.GRAY));
        }
    }
}
