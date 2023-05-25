/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.AlchemicalSaltItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ENUSProvider extends LanguageProvider implements TooltipLanguageProvider {
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

    private void addJEI() {
        this.add(TheurgyConstants.I18n.JEI.CALCINATION_CATEGORY, "Calcination");
        this.add(TheurgyConstants.I18n.JEI.LIQUEFACTION_CATEGORY, "Liquefaction");
        this.add(TheurgyConstants.I18n.JEI.DISTILLATION_CATEGORY, "Distillation");
        this.add(TheurgyConstants.I18n.JEI.INCUBATION_CATEGORY, "Incubation");
        this.add(TheurgyConstants.I18n.JEI.ACCUMULATION_CATEGORY, "Accumulation");
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
                                Place this on top of a heating device such as a Pyromantic Brazier.
                                {0} with water buckets to fill the accumulator.
                                The water will be consumed to fill the accumulator.
                                You can additionally add Sal Ammoniac Crystals to speed up the process.
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

        this.addBlock(BlockRegistry.SAL_AMMONIAC_ORE, "Sal Ammoniac Ore");
        this.addExtendedTooltip(BlockRegistry.SAL_AMMONIAC_ORE.get()::asItem,
                "Ore that yields Sal Ammoniac Crystals for use in a Sal Ammoniac Accumulator.");

        this.addBlock(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE, "Deepslate Sal Ammoniac Ore");
        this.addExtendedTooltip(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()::asItem,
                "Ore that yields Sal Ammoniac Crystals for use in a Sal Ammoniac Accumulator.");
    }

    private void addSulfurs() {
        this.add(TheurgyConstants.I18n.Item.ALCHEMICAL_SULFUR_UNKNOWN_SOURCE, "Unknown Source");

        //Automatic sulfur name rendering
        SulfurRegistry.SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
            if (sulfur.useAutomaticNameRendering) {
                this.addItem(() -> sulfur, "Alchemical Sulfur %s");
            }
            if (sulfur.provideAutomaticTooltipData) {
                this.addTooltip(() -> sulfur,
                        "Alchemical Sulfur crafted from %s.",
                        null,
                        "Sulfur represents the \"idea\" or \"soul\" of an object and is the key to replication and transmutation.");
            }
        });

        //Tag Names for Sulfurs with overrideTagSourceName
        this.add(Util.makeDescriptionId("tag", ItemTags.LOGS.location()), "Logs");

        //Note: It was considered to try and warn here if a sulfur has overrideTagSourceName set to true, but no override lang key set.
        //      This is not possible, however, as the tag source comes from item nbt that is not available at this point.
    }

    private void addSalts() {
        //Salt source names used in automatic name rendering
        this.addSaltSource(SaltRegistry.MINERAL, "Minerals");
        this.addSaltSource(SaltRegistry.CROPS, "Crops");
        this.addSaltSource(SaltRegistry.STRATA, "Strata");
        this.addExtendedTooltip(SaltRegistry.STRATA.get()::asItem,
                "Salt extracted from the strata, that is, sedimentary rock, soil, clay and so on.");

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

//        this.addItem(ItemRegistry.DIVINATION_ROD_T4, "Emerald Divination Rod");
//        this.add(ItemRegistry.DIVINATION_ROD_T4.get().getDescriptionId() + ".linked", "Emerald Divination Rod %s");
//        //tooltip is handled by rod itself
//        this.addExtendedTooltip(ItemRegistry.DIVINATION_ROD_T4,
//                "This divination rod type cannot be manually attuned, instead comes pre-attuned after crafting..",
//                divinationRodUsage);

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

        this.addItem(ItemRegistry.SAL_AMMONIAC_CRYSTAL, "Sal Ammoniac Crystal");
        this.addExtendedTooltip(ItemRegistry.SAL_AMMONIAC_CRYSTAL,
                "Can be used in a Sal Ammoniac Accumulator to rapidly create Sal Ammoniac to be used as a solvent.");
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
