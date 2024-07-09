// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityBehaviour {
    protected BlockEntity blockEntity;

    public BlockEntityBehaviour(BlockEntity blockEntity){
        this.blockEntity = blockEntity;
    }

    public BlockEntity blockEntity(){
        return this.blockEntity;
    }

    protected Level level(){
        return this.blockEntity.getLevel();
    }

    protected BlockPos getBlockPos(){
        return this.blockEntity.getBlockPos();
    }

    protected BlockState getBlockState(){
        return this.blockEntity.getBlockState();
    }
}
