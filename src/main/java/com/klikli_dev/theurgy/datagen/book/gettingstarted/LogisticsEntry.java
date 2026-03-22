// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class LogisticsEntry extends EntryProvider {

    public static final String ENTRY_ID = "logistics";

    public LogisticsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Mercurial Logistics");
        this.pageText(
                """
                        Alchemical Mercury has the unique property of temporarily converting matter to energy and back. Properly applied, this allows for the transportation of matter and automation of processes in ways previously thought impossible.
                        \\
                        \\
                        Visit the chapter on {0} to learn how to use mercury beyond spagyrics.
                        """,
                this.categoryLink("Mercurial Logistics", LogisticsCategory.CATEGORY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Logistics";
    }

    @Override
    protected String entryDescription() {
        return "Transportation and Automation";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.LINK_TO_CATEGORY;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURIAL_WAND.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}