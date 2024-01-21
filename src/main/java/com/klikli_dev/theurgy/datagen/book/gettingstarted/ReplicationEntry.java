// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.mojang.datafixers.util.Pair;

public class ReplicationEntry extends EntryProvider {
    public static final String ENTRY_ID = "replication";

    public ReplicationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("True Alchemy");
        this.pageText("""
                The pursuit of Spagyrics has opened to door to more efficient processing of raw materials, but on it's own it is not truly revolutionary.
                \\
                \\
                A true alchemist's goal is mastery over matter, often exemplified by the ability to turn base metals into gold.
                """
        );

        this.page("intro2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Transformation");
        this.pageText("""
                Underlying the idea of transformation are three distinct processes:
                1. *Reformation*, changing Sulfur without changing the type ("gems") or tier ("precious")
                2. *Transmutation*, changing Sulfur's [#]($PURPLE)type[#]() without changing the tier
                3. *Exaltation*, changing Sulfur's [#]($PURPLE)tier[#]()
                """
        );

        this.page("intro3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Transformation");
        this.pageText("""
                Each process on it's own is useful, but only the combination allows unlimited replication of matter, using almost any other type of matter as a source.
                \\
                \\
                The following chapters will cover each process, one building upon the other.
                """
        );

    }

    @Override
    protected String entryName() {
        return "Unlimited Replication";
    }

    @Override
    protected String entryDescription() {
        return "True mastery over matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.CATEGORY_START;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/conversion.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
