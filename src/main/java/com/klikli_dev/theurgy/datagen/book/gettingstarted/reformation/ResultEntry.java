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

public class ResultEntry extends EntryProvider {

    public static final String ENTRY_ID = "result_pedestal";

    public ResultEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("result", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Result");
        this.add(this.context().pageText(),
                """
                        After the process is finished a glowing orb should appear above the result pedestal, indicating it contains Sulfur.
                        \\
                        \\
                        [#]($INPUT)Right-click[#]() on the pedestal to retrieve the {0}.
                        """,
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get())
        );

        this.page("result2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "The Result");
        this.add(this.context().pageText(),
                """
                        Also [#]($INPUT)right-click[#]() on the target pedestal to retrieve the original {1}.
                        \\
                        \\
                        You now doubled your desired Sulfur, by consuming one {0} and creating one extra {1}.
                        """,
                this.itemLink("Alchemical Sulfur: Lapis", SulfurRegistry.LAPIS.get()),
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get())
        );


        this.page("repeat", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Crafting more");
        this.add(this.context().pageText(),
                """
                        As long as the emitter is supplied with mercury flux and there is enough source sulfur in the pedestals, the process will continue, creating more sulfur in the target pedestal.
                        """
        );
    }

    @Override
    protected String entryName() {
        return "Result Sulfur";
    }

    @Override
    protected String entryDescription() {
        return "The Lapis Sulfur Transformed into Quartz Sulfur";
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