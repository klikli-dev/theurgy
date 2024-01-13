// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.reformation.ReformationArrayEntry;
import com.mojang.datafixers.util.Pair;

public class TransmutationAndExaltationEntry extends EntryProvider {

    public static final String ENTRY_ID = "transmutation_and_exaltation";

    public TransmutationAndExaltationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("about", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Transmutation and Exaltation Apparatus");
        this.pageText(
                """
                        [#]($PURPLE)Transmutation[#]() is the process of converting sulfurs into other types (such as gems to metals),
                        [#]($PURPLE)Exaltation[#]() into different tiers (such as common to rare).
                          """
        );

        this.page("transmutation", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Transmutation");
        this.add(this.context().pageText(),
                """
                        Transmutation requires two apparatus: a Fermentation Vat and a Reformation Array.
                        \\
                        \\
                        The vat, is a standalone objects that need no particular structure, the Reformation Array is discussed {0}
                        """,
                this.entryLink("Reformation Array Entry", ApparatusCategory.CATEGORY_ID, ReformationArrayEntry.ENTRY_ID)
        );

        this.page("exaltation", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Exaltation");
        this.add(this.context().pageText(),
                """
                        Exaltation similarly requires two apparatus: a Digestion Vat and a Reformation Array.
                        \\
                        \\
                        The vat again is a standalone object.
                        """
        );
    }

    @Override
    protected String entryName() {
        return "Exaltation Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "Conversion of Sulfur into other types or other tiers of Sulfur";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/convert_niter.png"), 64, 64);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
