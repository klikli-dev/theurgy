package com.klikli_dev.theurgy.content.behaviour.interaction;

import com.klikli_dev.theurgy.content.render.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SelectedPoint {
    protected Level level;
    protected BlockPos blockPos;
    protected BlockState blockState;

    protected SelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        this.level = level;
        this.blockPos = blockPos;
        this.blockState = blockState;
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

    public abstract void cycleMode();
}
