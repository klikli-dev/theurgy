/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.occultism;

import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.integration.occultism.impl.OccultismIntegrationImpl;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public interface OccultismIntegration {

    OccultismIntegration instance = new OccultismIntegrationImpl();

    static OccultismIntegration get() {
        return instance;
    }

    boolean isLoaded();

    /**
     * If the extract cap is an occultism storage actuator, this method will use optimized extraction logic.
     * The storage actuator stores items in a map of item stack -> count. This allows for faster extraction, if there is an allow list filter.
     * If there is a deny list filter, or an AttributeFilter this returns false to use normal extraction logic that loops through all entries.
     */
    boolean tryPerformStorageActuatorExtraction(Level level, IItemHandler extractCap, Filter extractFilter, IItemHandler insertCap, Filter insertFilter, int extractionAmount);
}
