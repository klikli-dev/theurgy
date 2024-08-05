// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.occultism.impl;

import com.klikli_dev.occultism.common.misc.ItemStackKey;
import com.klikli_dev.occultism.common.misc.MapItemStackHandler;
import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.content.behaviour.filter.ListFilter;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class OccultismIntegrationImpl implements OccultismIntegration {
    public boolean isLoaded() {
        return ModList.get().isLoaded("occultism");
    }

    public boolean tryPerformStorageActuatorExtraction(Level level, IItemHandler extractCap, Filter extractFilter, IItemHandler insertCap, Filter insertFilter, int extractionAmount) {
        if (!this.isLoaded())
            return false;

        return OccultismHelper.tryPerformStorageActuatorExtraction(level, extractCap, extractFilter, insertCap, insertFilter, extractionAmount);
    }

    public static class OccultismHelper {

        public static boolean tryPerformStorageActuatorExtraction(Level level, IItemHandler extractCap, Filter extractFilter, IItemHandler insertCap, Filter insertFilter, int extractionAmount) {

            if (!(extractCap instanceof MapItemStackHandler mapItemStackHandler) || !(extractFilter instanceof ListFilter listFilter))
                return false;

            if (listFilter.isDenyList())
                return false;

            return performExtraction(level, mapItemStackHandler, listFilter, insertCap, insertFilter, extractionAmount);

        }

        protected static boolean performExtraction(Level level, MapItemStackHandler extractCap, ListFilter extractFilter, IItemHandler insertCap, Filter insertFilter, int extractionAmount) {
            var filterItems = extractFilter.filterItems();

            for (var filterItem : filterItems) {
                var key = ItemStackKey.of(filterItem);
                var extractStack =
                        extractFilter.shouldRespectDataComponents() ?
                        extractCap.extractItem(key, extractionAmount, true)
                                //if we ignore data components, we let the storage system find the first matching stack for us
                        : extractCap.extractItemIgnoreComponents(key.stack(), extractionAmount, true);

                if (!extractStack.isEmpty() && insertFilter.test(level, extractStack)) {
                    var inserted = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, true);

                    if (inserted.getCount() != extractStack.getCount()) {
                        ItemStack remaining = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, false);
                        extractCap.extractItem(
                                //if we ignore data components, we build a new key from the actual extracted stack
                                extractFilter.shouldRespectDataComponents() ? key : ItemStackKey.of(extractStack),
                                extractStack.getCount() - remaining.getCount(), false);
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
