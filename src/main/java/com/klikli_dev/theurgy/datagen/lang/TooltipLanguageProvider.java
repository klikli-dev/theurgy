/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public interface TooltipLanguageProvider {

    private LanguageProvider self() {
        return (LanguageProvider) this;
    }

    default void addTooltip(Supplier<? extends Item> key, String tooltip) {
        if (tooltip != null)
            this.self().add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_SUFFIX, tooltip);
    }

    default void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip) {
        this.addTooltip(key, tooltip);
        if (tooltip != null)
            this.self().add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_EXTENDED_SUFFIX, extendedTooltip);
    }

    default void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip, String usageTooltip) {
        this.addTooltip(key, tooltip, extendedTooltip);
        if (tooltip != null)
            this.self().add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_USAGE_SUFFIX, usageTooltip);
    }
}
