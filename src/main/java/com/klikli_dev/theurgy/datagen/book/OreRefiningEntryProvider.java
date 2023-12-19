// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
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
    protected void generateEntries() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    @Override
    protected BookCategoryModel generateCategory() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    public BookEntryModel aboutOreRefiningEntry(char location) {
        this.context().entry("about_ore_refining");
        this.add(this.context().entryName(), "Ore Refining");
        this.add(this.context().entryDescription(), "Triple your ore yield - at a cost!");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.RAW_IRON))
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
                        The easiest application of Spagyrics is the refining of ores and raw metals. The process of smelting ores in a furnace is wasteful, as it only yields a single ingot per ore, losing a lot of the precious raw materials in the process. Alchemists can extract even the last iota of value from ores, but the process is somewhat more complex.
                        """);

        this.context().page("overview");
        var overview = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Process");
        this.add(this.context().pageText(),
                """
                        To refine an Ore or Raw Metal you first need to {0} it, which will yield multiple Ore Sulfur. Additionally you need to obtain multiple heaps of {1} to provide a body for this multiplied Sulfur, and some {2} to provide the mercury for the soul of the resulting refined ingots.
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

        return this.entry(location)
                .withIcon(Items.RAW_IRON)
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

    public BookEntryModel neededApparatusEntry(char location) {
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

        this.context().page("next_steps");
        var next_steps = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Next Steps");
        this.add(this.context().pageText(),
                """
                        Place all the apparatus, those that need heating on pyromantic braziers. Prepare some Coal to heat the braziers, then open the next entry.
                         """
        );

        return this.entry(location)
                .withIcon(ItemRegistry.DISTILLER.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        pyromantic_brazier,
                        calcination_oven,
                        sal_ammoniac_accumulator,
                        sal_ammoniac_tank,
                        liquefaction_cauldron,
                        distiller,
                        incubator,
                        next_steps
                );
    }

    public BookEntryModel createSolventEntry(char location) {
        this.context().entry("create_solvent");
        this.add(this.context().entryName(), "Accumulating Solvent");
        this.add(this.context().entryDescription(), "Obtaining Sal Ammoniac - they key to Sulfur Extraction");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_BUCKET.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        There are two ways of obtaining Sal Ammoniac, both use the Accumulator Apparatus. The first, slower, option is to simply place water in the accumulator, and let it slowly concentrate the inherently contained Sal Ammoniac. The second, faster, option is to additionally add a {0} to speed up the process significantly.
                            """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.context().page("step1");
        var step1 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Filling the Accumulator");
        this.add(this.context().pageText(),
                """
                        After placing the {0} on a {1}, right-click the {0} with water buckets (up to 10) to fill it.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get())
        );

        this.context().page("step2");
        var step2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Adding Crystals");
        this.add(this.context().pageText(),
                """
                        Optionally you can now right-click the {0} with a {1} (obtained by mining). You will get Sal Ammoniac regardless, but the crystal will speed up the process significantly.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );


        this.context().page("step3");
        var step3 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Obtaining the Sal Ammoniac");
        this.add(this.context().pageText(),
                """
                        Once the {0} has filled up sufficiently, you can right-click it with an empty bucket to obtain a {1}.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        return this.entry(location, ItemRegistry.SAL_AMMONIAC_BUCKET.get())
                .withPages(
                        intro,
                        step1,
                        step2,
                        step3
                );
    }

    public BookEntryModel createSulfurEntry(char location) {
        this.context().entry("create_sulfur");
        this.add(this.context().entryName(), "Extracting Sulfur");
        this.add(this.context().entryDescription(), "Obtaining Sulfur - the \"Soul\"");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.IRON.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        Sulfur extraction is the part of ore purification that leads to [#]($PURPLE)multiplication[#](). One Ore or Raw Metal yields multiple sulfurs, which then each can be refined into an ingot.
                                 """
        );

        this.context().page("step1");
        var step1 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Filling the Liquefaction Cauldron");
        this.add(this.context().pageText(),
                """
                        Right-click the {0} with a {1} to fill it.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        this.context().page("step2");
        var step2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Adding Raw Materials");
        this.add(this.context().pageText(),
                """
                        Now right-click the {0} with the item you want to extract sulfur from, such as {1}. The item will be placed inside.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(Items.RAW_IRON)
        );

        this.context().page("step3");
        var step3 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Providing Heat");
        this.add(this.context().pageText(),
                """
                        Now add fuel, such as Coal, to the {0} below the {1} to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get())
        );

        this.context().page("step4");
        var step4 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Obtaining the Sulfur");
        this.add(this.context().pageText(),
                """
                        After a while some sulfur will have been extracted, you can right-click the {0} with an empty hand to obtain {1}.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink("Alchemical Sulfur", SulfurRegistry.IRON.get())
        );


        return this.entry(location, SulfurRegistry.IRON.get())
                .withPages(
                        intro,
                        step1,
                        step2,
                        step3,
                        step4
                );
    }

    public BookEntryModel createSaltEntry(char location) {
        this.context().entry("create_salt");
        this.add(this.context().entryName(), "Extracting Salt");
        this.add(this.context().entryDescription(), "Obtaining Salt - the \"Body\"");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SaltRegistry.MINERAL.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        Salt is needed in order to create items from Alchemical Sulfur. The type of salt needs to match the type of sulfur - for our project that involves {0} we need {1}, which covers all types of ores and metals.
                        """,
                this.itemLink("Iron Sulfur", SulfurRegistry.IRON.get()),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );

        this.context().page("step1");
        var step1 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Calcinating Minerals");
        this.add(this.context().pageText(),
                """
                        Right-click the {0} with any Mineral such as Ores, Raw Metals or Ingots to calcinate it.
                        One option is to use a Stack of {1}, which in turn is calcinated from Stone, Sand, Gravel, Dirt, etc.
                        Another great source is {2}, as it is renewable.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink("Strata Salt", SaltRegistry.STRATA.get()),
                this.itemLink(Items.CHARCOAL)
        );

        this.context().page("step2");
        var step2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Providing Heat");
        this.add(this.context().pageText(),
                """
                        Now add fuel, such as Coal, to the {0} below the {1} to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        this.context().page("step3");
        var step3 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Obtaining the Salt");
        this.add(this.context().pageText(),
                """
                        After a while some salt will have been created, you can right-click the {0} with an empty hand to obtain {1}.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );


        return this.entry(location, SaltRegistry.MINERAL.get())
                .withPages(
                        intro,
                        step1,
                        step2,
                        step3
                );
    }

    public BookEntryModel createMercuryEntry(char location) {
        this.context().entry("create_mercury");
        this.add(this.context().entryName(), "Extracting Mercury");
        this.add(this.context().entryDescription(), "Obtaining Mercury - the \"Spirit\"");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_SHARD.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        Mercury is the spirit, or energy, contained within matter. Like Salt, it is needed to create items from Alchemical Sulfur. Almost all items yield Mercury, but the more valuable and the more refined the item, the more mercury they will yield.
                        """
        );

        this.context().page("step1");
        var step1 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Distilling Mercury");
        this.add(this.context().pageText(),
                """
                        Right-click the {0} with any mercury-yielding item, such as Crops, Food, Ores, Raw Metals, but also Stone, Sand, Glass, ...\\
                        For low-value items you may need to add multiple items at once to obtain even one {1}.
                         """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.context().page("step2");
        var step2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Providing Heat");
        this.add(this.context().pageText(),
                """
                        Now add fuel, such as Coal, to the {0} below the {1} to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.DISTILLER.get())
        );

        this.context().page("step3");
        var step3 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Obtaining the Mercury");
        this.add(this.context().pageText(),
                """
                        After a while some mercury shards will have been created, you can right-click the {0} with an empty hand to obtain {1}.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );


        return this.entry(location, ItemRegistry.MERCURY_SHARD.get())
                .withPages(
                        intro,
                        step1,
                        step2,
                        step3
                );
    }

    public BookEntryModel incubationEntry(char location) {
        this.context().entry("incubation");
        this.add(this.context().entryName(), "Incubating Iron");
        this.add(this.context().entryDescription(), "Creating Iron Ingots from Sulfur, Salt and Mercury");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Incubating Iron");
        this.add(this.context().pageText(),
                """
                        The final step is to recombine the three principles into usable Iron Ingots.
                        """
        );

        this.context().page("step1");
        var step1 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Filling the Vessels");
        this.add(this.context().pageText(),
                """
                        Right-click the {0}, {1} and {2} with the Sulfur, Salt and Mercury you obtained earlier respectively to fill them.
                         """,
                this.itemLink(ItemRegistry.INCUBATOR_SULFUR_VESSEL.get()),
                this.itemLink(ItemRegistry.INCUBATOR_SALT_VESSEL.get()),
                this.itemLink(ItemRegistry.INCUBATOR_MERCURY_VESSEL.get())
        );

        this.context().page("step2");
        var step2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Providing Heat");
        this.add(this.context().pageText(),
                """
                        Now add fuel, such as Coal, to the {0} below the {1} to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.INCUBATOR.get())
        );

        this.context().page("step3");
        var step3 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Obtaining the Incubated Item");
        this.add(this.context().pageText(),
                """
                        After a while the input items will have been consumed and incubated into the result, you can right-click the {0} with an empty hand to obtain 3x {1}.
                        \\
                        \\
                        **Congratulations, you created 3 Ingots from 1 Raw Metal!**
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get()),
                this.itemLink(Items.IRON_INGOT)
        );


        return this.entry(location, Items.IRON_INGOT)
                .withPages(
                        intro,
                        step1,
                        step2,
                        step3
                );
    }
}