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

public class TargetEntry extends EntryProvider {

    public static final String ENTRY_ID = "target_pedestal";

    public TargetEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("target", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Target");
        this.add(this.context().pageText(),
                """
                        Place (= [#]($INPUT)right-click[#]()) at least one {0} into the target pedestal.\\
                        A glowing orb should appear above it.
                        \\
                        \\
                        The target sulfur will *not* be consumed - you can take it out after the process is finished!
                        """,
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get())
        );
    }

    @Override
    protected String entryName() {
        return "Target Sulfur";
    }

    @Override
    protected String entryDescription() {
        return "Quartz as Replication Product";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.QUARTZ.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}