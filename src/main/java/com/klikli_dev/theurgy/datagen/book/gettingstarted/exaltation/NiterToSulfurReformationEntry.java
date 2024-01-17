// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ConvertWithinTypeAndTierEntry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class NiterToSulfurReformationEntry extends EntryProvider {
    public static final String ENTRY_ID = "niter_to_sulfur_reformation_exaltation";

    public NiterToSulfurReformationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Sulfur Reformation");
        this.pageText("""
                        The {0} can now be converted into {1} via reformation.
                        \\
                        \\
                        This again works exactly as already described in the previous entry and in the {2} entries.
                        """,
                this.itemLink("Alchemical Niter: Rare Metals", SulfurRegistry.METALS_RARE.get()),
                this.itemLink("Alchemical Sulfur: Gold", SulfurRegistry.GOLD.get()),
                this.entryLink("Reformation", GettingStartedCategoryProvider.CATEGORY_ID, ConvertWithinTypeAndTierEntry.ENTRY_ID)
        );

        this.page("instructions", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Instruction Reminder");
        this.pageText("""
                Simply place the Rare Metals Niter as Source and the Gold Sulfur as Target in the Reformation Array, supply Mercury Flux and wait for the process to complete.
                \\
                \\
                Now repeat this entire process until you have enough Gold Sulfur.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Niter to Sulfur Reformation";
    }

    @Override
    protected String entryDescription() {
        return "Convert Rare Metals Niter into Gold Sulfur";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.GOLD.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
