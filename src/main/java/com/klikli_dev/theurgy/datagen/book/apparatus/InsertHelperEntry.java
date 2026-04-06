// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.mojang.datafixers.util.Pair;

public class InsertHelperEntry extends EntryProvider {
    public static final String ENTRY_ID = "insert_helper";

    public InsertHelperEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Insertion Helper");
        this.pageText("""
                When you look at a supported apparatus while holding an item or fluid container, a colored frame can preview whether that stack fits.
                \
                \
                By default, only fitting items show a green overlay. Non-fitting items show no overlay unless you enable red rejection previews in the client config.
                """);

        this.page("intro_config", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Config and Keybind");
        this.pageText("""
                The client config can switch this helper between green-only and green-plus-red previews.
                \
                \
                This helper only appears while the held-stack fit outline option is enabled. If you bind its key, the preview only appears while that key is held.
                """);

        this.page("accepted", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/insert_helper_green.png")));
        this.pageTitle("Accepted Items");
        this.pageText("""
                A green frame marks a held stack that the target apparatus accepts for insertion or fluid transfer.
                """);

        this.page("rejected", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/insert_helper_red.png")));
        this.pageTitle("Rejected Items");
        this.pageText("""
                A red frame marks a held stack that does not fit the targeted apparatus. If you prefer less visual noise, switch the client setting to green-only to hide red previews.
                """);
    }

    @Override
    protected String entryName() {
        return "Insertion Helper";
    }

    @Override
    protected String entryDescription() {
        return "Colored frames preview whether your held stack fits an apparatus.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/insert_helper_green.png"), 64, 64);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
