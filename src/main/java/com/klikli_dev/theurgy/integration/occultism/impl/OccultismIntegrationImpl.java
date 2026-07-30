// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.occultism.impl;

import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class OccultismIntegrationImpl implements OccultismIntegration {

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean tryPerformStorageActuatorExtraction(Level level, ResourceHandler<ItemResource> extractCap, Filter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {
        return false;
    }
}
