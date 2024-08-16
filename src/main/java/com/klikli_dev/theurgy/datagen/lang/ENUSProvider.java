// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.filter.attribute.*;
import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.niter.AlchemicalNiterItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ENUSProvider extends AbstractModonomiconLanguageProvider implements TooltipLanguageProvider {
    public ENUSProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "en_us");
    }

    protected String f(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    protected String green(String text) {
        return ChatFormatting.GREEN + text + ChatFormatting.RESET + ChatFormatting.GRAY;
    }

    protected String darkRed(String text) {
        return ChatFormatting.DARK_RED + text + ChatFormatting.RESET + ChatFormatting.GRAY;
    }

    private void addKeys() {
        this.add(TheurgyConstants.I18n.Key.CATEGORY, "Theurgy");
        this.add(TheurgyConstants.I18n.Key.CHANGE_ITEM_MODE, "Change Item Mode");
    }

    private void addItemTag(ResourceLocation resourceLocation, String string) {
        this.add("tag.item." + resourceLocation.getNamespace() + "." + resourceLocation.getPath().replace("/", "."), string);
    }

    private void addItemTag(TagKey<Item> item, String string) {
        this.addItemTag(item.location(), string);
    }

    private void addFluidTag(ResourceLocation resourceLocation, String string) {
        this.add("tag.fluid." + resourceLocation.getNamespace() + "." + resourceLocation.getPath().replace("/", "."), string);
    }

    private void addFluidTag(TagKey<Fluid> fluid, String string) {
        this.addFluidTag(fluid.location(), string);
    }

    private void addMisc() {
        this.add(TheurgyConstants.I18n.Tooltip.SHOW_EXTENDED, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "shift " +
                ChatFormatting.GRAY + "show more" +
                ChatFormatting.GOLD + "]");
        this.add(TheurgyConstants.I18n.Tooltip.EXTENDED_HEADING, ChatFormatting.GOLD + "[" +
                ChatFormatting.GRAY + "more" +
                ChatFormatting.GOLD + "]");

        this.add(TheurgyConstants.I18n.Tooltip.SHOW_USAGE, ChatFormatting.GOLD + "[" +
                ChatFormatting.LIGHT_PURPLE + "ctrl " +
                ChatFormatting.GRAY + "show usage" +
                ChatFormatting.GOLD + "]");
        this.add(TheurgyConstants.I18n.Tooltip.USAGE_HEADING, ChatFormatting.GOLD + "[" +
                ChatFormatting.GRAY + "usage" +
                ChatFormatting.GOLD + "]");

        this.add(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, "%smB");
    }

    private void addGui() {
        this.add(TheurgyConstants.I18n.Gui.FILTER_RESET_BUTTON_TOOLTIP, "Reset Filter Settings");
        this.add(TheurgyConstants.I18n.Gui.FILTER_CONFIRM_BUTTON_TOOLTIP, "Save Filter Settings");

        this.add(TheurgyConstants.I18n.Gui.LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP, "Allow-List");
        this.add(TheurgyConstants.I18n.Gui.LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP_SHIFT, "Items pass if they match any of the above. An empty Allow-List rejects everything.");

        this.add(TheurgyConstants.I18n.Gui.LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP, "Deny-List");
        this.add(TheurgyConstants.I18n.Gui.LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT, "Items pass if they DO NOT match any of the above. An empty Deny-List accepts everything.");

        this.add(TheurgyConstants.I18n.Gui.FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP, "Respect Data");
        this.add(TheurgyConstants.I18n.Gui.FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT, "Items pass if they match the data components of the filter item (durability, enchantments and others).");

        this.add(TheurgyConstants.I18n.Gui.FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP, "Ignore Data");
        this.add(TheurgyConstants.I18n.Gui.FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT, "Items pass regardless of their data components (durability, enchantments and others).");

        this.add(TheurgyConstants.I18n.Gui.SCROLL_DEFAULT_TITLE, "Choose an Option:");
        this.add(TheurgyConstants.I18n.Gui.SCROLL_TO_MODIFY, "Scroll to Modify");
        this.add(TheurgyConstants.I18n.Gui.SCROLL_TO_SELECT, "Scroll to Select");
        this.add(TheurgyConstants.I18n.Gui.SCROLL_SHIFT_SCROLLS_FASTER, "Shift to Scroll Faster");

        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP, "Allow-List (Any)");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP_SHIFT, "Items pass if they have ANY of the selected attributes.");

        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP, "Allow-List (All)");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP_SHIFT, "Items pass if they have ALL of the selected attributes.");

        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP, "Deny-List");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT, "Items pass if they do NOT have any of the selected attributes.");

        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_BUTTON_TOOLTIP, "Add attribute to list");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_INVERTED_BUTTON_TOOLTIP, "Add opposite attribute to list");

        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_REFERENCE_ITEM, "Add Reference Item");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_NO_SELECTED_ATTRIBUTES, "No attributes selected");
        this.add(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_SELECTED_ATTRIBUTES, "Selected attributes:");
    }

    private void addBehaviours() {
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_MODE, "%1$s %2$s"); //First is the mode message text, the second the block name
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_CALORIC_FLUX_EMITTER, "Send caloric flux to");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_OUTSIDE_RANGE, "%1$s selected blocks removed for being out of range.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER, "Caloric Flux Emitter targets %1$s.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_CALORIC_FLUX_EMITTER_NO_SELECTION, "Caloric Flux Emitter has no target.");

        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_SULFURIC_FLUX_EMITTER, "Add to reformation array: ");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER, "Sulfuric Flux Emitter is linked to %1$s source, %2$s target and %3$s result pedestals.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SELECTION, "Sulfuric Flux Emitter has no linked pedestals.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_TARGET, "Sulfuric Flux Emitter has no linked target pedestal.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SOURCES, "Sulfuric Flux Emitter has no linked source pedestals.");
        this.add(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_RESULT, "Sulfuric Flux Emitter has no linked result pedestal.");

        this.add(TheurgyConstants.I18n.Behaviour.INTERACTION_FERMENTATION_VAT_NO_RECIPE, "Cannot close vat, the items in it do not form a valid fermentation recipe.");
        this.add(TheurgyConstants.I18n.Behaviour.INTERACTION_FERMENTATION_VAT_CLOSED, "Cannot add or remove items or fluids from the vat while it is closed. Shift+Click to open.");

        this.add(TheurgyConstants.I18n.Behaviour.INTERACTION_DIGESTION_VAT_NO_RECIPE, "Cannot close vat, the items in it do not form a valid digestion recipe.");
        this.add(TheurgyConstants.I18n.Behaviour.INTERACTION_DIGESTION_VAT_CLOSED, "Cannot add or remove items or fluids from the vat while it is closed. Shift+Click to open.");
    }

    private void addIntegrations() {
        this.add(TheurgyConstants.I18n.JEI.CALCINATION_CATEGORY, "Calcination");
        this.add(TheurgyConstants.I18n.JEI.LIQUEFACTION_CATEGORY, "Liquefaction");
        this.add(TheurgyConstants.I18n.JEI.DISTILLATION_CATEGORY, "Alchemical Distillation");
        this.add(TheurgyConstants.I18n.JEI.INCUBATION_CATEGORY, "Incubation");
        this.add(TheurgyConstants.I18n.JEI.ACCUMULATION_CATEGORY, "Accumulation");
        this.add(TheurgyConstants.I18n.JEI.REFORMATION_CATEGORY, "Reformation");
        this.add(TheurgyConstants.I18n.JEI.FERMENTATION_CATEGORY, "Alchemical Fermentation");
        this.add(TheurgyConstants.I18n.JEI.DIGESTION_CATEGORY, "Digestion");
        this.add(TheurgyConstants.I18n.JEI.MERCURY_FLUX, "Mercury Flux: %s");
        this.add(TheurgyConstants.I18n.JEI.SOURCE_PEDESTAL_COUNT, "%sx");
        this.add(TheurgyConstants.I18n.JEI.TARGET_SULFUR_TOOLTIP, "The Target Sulfur will not be consumed!\nYou can retrieve it after crafting is complete.");

        this.add("config.jade.plugin_theurgy.mercury_flux", "Theurgy Mercury Flux");

        this.addItemTag(ItemTagRegistry.GEMS_SAL_AMMONIAC, "Sal Ammoniac Gems");
        this.addItemTag(ItemTagRegistry.ORES_SAL_AMMONIAC, "Sal Ammoniac Ores");
        this.addItemTag(ItemTagRegistry.SUGARS, "Sugars");
        this.addItemTag(ItemTagRegistry.FERMENTATION_STARTERS, "Fermentation Starters");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_MERCURIES, "Alchemical Mercuries");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_NITERS, "Alchemical Niters");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SALTS, "Alchemical Salts");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_AND_NITERS, "Alchemical Sulfurs and Niters");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS, "Alchemical Sulfurs");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ABUNDANT, "Alchemical Sulfurs: Abundant");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_COMMON, "Alchemical Sulfurs: Common");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_RARE, "Alchemical Sulfurs: Rare");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_PRECIOUS, "Alchemical Sulfurs: Precious");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS, "Alchemical Sulfurs: Earthen Matters");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT, "Alchemical Sulfurs: Abundant Earthen Matters");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON, "Alchemical Sulfurs: Common Earthen Matters");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_RARE, "Alchemical Sulfurs: Rare Earthen Matters");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS, "Alchemical Sulfurs: Gems");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT, "Alchemical Sulfurs: Abundant Gems");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON, "Alchemical Sulfurs: Common Gems");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS, "Alchemical Sulfurs: Precious Gems");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE, "Alchemical Sulfurs: Rare Gems");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS, "Alchemical Sulfurs: Metals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT, "Alchemical Sulfurs: Abundant Metals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, "Alchemical Sulfurs: Common Metals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS, "Alchemical Sulfurs: Precious Metals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE, "Alchemical Sulfurs: Rare Metals");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS, "Alchemical Sulfurs: Other Minerals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT, "Alchemical Sulfurs: Abundant Other Minerals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON, "Alchemical Sulfurs: Common Other Minerals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE, "Alchemical Sulfurs: Rare Other Minerals");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS, "Alchemical Sulfurs: Precious Other Minerals");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS, "Alchemical Sulfurs: Logs");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT, "Alchemical Sulfurs: Abundant Logs");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_COMMON, "Alchemical Sulfurs: Common Logs");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_RARE, "Alchemical Sulfurs: Rare Logs");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_PRECIOUS, "Alchemical Sulfurs: Precious Logs");
        
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS, "Alchemical Sulfurs: Crops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT, "Alchemical Sulfurs: Abundant Crops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_COMMON, "Alchemical Sulfurs: Common Crops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_RARE, "Alchemical Sulfurs: Rare Crops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_PRECIOUS, "Alchemical Sulfurs: Precious Crops");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS, "Alchemical Sulfurs: Animal Parts");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT, "Alchemical Sulfurs: Abundant Animal Parts");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON, "Alchemical Sulfurs: Common Animal Parts");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE, "Alchemical Sulfurs: Rare Animal Parts");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_PRECIOUS, "Alchemical Sulfurs: Precious Animal Parts");

        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS, "Alchemical Sulfurs: Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT, "Alchemical Sulfurs: Abundant Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON, "Alchemical Sulfurs: Common Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON_FOR_AUTOMATIC_RECIPES, "Alchemical Sulfurs: Common Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE, "Alchemical Sulfurs: Rare Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE_FOR_AUTOMATIC_RECIPES, "Alchemical Sulfurs: Rare Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS, "Alchemical Sulfurs: Precious Mob Drops");
        this.addItemTag(ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS_FOR_AUTOMATIC_RECIPES, "Alchemical Sulfurs: Precious Mob Drops");

        this.addItemTag(ItemTagRegistry.HIGH_MERCURY_GEMS, "High Mercury Gems");
        this.addItemTag(ItemTagRegistry.LOW_MERCURY_GEMS, "Low Mercury Gems");
        this.addItemTag(ItemTagRegistry.MEDIUM_MERCURY_GEMS, "Medium Mercury Gems");
        this.addItemTag(ItemTagRegistry.HIGH_MERCURY_METALS, "High Mercury Metals");
        this.addItemTag(ItemTagRegistry.LOW_MERCURY_METALS, "Low Mercury Metals");
        this.addItemTag(ItemTagRegistry.MEDIUM_MERCURY_METALS, "Medium Mercury Metals");
        this.addItemTag(ItemTagRegistry.HIGH_MERCURY_ORES, "High Mercury Ores");
        this.addItemTag(ItemTagRegistry.LOW_MERCURY_ORES, "Low Mercury Ores");
        this.addItemTag(ItemTagRegistry.MEDIUM_MERCURY_ORES, "Medium Mercury Ores");
        this.addItemTag(ItemTagRegistry.OTHER_MINERALS, "Other Minerals");
        this.addItemTag(ItemTagRegistry.LOW_MERCURY_OTHER_MINERALS, "Low Mercury Other Minerals");
        this.addItemTag(ItemTagRegistry.MEDIUM_MERCURY_OTHER_MINERALS, "Medium Mercury Other Minerals");
        this.addItemTag(ItemTagRegistry.HIGH_MERCURY_OTHER_MINERALS, "High Mercury Other Minerals");
        this.addItemTag(ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS, "Low Mercury Raw Materials");
        this.addItemTag(ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS, "Medium Mercury Raw Materials");
        this.addItemTag(ItemTagRegistry.HIGH_MERCURY_RAW_MATERIALS, "Medium Mercury Raw Materials");

        this.addFluidTag(FluidTagRegistry.SAL_AMMONIAC, "Sal Ammoniac");
        this.addFluidTag(FluidTagRegistry.SOLVENT, "Solvent");

        this.add("emi.category.theurgy.calcination", "Calcination");
        this.add("emi.category.theurgy.accumulation", "Accumulation");
        this.add("emi.category.theurgy.digestion", "Digestion");
        this.add("emi.category.theurgy.distillation", "Distillation");
        this.add("emi.category.theurgy.fermentation", "Fermentation");
        this.add("emi.category.theurgy.incubation", "Incubation");
        this.add("emi.category.theurgy.liquefaction", "Liquefaction");
        this.add("emi.category.theurgy.reformation", "Reformation");

        this.add(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, "%ss");
    }

    private void addSubtitles() {
        this.add(Theurgy.MODID + ".subtitle.tuning_fork", "Using Divination Rod");
    }

    private void addMessages() {
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED, this.f("{0}: The divination rod is now attuned to %s.", this.green("Success")));

        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW, this.f("{0}: You need a higher tier divination rod to attune to %s.", this.darkRed("Warning")));
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED, this.f("{0}: The divination rod cannot be attuned to this type of block: %s.", this.darkRed("Warning")));
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_DISALLOWED, this.f("{0}: The divination rod cannot be attuned to this type of block: %s.", this.darkRed("Warning")));
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK, "The divination rod is not attuned to any material.");
        this.add(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED, this.f("{0}: This type of divination rod cannot be manually attuned.", this.darkRed("Warning")));
    }

    private void addFluids() {
        this.add("fluid_type.theurgy.sal_ammoniac", "Sal Ammoniac");
    }

    private void addBlocks() {
        this.addBlock(BlockRegistry.CALCINATION_OVEN, "Calcination Oven");
        this.addTooltip(BlockRegistry.CALCINATION_OVEN.get()::asItem,
                "A device to extract Alchemical Salt from Items.",
                "Salt represents the \"body\" or \"physical matter\" of an object.",
                this.f(
                        """
                                Place this on top of a heating device such as a Pyromantic Brazier.
                                {0} with ingredients to place them in the oven for processing.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.PYROMANTIC_BRAZIER, "Pyromantic Brazier");
        this.addTooltip(BlockRegistry.PYROMANTIC_BRAZIER.get()::asItem,
                "A simple device to heat alchemical apparati.",
                "Place this below other alchemical apparati to heat them up",
                this.f("{0} with a fuel item to insert it (or using a hopper)", this.green("Right-Click")));

        this.addBlock(BlockRegistry.LIQUEFACTION_CAULDRON, "Liquefaction Cauldron");
        this.addTooltip(BlockRegistry.LIQUEFACTION_CAULDRON.get()::asItem,
                "A device to extract Alchemical Sulfur from Items using a Solvent.",
                "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.",
                this.f(
                        """
                                Place this on top of a heating device such as a Pyromantic Brazier.
                                {0} with ingredients to add them to the cauldron for processing.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.DISTILLER, "Mercury Distiller");
        this.addTooltip(BlockRegistry.DISTILLER.get()::asItem,
                "A device to extract Alchemical Mercury from Items.",
                "Mercury represents the \"energy\" of an object. It has applications both as an energy source and as a catalyst.",
                this.f(
                        """
                                Place this on top of a heating device such as a Pyromantic Brazier.
                                {0} with ingredients to add them to the distiller for processing.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.INCUBATOR, "Incubator");
        this.addTooltip(BlockRegistry.INCUBATOR.get()::asItem,
                "A device to recombine Alchemical Sulfur, Salt and Mercury into items.",
                null,
                """
                        Place this on top of a heating device such as a Pyromantic Brazier.
                        Needs Incubator Vessels for all three ingredient types adjacent horizontally to the incubator.
                        """);

        this.addBlock(BlockRegistry.INCUBATOR_MERCURY_VESSEL, "Incubator Mercury Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()::asItem,
                "A vessel to hold Mercury for the Incubator.",
                null,
                this.f("""
                                Place horizontally next to the incubator.
                                {0} with Alchemical Mercury to fill the vessel to allow the Incubator to process it.
                                """,
                        this.green("Right-Click")));
        this.addBlock(BlockRegistry.INCUBATOR_SALT_VESSEL, "Incubator Salt Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_SALT_VESSEL.get()::asItem,
                "A vessel to hold Salt for the Incubator.",
                null,
                this.f("""
                                Place horizontally next to the incubator.
                                {0} with Alchemical Salt to fill the vessel to allow the Incubator to process it.
                                """,
                        this.green("Right-Click")));
        this.addBlock(BlockRegistry.INCUBATOR_SULFUR_VESSEL, "Incubator Sulfur Vessel");
        this.addTooltip(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()::asItem,
                "A vessel to hold Sulfur for the Incubator.",
                null,
                this.f("""
                                Place horizontally next to the incubator.
                                {0} with Alchemical Sulfur to fill the vessel to allow the Incubator to process it.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR, "Sal Ammoniac Accumulator");
        this.addTooltip(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()::asItem,
                "A simple device to increase the concentration of residual Sal Ammoniac in water by evaporation.",
                "The resulting concentrated Sal Ammoniac fluid is a solvent that can be used in Liquefaction Cauldrons.",
                this.f("""
                                Place this on top of a Sal Ammoniac Tank.
                                {0} with water buckets to fill the accumulator.
                                The water will be consumed to fill the Sal Ammoniac Tank below.
                                You can additionally add Sal Ammoniac Crystals to the Accumulator to speed up the process.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.SAL_AMMONIAC_TANK, "Sal Ammoniac Tank");
        this.addTooltip(BlockRegistry.SAL_AMMONIAC_TANK.get()::asItem,
                "A tank to store concentrated Sal Ammoniac fluid.",
                "Sal Ammoniac is a solvent that can be used in Liquefaction Cauldrons.",
                """
                        Place this below a filled Sal Ammoniac Accumulator.
                        The Tank will slowly be filled with Sal Ammoniac.
                        """);

        this.addBlock(BlockRegistry.MERCURY_CATALYST, "Mercury Catalyst");
        this.addTooltip(BlockRegistry.MERCURY_CATALYST.get()::asItem,
                "Converts mercury from it's crystal form into it's flux form.",
                "Mercury in flux form is pure energy and may be used as an energy source for certain processes.",
                this.f("""
                                {0} with mercury shards to add them to the catalyst.
                                They will be slowly consumed to fill the internal energy storage of the catalyst with mercury flux.
                                """,
                        this.green("Right-Click")));

        this.addBlock(BlockRegistry.CALORIC_FLUX_EMITTER, "Caloric Flux Emitter");
        this.addTooltip(BlockRegistry.CALORIC_FLUX_EMITTER.get()::asItem,
                "Remotely heats an Apparatus by emitting Caloric Flux at it.",
                "Caloric Flux is energy in the form of raw heat, and as such a derivative of Mercury Flux.",
                this.f("""
                                {0} an apparatus block to set it as the target for the emitter.
                                Then place the emitter on a Mercury Flux source, such as a Mercury Catalyst.
                                It will "shoot" Caloric Flux at the apparatus, heating it up.
                                """,
                        this.green("Sneak-Right-Click")));

        this.addBlock(BlockRegistry.SULFURIC_FLUX_EMITTER, "Sulfuric Flux Emitter");
        this.addTooltip(BlockRegistry.SULFURIC_FLUX_EMITTER.get()::asItem,
                "Used to power a sulfur reformation array to replicate Alchemical Sulfur.",
                "Sulfuric Flux allows to transfer, merge and manipulate Alchemical Sulfur.",
                this.f("""
                                 {0} on Source, Target and Reformation pedestals to set them as the targets for the emitter to form a reformation array.
                                Then place the emitter on a Mercury Flux source, such as a Mercury Catalyst.
                                View The Hermetica for more information.
                                """,
                        this.green("Right-Click"))
        );


        this.addBlock(BlockRegistry.REFORMATION_SOURCE_PEDESTAL, "Reformation Source Pedestal");
        this.addTooltip(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get()::asItem,
                "Holds the source Alchemical Sulfur to be reformed into the target Alchemical Sulfur.");

        this.addBlock(BlockRegistry.REFORMATION_TARGET_PEDESTAL, "Reformation Target Pedestal");
        this.addTooltip(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get()::asItem,
                "Holds the target Alchemical Sulfur that the source Alchemical Sulfur should be reformed into.");

        this.addBlock(BlockRegistry.REFORMATION_RESULT_PEDESTAL, "Reformation Result Pedestal");
        this.addTooltip(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get()::asItem,
                "The reformation result will appear in this pedestal.");

        this.addBlock(BlockRegistry.FERMENTATION_VAT, "Fermentation Vat");
        this.addTooltip(BlockRegistry.FERMENTATION_VAT.get()::asItem,
                "A vat for alchemical fermentation, required for transmutation.",
                "Fermentation is fundamental the conversion between types of matter (such as between metals and gems).",
                this.f(
                        """
                                {0} with ingredients to place them in the vat for processing.
                                {1} with an empty hand to close or open the vat to start or stop processing.
                                """,
                        this.green("Right-Click"),
                        this.green("Shift-right-Click")
                )
        );

        this.addBlock(BlockRegistry.DIGESTION_VAT, "Digestion Vat");
        this.addTooltip(BlockRegistry.DIGESTION_VAT.get()::asItem,
                "A vat for alchemical digestion, required for exaltation.",
                "Digestion is fundamental the conversion between tiers of matter (such as between common and rare [e.g. iron and gold]).",
                this.f(
                        """
                                {0} with ingredients to place them in the vat for processing.
                                {1} with an empty hand to close or open the vat to start or stop processing.
                                """,
                        this.green("Right-Click"),
                        this.green("Shift-right-Click")
                )
        );

        this.addBlock(BlockRegistry.SAL_AMMONIAC_ORE, "Sal Ammoniac Ore");
        this.addExtendedTooltip(BlockRegistry.SAL_AMMONIAC_ORE.get()::asItem,
                "Ore that yields Sal Ammoniac Crystals for use in a Sal Ammoniac Accumulator.");

        this.addBlock(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE, "Deepslate Sal Ammoniac Ore");
        this.addExtendedTooltip(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()::asItem,
                "Ore that yields Sal Ammoniac Crystals for use in a Sal Ammoniac Accumulator.");

        this.addBlock(BlockRegistry.LOGISTICS_ITEM_INSERTER, "Mercurial Item Inserter");
        this.addTooltip(BlockRegistry.LOGISTICS_ITEM_INSERTER.get()::asItem,
                "Allows to insert items into a block from a Mercurial Logistics Network",
                null,
                this.f(
                        """
                                {0} the target block with the inserter to place it.
                                Then {0} the inserter with a cable to connect it to the network.
                                """,
                        this.green("Right-Click")
                )
        );

        this.addBlock(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR, "Mercurial Item Extractor");
        this.addTooltip(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get()::asItem,
                "Allows to extract items from a block into a Mercurial Logistics Network",
                null,
                this.f(
                        """
                                {0} the target block with the extractor to place it.
                                Then {0} the extractor with a cable to connect it to the network.
                                """,
                        this.green("Right-Click")
                )
        );

        this.addBlock(BlockRegistry.LOGISTICS_CONNECTION_NODE, "Mercurial Connection Node");
        this.addTooltip(BlockRegistry.LOGISTICS_CONNECTION_NODE.get()::asItem,
                "Allows to connect multiple wires in a Mercurial Logistics Network",
                null,
                this.f(
                        """
                                Place the connection node, then use wires to connect it with other connection nodes, or other mercurial logistics blocks.
                                """,
                        this.green("Right-Click")
                )
        );

        this.addBlock(BlockRegistry.LOGISTICS_FLUID_INSERTER, "Mercurial Fluid Inserter");
        this.addTooltip(BlockRegistry.LOGISTICS_FLUID_INSERTER.get()::asItem,
                "Allows to insert fluids into a block from a Mercurial Logistics Network",
                null,
                this.f(
                        """
                                {0} the target block with the inserter to place it.
                                Then {0} the inserter with a cable to connect it to the network.
                                """,
                        this.green("Right-Click")
                )
        );

        this.addBlock(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR, "Mercurial Fluid Extractor");
        this.addTooltip(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get()::asItem,
                "Allows to extract fluids from a block into a Mercurial Logistics Network",
                null,
                this.f(
                        """
                                {0} the target block with the extractor to place it.
                                Then {0} the extractor with a cable to connect it to the network.
                                """,
                        this.green("Right-Click")
                )
        );
    }

    private void addNiter(AlchemicalNiterItem niter) {
        this.addItem(() -> niter, "Alchemical Niter %s");
        this.addTooltip(() -> niter,
                "Alchemical Niter crafted from Alchemical Sulfur of any %s.",
                "Niter represents the abstract category and value of an object, thus it is a further abstraction of the \"idea\" or \"soul\" represented by Sulfur.",
                "Niter extraction is a required intermediate step to transform one type of Sulfur into another type.");
    }

    private void addSulfurs() {
        this.add(TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_UNKNOWN_SOURCE, "Unknown Source");

        this.add(AlchemicalDerivativeTier.ABUNDANT.descriptionId(), "Abundant");
        this.add(AlchemicalDerivativeTier.COMMON.descriptionId(), "Common");
        this.add(AlchemicalDerivativeTier.RARE.descriptionId(), "Rare");
        this.add(AlchemicalDerivativeTier.PRECIOUS.descriptionId(), "Precious");

        this.add(AlchemicalSulfurType.MISC.descriptionId(), "Misc");
        this.add(AlchemicalSulfurType.EARTHEN_MATTERS.descriptionId(), "Earthen Matters");
        this.add(AlchemicalSulfurType.METALS.descriptionId(), "Metals");
        this.add(AlchemicalSulfurType.GEMS.descriptionId(), "Gems");
        this.add(AlchemicalSulfurType.OTHER_MINERALS.descriptionId(), "Other Minerals");
        this.add(AlchemicalSulfurType.LOGS.descriptionId(), "Logs");
        this.add(AlchemicalSulfurType.CROPS.descriptionId(), "Crops");
        this.add(AlchemicalSulfurType.ANIMALS.descriptionId(), "Animal Parts");
        this.add(AlchemicalSulfurType.MOBS.descriptionId(), "Mob Drops");
        this.add(TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_TYPE_NITER, "Niter");

        //Automatic sulfur name rendering
        SulfurRegistry.SULFURS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
            if (sulfur.useAutomaticNameLangGeneration) {
                this.addItem(() -> sulfur, "Alchemical Sulfur %s");
            }
            if (sulfur.useAutomaticTooltipLangGeneration) {
                this.addTooltip(() -> sulfur,
                        "Alchemical Sulfur crafted from %s %s %s.",
                        "Sulfur represents the \"idea\" or \"soul\" of an object",
                        "Sulfur is the central element used in Spagyrics processes." +
                                "\n\n" + ChatFormatting.ITALIC + "Hint: Sulfurs crafted from different states of the same material (such as from Ore or Ingots) are interchangeable." + ChatFormatting.RESET);
            }
        });

        NiterRegistry.NITERS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalNiterItem.class::cast).forEach(this::addNiter);

        //Sources for Niters
        this.addDerivativeSource(NiterRegistry.GEMS_ABUNDANT, "Abundant Gems");
        this.addDerivativeSource(NiterRegistry.GEMS_COMMON, "Common Gems");
        this.addDerivativeSource(NiterRegistry.GEMS_RARE, "Rare Gems");
        this.addDerivativeSource(NiterRegistry.GEMS_PRECIOUS, "Precious Gems");

        this.addDerivativeSource(NiterRegistry.METALS_ABUNDANT, "Abundant Metals");
        this.addDerivativeSource(NiterRegistry.METALS_COMMON, "Common Metals");
        this.addDerivativeSource(NiterRegistry.METALS_RARE, "Rare Metals");
        this.addDerivativeSource(NiterRegistry.METALS_PRECIOUS, "Precious Metals");

        this.addDerivativeSource(NiterRegistry.OTHER_MINERALS_ABUNDANT, "Abundant Other Minerals");
        this.addDerivativeSource(NiterRegistry.OTHER_MINERALS_COMMON, "Common Other Minerals");
        this.addDerivativeSource(NiterRegistry.OTHER_MINERALS_RARE, "Rare Other Minerals");
        this.addDerivativeSource(NiterRegistry.OTHER_MINERALS_PRECIOUS, "Precious Other Minerals");

        this.addDerivativeSource(NiterRegistry.LOGS_ABUNDANT, "Abundant Logs");
        this.addDerivativeSource(NiterRegistry.CROPS_ABUNDANT, "Abundant Crops");

        this.addDerivativeSource(NiterRegistry.ANIMALS_ABUNDANT, "Abundant Animal Parts");
        this.addDerivativeSource(NiterRegistry.ANIMALS_COMMON, "Common Animal Parts");
        this.addDerivativeSource(NiterRegistry.ANIMALS_RARE, "Rare Animal Parts");


        this.addDerivativeSource(NiterRegistry.MOBS_ABUNDANT, "Abundant Mob Drops");
        this.addDerivativeSource(NiterRegistry.MOBS_COMMON, "Common Mob Drops");
        this.addDerivativeSource(NiterRegistry.MOBS_RARE, "Rare Mob Drops");
        this.addDerivativeSource(NiterRegistry.MOBS_PRECIOUS, "Precious Mob Drops");

        //Sources for Sulfurs with {@link AlchemicalDerivativeItem#useCustomSourceName}
        this.addDerivativeSource(SulfurRegistry.OAK_LOG, "Oak");
        this.addDerivativeSource(SulfurRegistry.SPRUCE_LOG, "Spruce");
        this.addDerivativeSource(SulfurRegistry.BIRCH_LOG, "Birch");
        this.addDerivativeSource(SulfurRegistry.JUNGLE_LOG, "Jungle");
        this.addDerivativeSource(SulfurRegistry.ACACIA_LOG, "Acacia");
        this.addDerivativeSource(SulfurRegistry.CHERRY_LOG, "Cherry");
        this.addDerivativeSource(SulfurRegistry.DARK_OAK_LOG, "Dark Oak");
        this.addDerivativeSource(SulfurRegistry.MANGROVE_LOG, "Mangrove");
        this.addDerivativeSource(SulfurRegistry.CRIMSON_STEM, "Crimson");
        this.addDerivativeSource(SulfurRegistry.WARPED_STEM, "Warped");

        this.addDerivativeSource(SulfurRegistry.ROWAN_LOG, "Rowan");
        this.addDerivativeSource(SulfurRegistry.FIR_LOG, "Fir");
        this.addDerivativeSource(SulfurRegistry.REDWOOD_LOG, "Redwood");
        this.addDerivativeSource(SulfurRegistry.MAHOGANY_LOG, "Mahogany");
        this.addDerivativeSource(SulfurRegistry.JACARANDA_LOG, "Jacaranda");
        this.addDerivativeSource(SulfurRegistry.PALM_LOG, "Palm");
        this.addDerivativeSource(SulfurRegistry.WILLOW_LOG, "Willow");
        this.addDerivativeSource(SulfurRegistry.DEAD_LOG, "Dead");
        this.addDerivativeSource(SulfurRegistry.MAGIC_LOG, "Magic");
        this.addDerivativeSource(SulfurRegistry.UMBRAN_LOG, "Umbran");
        this.addDerivativeSource(SulfurRegistry.HELLBARK_LOG, "Hellbark");
        this.addDerivativeSource(SulfurRegistry.CINNAMON_LOG, "Cinnamon");
        this.addDerivativeSource(SulfurRegistry.GLACIAN_LOG, "Glacian");
        this.addDerivativeSource(SulfurRegistry.ARCHWOOD_LOG, "Archwood");
        this.addDerivativeSource(SulfurRegistry.BLUEBRIGHT_LOG, "Bluebright");
        this.addDerivativeSource(SulfurRegistry.STARLIT_LOG, "Starlit");
        this.addDerivativeSource(SulfurRegistry.FROSTBRIGHT_LOG, "Frostbright");
        this.addDerivativeSource(SulfurRegistry.COMET_LOG, "Comet");
        this.addDerivativeSource(SulfurRegistry.LUNAR_LOG, "Lunar");
        this.addDerivativeSource(SulfurRegistry.DUSK_LOG, "Dusk");
        this.addDerivativeSource(SulfurRegistry.MAPLE_LOG, "Maple");
        this.addDerivativeSource(SulfurRegistry.CRYSTALLIZED_LOG, "Crystallized");
        this.addDerivativeSource(SulfurRegistry.LIVINGWOOD_LOG, "Livingwood");
        this.addDerivativeSource(SulfurRegistry.GLIMMERING_LIVINGWOOD_LOG, "Glimmering Livingwood");
        this.addDerivativeSource(SulfurRegistry.DREAMWOOD_LOG, "Dreamwood");
        this.addDerivativeSource(SulfurRegistry.GLIMMERING_DREAMWOOD_LOG, "Glimmering Dreamwood");
        this.addDerivativeSource(SulfurRegistry.WALNUT_LOG, "Walnut");
        this.addDerivativeSource(SulfurRegistry.FIG_LOG, "Fig");
        this.addDerivativeSource(SulfurRegistry.WOLFBERRY_LOG, "Wolfberry");
        this.addDerivativeSource(SulfurRegistry.ECHO_LOG, "Echo");
        this.addDerivativeSource(SulfurRegistry.ILLWOOD_LOG, "Illwood");
        this.addDerivativeSource(SulfurRegistry.UNDEAD_LOG, "Undead");
        this.addDerivativeSource(SulfurRegistry.AURUM_LOG, "Aurum");
        this.addDerivativeSource(SulfurRegistry.MENRIL_LOG, "Menril");
        this.addDerivativeSource(SulfurRegistry.ASHEN_LOG, "Ashen");
        this.addDerivativeSource(SulfurRegistry.AZALEA_LOG, "Azalea");
        this.addDerivativeSource(SulfurRegistry.TRUMPET_LOG, "Trumpet");
        this.addDerivativeSource(SulfurRegistry.NETHERWOOD_LOG, "Netherwood");
        this.addDerivativeSource(SulfurRegistry.SKYROOT_LOG, "Skyroot");
        this.addDerivativeSource(SulfurRegistry.GOLDEN_OAK_LOG, "Golden Oak");
        this.addDerivativeSource(SulfurRegistry.TWILIGHT_OAK_LOG, "Twilight Oak");
        this.addDerivativeSource(SulfurRegistry.CANOPY_TREE_LOG, "Canopy Tree");
        this.addDerivativeSource(SulfurRegistry.DARKWOOD_LOG, "Darkwood");
        this.addDerivativeSource(SulfurRegistry.TIMEWOOD_LOG, "Timewood");
        this.addDerivativeSource(SulfurRegistry.TRANSWOOD_LOG, "Transwood");
        this.addDerivativeSource(SulfurRegistry.SORTINGWOOD_LOG, "Sortingwood");
        this.addDerivativeSource(SulfurRegistry.MINEWOOD_LOG, "Minewood");
        this.addDerivativeSource(SulfurRegistry.SMOGSTEM_LOG, "Smogstem");
        this.addDerivativeSource(SulfurRegistry.WIGGLEWOOD_LOG, "Wigglewood");
        this.addDerivativeSource(SulfurRegistry.GRONGLE_LOG, "Grongle");
        this.addDerivativeSource(SulfurRegistry.RUBBERWOOD_LOG, "Rubberwood");
        this.addDerivativeSource(SulfurRegistry.OTHERWORLD_LOG, "Otherworld");

        //Add source names for crop sulfurs:
        this.addDerivativeSource(SulfurRegistry.BEETROOT, "Beetroot");
        this.addDerivativeSource(SulfurRegistry.CARROT, "Carrot");
        this.addDerivativeSource(SulfurRegistry.POTATO, "Potato");
        this.addDerivativeSource(SulfurRegistry.WHEAT, "Wheat");
        this.addDerivativeSource(SulfurRegistry.APPLE, "Apple");
        this.addDerivativeSource(SulfurRegistry.COCOA, "Cocoa");
        this.addDerivativeSource(SulfurRegistry.NETHER_WART, "Nether Wart");
        this.addDerivativeSource(SulfurRegistry.ARTICHOKE, "Artichoke");
        this.addDerivativeSource(SulfurRegistry.ASPARAGUS, "Asparagus");
        this.addDerivativeSource(SulfurRegistry.BARLEY, "Barley");
        this.addDerivativeSource(SulfurRegistry.BASIL, "Basil");
        this.addDerivativeSource(SulfurRegistry.BELLPEPPER, "Bell Pepper");
        this.addDerivativeSource(SulfurRegistry.BLACKBEAN, "Black Bean");
        this.addDerivativeSource(SulfurRegistry.BLACKBERRY, "Blackberry");
        this.addDerivativeSource(SulfurRegistry.BLUEBERRY, "Blueberry");
        this.addDerivativeSource(SulfurRegistry.BROCCOLI, "Broccoli");
        this.addDerivativeSource(SulfurRegistry.CABBAGE, "Cabbage");
        this.addDerivativeSource(SulfurRegistry.CANTALOUPE, "Cantaloupe");
        this.addDerivativeSource(SulfurRegistry.CAULIFLOWER, "Cauliflower");
        this.addDerivativeSource(SulfurRegistry.CELERY, "Celery");
        this.addDerivativeSource(SulfurRegistry.CHILE_PEPPER, "Chile Pepper");
        this.addDerivativeSource(SulfurRegistry.COFFEE_BEANS, "Coffee Beans");
        this.addDerivativeSource(SulfurRegistry.CORN, "Corn");
        this.addDerivativeSource(SulfurRegistry.CRANBERRY, "Cranberry");
        this.addDerivativeSource(SulfurRegistry.CUCUMBER, "Cucumber");
        this.addDerivativeSource(SulfurRegistry.CURRANT, "Currant");
        this.addDerivativeSource(SulfurRegistry.EGGPLANT, "Eggplant");
        this.addDerivativeSource(SulfurRegistry.ELDERBERRY, "Elderberry");
        this.addDerivativeSource(SulfurRegistry.GARLIC, "Garlic");
        this.addDerivativeSource(SulfurRegistry.GINGER, "Ginger");
        this.addDerivativeSource(SulfurRegistry.GRAPE, "Grape");
        this.addDerivativeSource(SulfurRegistry.GREENBEAN, "Green Bean");
        this.addDerivativeSource(SulfurRegistry.GREENONION, "Green Onion");
        this.addDerivativeSource(SulfurRegistry.HONEYDEW, "Honeydew");
        this.addDerivativeSource(SulfurRegistry.HOPS, "Hops");
        this.addDerivativeSource(SulfurRegistry.KALE, "Kale");
        this.addDerivativeSource(SulfurRegistry.KIWI, "Kiwi");
        this.addDerivativeSource(SulfurRegistry.LEEK, "Leek");
        this.addDerivativeSource(SulfurRegistry.LETTUCE, "Lettuce");
        this.addDerivativeSource(SulfurRegistry.MUSTARD, "Mustard");
        this.addDerivativeSource(SulfurRegistry.OAT, "Oat");
        this.addDerivativeSource(SulfurRegistry.OLIVE, "Olive");
        this.addDerivativeSource(SulfurRegistry.ONION, "Onion");
        this.addDerivativeSource(SulfurRegistry.PEANUT, "Peanut");
        this.addDerivativeSource(SulfurRegistry.PEPPER, "Pepper");
        this.addDerivativeSource(SulfurRegistry.PINEAPPLE, "Pineapple");
        this.addDerivativeSource(SulfurRegistry.RADISH, "Radish");
        this.addDerivativeSource(SulfurRegistry.RASPBERRY, "Raspberry");
        this.addDerivativeSource(SulfurRegistry.RHUBARB, "Rhubarb");
        this.addDerivativeSource(SulfurRegistry.RICE, "Rice");
        this.addDerivativeSource(SulfurRegistry.RUTABAGA, "Rutabaga");
        this.addDerivativeSource(SulfurRegistry.SAGUARO, "Saguaro");
        this.addDerivativeSource(SulfurRegistry.SOYBEAN, "Soybean");
        this.addDerivativeSource(SulfurRegistry.SPINACH, "Spinach");
        this.addDerivativeSource(SulfurRegistry.SQUASH, "Squash");
        this.addDerivativeSource(SulfurRegistry.STRAWBERRY, "Strawberry");
        this.addDerivativeSource(SulfurRegistry.SWEETPOTATO, "Sweet Potato");
        this.addDerivativeSource(SulfurRegistry.TEA_LEAVES, "Tea Leaves");
        this.addDerivativeSource(SulfurRegistry.TOMATILLO, "Tomatillo");
        this.addDerivativeSource(SulfurRegistry.TOMATO, "Tomato");
        this.addDerivativeSource(SulfurRegistry.TURMERIC, "Turmeric");
        this.addDerivativeSource(SulfurRegistry.TURNIP, "Turnip");
        this.addDerivativeSource(SulfurRegistry.VANILLA, "Vanilla");
        this.addDerivativeSource(SulfurRegistry.YAM, "Yam");
        this.addDerivativeSource(SulfurRegistry.ZUCCHINI, "Zucchini");
        this.addDerivativeSource(SulfurRegistry.FLAX, "Flax");
        this.addDerivativeSource(SulfurRegistry.JUNIPERBERRY, "Juniperberry");
        this.addDerivativeSource(SulfurRegistry.ALMOND, "Almond");
        this.addDerivativeSource(SulfurRegistry.APRICOT, "Apricot");
        this.addDerivativeSource(SulfurRegistry.AVOCADO, "Avocado");
        this.addDerivativeSource(SulfurRegistry.BANANA, "Banana");
        this.addDerivativeSource(SulfurRegistry.CASHEW, "Cashew");
        this.addDerivativeSource(SulfurRegistry.CHERRY, "Cherry");
        this.addDerivativeSource(SulfurRegistry.COCONUT, "Coconut");
        this.addDerivativeSource(SulfurRegistry.DATE, "Date");
        this.addDerivativeSource(SulfurRegistry.DRAGONFRUIT, "Dragonfruit");
        this.addDerivativeSource(SulfurRegistry.FIG, "Fig");
        this.addDerivativeSource(SulfurRegistry.GRAPEFRUIT, "Grapefruit");
        this.addDerivativeSource(SulfurRegistry.KUMQUAT, "Kumquat");
        this.addDerivativeSource(SulfurRegistry.LEMON, "Lemon");
        this.addDerivativeSource(SulfurRegistry.LIME, "Lime");
        this.addDerivativeSource(SulfurRegistry.MANDARIN, "Mandarin");
        this.addDerivativeSource(SulfurRegistry.MANGO, "Mango");
        this.addDerivativeSource(SulfurRegistry.NECTARINE, "Nectarine");
        this.addDerivativeSource(SulfurRegistry.NUTMEG, "Nutmeg");
        this.addDerivativeSource(SulfurRegistry.ORANGE, "Orange");
        this.addDerivativeSource(SulfurRegistry.PEACH, "Peach");
        this.addDerivativeSource(SulfurRegistry.PEAR, "Pear");
        this.addDerivativeSource(SulfurRegistry.PECAN, "Pecan");
        this.addDerivativeSource(SulfurRegistry.PERSIMMON, "Persimmon");
        this.addDerivativeSource(SulfurRegistry.PLUM, "Plum");

        //Creature Parts
        this.addDerivativeSource(SulfurRegistry.PORKCHOP, "Porkchop");
        this.addDerivativeSource(SulfurRegistry.BEEF, "Beef");
        this.addDerivativeSource(SulfurRegistry.MUTTON, "Mutton");
        this.addDerivativeSource(SulfurRegistry.CHICKEN, "Chicken");
        this.addDerivativeSource(SulfurRegistry.EGG, "Egg");
        this.addDerivativeSource(SulfurRegistry.INK_SAC, "Ink Sac");
        this.addDerivativeSource(SulfurRegistry.GLOW_INK_SAC, "Glow Ink Sac");
        this.addDerivativeSource(SulfurRegistry.RABBIT, "Rabbit");
        this.addDerivativeSource(SulfurRegistry.RABBIT_HIDE, "Rabbit Hide");
        this.addDerivativeSource(SulfurRegistry.RABBIT_FOOT, "Rabbit Foot");
        this.addDerivativeSource(SulfurRegistry.LEATHER, "Leather");
        this.addDerivativeSource(SulfurRegistry.FEATHER, "Feather");
        this.addDerivativeSource(SulfurRegistry.WOOL, "Wool");
        this.addDerivativeSource(SulfurRegistry.COD, "Cod");
        this.addDerivativeSource(SulfurRegistry.SALMON, "Salmon");
        this.addDerivativeSource(SulfurRegistry.TROPICAL_FISH, "Tropical Fish");
        this.addDerivativeSource(SulfurRegistry.PUFFERFISH, "Pufferfish");
        this.addDerivativeSource(SulfurRegistry.TURTLE_SCUTE, "Turtle Scute");
        this.addDerivativeSource(SulfurRegistry.ARMADILLO_SCUTE, "Armadillo Scute");

        //Earthly Matters
        this.addDerivativeSource(SulfurRegistry.DIRT, "Dirt");
        this.addDerivativeSource(SulfurRegistry.COARSE_DIRT, "Coarse Dirt");
        this.addDerivativeSource(SulfurRegistry.PODZOL, "Podzol");
        this.addDerivativeSource(SulfurRegistry.GRASS_BLOCK, "Grass Block");
        this.addDerivativeSource(SulfurRegistry.ROOTED_DIRT, "Rooted Dirt");
        this.addDerivativeSource(SulfurRegistry.MOSS_BLOCK, "Moss Block");
        this.addDerivativeSource(SulfurRegistry.MUD, "Mud");
        this.addDerivativeSource(SulfurRegistry.MUDDY_MANGROVE_ROOTS, "Muddy Mangrove Roots");

        this.addDerivativeSource(SulfurRegistry.SAND, "Sand");
        this.addDerivativeSource(SulfurRegistry.RED_SAND, "Red Sand");
        this.addDerivativeSource(SulfurRegistry.GRAVEL, "Gravel");
        this.addDerivativeSource(SulfurRegistry.NETHERRACK, "Netherrack");
        this.addDerivativeSource(SulfurRegistry.SOUL_SAND, "Soul Sand");
        this.addDerivativeSource(SulfurRegistry.SOUL_SOIL, "Soul Soil");

        this.addDerivativeSource(SulfurRegistry.STONE, "Stone");
        this.addDerivativeSource(SulfurRegistry.INFESTED_STONE, "Infested Stone");
        this.addDerivativeSource(SulfurRegistry.COBBLESTONE, "Cobblestone");
        this.addDerivativeSource(SulfurRegistry.COBBLESTONE_MOSSY, "Mossy Cobblestone");
        this.addDerivativeSource(SulfurRegistry.COBBLESTONES_INFESTED, "Infested Cobblestone");

        this.addDerivativeSource(SulfurRegistry.DEEPSLATE, "Deepslate");
        this.addDerivativeSource(SulfurRegistry.COBBLESTONE_DEEPSLATE, "Deepslate Cobblestone");

        this.addDerivativeSource(SulfurRegistry.GRANITE, "Granite");
        this.addDerivativeSource(SulfurRegistry.DIORITE, "Diorite");
        this.addDerivativeSource(SulfurRegistry.ANDESITE, "Andesite");
        this.addDerivativeSource(SulfurRegistry.BLACKSTONE, "Blackstone");
        this.addDerivativeSource(SulfurRegistry.BASALT, "Basalt");

        this.addDerivativeSource(SulfurRegistry.SANDSTONE, "Sandstone");
        this.addDerivativeSource(SulfurRegistry.RED_SANDSTONE, "Red Sandstone");

        this.addDerivativeSource(SulfurRegistry.CLAY, "Clay");
        this.addDerivativeSource(SulfurRegistry.TERRACOTTA, "Terracotta");
        this.addDerivativeSource(SulfurRegistry.CRIMSON_NYLIUM, "Crimson Nylium");
        this.addDerivativeSource(SulfurRegistry.WARPED_NYLIUM, "Warped Nylium");
        this.addDerivativeSource(SulfurRegistry.END_STONE, "End Stone");
        this.addDerivativeSource(SulfurRegistry.PURPUR_BLOCK, "Purpur Block");
        this.addDerivativeSource(SulfurRegistry.MYCELIUM, "Mycelium");

        //Common Metals
        this.addDerivativeSource(SulfurRegistry.IRON, "Iron");
        this.addDerivativeSource(SulfurRegistry.COPPER, "Copper");
        this.addDerivativeSource(SulfurRegistry.SILVER, "Silver");
        this.addDerivativeSource(SulfurRegistry.GOLD, "Gold");
        this.addDerivativeSource(SulfurRegistry.NETHERITE, "Netherite");
        this.addDerivativeSource(SulfurRegistry.URANIUM, "Uranium");
        this.addDerivativeSource(SulfurRegistry.URANINITE, "Uraninite");
        this.addDerivativeSource(SulfurRegistry.AZURE_SILVER, "Azure Silver");
        this.addDerivativeSource(SulfurRegistry.ZINC, "Zinc");
        this.addDerivativeSource(SulfurRegistry.OSMIUM, "Osmium");
        this.addDerivativeSource(SulfurRegistry.NICKEL, "Nickel");
        this.addDerivativeSource(SulfurRegistry.LEAD, "Lead");
        this.addDerivativeSource(SulfurRegistry.ALLTHEMODIUM, "Allthemodium");
        this.addDerivativeSource(SulfurRegistry.UNOBTAINIUM, "Unobtainium");
        this.addDerivativeSource(SulfurRegistry.IRIDIUM, "Iridium");
        this.addDerivativeSource(SulfurRegistry.TIN, "Tin");
        this.addDerivativeSource(SulfurRegistry.CINNABAR, "Cinnabar");
        this.addDerivativeSource(SulfurRegistry.CRIMSON_IRON, "Crimson Iron");
        this.addDerivativeSource(SulfurRegistry.PLATINUM, "Platinum");
        this.addDerivativeSource(SulfurRegistry.VIBRANIUM, "Vibranium");
        this.addDerivativeSource(SulfurRegistry.DESH, "Desh");
        this.addDerivativeSource(SulfurRegistry.OSTRUM, "Ostrum");
        this.addDerivativeSource(SulfurRegistry.CALORITE, "Calorite");
        this.addDerivativeSource(SulfurRegistry.IESNIUM, "Iesnium");

        //Common Gems
        this.addDerivativeSource(SulfurRegistry.DIAMOND, "Diamond");
        this.addDerivativeSource(SulfurRegistry.EMERALD, "Emerald");
        this.addDerivativeSource(SulfurRegistry.LAPIS, "Lapis");
        this.addDerivativeSource(SulfurRegistry.QUARTZ, "Quartz");
        this.addDerivativeSource(SulfurRegistry.AMETHYST, "Amethyst");
        this.addDerivativeSource(SulfurRegistry.PRISMARINE, "Prismarine");
        this.addDerivativeSource(SulfurRegistry.RUBY, "Ruby");
        this.addDerivativeSource(SulfurRegistry.APATITE, "Apatite");
        this.addDerivativeSource(SulfurRegistry.PERIDOT, "Peridot");
        this.addDerivativeSource(SulfurRegistry.FLUORITE, "Fluorite");
        this.addDerivativeSource(SulfurRegistry.SAPPHIRE, "Sapphire");
        this.addDerivativeSource(SulfurRegistry.DARK_GEM, "Dark Gem");
        this.addDerivativeSource(SulfurRegistry.SAL_AMMONIAC, "Sal Ammoniac");
        this.addDerivativeSource(SulfurRegistry.CERTUS_QUARTZ, "Certus Quartz");
        this.addDerivativeSource(SulfurRegistry.FLUIX, "Fluix");
        this.addDerivativeSource(SulfurRegistry.NITER, "Niter");
        this.addDerivativeSource(SulfurRegistry.CHIMERITE, "Chimerite");
        this.addDerivativeSource(SulfurRegistry.DEMONITE, "Demonite");

        //Other Common Minerals
        this.addDerivativeSource(SulfurRegistry.REDSTONE, "Redstone");
        this.addDerivativeSource(SulfurRegistry.COAL, "Coal");
        this.addDerivativeSource(SulfurRegistry.SULFUR, "Sulfur");
        this.addDerivativeSource(SulfurRegistry.GLOWSTONE, "Glowstone");
    }

    private void addSalts() {
        //Salt source names used in automatic name rendering
        this.addDerivativeSource(SaltRegistry.MINERAL, "Minerals");
        this.addDerivativeSource(SaltRegistry.PLANT, "Plants");
        this.addDerivativeSource(SaltRegistry.STRATA, "Strata");
        this.addExtendedTooltip(SaltRegistry.STRATA.get()::asItem,
                "Salt extracted from the strata, that is, sedimentary rock, soil, clay and so on.");
        this.addDerivativeSource(SaltRegistry.CREATURE, "Creatures");

        //Automatic salt name rendering
        SaltRegistry.SALTS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalSaltItem.class::cast).forEach(salt -> {

            this.addItem(() -> salt, "Alchemical Salt %s");

            this.addTooltip(() -> salt,
                    "Alchemical Salt calcinated from %s.",
                    null,
                    "Salt represents the \"body\" or \"physical matter\" of an object.");
        });

    }

    public void addDerivativeSource(Supplier<? extends Item> key, String name) {
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_SOURCE_SUFFIX, name);
    }

    public void addIngredientInfo(Supplier<Item> ingredient, String info) {
        this.add("jei." + Theurgy.MODID + ".ingredient." +
                BuiltInRegistries.ITEM.getKey(ingredient.get()).getPath() + ".description", info);
    }

    private void addDivinationRods() {
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK, "The divination rod is " + ChatFormatting.RED + "not attuned" + ChatFormatting.RESET + " to any material.");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO, "The divination rod is attuned to %s");
        this.add(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT, "Found %s at %s.");

        this.add(TheurgyConstants.I18n.Item.DIVINATION_ROD_UNKNOWN_LINKED_BLOCK, "Unknown Block (something went wrong)");


        var divinationRodUsage = this.f("""
                        {0} a block to attune the rod to it.
                        {1} to let the rod search for blocks.
                        {2} after a successful search to let the rod show the last found block without consuming durability.
                        """,
                this.green("Crouch-Click"),
                this.green("Right-Click and hold"),
                this.green("Right-Click without holding")
        );

        var sulfurAttunedDivinationRodUsage = this.f("""
                        {0} the rod with a type of Alchemical Sulfur to attune the rod to it.
                        {1} to let the rod search for blocks.
                        {2} after a successful search to let the rod show the last found block without consuming durability.
                        """,
                this.green("Craft"),
                this.green("Right-Click and hold"),
                this.green("Right-Click without holding")
        );

        var amethystDivinationRodUsage = this.f("""
                        {0} the rod, it is automatically attuned to Amethyst.
                        {1} to let the rod search for blocks.
                        {2} after a successful search to let the rod show the last found block without consuming durability.
                        """,
                this.green("Craft"),
                this.green("Right-Click and hold"),
                this.green("Right-Click without holding")
        );

        this.addItem(ItemRegistry.DIVINATION_ROD_T1, "Glass Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T1.get().getDescriptionId() + ".linked", "Glass Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T1,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                divinationRodUsage);

        this.addItem(ItemRegistry.DIVINATION_ROD_T2, "Iron Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T2.get().getDescriptionId() + ".linked", "Iron Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T2,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                divinationRodUsage);

        this.addItem(ItemRegistry.DIVINATION_ROD_T3, "Diamond Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T3.get().getDescriptionId() + ".linked", "Diamond Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T3,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                divinationRodUsage);

        this.addItem(ItemRegistry.DIVINATION_ROD_T4, "Netherite Divination Rod");
        this.add(ItemRegistry.DIVINATION_ROD_T4.get().getDescriptionId() + ".linked", "Netherite Divination Rod %s");
        //tooltip is handled by rod itself
        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T4,
                "Divination rods can be attuned to a block and are then used to find other blocks of the same type.",
                divinationRodUsage);

        this.addItem(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT, "Divination Rod: Abundant Materials");
        this.add(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get().getDescriptionId() + ".linked", "Divination Rod: Abundant Materials %s");
        this.addTooltip(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT,
                "A divination rod attuned to find abundant materials by crafting it with an abundant alchemical sulfur.",
                "This type of divination rod is crafted pre-attuned to find blocks of the a specific type.",
                sulfurAttunedDivinationRodUsage);

        this.addItem(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON, "Divination Rod: Common Materials");
        this.add(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get().getDescriptionId() + ".linked", "Divination Rod: Common Materials %s");
        this.addTooltip(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON,
                "A divination rod attuned to find common materials by crafting it with a common alchemical sulfur.",
                "This type of divination rod is crafted pre-attuned to find blocks of the a specific type.",
                sulfurAttunedDivinationRodUsage);

        this.addItem(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE, "Divination Rod: Rare Materials");
        this.add(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get().getDescriptionId() + ".linked", "Divination Rod: Rare Materials %s");
        this.addTooltip(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE,
                "A divination rod attuned to find rare materials by crafting it with a rare alchemical sulfur.",
                "This type of divination rod is crafted pre-attuned to find blocks of the a specific type.",
                sulfurAttunedDivinationRodUsage);

        this.addItem(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS, "Divination Rod: Precious Materials");
        this.add(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get().getDescriptionId() + ".linked", "Divination Rod: Precious Materials %s");
        this.addTooltip(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS,
                "A divination rod attuned to find precious materials by crafting it with a precious alchemical sulfur.",
                "This type of divination rod is crafted pre-attuned to find blocks of the a specific type.",
                sulfurAttunedDivinationRodUsage);

        this.addItem(ItemRegistry.AMETHYST_DIVINATION_ROD, "Divination Rod: Amethyst");
        this.add(ItemRegistry.AMETHYST_DIVINATION_ROD.get().getDescriptionId() + ".linked", "Divination Rod: %s");
        this.addTooltip(ItemRegistry.AMETHYST_DIVINATION_ROD,
                "A divination rod attuned to find amethyst.",
                "This type of divination rod is crafted pre-attuned to find amethysts.",
                amethystDivinationRodUsage);
    }

    private void addItems() {
        this.add(TheurgyConstants.I18n.ITEM_GROUP, "Theurgy");

        this.addSalts();
        this.addSulfurs();
        this.addDivinationRods();
        this.addItem(ItemRegistry.EMPTY_JAR_ICON, "Empty Jar Icon");
        this.addTooltip(ItemRegistry.EMPTY_JAR_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.EMPTY_JAR_IRON_BAND_ICON, "Empty Jar with Iron Band Icon");
        this.addTooltip(ItemRegistry.EMPTY_JAR_IRON_BAND_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.EMPTY_CERAMIC_JAR_ICON, "Empty Ceramic Jar Icon");
        this.addTooltip(ItemRegistry.EMPTY_CERAMIC_JAR_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.EMPTY_JAR_LABELED_ICON, "Labeled Empty Jar Icon");
        this.addTooltip(ItemRegistry.EMPTY_JAR_LABELED_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.EMPTY_CERAMIC_JAR_LABELED_ICON, "Labeled Empty Ceramic Jar Icon");
        this.addTooltip(ItemRegistry.EMPTY_CERAMIC_JAR_LABELED_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.JAR_LABEL_ICON, "Jar Label Icon");
        this.addTooltip(ItemRegistry.JAR_LABEL_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON, "Jar Label Frame Abundant Icon");
        this.addTooltip(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON, "Jar Label Frame Common Icon");
        this.addTooltip(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON, "Jar Label Frame Rare Icon");
        this.addTooltip(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON, "Jar Label Frame Precious Icon");
        this.addTooltip(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.THE_HERMETICA_ICON, "The Hermetica Icon");
        this.addTooltip(ItemRegistry.THE_HERMETICA_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.GEMS_ABUNDANT_ICON, "Abundant Gems Icon");
        this.addTooltip(ItemRegistry.GEMS_ABUNDANT_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.GEMS_COMMON_ICON, "Common Gems Icon");
        this.addTooltip(ItemRegistry.GEMS_COMMON_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.GEMS_RARE_ICON, "Rare Gems Icon");
        this.addTooltip(ItemRegistry.GEMS_RARE_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.GEMS_PRECIOUS_ICON, "Precious Gems Icon");
        this.addTooltip(ItemRegistry.GEMS_PRECIOUS_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.METALS_ABUNDANT_ICON, "Abundant Metals Icon");
        this.addTooltip(ItemRegistry.METALS_ABUNDANT_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.METALS_COMMON_ICON, "Common Metals Icon");
        this.addTooltip(ItemRegistry.METALS_COMMON_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.METALS_RARE_ICON, "Rare Metals Icon");
        this.addTooltip(ItemRegistry.METALS_RARE_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.METALS_PRECIOUS_ICON, "Precious Metals Icon");
        this.addTooltip(ItemRegistry.METALS_PRECIOUS_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, "Abundant Other Minerals Icon");
        this.addTooltip(ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.OTHER_MINERALS_COMMON_ICON, "Common Other Minerals Icon");
        this.addTooltip(ItemRegistry.OTHER_MINERALS_COMMON_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.OTHER_MINERALS_RARE_ICON, "Rare Other Minerals Icon");
        this.addTooltip(ItemRegistry.OTHER_MINERALS_RARE_ICON, "Dummy item for rendering.");
        this.addItem(ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, "Precious Other Minerals Icon");
        this.addTooltip(ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, "Dummy item for rendering.");

        this.addItem(ItemRegistry.SAL_AMMONIAC_BUCKET, "Sal Ammoniac Bucket");
        this.addItem(ItemRegistry.COPPER_WIRE, "Mercurial Copper Wire");
        this.addTooltip(ItemRegistry.COPPER_WIRE,
                "A piece of copper wire capable of transferring matter in its mercurial form.",
                "Can be used to connect different parts of Mercurial Logistics Networks.",
                """
                        Right-click one connector, then right-click another connector to connect them with the wire.
                        """
        );

        this.addItem(ItemRegistry.MERCURIAL_WAND, "Mercurial Wand");
        var wandUsage = this.f("""
                        {0} to use current mode.
                        Hold {1} and {3} to cycle through modes.
                        """,
//these two are only relevant if we use the "Select Direction Mode" again, which we currently don't
//                        Hold {1} and {2} and {3} to use "Select Direction Mode".
//                        {0} without any keys held down in case a mode gets stuck.
//                        """,
                this.green("Right-Click"),
                this.green("Crouch"),
                this.green("Right Mouse Button"),
                this.green("Scroll")
        );
        this.addTooltip(ItemRegistry.MERCURIAL_WAND,
                "Definitely not just a wrench.",
                "Allows configuring alchemical apparatuses and mercurial logistics networks.",
                wandUsage
        );


        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_DIRECTION, "Select direction (Currently: %s)");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_DIRECTION_SUCCESS, "Direction: %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION, "Set direction to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION_WITH_TARGET, "Set direction from %s to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION_SUCCESS, "Set direction to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION, "Cycle selected direction");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION_WITH_TARGET, "Set direction from %s to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION_SUCCESS, "Cycle direction to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED, "Enable/Disable");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_HUD, " (Currently: %s)");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_SUCCESS, "Logistics Connector is now %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_ENABLED, "Enabled");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_DISABLED, "Disabled");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_FREQUENCY, "Selected frequency: %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_FREQUENCY, "Set frequency to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_FREQUENCY_WITH_TARGET, "Set frequency from %s to %s");
        this.add(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_FREQUENCY_SUCCESS, "Set frequency to %s");

        this.addItem(ItemRegistry.LIST_FILTER, "Mercurial List Filter");
        this.addUsageTooltip(ItemRegistry.LIST_FILTER,
                this.f("""
                        {0} the air to open the filter GUI and add items.
                        {0} a logistics inserter or extractor to apply the filter.
                        {0} a filtered block with an empty hand to remove the filter.
                        """,
                        this.green("Right-Click")
                )
        );
        this.addItem(ItemRegistry.ATTRIBUTE_FILTER, "Mercurial Attribute Filter");
        this.addUsageTooltip(ItemRegistry.ATTRIBUTE_FILTER,
                this.f("""
                        {0} the air to open the filter GUI and add items.
                        {0} a logistics inserter or extractor to apply the filter.
                        {0} a filtered block with an empty hand to remove the filter.
                        """,
                        this.green("Right-Click")
                )
        );

        this.addItem(ItemRegistry.MERCURY_SHARD, "Mercury Shard");
        this.addExtendedTooltip(ItemRegistry.MERCURY_SHARD,
                "Mercury shards are small pieces of Mercury in crystalline form. Their main uses are as ingredient in Digestion processes and as an energy source.");

        this.addItem(ItemRegistry.MERCURY_CRYSTAL, "Mercury Crystal");
        this.addExtendedTooltip(ItemRegistry.MERCURY_CRYSTAL,
                "Mercury crystals are large pieces of Mercury in crystalline form. Their main uses are as ingredient in Digestion processes and as an energy source.");

        this.addItem(ItemRegistry.SAL_AMMONIAC_CRYSTAL, "Sal Ammoniac Crystal");
        this.addExtendedTooltip(ItemRegistry.SAL_AMMONIAC_CRYSTAL,
                """
                        Obtained by mining Sal Ammoniac Ore, or by crafting a Sal Ammoniac Bucket in a crafting grid.
                        """);
        this.addUsageTooltip(ItemRegistry.SAL_AMMONIAC_CRYSTAL,
                """
                        Can be used in a Sal Ammoniac Accumulator to rapidly create Sal Ammoniac to be used as a solvent.
                        """);
        this.addIngredientInfo(ItemRegistry.SAL_AMMONIAC_CRYSTAL, "Obtained by mining Sal Ammoniac Ore.");

        this.addItem(ItemRegistry.PURIFIED_GOLD, "Purified Gold");
        this.addTooltip(ItemRegistry.PURIFIED_GOLD,
                "Alchemically pure gold.",
                """
                        Gold that has been purified by alchemical Digestion. This further improves the property of Gold not to react with other elements, allowing it to be used in alchemical processes without adding impurities to the result.
                        """,
                "Acts as a catalysator, enabling various alchemical processes."
        );

        this.addItem(ItemRegistry.FERMENTATION_STARTER, "Fermentation Starter");
        this.addTooltip(ItemRegistry.FERMENTATION_STARTER,
                "Fermentation aid for recipes in the Fermentation Vat.",
                """
                        An extract of sugar and plant material that exhibits a high fermentation potential. Can be used as a more efficient alternative to raw sugar or crops in the Fermentation Vat.
                        """
        );
    }

    protected void addItemAttributes() {
        this.addItemAttribute(ItemAttribute.addedBy, false, "was added by %1$s");
        this.addItemAttribute(ItemAttribute.addedBy, true, "was not added by %1$s");
        this.addItemAttribute(StandardAttributes.BADLY_DAMAGED, false, "is heavily damaged");
        this.addItemAttribute(StandardAttributes.BADLY_DAMAGED, true, "is not heavily damaged");
        this.addItemAttribute(StandardAttributes.BLASTABLE, false, "can be Smelted in a Blast Furnace");
        this.addItemAttribute(StandardAttributes.BLASTABLE, true, "cannot be Smelted in a Blast Furnace");
        this.addItemAttribute(StandardAttributes.COMPOSTABLE, false, "can be composted");
        this.addItemAttribute(StandardAttributes.COMPOSTABLE, true, "cannot be composted");
        this.addItemAttribute(StandardAttributes.CONSUMABLE, false, "can be eaten");
        this.addItemAttribute(StandardAttributes.CONSUMABLE, true, "cannot be eaten");
        this.addItemAttribute(StandardAttributes.DAMAGED, false, "is damaged");
        this.addItemAttribute(StandardAttributes.DAMAGED, true, "is not damaged");
        this.addItemAttribute(StandardAttributes.ENCHANTED, false, "is enchanted");
        this.addItemAttribute(StandardAttributes.ENCHANTED, true, "is unenchanted");
        this.addItemAttribute(StandardAttributes.EQUIPABLE, false, "can be equipped");
        this.addItemAttribute(StandardAttributes.EQUIPABLE, true, "cannot be equipped");
        this.addItemAttribute(StandardAttributes.FLUID_CONTAINER, false, "can store fluids");
        this.addItemAttribute(StandardAttributes.FLUID_CONTAINER, true, "cannot store fluids");
        this.addItemAttribute(StandardAttributes.FURNACE_FUEL, false, "is furnace fuel");
        this.addItemAttribute(StandardAttributes.FURNACE_FUEL, true, "is not furnace fuel");
        this.addItemAttribute(EnchantAttribute.EMPTY, false, "is enchanted with %1$s");
        this.addItemAttribute(EnchantAttribute.EMPTY, true, "is not enchanted with %1$s");
        this.addItemAttribute(FluidContentsAttribute.EMPTY, false, "contains %1$s");
        this.addItemAttribute(FluidContentsAttribute.EMPTY, true, "does not contain %1$s");
        this.addItemAttribute(ItemNameAttribute.DUMMY, false, "has the custom name %1$s");
        this.addItemAttribute(ItemNameAttribute.DUMMY, true, "does not have the custom name %1$s");
        this.addItemAttribute(InTagAttribute.DUMMY, false, "is tagged %1$s");
        this.addItemAttribute(InTagAttribute.DUMMY, true, "is not tagged %1$s");
        this.addItemAttribute(StandardAttributes.MAX_ENCHANTED, false, "is enchanted at max level");
        this.addItemAttribute(StandardAttributes.MAX_ENCHANTED, true, "is not enchanted at max level");
        this.addItemAttribute(StandardAttributes.NOT_STACKABLE, false, "cannot be stacked");
        this.addItemAttribute(StandardAttributes.NOT_STACKABLE, true, "can be stacked");
        this.addItemAttribute(StandardAttributes.PLACEABLE, false, "is placeable");
        this.addItemAttribute(StandardAttributes.PLACEABLE, true, "is not placeable");
        this.addItemAttribute(StandardAttributes.RENAMED, false, "has a custom name");
        this.addItemAttribute(StandardAttributes.RENAMED, true, "does not have a custom name");
        this.addItemAttribute(StandardAttributes.SMELTABLE, false, "can be Smelted");
        this.addItemAttribute(StandardAttributes.SMELTABLE, true, "cannot be Smelted");
        this.addItemAttribute(StandardAttributes.SMOKABLE, false, "can be Smoked");
        this.addItemAttribute(StandardAttributes.SMOKABLE, true, "cannot be Smoked");
    }

    protected void addItemAttribute(ItemAttribute attribute, boolean inverted, String text) {
        this.add("item_attributes." + attribute.getTranslationKey() + (inverted ? ".inverted" : ""), text);
    }

    private void addConfigurationTranslations() {
        this.addConfig("recipes", "Recipe Settings");
        this.addConfig("sulfurSourceToBlockMapping", "Sulfur Source to Block Mapping");

        this.addConfig("rendering", "Rendering Settings");
        this.addConfig("renderSulfurSourceItem", "Render Sulfur Source Items");
        this.addConfig("enableItemHUD", "Enable Item HUD");
        this.addConfig("hudScale", "HUD Scale");

        this.addConfig("misc", "Misc Settings");
    }

    private void addConfig(String key, String name) {
        this.add(Theurgy.MODID + ".configuration." + key, name);
    }


    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addKeys();
        this.addSubtitles();
        this.addMessages();
        this.addItems();
        this.addBlocks();
        this.addFluids();
        this.addIntegrations();
        this.addBehaviours();
        this.addGui();

        this.addItemAttributes();
        this.addConfigurationTranslations();
    }


    @Override
    public void add(String key, String name) {
        super.add(key, name);
    }
}
