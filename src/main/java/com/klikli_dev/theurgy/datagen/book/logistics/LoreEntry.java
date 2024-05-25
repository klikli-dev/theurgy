// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class LoreEntry extends EntryProvider {
    public LoreEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("conversion", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Temporary Mercuriality");
        this.pageText("""
                When matter comes into contact with Alchemical Mercury an odd reaction occurs. The matter seems to flicker, as if it is not quite there. Apparently the matter, for the briefest moment, shifts entirely into mercurial form, before returning to its original state.
                """
        );

        this.page("wires", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Mercury Wires");
        this.pageText("""
               Interestingly, this conversion can be persisted. If the Alchemical Mercury is in contact with an appropriate metal wire, the matter will remain in its mercurial form and flow along the wire, as if it were energy.
                """
        );

        this.page("reversal", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Reversal of Conversion");
        this.pageText("""
                If there is sufficient space for the matter to return to its original state, it will do so. To this end another piece of Alchemical Mercury can be placed at the end of the wire, allowing the matter to return to its original state.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Temporary Mercuriality";
    }

    @Override
    protected String entryDescription() {
        return "The energetic aspect of matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_SHARD.get());
    }

    @Override
    protected String entryId() {
        return "lore";
    }
}
