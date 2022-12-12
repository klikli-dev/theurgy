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
                ChatFormatting.LIGHT_PURPLE + "ctrl " +
                ChatFormatting.GRAY + "show usage" +
                ChatFormatting.GOLD + "]");
    }

    private void addSubtitles() {
        this.add(Theurgy.MODID + ".subtitle.tuning_fork", "Using Divination Rod");
    }

    private void addMessages() {
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED, ChatFormatting.GREEN +"Success"+ChatFormatting.RESET +": The divination rod is now attuned to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW, ChatFormatting.DARK_RED +"Warning"+ChatFormatting.RESET +": You need a higher tier divination rod to attune to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED, ChatFormatting.DARK_RED +"Warning"+ChatFormatting.RESET +": The divination rod cannot be attuned to this type of block: %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_DISALLOWED, ChatFormatting.DARK_RED +"Warning"+ChatFormatting.RESET +": The divination rod cannot be attuned to this type of block: %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK, "The divination rod is not attuned to any material.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED, ChatFormatting.DARK_RED +"Warning"+ChatFormatting.RESET +": This type of divination rod cannot be manually attuned.");
    }

    private void addItems() {
        this.add(TheurgyConstants.I18n.ITEM_GROUP, "Theurgy");

        this.addItem(ItemRegistry.EMPTY_JAR, "Empty Jar");

        this.addItem(ItemRegistry.EMPTY_JAR_LABELED, "Labeled Empty Jar");
        this.addTooltip(ItemRegistry.EMPTY_JAR_LABELED, "Dummy item for rendering Alchemical Sulfur if source items are not shown.");

        this.addItem(ItemRegistry.JAR_LABEL, "Jar Label");
        this.addTooltip(ItemRegistry.JAR_LABEL, "Dummy item for rendering Alchemical Sulfur if source items are shown.");

        this.addItem(ItemRegistry.ALCHEMICAL_SULFUR, "Alchemical Sulfur %s");
        this.addTooltip(ItemRegistry.ALCHEMICAL_SULFUR,
                "Alchemical sulfur crafted from %s.",
                "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.");


        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK, "The divination rod is "+ ChatFormatting.RED +"not attuned"+ ChatFormatting.RESET +" to any material.");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO, "The divination rod is attuned to %s");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT, "Found %s at %s.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T1, "Glass Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T1.get().getDescriptionId() + ".linked", "Glass Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T1,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                ChatFormatting.GREEN + "Shift-Click"+ChatFormatting.GRAY + " a block to attune the rod to it.\n" +
                        ChatFormatting.GREEN + "Right-Click and hold"+ChatFormatting.GRAY +" and hold to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click"+ChatFormatting.GRAY + " without holding after a successful search to let the rod show the last found block without consuming durability.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T4, "Emerald Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T4.get().getDescriptionId() + ".linked", "Emerald Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T4,
                "This divination rod type cannot be manually attuned, instead comes pre-attuned after crafting..",
                ChatFormatting.GREEN + "Right-Click and hold"+ChatFormatting.GRAY +" and hold to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click"+ChatFormatting.GRAY + " without holding after a successful search to let the rod show the last found block without consuming durability.");
    }

    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addSubtitles();
        this.addMessages();
        this.addItems();
    }
}
