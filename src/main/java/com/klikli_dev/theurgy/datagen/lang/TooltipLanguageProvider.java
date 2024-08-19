// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface TooltipLanguageProvider {
    void add(String key, String value);

    default void addTooltip(Supplier<? extends Item> key, String tooltip) {
        if (tooltip != null)
            this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Tooltip.SUFFIX, tooltip);
    }

    default void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip) {
        this.addTooltip(key, tooltip);
        if (extendedTooltip != null)
            this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Tooltip.EXTENDED_SUFFIX, extendedTooltip);
    }

    default void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip, String usageTooltip) {
        this.addTooltip(key, tooltip, extendedTooltip);
        if (usageTooltip != null)
            this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Tooltip.USAGE_SUFFIX, usageTooltip);
    }

    default void addExtendedTooltip(Supplier<? extends Item> key, String extendedTooltip) {
        this.addTooltip(key, null, extendedTooltip);
    }

    default void addExtendedTooltip(Supplier<? extends Item> key, String extendedTooltip, String usageTooltip) {
        this.addTooltip(key, null, extendedTooltip, usageTooltip);
    }

    default void addUsageTooltip(Supplier<? extends Item> key, String usageTooltip) {
        this.addTooltip(key, null, null, usageTooltip);
    }

    default void addDynamicTooltip(Supplier<? extends Item> key, String tooltip) {
        this.addDynamicTooltip(key, "", tooltip);
    }

    default void addDynamicTooltip(Supplier<? extends Item> key, String keySuffix, String tooltip) {
        if (tooltip != null)
            this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Tooltip.DYNMIC_SUFFIX + keySuffix, tooltip);
    }
}
