// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ConvertWithinTypeAndTierEntry;
import com.klikli_dev.theurgy.registry.NiterRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class NiterToNiterReformationEntry extends EntryProvider {
    public static final String ENTRY_ID = "niter_to_niter_reformation";

    public NiterToNiterReformationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Niter Reformation");
        this.pageText("""
                        The {0} can now be converted into {1} via reformation.
                        \\
                        \\
                        This works exactly as already described in the {2} entries.
                        """,
                this.itemLink("Alchemical Niter: Common Gems", NiterRegistry.GEMS_COMMON.get()),
                this.itemLink("Alchemical Niter: Common Metals", NiterRegistry.METALS_COMMON.get()),
                this.entryLink("Reformation", GettingStartedCategoryProvider.CATEGORY_ID, ConvertWithinTypeAndTierEntry.ENTRY_ID)
        );

        this.page("instructions", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Instruction Reminder");
        this.pageText("""
                Simply place the Common Gems Niter as Source and the Common Metals Niter as Target in the Reformation Array, supply Mercury Flux and wait for the process to complete.
                \\
                \\
                *Note: Depending on the Niters involved you may need multiple source Niters to gain one target Niter. Refer to JEI for details.* 
                """
        );
    }

    @Override
    protected String entryName() {
        return "Niter to Niter Reformation";
    }

    @Override
    protected String entryDescription() {
        return "Convert Common Gems Niter into Common Metals Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(NiterRegistry.METALS_COMMON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
