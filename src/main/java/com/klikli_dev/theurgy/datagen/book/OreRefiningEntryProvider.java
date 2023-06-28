/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * A dummy provider to separate the generation of ore refining entries from the rest of the getting started category
 */
public class OreRefiningEntryProvider extends CategoryProvider {

    public OreRefiningEntryProvider(TheurgyBookProvider parent, CategoryEntryMap entryMap) {
        super(parent, "dummy");
        this.entryMap = entryMap;
    }

    public TheurgyBookProvider parent() {
        return (TheurgyBookProvider) this.parent;
    }

    @Override
    protected String[] generateEntryMap() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    @Override
    protected BookCategoryModel generateCategory() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    public BookEntryModel.Builder makeAboutOreRefiningEntry(char icon) {
        this.context().entry("about_ore_refining");
        this.add(this.context().entryName(), "Ore Refining");
        this.add(this.context().entryDescription(), "Triple your ore yield - at a cost!");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.IRON_ORE))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Ore Duplication");
        this.add(this.context().pageText(),
                """
                    In the following pages and entries we will attempt to create three iron ingots out of just one iron ore using alchemical processes.
                        """
        );

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Spagyrics for Refining");
        this.add(this.context().pageText(),
                """
                        The easiest application of Spagyrics is the refining of ores. The process of smelting ores in a furnace is wasteful, as it only yields a single ingot per ore, losing a lot of the precious raw materials in the process. Alchemists can extract even the last iota of value from ores, but the process is somewhat more complex.
                        """);

        this.context().page("overview");
        var overview = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Process");
        this.add(this.context().pageText(),
                """
                        To refine an Ore you first need to {0} it, which will yield multiple Ore Sulfur. Additionally you need to obtain multiple heaps of {1} to provide a body for this multiplied Sulfur, and some {2} to provide the mercury for the soul of the resulting refined ingots.
                            """,
                this.entryLink("liquefy", SpagyricsCategoryProvider.CATEGORY_ID, "liquefaction_cauldron"),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get()),
                this.itemLink("Mercury Shards", ItemRegistry.MERCURY_SHARD.get())
        );

        this.context().page("cost");
        var cost = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Cost");
        this.add(this.context().pageText(),
                """
                        To bring it to a point, the cost of significantly increasing ore yield with Spagyrics is having to obtain high amounts of {0} which is mainly sourced from ... Metals.
                            """,
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );

        this.context().page("cost2");
        var cost2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Cost");
        this.add(this.context().pageText(),
                """
                        The first and obvious solution to this chicken-and-egg problem is to {0} abundant metals, such as {1}, and use the salt to {2} more valuable materials such as {3} or even {4} as output.
                            """,
                this.entryLink("calcinate", SpagyricsCategoryProvider.CATEGORY_ID, "calcination_oven"),
                this.itemLink(Items.RAW_COPPER),
                this.entryLink("incubate", SpagyricsCategoryProvider.CATEGORY_ID, "incubator"),
                this.itemLink(Items.IRON_INGOT),
                this.itemLink(Items.DIAMOND)
        );

        this.context().page("silver_lining");
        var silverLining = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Silver Lining");
        this.add(this.context().pageText(),
                """
                        As luck would have it another option to obtain {0} is to calcinate it from {1}, which is in turn obtained by calcinating Sand, Cobblestone, etc. This is a somewhat lossy process as it requires a lot of {1}, but it does have the upside of giving those abundant materials a use.
                            """,
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get()),
                this.itemLink("Strata Salt", SaltRegistry.STRATA.get())
        );

        this.context().page("soul");
        var soul = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Soul");
        this.add(this.context().pageText(),
                """
                        Obtaining {0} is usually not much of an issue as a wide variety of materials can be {1} to obtain in.
                            """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get()),
                this.entryLink("distilled", SpagyricsCategoryProvider.CATEGORY_ID, "distiller")
        );

        this.context().page("next");
        var next = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Further Reading");
        this.add(this.context().pageText(),
                """
                        The next entries will guide you through the process of obtaining all the materials and creating your iron ingots.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(Items.IRON_ORE)
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2,
                        overview,
                        cost,
                        cost2,
                        silverLining,
                        soul,
                        next
                );
    }

    public BookEntryModel.Builder makeNeededApparatusEntry(char icon) {
        this.context().entry("needed_apparatus");
        this.add(this.context().entryName(), "Required Apparatus");
        this.add(this.context().entryDescription(), "Tools for Refinement");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Required Apparatus");
        this.add(this.context().pageText(),
                """
                        Ore Refining needs all of the Spagyrics Apparatus to extract all the needed materials and recombine them.
                        Review the full {0} Category on how to craft and use them.
                        """,
                this.categoryLink("Spagyrics", SpagyricsCategoryProvider.CATEGORY_ID)
        );

        this.context().page("pyromantic_brazier");
        var pyromantic_brazier = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.PYROMANTIC_BRAZIER.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        You will need 4 of these to power the other Apparatus.
                        """
        );

        this.context().page("calcination_oven");
        var calcination_oven = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.CALCINATION_OVEN.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The Calcination Oven will allow you to create the {0}.
                        """,
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );

        this.context().page("sal_ammoniac_accumulator");
        var sal_ammoniac_accumulator = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The Sal Ammoniac Accumulator will fill the Sal Ammoniac Tank with solvent.
                        """
        );

        this.context().page("sal_ammoniac_tank");
        var sal_ammoniac_tank = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_TANK.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The Sal Ammoniac Tank stores the solvent - Sal Ammoniac - for use in the Liquefaction Cauldron.
                        """
        );

        this.context().page("liquefaction_cauldron");
        var liquefaction_cauldron = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.LIQUEFACTION_CAULDRON.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The Liquefaction Cauldron uses Sal Ammoniac to extract Alchemical Sulfur.
                        """
        );

        this.context().page("distiller");
        var distiller = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DISTILLER.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The Distiller creates {0}.
                        """,
                this.itemLink("Mercury Shards", ItemRegistry.MERCURY_SHARD.get())
        );

        this.context().page("incubator");
        var incubator = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.INCUBATOR.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        Finally, the incubator recombines the Salt, Sulfur, and Mercury into the refined item, Iron Ingots in our case.
                        It needs one each of {0}, {1}, {2} to hold the input materials.
                        """,
                this.itemLink("Salt Vessel", ItemRegistry.INCUBATOR_SALT_VESSEL.get()),
                this.itemLink("Mercury Vessel", ItemRegistry.INCUBATOR_MERCURY_VESSEL.get()),
                this.itemLink("Sulfur Vessel", ItemRegistry.INCUBATOR_SULFUR_VESSEL.get())
        );


        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DISTILLER.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        pyromantic_brazier,
                        calcination_oven,
                        sal_ammoniac_accumulator,
                        sal_ammoniac_tank,
                        liquefaction_cauldron,
                        distiller,
                        incubator
                );
    }
}