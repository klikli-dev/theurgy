// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter;

import com.klikli_dev.theurgy.content.behaviour.logistics.InserterNodeBehaviour;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemInserterBehaviour extends InserterNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction> {

    private Direction directionOverride = null;
    private boolean enabled = true;

    public LogisticsItemInserterBehaviour(BlockEntity blockEntity) {
        super(blockEntity, CapabilityRegistry.ITEM_HANDLER);
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    @Override
    public void enabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
        this.onEnabledChanged();
    }

    public void directionOverride(Direction directionOverride) {
        this.directionOverride = directionOverride;

        //first notify the network to remove the old target capabilities
        this.targetCapabilities.forEach(c -> this.onCapabilityInvalidated(c.pos(), true));

        //then build the new capabilities
        this.rebuildTargetCapabilities();

        //then notify the network to add the new target capabilities
        this.targetCapabilities.forEach(this::notifyTargetCapabilityCacheCreated);
    }

    public Direction directionOverride() {
        return this.directionOverride;
    }

    protected void onEnabledChanged() {
        //a disabled logistics node is equivalent to one not existing
        if (this.enabled) {
            this.rebuildTargetCapabilities();
            Logistics.get().add(this);
        } else {
            this.targetCapabilities.forEach(c -> this.onCapabilityInvalidated(c.pos(), true));
            this.deactivateTargetCapabilities();
            Logistics.get().remove(this, false);
        }
    }

    @Override
    public @Nullable Direction getTargetContext(BlockPos targetPos) {
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }

    @Override
    public void writeNetwork(ValueOutput output) {
        super.writeNetwork(output);
        output.putBoolean("enabled", this.enabled);
        if (this.directionOverride != null)
            output.putInt("directionOverride", this.directionOverride.get3DDataValue());
    }

    @Override
    public void readNetwork(ValueInput input) {
        super.readNetwork(input);
        input.getInt("directionOverride").ifPresent(direction -> this.directionOverride = Direction.from3DDataValue(direction));
        this.enabled = input.getBooleanOr("enabled", this.enabled);
    }
}
