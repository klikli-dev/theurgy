// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class IntroEntry extends EntryProvider {
    public IntroEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Mercurial Logistics");
        this.pageText("""
                Spagyrics show that matter and energy are closely related, interchangeable even.
                \\
                \\
                Mercury is the energetic aspect of matter, which can be moved both in crystal form and as energy, over metal wires.
                """
        );

        this.page("intro2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Mercurial Logistics");
        this.pageText("""
                If the conversion between matter and energy in both directions can be controlled, then a much more efficient transportation system can be created, utilizing metal wires instead of hoppers or pipes: Mercurial Logistics.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Logistics";
    }

    @Override
    protected String entryDescription() {
        return "Matter Transport & Automation";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.CATEGORY_START;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURIAL_WAND.get());
    }

    @Override
    protected String entryId() {
        return "intro";
    }
}
