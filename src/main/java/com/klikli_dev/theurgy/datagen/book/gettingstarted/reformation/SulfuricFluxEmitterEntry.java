// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.reformation.ReformationArrayEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class SulfuricFluxEmitterEntry extends EntryProvider {

    public static final String ENTRY_ID = "sulfuric_flux_emitter";

    public SulfuricFluxEmitterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("target", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Supplying Sulfuric Flux");
        this.add(this.context().pageText(),
                """
                        Make sure the Sulfuric Flux Emitter is supplied with mercury flux, e.g. from a Mercury Catalyst.\\
                        The replication process will start automatically, once a target and sufficient source sulfur are present in the pedestals, and enough mercury flux is supplied to the emitter.
                        """
        );

        this.page("visuals", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Visuals");
        this.add(this.context().pageText(),
                """
                        If the process is working you will see particles fly from the emitter to the pedestals.
                        \\
                        \\
                        Further, you should see glowing orbs above the target and source pedestals, indicating that sulfur is present.
                        """
        );

        this.page("problems", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Problem Resolution");
        this.add(this.context().pageText(),
                """
                        If that is not the case, double check the recipe in JEI, and visit {0} to review your knowledge of the reformation array setup.           
                        """,
                this.entryLink("Reformation Array", ApparatusCategory.CATEGORY_ID, ReformationArrayEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Supplying Sulfuric Mercury Flux";
    }

    @Override
    protected String entryDescription() {
        return "Starting the Replication Process";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}