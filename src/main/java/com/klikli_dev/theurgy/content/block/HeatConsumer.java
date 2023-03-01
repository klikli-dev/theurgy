/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block;

import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface HeatConsumer {

    int CHECK_HEAT_TICK_INTERVAL = 20;

    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default boolean hasHeatProvider() {
        var blockEntity = this.self().getLevel().getBlockEntity(this.self().getBlockPos().below());
        if (blockEntity == null) {
            return false;
        }

        return blockEntity.getCapability(CapabilityRegistry.HEAT_PROVIDER, Direction.UP).map(provider -> provider.isHot()).orElse(false);
    }

    default boolean isHeated() {
        if (this.self().getLevel().getGameTime() % CHECK_HEAT_TICK_INTERVAL == 0) {
            var wasHeated = this.getHeatedCache();

            var isHeated = this.hasHeatProvider();
            this.setHeatedCache(isHeated);

            if (wasHeated != isHeated) {
                if(this.self().getBlockState().hasProperty(BlockStateProperties.LIT)){
                    var newState = this.self().getBlockState().setValue(BlockStateProperties.LIT, isHeated);
                    this.self().getLevel().setBlock(this.self().getBlockPos(), newState, 1 | 2);
                }
                this.self().setChanged();
            }
        }
        return this.getHeatedCache();
    }

    boolean getHeatedCache();

    void setHeatedCache(boolean heated);
}
