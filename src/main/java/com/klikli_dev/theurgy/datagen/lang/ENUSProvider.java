/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class ENUSProvider extends LanguageProvider implements TooltipLanguageProvider {
    public ENUSProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "en_us");
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

        this.add(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, "%smB");
    }

    private void addJEI() {
        this.add(TheurgyConstants.I18n.JEI.CALCINATION_CATEGORY, "Calcination");
        this.add(TheurgyConstants.I18n.JEI.LIQUEFACTION_CATEGORY, "Liquefaction");
        this.add(TheurgyConstants.I18n.JEI.DISTILLATION_CATEGORY, "Distillation");
    }

    private void addSubtitles() {
        this.add(Theurgy.MODID + ".subtitle.tuning_fork", "Using Divination Rod");
    }

    private void addMessages() {
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED, ChatFormatting.GREEN + "Success" + ChatFormatting.RESET + ": The divination rod is now attuned to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW, ChatFormatting.DARK_RED + "Warning" + ChatFormatting.RESET + ": You need a higher tier divination rod to attune to %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED, ChatFormatting.DARK_RED + "Warning" + ChatFormatting.RESET + ": The divination rod cannot be attuned to this type of block: %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_DISALLOWED, ChatFormatting.DARK_RED + "Warning" + ChatFormatting.RESET + ": The divination rod cannot be attuned to this type of block: %s.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK, "The divination rod is not attuned to any material.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED, ChatFormatting.DARK_RED + "Warning" + ChatFormatting.RESET + ": This type of divination rod cannot be manually attuned.");
    }

    private void addFluids() {
        this.add("fluid_type.theurgy.sal_ammoniac", "Sal Ammoniac");
    }

    private void addBlocks() {
        this.addBlock(BlockRegistry.CALCINATION_OVEN, "Calcination Oven");
        this.addTooltip(BlockRegistry.CALCINATION_OVEN.get()::asItem,
                "A device to extract Alchemical Salt from Items.",
                "Salt represents the \"body\" or \"physical matter\" of an object.",
                "Place this on top of a heating device such as a Pyromantic Brazier.\nRight-click with ingredients to place them in the oven for processing.");

        this.addBlock(BlockRegistry.PYROMANTIC_BRAZIER, "Pyromantic Brazier");
        this.addTooltip(BlockRegistry.PYROMANTIC_BRAZIER.get()::asItem,
                "A simple device to heat alchemical apparati.",
                "Place this below other alchemical apparati to heat them up",
                "Insert a fuel item by right-clicking the brazier with it, or using a hopper");

        this.addBlock(BlockRegistry.LIQUEFACTION_CAULDRON, "Liquefaction Cauldron");
        this.addTooltip(BlockRegistry.LIQUEFACTION_CAULDRON.get()::asItem,
                "A device to extract Alchemical Sulfur from Items using a Solvent.",
                "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.",
                "Place this on top of a heating device such as a Pyromantic Brazier.\nRight-click with ingredients to add them to the cauldron for processing.");

        this.addBlock(BlockRegistry.DISTILLER, "Mercury Distiller");
        this.addTooltip(BlockRegistry.DISTILLER.get()::asItem,
                "A device to extract Alchemical Mercury from Items.",
                "Mercury represents the \"energy\" of an object. It has applications both as an energy source and as a catalyst.",
                "Place this on top of a heating device such as a Pyromantic Brazier.\nRight-click with ingredients to add them to the distiller for processing.");

        this.addBlock(BlockRegistry.INCUBATOR, "Incubator");
        this.addTooltip(BlockRegistry.INCUBATOR.get()::asItem,
                "A device to recombine Alchemical Sulfur, Salt and Mercury into items.",
                null,
                "Place this on top of a heating device such as a Pyromantic Brazier.\nNeeds Incubator Vessels for all three ingredient types adjacent horizontally to the incubator.");

        this.addBlock(BlockRegistry.INCUBATOR_MERCURY_VESSEL, "Incubator Mercury Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()::asItem,
                "A vessel to hold Mercury for the Incubator.",
                null,
                "Place horizontally next to the incubator.\nRight-click with Alchemical Mercury to fill the vessel to allow the Incubator to process it.");
        this.addBlock(BlockRegistry.INCUBATOR_SALT_VESSEL, "Incubator Salt Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_SALT_VESSEL.get()::asItem,
                "A vessel to hold Salt for the Incubator.",
                null,
                "Place horizontally next to the incubator.\nRight-click with Alchemical Salt to fill the vessel to allow the Incubator to process it.");
        this.addBlock(BlockRegistry.INCUBATOR_SULFUR_VESSEL, "Incubator Sulfur Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()::asItem,
                "A vessel to hold Sulfur for the Incubator.",
                null,
                "Place horizontally next to the incubator.\nRight-click with Alchemical Sulfur to fill the vessel to allow the Incubator to process it.");
    }

    private void addSulfurs() {
        this.add(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE, "Unknown Source");

        //Automatic sulfur name rendering
        SulfurRegistry.SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
            if (sulfur.useAutomaticNameRendering) {
                this.addItem(() -> sulfur, "Alchemical Sulfur %s");
            }
            if(sulfur.provideAutomaticTooltipData){
                this.addTooltip(() -> sulfur,
                        "Alchemical Sulfur crafted from %s.",
                        "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.");
            }
        });

        //Tag Names for Sulfurs with overrideTagSourceName
        this.add(Util.makeDescriptionId("tag", ItemTags.LOGS.location()), "Logs");

        //Note: It was considered to try and warn here if a sulfur has overrideTagSourceName set to true, but no override lang key set.
        //      This is not possible, however, as the tag source comes from item nbt that is not available at this point.
    }

    private void addSalts() {
        this.addItem(SaltRegistry.ORE, "Alchemical Salt: " + ChatFormatting.GREEN + ChatFormatting.ITALIC + "Ore" + ChatFormatting.RESET);
        this.addTooltip(SaltRegistry.ORE,
                "Alchemical Salt calcinated from Ore.",
                "Salt represents the \"body\" or \"physical matter\" of an object.");

    }

    private void addDivinationRods() {
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK, "The divination rod is " + ChatFormatting.RED + "not attuned" + ChatFormatting.RESET + " to any material.");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO, "The divination rod is attuned to %s");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT, "Found %s at %s.");

        this.add(TheurgyConstants.I18n.Item.DIVINATION_ROD_UNKNOWN_LINKED_BLOCK, "Unknown Block (something went wrong)");

        this.addItem(ItemRegistry.DIVINATION_ROD_T1, "Glass Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T1.get().getDescriptionId() + ".linked", "Glass Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T1,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                ChatFormatting.GREEN + "Shift-Click" + ChatFormatting.GRAY + " a block to attune the rod to it.\n" +
                        ChatFormatting.GREEN + "Right-Click and hold" + ChatFormatting.GRAY + "to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click without holding" + ChatFormatting.GRAY + " after a successful search to let the rod show the last found block without consuming durability.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T2, "Iron Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T2.get().getDescriptionId() + ".linked", "Iron Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T2,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                ChatFormatting.GREEN + "Shift-Click" + ChatFormatting.GRAY + " a block to attune the rod to it.\n" +
                        ChatFormatting.GREEN + "Right-Click and hold" + ChatFormatting.GRAY + "to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click without holding" + ChatFormatting.GRAY + " after a successful search to let the rod show the last found block without consuming durability.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T3, "Diamond Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T3.get().getDescriptionId() + ".linked", "Diamond Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T3,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                ChatFormatting.GREEN + "Shift-Click" + ChatFormatting.GRAY + " a block to attune the rod to it.\n" +
                        ChatFormatting.GREEN + "Right-Click and hold" + ChatFormatting.GRAY + "to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click without holding" + ChatFormatting.GRAY + " after a successful search to let the rod show the last found block without consuming durability.");

        this.addItem(ItemRegistry.DIVINATION_ROD_T4, "Netherite Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T4.get().getDescriptionId() + ".linked", "Netherite Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T4,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                ChatFormatting.GREEN + "Shift-Click" + ChatFormatting.GRAY + " a block to attune the rod to it.\n" +
                        ChatFormatting.GREEN + "Right-Click and hold" + ChatFormatting.GRAY + "to let the rod search for blocks.\n" +
                        ChatFormatting.GREEN + "Right-Click without holding" + ChatFormatting.GRAY + " after a successful search to let the rod show the last found block without consuming durability.");

