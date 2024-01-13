// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.IncubatorEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.LiquefactionCauldronEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.world.item.Items;

public class ReformationCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "reformation";

    public ReformationCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "________________s_________________",
                "__________________________________",
                "__________i_r_e___o_p_____________",
                "__________________________________",
                "________________t_________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {
        var replication = this.add(this.replication('p'));

        //TODO: link to sulfuric flux emitter for crafting
    }


    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Reformation");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"))
                .withIcon(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get());
    }

    private BookEntryModel replication(char location) {
        this.context().entry("replication");
        this.add(this.context().entryName(), "Replicating Iron");
        this.add(this.context().entryDescription(), "Putting it all together");

        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Replicating Iron");
        this.add(this.context().pageText(),
                """
                    In the following pages we will go through the steps necessary to create more iron ingots from a single one.
                        """
        );

        this.page("array", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Array");
        this.add(this.context().pageText(),
                """
                        Start by building a reformation array as described in the previous pages, make sure to include two source pedestals.\\
                        After, right-click the {0} with an empty hand to see if it is linked to all the pedestals.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
        );

        this.page("sulfur", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Alchemical Sulfurs");
        this.add(this.context().pageText(),
                """
                        As mentioned before, the reformation processes does not use raw materials, but rather their alchemical sulfur.\\
                        To obtain sulfurs of materials, take a look at {0} and see also {1} .
                        """,
                "TODO",
//                this.categoryLink("Spagyrics", SpagyricsCategoryProvider.CATEGORY_ID), //TODO: probably link to the relevant entry in getting started and/or the apparatus entry, probably both
                this.entryLink("Liquefaction", ApparatusCategory.CATEGORY_ID, LiquefactionCauldronEntry.ENTRY_ID)
//                this.entryLink("Extracting Sulfur", GettingStartedCategoryProvider.CATEGORY_ID, "create_sulfur")
        );


        this.page("source", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Source");
        this.add(this.context().pageText(),
                """
                        Generally only materials of similar value can be converted into each other. Depending on the mods you are using the materials available one each "value level" will vary. Use JEI or REI to look up recipes for your desired *target* sulfur to find which *source* sulfurs are available.
                        """
        );

        this.page("source2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Source");
        this.add(this.context().pageText(),
                """
                        It should always be possible to convert 2x {0} into 1x {1}, so place at least one {0} into each of your two source pedestals.\\
                        A glowing orb should appear above each.\\
                        The source sulfur will be consumed!
                        """,
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get()),
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get())
        );

        this.page("target", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Target");
        this.add(this.context().pageText(),
                """
                        Place at least one {0} into the target pedestal.\\
                        A glowing orb should appear above it.\\
                        The target sulfur will **not** be consumed!
                        """,
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get())
        );

        this.page("crafting", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Et voilà: Reformation");
        this.add(this.context().pageText(),
                """
                        As soon as all ingredients are in place, the sulfuric flux emitter will start the reformation process, as long as it is supplied with mercury flux.\\
                        Glowing flux flying between the pedestals will indicate the process is running.\\
                        Once complete, a glowing orb will appear over the result pedestal.
                        """
        );

        this.page("result", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Collecting the spoils");
        this.add(this.context().pageText(),
                """
                        You can repeat this process as long as you have enough source sulfur.\\
                        To collect the resulting sulfur, right-click the result pedestal with an empty hand.\\
                        You can also collect the target sulfur again.
                        """
        );

        this.page("incubation", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Incubating Ingots");
        this.add(this.context().pageText(),
                """
                        To reassemble the {0} into {1}, you now need to incubate the sulfur.\\
                        See {2} and {3} on how to accomplish that.
                        """,
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get()),
                this.itemLink(Items.IRON_INGOT),
                this.entryLink("Incubator", ApparatusCategory.CATEGORY_ID, IncubatorEntry.ENTRY_ID),
                this.entryLink("Incubating Iron", GettingStartedCategoryProvider.CATEGORY_ID, "incubation")
        );

        return this.entry(location)
                .withIcon(this.modLoc("textures/gui/book/three_iron_ingots.png"), 32, 32)
                .withEntryBackground(EntryBackground.DEFAULT);
    }
}
