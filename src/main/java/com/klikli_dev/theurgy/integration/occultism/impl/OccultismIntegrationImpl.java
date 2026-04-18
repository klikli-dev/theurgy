// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.occultism.impl;

import com.klikli_dev.occultism.common.misc.MapItemResourceHandler;
import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.content.behaviour.filter.ListFilter;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class OccultismIntegrationImpl implements OccultismIntegration {
    public boolean isLoaded() {
        return ModList.get().isLoaded("occultism");
    }

    public boolean tryPerformStorageActuatorExtraction(Level level, ResourceHandler<ItemResource> extractCap, Filter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {
        if (!this.isLoaded())
            return false;

        return OccultismHelper.tryPerformStorageActuatorExtraction(level, extractCap, extractFilter, insertCap, insertFilter, extractionAmount);
    }

    public static class OccultismHelper {

        public static boolean tryPerformStorageActuatorExtraction(Level level, ResourceHandler<ItemResource> extractCap, Filter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {

            if (!(extractCap instanceof MapItemResourceHandler mapItemStackHandler) || !(extractFilter instanceof ListFilter listFilter))
                return false;

            if (listFilter.isDenyList())
                return false;

            return performExtraction(level, mapItemStackHandler, listFilter, insertCap, insertFilter, extractionAmount);

        }

        protected static boolean performExtraction(Level level, MapItemResourceHandler extractCap, ListFilter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {
            var filterItems = extractFilter.filterItems();

            for (var filterItem : filterItems) {
                var predicate = (java.util.function.Predicate<ItemResource>) resource -> {
                    var resourceStack = resource.toStack(1);
                    return extractFilter.shouldRespectDataComponents()
                            ? ItemStack.isSameItemSameComponents(resourceStack, filterItem)
                            : ItemStack.isSameItem(resourceStack, filterItem);
                };

                try (var tx = Transaction.openRoot()) {
                    int insertedAmount;

                    try (var simulateTx = Transaction.open(tx)) {
                        var extracted = ResourceHandlerUtil.extractFirst(extractCap, predicate, extractionAmount, simulateTx);

                        if (extracted == null || extracted.amount() <= 0) {
                            continue;
                        }

                        var extractStack = extracted.resource().toStack(extracted.amount());
                        if (!insertFilter.test(level, extractStack)) {
                            continue;
                        }

                        var remaining = ItemUtil.insertItemReturnRemaining(insertCap, extractStack, false, simulateTx);
                        insertedAmount = extractStack.getCount() - remaining.getCount();
                    }

                    if (insertedAmount == 0) {
                        continue;
                    }

                    var transferred = ResourceHandlerUtil.extractFirst(extractCap, predicate, insertedAmount, tx);
                    if (transferred == null || transferred.amount() <= 0) {
                        continue;
                    }

                    var remaining = ItemUtil.insertItemReturnRemaining(insertCap, transferred.resource().toStack(transferred.amount()), false, tx);
                    if (!remaining.isEmpty()) {
                        continue;
                    }

                    tx.commit();
                    return true;
                }
            }

            return false;
        }
    }
}
