// SPDX-FileCopyrightText: 2024 klikli_dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.occultism;

import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public class OccultismIntegrationDummy implements OccultismIntegration {
    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean tryPerformStorageActuatorExtraction(Level level, IItemHandler extractCap, Filter extractFilter, IItemHandler insertCap, Filter insertFilter, int extractionAmount) {
        return false;
    }

}
