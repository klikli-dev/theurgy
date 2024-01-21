// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.IncubatorEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.SpagyricsEntry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class ConvertWithinTypeAndTierEntry extends EntryProvider {
    public static final String ENTRY_ID = "convert_within_type_and_tier";

    public ConvertWithinTypeAndTierEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Reformation");
        this.pageText("""
                The core of every replication process is the reformation of Sulfur. Sulfur represents the "Soul" of matter, so to transform matter one needs to transform sulfur.
                \\
                \\
                As already discussed, sulfur is of limited malleability, making it hard to transform.
                """
        );

        this.page("possibilities", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Overcoming Limitations");
        this.pageText("""
                However, even without further processing, Sulfur can be transformed into another type of Sulfur, if it is sufficiently similar.
                \\
                \\
                Specifically it seems that Sulfurs that are of the same type (such as "gem") and tier (such as "precious") can be transformed into each other.
                """
        );

        this.page("further_research", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Further Research");
        this.pageText("""
                The transformation of Sulfur into other types or tiers, such as the legendary conversion of lower grade metals into gold, is more advanced. This book will cover those processes after you have mastered the basics of reformation.
                """
        );

        this.page("theory", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Theoretical Underpinnings");
        this.pageText("""
                This Reformation of one sulfur into the other can be achieved by the application of Sulfuric Flux. This is a type of Mercury flux that has been infused with the essence of one type of Sulfur, allowing it to transform other Sulfur it gets in contact with into the same type.
                """
        );

        this.page("reassembly", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );

        this.pageTitle("Reassembly");
        this.pageText("""
                        Once the desired Sulfur has been produced, {0}, specifically the {1} can be used to reassemble the matter into the desired form.
                        """,
                this.entryLink("Spagyrics", ApparatusCategory.CATEGORY_ID, SpagyricsEntry.ENTRY_ID),
                this.entryLink("Incubator", ApparatusCategory.CATEGORY_ID, IncubatorEntry.ENTRY_ID)
        );

        this.page("instructions", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Instructions");
        this.pageText("""
                        The following entries will guide you through the conversion of {0} into {1}, *assuming you already obtained at least one Sulfur of each*.
                        """,
                this.itemLink("Alchemical Sulfur: Lapis", SulfurRegistry.LAPIS.get()),
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get())
        );

        this.page("instructions2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Instructions");
        this.pageText("""
                        The instructions apply equally to all other reformation recipes within the same type and tier, such as {0} to {1}.
                        """,
                this.itemLink("Alchemical Sulfur: Diamond", SulfurRegistry.DIAMOND.get()),
                this.itemLink("Alchemical Sulfur: Emerald", SulfurRegistry.EMERALD.get())
        );
    }

    @Override
    protected String entryName() {
        return "Replication by Reformation";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining more of an item by converting items of the same type and tier";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/convert_items_rare.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
