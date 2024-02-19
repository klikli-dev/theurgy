// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemInserterBehaviour extends InserterNodeBehaviour<IItemHandler, @Nullable Direction> {

    private Direction directionOverride = null;
    private boolean enabled = true;

    public LogisticsItemInserterBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.ItemHandler.BLOCK);
    }

    public boolean enabled() {
        return this.enabled;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
        this.onEnabledChanged();
    }

    public void directionOverride(Direction directionOverride) {
        this.directionOverride = directionOverride;
    }

    public Direction directionOverride() {
        return this.directionOverride;
    }

    protected void onEnabledChanged() {
        //a disabled logistics node is equivalent to one not existing
        if (this.enabled) {
            Logistics.get().add(this);
        } else {
            Logistics.get().remove(this, false);
        }
    }

    @Override
    protected @Nullable Direction getTargetContext(BlockPos targetPos) {
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putBoolean("enabled", this.enabled);
        if (this.directionOverride != null)
            pTag.putInt("directionOverride", this.directionOverride.get3DDataValue());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("directionOverride")) {
            this.directionOverride = Direction.from3DDataValue(pTag.getInt("directionOverride"));
        }
        if (pTag.contains("enabled")) {
            this.enabled = pTag.getBoolean("enabled");
        }
    }
}
