/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.data.LanguageProvider;

public class ENUSProvider extends LanguageProvider implements TooltipLanguageProvider {
    public ENUSProvider(DataGenerator gen) {
        super(gen, Theurgy.MODID, "en_us");
    }

    private void addMisc() {
        this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_EXTENDED, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "shift " +
                ChatFormatting.GRAY + "read more" +
                ChatFormatting.GOLD + "]");
        this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_USAGE, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "ctrl-shift " +
                ChatFormatting.GRAY + "show usage" +
                ChatFormatting.GOLD + "]");
    }

    private void addItems() {
        this.add(((TranslatableContents)Theurgy.CREATIVE_MODE_TAB.getDisplayName().getContents()).getKey(), "Theurgy");

        this.addItem(ItemRegistry.EMPTY_JAR, "Empty Jar");

        this.addItem(ItemRegistry.EMPTY_JAR_LABELED, "Labeled Empty Jar");
        this.addTooltip(ItemRegistry.EMPTY_JAR_LABELED, "Dummy item for rendering Alchemical Sulfur if source items are not shown.");

        this.addItem(ItemRegistry.JAR_LABEL, "Jar Label");
        this.addTooltip(ItemRegistry.JAR_LABEL, "Dummy item for rendering Alchemical Sulfur if source items are shown.");

        this.addItem(ItemRegistry.ALCHEMICAL_SULFUR, "Alchemical Sulfur %s");
        this.addTooltip(ItemRegistry.ALCHEMICAL_SULFUR,
                "Alchemical sulfur crafted from %s.",
                "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.");
    }

    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addItems();
    }
}