//        this.addItem(ItemRegistry.DIVINATION_ROD_T4, "Emerald Divination Rod");
//        this.add(ItemRegistry.DIVINATION_ROD_T4.get().getDescriptionId() + ".linked", "Emerald Divination Rod %s");
//        //tooltip is handled by rod itself
//        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T4,
//                "This divination rod type cannot be manually attuned, instead comes pre-attuned after crafting..",
//                ChatFormatting.GREEN + "Right-Click and hold"+ChatFormatting.GRAY +" to let the rod search for blocks.\n" +
//                        ChatFormatting.GREEN + "Right-Click without holding"+ChatFormatting.GRAY + " after a successful search to let the rod show the last found block without consuming durability.");

    }

    private void addItems() {
        this.add(TheurgyConstants.I18n.ITEM_GROUP, "Theurgy");

        this.addSalts();
        this.addSulfurs();
        this.addDivinationRods();

        this.addItem(ItemRegistry.EMPTY_JAR, "Empty Jar");

        this.addItem(ItemRegistry.EMPTY_JAR_LABELED, "Labeled Empty Jar");
        this.addTooltip(ItemRegistry.EMPTY_JAR_LABELED, "Dummy item for rendering Alchemical Sulfur if source items are not shown.");

        this.addItem(ItemRegistry.JAR_LABEL, "Jar Label");
        this.addTooltip(ItemRegistry.JAR_LABEL, "Dummy item for rendering Alchemical Sulfur if source items are shown.");


        this.addItem(ItemRegistry.SAL_AMMONIAC_BUCKET, "Sal Ammoniac Bucket");

        this.addItem(ItemRegistry.MERCURY_SHARD, "Mercury Shard");
        this.addExtendedTooltip(ItemRegistry.MERCURY_SHARD,
                "Mercury shards are small pieces of Mercury in crystalline form. Their main uses are as ingredient in Digestion processes and as an energy source.");

        this.addItem(ItemRegistry.MERCURY_CRYSTAL, "Mercury Crystal");
        this.addExtendedTooltip(ItemRegistry.MERCURY_CRYSTAL,
                "Mercury crystals are large pieces of Mercury in crystalline form. Their main uses are as ingredient in Digestion processes and as an energy source.");
    }

    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addSubtitles();
        this.addMessages();
        this.addItems();
        this.addBlocks();
        this.addFluids();
        this.addJEI();
    }
}
