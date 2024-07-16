// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.content.item.filter.FilterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
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

    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.writeNetwork(pTag, pRegistries);
    }

    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.readNetwork(pTag, pRegistries);
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("filter", this.filter.serializeNBT(pRegistries));
    }

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.filter(Filter.of(pRegistries, pTag.getCompound("filter")));
    }

    public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if (this.filter().isEmpty()) {
            //if we have an empty filter, we can try to set one from the item in hand.

            //if the item is hand is empty, we pass back to other interaction behaviours
            if (pStack.isEmpty() || !(pStack.getItem() instanceof FilterItem))
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            var filter = Filter.of(pPlayer.registryAccess(), pStack.copyWithCount(1));

            if (!filter.isEmpty()) {
                this.filter(filter);
                pStack.consume(1, pPlayer);
            }

            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
        } else if (pStack.isEmpty()) {
            //if we have a filter and an empty hand we take the filter
            var stack = this.filter().item().copy();

            this.filter(Filter.empty());

            ItemHandlerHelper.giveItemToPlayer(pPlayer, stack);

            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!this.filter.isEmpty()) {
            var stack = this.filter().item().copy();

            this.filter(Filter.empty(), false);

            Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), stack);
        }
    }
}
