// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.codedefinedgui.filter.core.FilterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class FilterBehaviour {

    protected BlockEntity blockEntity;
    protected Consumer<Filter> callback;

    protected Filter filter;

    public FilterBehaviour(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.callback = (stack) -> {
        };
        this.filter = Filter.empty();
    }

    public Filter filter() {
        return this.filter;
    }

    public void filter(Filter filter) {
        this.filter(filter, true);
    }

    public void filter(Filter filter, boolean notify) {
        this.filter = filter;
        if (notify) {
            this.callback.accept(filter);
        }
    }

    public FilterBehaviour withCallback(Consumer<Filter> callback) {
        this.callback = callback;
        return this;
    }

    public void saveAdditional(ValueOutput output) {
        this.writeNetwork(output);
    }

    public void loadAdditional(ValueInput input) {
        this.readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        output.store("filter", ItemStack.OPTIONAL_CODEC, this.filter.item());
    }

    public void readNetwork(ValueInput input) {
        this.filter(Filter.of(input.lookup(), input.read("filter", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY)));
    }

    public @NotNull InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if (pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        if (this.filter().isEmpty()) {
            //if we have an empty filter, we can try to set one from the item in hand.

            //if the item is hand is empty, we pass back to other interaction behaviours
            if (pStack.isEmpty() || !(pStack.getItem() instanceof FilterItem))
                return InteractionResult.PASS;

            var filter = Filter.of(pPlayer.registryAccess(), pStack.copyWithCount(1));

            if (!filter.isEmpty()) {
                this.filter(filter);
                pStack.consume(1, pPlayer);
            }

            return InteractionResult.SUCCESS;
        } else if (pStack.isEmpty() && pPlayer.isShiftKeyDown()) {
            //if we have a filter and an empty hand we take the filter
            var stack = this.filter().item().copy();

            this.filter(Filter.empty());

            pPlayer.getInventory().placeItemBackInInventory(stack);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!this.filter.isEmpty()) {
            var stack = this.filter().item().copy();

            this.filter(Filter.empty(), false);

            Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), stack);
        }
    }
}
