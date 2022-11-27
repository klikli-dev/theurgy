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
        this.add(TheurgyConstants.I18n.Tooltip.SHOW_EXTENDED, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "shift " +
                ChatFormatting.GRAY + "read more" +
                ChatFormatting.GOLD + "]");
        this.add(TheurgyConstants.I18n.Tooltip.SHOW_USAGE, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "ctrl-shift " +
                ChatFormatting.GRAY + "show usage" +
                ChatFormatting.GOLD + "]");
    }

    private void addSubtitles() {
        this.add(Theurgy.MODID + ".subtitle.tuning_fork", "Using Divination Rod");
    }

    private void addMessages() {
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED, "§aSuccess§r:The divination rod is now attuned to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW, "§4Warning§r: You need a higher tier divination rod to attune to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED, "§4Warning§r: The divination rod cannot be attuned to this type of block: %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK, "The divination rod is not attuned to any material.");
    }

    private void addItems() {
        this.add(((TranslatableContents)Theurgy.CREATIVE_MODE_TAB.getDisplayName().getContents()).getKey(), "Theurgy");

        this.addItem(ItemRegistry.EMPTY_JAR, "Empty Jar");

        this.addItem(ItemRegistry.JAR_LABEL, "Jar Label");
        this.addTooltip(ItemRegistry.JAR_LABEL, "Dummy item for eay render access to Alchemical Sulfur labels.");

        this.addItem(ItemRegistry.ALCHEMICAL_SULFUR, "Alchemical Sulfur %s");
        this.addTooltip(ItemRegistry.ALCHEMICAL_SULFUR,
                "Alchemical sulfur crafted from %s.",
                "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T1, "Wooden Divination Rod");
        //TODO: Tooltip with usage instructions for divination rod, including tier, display mode, linked item ...
    }

    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addSubtitles();
        this.addMessages();
        this.addItems();
    }
}
