// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;

public class SpagyricsEntry extends EntryProvider {

    public static final String ENTRY_ID = "spagyrics";

    public SpagyricsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Spagyrics Apparatus");
        this.pageText("""
                Spagyrics Apparatus enable to separate and recombine the three principles of a substance.
                """);

        this.page("further_reading", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Further Reading");
        this.pageText("""
                        For more information on how to use these contraptions, see also {0} in {1}.
                        """,
                this.entryLink("", GettingStartedCategoryProvider.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.SpagyricsEntry.ENTRY_ID),
                this.categoryLink("Getting Started", GettingStartedCategoryProvider.CATEGORY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Spagyrics Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "Separating and Recombining the Three Principles";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.CIRCLE_GRAY;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
