// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.CalcinationOvenEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.DistillerEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.IncubatorEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.LiquefactionCauldronEntry;
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
                        There are two ways of obtaining Sal Ammoniac, both use the Accumulator Apparatus. The first, slower, option is to simply place water in the accumulator, and let it slowly concentrate the inherently contained Sal Ammoniac.
                            """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Speeding it up");
        this.add(this.context().pageText(),
                """
                        The second, faster, option is to additionally add a {0} to speed up the process significantly. Now the crystal merely needs to dissolve in the water.
                            """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.context().page("reverse");
        var reverse = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Reversing the process");
        this.add(this.context().pageText(),
                """
                        You can reverse this by filling the fluid Sal Ammoniac in a bucket and then crafting the bucket with no other item. You will receive a {0} and an empty bucket.
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
                        intro2,
                        reverse,
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