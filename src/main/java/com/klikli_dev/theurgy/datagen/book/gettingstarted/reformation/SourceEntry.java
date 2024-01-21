// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class SourceEntry extends EntryProvider {

    public static final String ENTRY_ID = "source_pedestal";

    public SourceEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("source", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("The Source");
        this.pageText("""
                 Generally only materials of the same type and similar value can be converted into each other. Depending on the mods you are using the materials available one each "value level" will vary. Use JEI or REI to look up recipes for your desired *target* sulfur to find which *source* sulfurs are available.
                """);


        this.page("source2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("The Source");
        this.pageText("""
                        It should always be possible to convert 1x {0} into 1x {1}, so place (= [#]($INPUT)right-click[#]()) at least one {0} into one of your source pedestals.\\
                        A glowing orb should appear above each.\\
                        The source sulfur will be consumed!
                        """,
                this.itemLink("Alchemical Sulfur: Lapis", SulfurRegistry.LAPIS.get()),
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get())
        );
    }

    @Override
    protected String entryName() {
        return "Source Sulfur";
    }

    @Override
    protected String entryDescription() {
        return "Lapis as a replication source";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.LAPIS.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}