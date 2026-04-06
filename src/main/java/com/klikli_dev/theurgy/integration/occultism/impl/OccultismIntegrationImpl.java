// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.occultism.impl;

import com.klikli_dev.occultism.common.misc.IMapItemHandlerModifiable;
import com.klikli_dev.occultism.common.misc.ItemStackKey;
import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.content.behaviour.filter.ListFilter;
import com.klikli_dev.theurgy.content.storage.ItemStorageHelper;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        private static final Map<Class<?>, Optional<Field>> DELEGATE_FIELD_CACHE = new ConcurrentHashMap<>();

        public static boolean tryPerformStorageActuatorExtraction(Level level, ResourceHandler<ItemResource> extractCap, Filter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {
            var mapItemHandler = unwrapMapItemHandler(extractCap);

            if (mapItemHandler == null || !(extractFilter instanceof ListFilter listFilter))
                return false;

            if (listFilter.isDenyList())
                return false;

            return performExtraction(level, mapItemHandler, listFilter, insertCap, insertFilter, extractionAmount);

        }

        protected static boolean performExtraction(Level level, IMapItemHandlerModifiable extractCap, ListFilter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter, int extractionAmount) {
            var filterItems = extractFilter.filterItems();

            for (var filterItem : filterItems) {
                var key = ItemStackKey.of(filterItem);
                var extractStack =
                        extractFilter.shouldRespectDataComponents() ?
                                extractCap.extractItem(key, extractionAmount, true)
                                //if we ignore data components, we let the storage system find the first matching stack for us
                                : extractCap.extractItemIgnoreComponents(key.stack(), extractionAmount, true);

                if (!extractStack.isEmpty() && insertFilter.test(level, extractStack)) {
                    var inserted = ItemStorageHelper.insertItemStacked(insertCap, extractStack, true);

                    if (inserted.getCount() != extractStack.getCount()) {
                        ItemStack remaining = ItemStorageHelper.insertItemStacked(insertCap, extractStack, false);
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

        private static @Nullable IMapItemHandlerModifiable unwrapMapItemHandler(ResourceHandler<ItemResource> handler) {
            Object current = handler;

            while (current != null) {
                if (current instanceof IMapItemHandlerModifiable mapItemHandler) {
                    return mapItemHandler;
                }

                var delegateField = findDelegateField(current.getClass());
                if (delegateField == null) {
                    return null;
                }

                try {
                    current = delegateField.get(current);
                } catch (IllegalAccessException | IllegalArgumentException ignored) {
                    return null;
                }
            }

            return null;
        }

        private static @Nullable Field findDelegateField(Class<?> clazz) {
            var cached = DELEGATE_FIELD_CACHE.get(clazz);
            if (cached != null) {
                return cached.orElse(null);
            }

            Class<?> current = clazz;
            while (current != null) {
                try {
                    var field = current.getDeclaredField("delegate");
                    field.setAccessible(true);
                    DELEGATE_FIELD_CACHE.put(clazz, Optional.of(field));
                    return field;
                } catch (NoSuchFieldException ignored) {
                    current = current.getSuperclass();
                } catch (RuntimeException ignored) {
                    break;
                }
            }

            DELEGATE_FIELD_CACHE.put(clazz, Optional.empty());
            return null;
        }
    }
}
