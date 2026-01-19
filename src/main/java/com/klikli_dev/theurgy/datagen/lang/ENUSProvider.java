// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurType;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
    }

    private void addGenericSulfur(AlchemicalSulfurItem sulfur) {
        this.addItem(() -> sulfur, "Alchemical Niter %s");
        this.addTooltip(() -> sulfur,
                "Alchemical Niter crafted from Alchemical Sulfur of any %s.",
                "Niter represents the abstract category and value of an object, thus it is a further abstraction the \"idea\" or \"soul\" represented by Sulfur.",
                "Niter extraction is a required intermediate step to transform one type of Sulfur into another type.");
    }
    private void addSulfurs() {
        this.add(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE, "Unknown Source");

        this.add(AlchemicalSulfurTier.ABUNDANT.descriptionId(), "Abundant");
        this.add(AlchemicalSulfurTier.COMMON.descriptionId(), "Common");
        this.add(AlchemicalSulfurTier.RARE.descriptionId(), "Rare");
        this.add(AlchemicalSulfurTier.PRECIOUS.descriptionId(), "Precious");

        this.add(AlchemicalSulfurType.MISC.descriptionId(), "Misc");
        this.add(AlchemicalSulfurType.EARTHEN_MATTERS.descriptionId(), "Earthen Matters");
        this.add(AlchemicalSulfurType.METALS.descriptionId(), "Metals");
        this.add(AlchemicalSulfurType.GEMS.descriptionId(), "Gems");
        this.add(AlchemicalSulfurType.OTHER_MINERALS.descriptionId(), "Other Minerals");
        this.add(AlchemicalSulfurType.LOGS.descriptionId(), "Logs");
        this.add(AlchemicalSulfurType.CROPS.descriptionId(), "Crops");
        this.add(AlchemicalSulfurType.ANIMALS.descriptionId(), "Animal Parts");
        this.add(AlchemicalSulfurType.MOBS.descriptionId(), "Mob Drops");
        this.add(AlchemicalSulfurType.NITER.descriptionId(), "Niter");


        //Automatic sulfur name rendering
        SulfurRegistry.SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
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

        //Tag Names for Sulfurs with overrideTagSourceName
        this.add(Util.makeDescriptionId("tag", ItemTags.LOGS.location()), "Logs");
        //Note: It was considered to try and warn here if a sulfur has overrideTagSourceName set to true, but no override lang key set.
        //      This is not possible, however, as the tag source comes from item nbt that is not available at this point.




        //Names for generic sulfurs
        this.addSulfurSource(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT, "Abundant Earthen Matters");
        this.addGenericSulfur(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.EARTHEN_MATTERS_COMMON, "Common Earthen Matters");
        this.addGenericSulfur(SulfurRegistry.EARTHEN_MATTERS_COMMON.get());

        this.addSulfurSource(SulfurRegistry.GEMS_ABUNDANT, "Abundant Gems");
        this.addGenericSulfur(SulfurRegistry.GEMS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.GEMS_COMMON, "Common Gems");
        this.addGenericSulfur(SulfurRegistry.GEMS_COMMON.get());
        this.addSulfurSource(SulfurRegistry.GEMS_RARE, "Rare Gems");
        this.addGenericSulfur(SulfurRegistry.GEMS_RARE.get());
        this.addSulfurSource(SulfurRegistry.GEMS_PRECIOUS, "Precious Gems");
        this.addGenericSulfur(SulfurRegistry.GEMS_PRECIOUS.get());

        this.addSulfurSource(SulfurRegistry.METALS_ABUNDANT, "Abundant Metals");
        this.addGenericSulfur(SulfurRegistry.METALS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.METALS_COMMON, "Common Metals");
        this.addGenericSulfur(SulfurRegistry.METALS_COMMON.get());
        this.addSulfurSource(SulfurRegistry.METALS_RARE, "Rare Metals");
        this.addGenericSulfur(SulfurRegistry.METALS_RARE.get());
        this.addSulfurSource(SulfurRegistry.METALS_PRECIOUS, "Precious Metals");
        this.addGenericSulfur(SulfurRegistry.METALS_PRECIOUS.get());

        this.addSulfurSource(SulfurRegistry.OTHER_MINERALS_ABUNDANT, "Abundant Other Minerals");
        this.addGenericSulfur(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.OTHER_MINERALS_COMMON, "Common Other Minerals");
        this.addGenericSulfur(SulfurRegistry.OTHER_MINERALS_COMMON.get());
        this.addSulfurSource(SulfurRegistry.OTHER_MINERALS_RARE, "Rare Other Minerals");
        this.addGenericSulfur(SulfurRegistry.OTHER_MINERALS_RARE.get());
        this.addSulfurSource(SulfurRegistry.OTHER_MINERALS_PRECIOUS, "Precious Other Minerals");
        this.addGenericSulfur(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get());

        this.addSulfurSource(SulfurRegistry.LOGS_ABUNDANT, "Abundant Logs");
        this.addGenericSulfur(SulfurRegistry.LOGS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.CROPS_ABUNDANT, "Abundant Crops");
        this.addGenericSulfur(SulfurRegistry.CROPS_ABUNDANT.get());

        this.addSulfurSource(SulfurRegistry.ANIMALS_ABUNDANT, "Abundant Animal Parts");
        this.addGenericSulfur(SulfurRegistry.ANIMALS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.ANIMALS_COMMON, "Common Animal Parts");
        this.addGenericSulfur(SulfurRegistry.ANIMALS_COMMON.get());
        this.addSulfurSource(SulfurRegistry.ANIMALS_RARE, "Rare Animal Parts");
        this.addGenericSulfur(SulfurRegistry.ANIMALS_RARE.get());


        this.addSulfurSource(SulfurRegistry.MOBS_ABUNDANT, "Abundant Mob Drops");
        this.addGenericSulfur(SulfurRegistry.MOBS_ABUNDANT.get());
        this.addSulfurSource(SulfurRegistry.MOBS_COMMON, "Common Mob Drops");
        this.addGenericSulfur(SulfurRegistry.MOBS_COMMON.get());
        this.addSulfurSource(SulfurRegistry.MOBS_RARE, "Rare Mob Drops");
        this.addGenericSulfur(SulfurRegistry.MOBS_RARE.get());
        this.addSulfurSource(SulfurRegistry.MOBS_PRECIOUS, "Precious Mob Drops");
        this.addGenericSulfur(SulfurRegistry.MOBS_PRECIOUS.get());

        //Names for Sulfurs with overrideSourceName
        this.addSulfurSource(SulfurRegistry.OAK_LOG, "Oak");
        this.addSulfurSource(SulfurRegistry.SPRUCE_LOG, "Spruce");
        this.addSulfurSource(SulfurRegistry.BIRCH_LOG, "Birch");
        this.addSulfurSource(SulfurRegistry.JUNGLE_LOG, "Jungle");
        this.addSulfurSource(SulfurRegistry.ACACIA_LOG, "Acacia");
        this.addSulfurSource(SulfurRegistry.CHERRY_LOG, "Cherry");
        this.addSulfurSource(SulfurRegistry.DARK_OAK_LOG, "Dark Oak");
        this.addSulfurSource(SulfurRegistry.MANGROVE_LOG, "Mangrove");
        this.addSulfurSource(SulfurRegistry.CRIMSON_STEM, "Crimson");
        this.addSulfurSource(SulfurRegistry.WARPED_STEM, "Warped");

        this.addSulfurSource(SulfurRegistry.ROWAN_LOG, "Rowan");
        this.addSulfurSource(SulfurRegistry.FIR_LOG, "Fir");
        this.addSulfurSource(SulfurRegistry.REDWOOD_LOG, "Redwood");
        this.addSulfurSource(SulfurRegistry.MAHOGANY_LOG, "Mahogany");
        this.addSulfurSource(SulfurRegistry.JACARANDA_LOG, "Jacaranda");
        this.addSulfurSource(SulfurRegistry.PALM_LOG, "Palm");
        this.addSulfurSource(SulfurRegistry.WILLOW_LOG, "Willow");
        this.addSulfurSource(SulfurRegistry.DEAD_LOG, "Dead");
        this.addSulfurSource(SulfurRegistry.MAGIC_LOG, "Magic");
        this.addSulfurSource(SulfurRegistry.UMBRAN_LOG, "Umbran");
        this.addSulfurSource(SulfurRegistry.HELLBARK_LOG, "Hellbark");
        this.addSulfurSource(SulfurRegistry.CINNAMON_LOG, "Cinnamon");
        this.addSulfurSource(SulfurRegistry.GLACIAN_LOG, "Glacian");
        this.addSulfurSource(SulfurRegistry.ARCHWOOD_LOG, "Archwood");
        this.addSulfurSource(SulfurRegistry.BLUEBRIGHT_LOG, "Bluebright");
        this.addSulfurSource(SulfurRegistry.STARLIT_LOG, "Starlit");
        this.addSulfurSource(SulfurRegistry.FROSTBRIGHT_LOG, "Frostbright");
        this.addSulfurSource(SulfurRegistry.COMET_LOG, "Comet");
        this.addSulfurSource(SulfurRegistry.LUNAR_LOG, "Lunar");
        this.addSulfurSource(SulfurRegistry.DUSK_LOG, "Dusk");
        this.addSulfurSource(SulfurRegistry.MAPLE_LOG, "Maple");
        this.addSulfurSource(SulfurRegistry.CRYSTALLIZED_LOG, "Crystallized");
        this.addSulfurSource(SulfurRegistry.LIVINGWOOD_LOG, "Livingwood");
        this.addSulfurSource(SulfurRegistry.GLIMMERING_LIVINGWOOD_LOG, "Glimmering Livingwood");
        this.addSulfurSource(SulfurRegistry.DREAMWOOD_LOG, "Dreamwood");
        this.addSulfurSource(SulfurRegistry.GLIMMERING_DREAMWOOD_LOG, "Glimmering Dreamwood");
        this.addSulfurSource(SulfurRegistry.WALNUT_LOG, "Walnut");
        this.addSulfurSource(SulfurRegistry.FIG_LOG, "Fig");
        this.addSulfurSource(SulfurRegistry.WOLFBERRY_LOG, "Wolfberry");
        this.addSulfurSource(SulfurRegistry.ECHO_LOG, "Echo");
        this.addSulfurSource(SulfurRegistry.ILLWOOD_LOG, "Illwood");
        this.addSulfurSource(SulfurRegistry.UNDEAD_LOG, "Undead");
        this.addSulfurSource(SulfurRegistry.AURUM_LOG, "Aurum");
        this.addSulfurSource(SulfurRegistry.MENRIL_LOG, "Menril");
        this.addSulfurSource(SulfurRegistry.ASHEN_LOG, "Ashen");
        this.addSulfurSource(SulfurRegistry.AZALEA_LOG, "Azalea");
        this.addSulfurSource(SulfurRegistry.TRUMPET_LOG, "Trumpet");
        this.addSulfurSource(SulfurRegistry.NETHERWOOD_LOG, "Netherwood");
        this.addSulfurSource(SulfurRegistry.SKYROOT_LOG, "Skyroot");
        this.addSulfurSource(SulfurRegistry.GOLDEN_OAK_LOG, "Golden Oak");
        this.addSulfurSource(SulfurRegistry.TWILIGHT_OAK_LOG, "Twilight Oak");
        this.addSulfurSource(SulfurRegistry.CANOPY_TREE_LOG, "Canopy Tree");
        this.addSulfurSource(SulfurRegistry.DARKWOOD_LOG, "Darkwood");
        this.addSulfurSource(SulfurRegistry.TIMEWOOD_LOG, "Timewood");
        this.addSulfurSource(SulfurRegistry.TRANSWOOD_LOG, "Transwood");
        this.addSulfurSource(SulfurRegistry.SORTINGWOOD_LOG, "Sortingwood");
        this.addSulfurSource(SulfurRegistry.MINEWOOD_LOG, "Minewood");
        this.addSulfurSource(SulfurRegistry.SMOGSTEM_LOG, "Smogstem");
        this.addSulfurSource(SulfurRegistry.WIGGLEWOOD_LOG, "Wigglewood");
        this.addSulfurSource(SulfurRegistry.GRONGLE_LOG, "Grongle");
        this.addSulfurSource(SulfurRegistry.RUBBERWOOD_LOG, "Rubberwood");
        this.addSulfurSource(SulfurRegistry.OTHERWORLD_LOG, "Otherworld");

        //Add source names for crop sulfurs:
        this.addSulfurSource(SulfurRegistry.BEETROOT, "Beetroot");
        this.addSulfurSource(SulfurRegistry.CARROT, "Carrot");
        this.addSulfurSource(SulfurRegistry.POTATO, "Potato");
        this.addSulfurSource(SulfurRegistry.WHEAT, "Wheat");
        this.addSulfurSource(SulfurRegistry.APPLE, "Apple");
        this.addSulfurSource(SulfurRegistry.COCOA, "Cocoa");
        this.addSulfurSource(SulfurRegistry.NETHER_WART, "Nether Wart");
        this.addSulfurSource(SulfurRegistry.ARTICHOKE, "Artichoke");
        this.addSulfurSource(SulfurRegistry.ASPARAGUS, "Asparagus");
        this.addSulfurSource(SulfurRegistry.BARLEY, "Barley");
        this.addSulfurSource(SulfurRegistry.BASIL, "Basil");
        this.addSulfurSource(SulfurRegistry.BELLPEPPER, "Bell Pepper");
        this.addSulfurSource(SulfurRegistry.BLACKBEAN, "Black Bean");
        this.addSulfurSource(SulfurRegistry.BLACKBERRY, "Blackberry");
        this.addSulfurSource(SulfurRegistry.BLUEBERRY, "Blueberry");
        this.addSulfurSource(SulfurRegistry.BROCCOLI, "Broccoli");
        this.addSulfurSource(SulfurRegistry.CABBAGE, "Cabbage");
        this.addSulfurSource(SulfurRegistry.CANTALOUPE, "Cantaloupe");
        this.addSulfurSource(SulfurRegistry.CAULIFLOWER, "Cauliflower");
        this.addSulfurSource(SulfurRegistry.CELERY, "Celery");
        this.addSulfurSource(SulfurRegistry.CHILE_PEPPER, "Chile Pepper");
        this.addSulfurSource(SulfurRegistry.COFFEE_BEANS, "Coffee Beans");
        this.addSulfurSource(SulfurRegistry.CORN, "Corn");
        this.addSulfurSource(SulfurRegistry.CRANBERRY, "Cranberry");
        this.addSulfurSource(SulfurRegistry.CUCUMBER, "Cucumber");
        this.addSulfurSource(SulfurRegistry.CURRANT, "Currant");
        this.addSulfurSource(SulfurRegistry.EGGPLANT, "Eggplant");
        this.addSulfurSource(SulfurRegistry.ELDERBERRY, "Elderberry");
        this.addSulfurSource(SulfurRegistry.GARLIC, "Garlic");
        this.addSulfurSource(SulfurRegistry.GINGER, "Ginger");
        this.addSulfurSource(SulfurRegistry.GRAPE, "Grape");
        this.addSulfurSource(SulfurRegistry.GREENBEAN, "Green Bean");
        this.addSulfurSource(SulfurRegistry.GREENONION, "Green Onion");
        this.addSulfurSource(SulfurRegistry.HONEYDEW, "Honeydew");
        this.addSulfurSource(SulfurRegistry.HOPS, "Hops");
        this.addSulfurSource(SulfurRegistry.KALE, "Kale");
        this.addSulfurSource(SulfurRegistry.KIWI, "Kiwi");
        this.addSulfurSource(SulfurRegistry.LEEK, "Leek");
        this.addSulfurSource(SulfurRegistry.LETTUCE, "Lettuce");
        this.addSulfurSource(SulfurRegistry.MUSTARD, "Mustard");
        this.addSulfurSource(SulfurRegistry.OAT, "Oat");
        this.addSulfurSource(SulfurRegistry.OLIVE, "Olive");
        this.addSulfurSource(SulfurRegistry.ONION, "Onion");
        this.addSulfurSource(SulfurRegistry.PEANUT, "Peanut");
        this.addSulfurSource(SulfurRegistry.PEPPER, "Pepper");
        this.addSulfurSource(SulfurRegistry.PINEAPPLE, "Pineapple");
        this.addSulfurSource(SulfurRegistry.RADISH, "Radish");
        this.addSulfurSource(SulfurRegistry.RASPBERRY, "Raspberry");
        this.addSulfurSource(SulfurRegistry.RHUBARB, "Rhubarb");
        this.addSulfurSource(SulfurRegistry.RICE, "Rice");
        this.addSulfurSource(SulfurRegistry.RUTABAGA, "Rutabaga");
        this.addSulfurSource(SulfurRegistry.SAGUARO, "Saguaro");
        this.addSulfurSource(SulfurRegistry.SOYBEAN, "Soybean");
        this.addSulfurSource(SulfurRegistry.SPINACH, "Spinach");
        this.addSulfurSource(SulfurRegistry.SQUASH, "Squash");
        this.addSulfurSource(SulfurRegistry.STRAWBERRY, "Strawberry");
        this.addSulfurSource(SulfurRegistry.SWEETPOTATO, "Sweet Potato");
        this.addSulfurSource(SulfurRegistry.TEA_LEAVES, "Tea Leaves");
        this.addSulfurSource(SulfurRegistry.TOMATILLO, "Tomatillo");
        this.addSulfurSource(SulfurRegistry.TOMATO, "Tomato");
        this.addSulfurSource(SulfurRegistry.TURMERIC, "Turmeric");
        this.addSulfurSource(SulfurRegistry.TURNIP, "Turnip");
        this.addSulfurSource(SulfurRegistry.VANILLA, "Vanilla");
        this.addSulfurSource(SulfurRegistry.YAM, "Yam");
        this.addSulfurSource(SulfurRegistry.ZUCCHINI, "Zucchini");
        this.addSulfurSource(SulfurRegistry.FLAX, "Flax");
        this.addSulfurSource(SulfurRegistry.JUNIPERBERRY, "Juniperberry");
        this.addSulfurSource(SulfurRegistry.ALMOND, "Almond");
        this.addSulfurSource(SulfurRegistry.APRICOT, "Apricot");
        this.addSulfurSource(SulfurRegistry.AVOCADO, "Avocado");
        this.addSulfurSource(SulfurRegistry.BANANA, "Banana");
        this.addSulfurSource(SulfurRegistry.CASHEW, "Cashew");
        this.addSulfurSource(SulfurRegistry.CHERRY, "Cherry");
        this.addSulfurSource(SulfurRegistry.COCONUT, "Coconut");
        this.addSulfurSource(SulfurRegistry.DATE, "Date");
        this.addSulfurSource(SulfurRegistry.DRAGONFRUIT, "Dragonfruit");
        this.addSulfurSource(SulfurRegistry.FIG, "Fig");
        this.addSulfurSource(SulfurRegistry.GRAPEFRUIT, "Grapefruit");
        this.addSulfurSource(SulfurRegistry.KUMQUAT, "Kumquat");
        this.addSulfurSource(SulfurRegistry.LEMON, "Lemon");
        this.addSulfurSource(SulfurRegistry.LIME, "Lime");
        this.addSulfurSource(SulfurRegistry.MANDARIN, "Mandarin");
        this.addSulfurSource(SulfurRegistry.MANGO, "Mango");
        this.addSulfurSource(SulfurRegistry.NECTARINE, "Nectarine");
        this.addSulfurSource(SulfurRegistry.NUTMEG, "Nutmeg");
        this.addSulfurSource(SulfurRegistry.ORANGE, "Orange");
        this.addSulfurSource(SulfurRegistry.PEACH, "Peach");
        this.addSulfurSource(SulfurRegistry.PEAR, "Pear");
        this.addSulfurSource(SulfurRegistry.PECAN, "Pecan");
        this.addSulfurSource(SulfurRegistry.PERSIMMON, "Persimmon");
        this.addSulfurSource(SulfurRegistry.PLUM, "Plum");

        //Mob Drops
        this.addSulfurSource(SulfurRegistry.ROTTEN_FLESH, "Rotten Flesh");
        this.addSulfurSource(SulfurRegistry.SPIDER_EYE, "Spider Eye");
        this.addSulfurSource(SulfurRegistry.STRING, "String");
        this.addSulfurSource(SulfurRegistry.GUNPOWDER, "Gunpowder");
        this.addSulfurSource(SulfurRegistry.BONE, "Bone");
        this.addSulfurSource(SulfurRegistry.ARROW, "Arrow");
        this.addSulfurSource(SulfurRegistry.SLIMEBALL, "Slimeball");
        this.addSulfurSource(SulfurRegistry.ENDER_PEARL, "Ender Pearl");
        this.addSulfurSource(SulfurRegistry.BLAZE_ROD, "Blaze Rod");
        this.addSulfurSource(SulfurRegistry.PRISMARINE_SHARD, "Prismarine Shard");
        this.addSulfurSource(SulfurRegistry.PHANTOM_MEMBRANE, "Phantom Membrane");
        this.addSulfurSource(SulfurRegistry.MAGMA_CREAM, "Magma cream");
        this.addSulfurSource(SulfurRegistry.SKELETON_SKULL, "Skeleton Skull");
        this.addSulfurSource(SulfurRegistry.WITHER_SKELETON_SKULL, "Wither Skeleton Skull");
        this.addSulfurSource(SulfurRegistry.GHAST_TEAR, "Ghast Tear");
        this.addSulfurSource(SulfurRegistry.SHULKER_SHELL, "Shulker Shell");
        this.addSulfurSource(SulfurRegistry.ELYTRA, "Elytra");
        this.addSulfurSource(SulfurRegistry.NETHER_STAR, "Nether Star");
        this.addSulfurSource(SulfurRegistry.DRAGON_EGG, "Dragon Egg");
        this.addSulfurSource(SulfurRegistry.HEART_OF_THE_SEA, "Heart Of The Sea");

        //Creature Parts
        this.addSulfurSource(SulfurRegistry.PORKCHOP, "Porkchop");
        this.addSulfurSource(SulfurRegistry.BEEF, "Beef");
        this.addSulfurSource(SulfurRegistry.MUTTON, "Mutton");
        this.addSulfurSource(SulfurRegistry.CHICKEN, "Chicken");
        this.addSulfurSource(SulfurRegistry.EGG, "Egg");
        this.addSulfurSource(SulfurRegistry.INK_SAC, "Ink Sac");
        this.addSulfurSource(SulfurRegistry.GLOW_INK_SAC, "Glow Ink Sac");
        this.addSulfurSource(SulfurRegistry.RABBIT, "Rabbit");
        this.addSulfurSource(SulfurRegistry.RABBIT_HIDE, "Rabbit Hide");
        this.addSulfurSource(SulfurRegistry.RABBIT_FOOT, "Rabbit Foot");
        this.addSulfurSource(SulfurRegistry.LEATHER, "Leather");
        this.addSulfurSource(SulfurRegistry.FEATHER, "Feather");
        this.addSulfurSource(SulfurRegistry.WOOL, "Wool");
        this.addSulfurSource(SulfurRegistry.COD, "Cod");
        this.addSulfurSource(SulfurRegistry.SALMON, "Salmon");
        this.addSulfurSource(SulfurRegistry.TROPICAL_FISH, "Tropical Fish");
        this.addSulfurSource(SulfurRegistry.PUFFERFISH, "Pufferfish");
        this.addSulfurSource(SulfurRegistry.TURTLE_SCUTE, "Turtle Scute");

        //Earthly Matters
        this.addSulfurSource(SulfurRegistry.DIRT, "Dirt");
        this.addSulfurSource(SulfurRegistry.COARSE_DIRT, "Coarse Dirt");
        this.addSulfurSource(SulfurRegistry.PODZOL, "Podzol");
        this.addSulfurSource(SulfurRegistry.GRASS_BLOCK, "Grass Block");
        this.addSulfurSource(SulfurRegistry.ROOTED_DIRT, "Rooted Dirt");
        this.addSulfurSource(SulfurRegistry.MOSS_BLOCK, "Moss Block");
        this.addSulfurSource(SulfurRegistry.MUD, "Mud");
        this.addSulfurSource(SulfurRegistry.MUDDY_MANGROVE_ROOTS, "Muddy Mangrove Roots");

        this.addSulfurSource(SulfurRegistry.SAND, "Sand");
        this.addSulfurSource(SulfurRegistry.RED_SAND, "Red Sand");
        this.addSulfurSource(SulfurRegistry.GRAVEL, "Gravel");
        this.addSulfurSource(SulfurRegistry.NETHERRACK, "Netherrack");
        this.addSulfurSource(SulfurRegistry.SOUL_SAND, "Soul Sand");
        this.addSulfurSource(SulfurRegistry.SOUL_SOIL, "Soul Soil");

        this.addSulfurSource(SulfurRegistry.STONE, "Stone");
        this.addSulfurSource(SulfurRegistry.INFESTED_STONE, "Infested Stone");
        this.addSulfurSource(SulfurRegistry.COBBLESTONE, "Cobblestone");
        this.addSulfurSource(SulfurRegistry.COBBLESTONE_MOSSY, "Mossy Cobblestone");
        this.addSulfurSource(SulfurRegistry.COBBLESTONE_INFESTED, "Infested Cobblestone");

        this.addSulfurSource(SulfurRegistry.DEEPSLATE, "Deepslate");
        this.addSulfurSource(SulfurRegistry.COBBLESTONE_DEEPSLATE, "Deepslate Cobblestone");

        this.addSulfurSource(SulfurRegistry.GRANITE, "Granite");
        this.addSulfurSource(SulfurRegistry.DIORITE, "Diorite");
        this.addSulfurSource(SulfurRegistry.ANDESITE, "Andesite");
        this.addSulfurSource(SulfurRegistry.BLACKSTONE, "Blackstone");
        this.addSulfurSource(SulfurRegistry.BASALT, "Basalt");

        this.addSulfurSource(SulfurRegistry.SANDSTONE, "Sandstone");
        this.addSulfurSource(SulfurRegistry.RED_SANDSTONE, "Red Sandstone");

        this.addSulfurSource(SulfurRegistry.CLAY, "Clay");
        this.addSulfurSource(SulfurRegistry.TERRACOTTA, "Terracotta");
        this.addSulfurSource(SulfurRegistry.CRIMSON_NYLIUM, "Crimson Nylium");
        this.addSulfurSource(SulfurRegistry.WARPED_NYLIUM, "Warped Nylium");
        this.addSulfurSource(SulfurRegistry.END_STONE, "End Stone");
        this.addSulfurSource(SulfurRegistry.PURPUR_BLOCK, "Purpur Block");
        this.addSulfurSource(SulfurRegistry.MYCELIUM, "Mycelium");
        this.addSulfurSource(SulfurRegistry.OBSIDIAN, "Obsidian");
        this.addSulfurSource(SulfurRegistry.CRYING_OBSIDIAN, "Crying Obsidian");

        this.addSulfurSource(SulfurRegistry.SNOW, "Snow");
        this.addSulfurSource(SulfurRegistry.ICE, "Ice");
        this.addSulfurSource(SulfurRegistry.MAGMA, "Magma");
        this.addSulfurSource(SulfurRegistry.WATER, "Water");
        this.addSulfurSource(SulfurRegistry.LAVA, "Lava");
        
        //Common Metals
        this.addSulfurSource(SulfurRegistry.IRON, "Iron");
        this.addSulfurSource(SulfurRegistry.COPPER, "Copper");
        this.addSulfurSource(SulfurRegistry.SILVER, "Silver");
        this.addSulfurSource(SulfurRegistry.GOLD, "Gold");
        this.addSulfurSource(SulfurRegistry.NETHERITE, "Netherite");
        this.addSulfurSource(SulfurRegistry.URANIUM, "Uranium");
        this.addSulfurSource(SulfurRegistry.AZURE_SILVER, "Azure Silver");
        this.addSulfurSource(SulfurRegistry.ZINC, "Zinc");
        this.addSulfurSource(SulfurRegistry.OSMIUM, "Osmium");
        this.addSulfurSource(SulfurRegistry.NICKEL, "Nickel");
        this.addSulfurSource(SulfurRegistry.LEAD, "Lead");
        this.addSulfurSource(SulfurRegistry.ALLTHEMODIUM, "Allthemodium");
        this.addSulfurSource(SulfurRegistry.UNOBTAINIUM, "Unobtainium");
        this.addSulfurSource(SulfurRegistry.IRIDIUM, "Iridium");
        this.addSulfurSource(SulfurRegistry.TIN, "Tin");
        this.addSulfurSource(SulfurRegistry.CINNABAR, "Cinnabar");
        this.addSulfurSource(SulfurRegistry.CRIMSON_IRON, "Crimson Iron");
        this.addSulfurSource(SulfurRegistry.PLATINUM, "Platinum");
        this.addSulfurSource(SulfurRegistry.VIBRANIUM, "Vibranium");
        this.addSulfurSource(SulfurRegistry.DESH, "Desh");
        this.addSulfurSource(SulfurRegistry.OSTRUM, "Ostrum");
        this.addSulfurSource(SulfurRegistry.CALORITE, "Calorite");
        this.addSulfurSource(SulfurRegistry.IESNIUM, "Iesnium");
        this.addSulfurSource(SulfurRegistry.ANTIMONY, "Antimony");
        this.addSulfurSource(SulfurRegistry.TITANIUM, "Titanium");
        this.addSulfurSource(SulfurRegistry.TUNGSTEN, "Tungsten");

        //Common Gems
        this.addSulfurSource(SulfurRegistry.DIAMOND, "Diamond");
        this.addSulfurSource(SulfurRegistry.EMERALD, "Emerald");
        this.addSulfurSource(SulfurRegistry.LAPIS, "Lapis");
        this.addSulfurSource(SulfurRegistry.QUARTZ, "Quartz");
        this.addSulfurSource(SulfurRegistry.AMETHYST, "Amethyst");
        this.addSulfurSource(SulfurRegistry.PRISMARINE, "Prismarine");
        this.addSulfurSource(SulfurRegistry.RUBY, "Ruby");
        this.addSulfurSource(SulfurRegistry.APATITE, "Apatite");
        this.addSulfurSource(SulfurRegistry.PERIDOT, "Peridot");
        this.addSulfurSource(SulfurRegistry.FLUORITE, "Fluorite");
        this.addSulfurSource(SulfurRegistry.SAPPHIRE, "Sapphire");
        this.addSulfurSource(SulfurRegistry.SAL_AMMONIAC, "Sal Ammoniac");
        this.addSulfurSource(SulfurRegistry.CERTUS_QUARTZ, "Certus Quartz");
        this.addSulfurSource(SulfurRegistry.FLUIX, "Fluix");
        this.addSulfurSource(SulfurRegistry.NITER, "Niter");
        this.addSulfurSource(SulfurRegistry.CHIMERITE, "Chimerite");
        this.addSulfurSource(SulfurRegistry.DEMONITE, "Demonite");

        //Other Common Minerals
        this.addSulfurSource(SulfurRegistry.REDSTONE, "Redstone");
        this.addSulfurSource(SulfurRegistry.COAL, "Coal");
        this.addSulfurSource(SulfurRegistry.SULFUR, "Sulfur");
        this.addSulfurSource(SulfurRegistry.GLOWSTONE, "Glowstone");
        this.addSulfurSource(SulfurRegistry.LIGNITE_COAL, "Lignite Coal");
        this.addSulfurSource(SulfurRegistry.MONAZITE, "Monazite");
        this.addSulfurSource(SulfurRegistry.BAUXITE, "Bauxite");
    }

    private void addSalts() {
        //Salt source names used in automatic name rendering
        this.addSaltSource(SaltRegistry.MINERAL, "Minerals");
        this.addSaltSource(SaltRegistry.PLANT, "Plants");
        this.addSaltSource(SaltRegistry.STRATA, "Strata");
        this.addExtendedTooltip(SaltRegistry.STRATA.get()::asItem,
                "Salt extracted from the strata, that is, sedimentary rock, soil, clay and so on.");
        this.addSaltSource(SaltRegistry.CREATURE, "Creatures");

        //Automatic salt name rendering
        SaltRegistry.SALTS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSaltItem.class::cast).forEach(salt -> {

            this.addItem(() -> salt, "Alchemical Salt %s");

            this.addTooltip(() -> salt,
                    "Alchemical Salt calcinated from %s.",
                    null,
                    "Salt represents the \"body\" or \"physical matter\" of an object.");
        });

    }

    public void addSaltSource(Supplier<? extends Item> key, String name) {
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_SALT_SOURCE_SUFFIX, name);
    }

    public void addSulfurSource(Supplier<? extends Item> key, String name) {
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_SOURCE_SUFFIX, name);
    }


    public void addIngredientInfo(Supplier<Item> ingredient, String info) {
        this.add("jei." + Theurgy.MODID + ".ingredient." + ForgeRegistries.ITEMS.getKey(ingredient.get()).getPath() + ".description", info);
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

        this.addItem(ItemRegistry.CRYSTALLIZED_WATER, "Crystallized Water");
        this.addTooltip(ItemRegistry.CRYSTALLIZED_WATER, "Water in solid form for easy transportation, and alchemical processing.");
        this.addItem(ItemRegistry.CRYSTALLIZED_LAVA, "Crystallized Lava");
        this.addTooltip(ItemRegistry.CRYSTALLIZED_LAVA, "Lava in solid form for easy transportation, and alchemical processing.");
    }

    @Override
    protected void addTranslations() {
        this.addMisc();
        this.addSubtitles();
        this.addMessages();
        this.addItems();
        this.addBlocks();
        this.addFluids();
        this.addIntegrations();
        this.addBehaviours();
    }
}
