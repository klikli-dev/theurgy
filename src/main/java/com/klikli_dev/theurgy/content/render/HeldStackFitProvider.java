// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.content.storage.MonitoredFluidTank;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.List;

public interface HeldStackFitProvider {
    default boolean heldStackFits(ItemStack stack) {
        for (var storage : this.heldStackFitItemStorages()) {
            for (int slot = 0; slot < storage.size(); slot++) {
                if (storage.isValid(slot, ItemResource.of(stack))) {
                    return true;
                }
            }
        }

        var fluidTanks = this.heldStackFitFluidTanks();
        if (!fluidTanks.isEmpty()) {
            var containedFluid = FluidUtil.getFirstStackContained(stack);
            if (containedFluid.isEmpty()) {
                return false;
            }

            for (var tank : fluidTanks) {
                if (tank.isFluidValid(containedFluid)) {
                    return true;
                }
            }
        }

        return false;
    }

    default List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of();
    }

    default List<? extends MonitoredFluidTank> heldStackFitFluidTanks() {
        return List.of();
    }
}
