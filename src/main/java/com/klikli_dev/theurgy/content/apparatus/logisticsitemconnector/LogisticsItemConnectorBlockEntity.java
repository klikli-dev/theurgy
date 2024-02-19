// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class LogisticsItemConnectorBlockEntity extends BlockEntity implements HasLeafNodeBehaviour<IItemHandler, @Nullable Direction>, TargetDirectionSetter, EnabledSetter {

    protected LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNodeBehaviour;

    protected LogisticsItemConnectorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (!this.level.isClientSide) {
            this.leafNode().onLoad();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (!this.level.isClientSide) {
            this.leafNode().onChunkUnload();
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.leafNode().load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.leafNode().saveAdditional(pTag);
    }

    @Override
    public LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNode() {
        return this.leafNodeBehaviour;
    }
}
