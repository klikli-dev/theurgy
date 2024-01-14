// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.selection;

import com.klikli_dev.theurgy.content.render.Color;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SelectedPoint<T extends SelectedPoint<T>> {

    protected Level level;
    protected BlockPos blockPos;
    protected BlockState blockState;

    protected SelectedPoint(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    protected SelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        this.level = level;
        this.blockPos = blockPos;
        this.blockState = blockState;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.blockState = this.level.getBlockState(this.blockPos);
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void refreshBlockStateCache() {
        this.blockState = this.level.getBlockState(this.blockPos);
    }

    public abstract Color getColor();

    public abstract Component getModeMessage();

    /**
     * Cycles the mode of the selected point
     *
     * @return false if the point should be removed.
     */
    public abstract boolean cycleMode();

    public abstract Codec<T> codec();
}
