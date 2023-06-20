/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.behaviour;

import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class HeatedBehaviour {

    private final BlockEntity blockEntity;
    int CHECK_HEAT_TICK_INTERVAL = 20;
    private boolean heatedCache;

    public HeatedBehaviour(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    boolean hasHeatProvider() {
        var below = this.blockEntity.getLevel().getBlockEntity(this.blockEntity.getBlockPos().below());
        if (below == null) {
            return false;
        }

        return below.getCapability(CapabilityRegistry.HEAT_PROVIDER, Direction.UP).map(provider -> provider.isHot()).orElse(false);
    }

    public boolean isHeated() {
        if (this.blockEntity.getLevel().getGameTime() % this.CHECK_HEAT_TICK_INTERVAL == 0) {
            var wasHeated = this.heatedCache;

            var isHeated = this.hasHeatProvider();
            this.heatedCache = isHeated;

            if (wasHeated != isHeated) {
                if (this.blockEntity.getBlockState().hasProperty(BlockStateProperties.LIT)) {
                    var newState = this.blockEntity.getBlockState().setValue(BlockStateProperties.LIT, isHeated);
                    this.blockEntity.getLevel().setBlock(this.blockEntity.getBlockPos(), newState, 1 | 2);
                }
                this.blockEntity.setChanged();
            }
        }
        return this.heatedCache;
    }
}
