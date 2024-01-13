// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.reformation.ReformationArrayEntry;
import com.mojang.datafixers.util.Pair;

public class ExaltationEntry extends EntryProvider {

    public static final String ENTRY_ID = "exaltation";

    public ExaltationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("about", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Exaltation Apparatus");
        this.pageText(
                """
                       Exaltation is the process of converting sulfurs into different types (such es gems to metals) or tiers (such as common to rare).\\
                       Exaltation consists of three processes: Fermentation to make Sulfurs more malleable for the other two steps, Digestion to change the tier and Reformation to change the type.
                         """
        );
        this.page("structure", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Structure");
        this.add(this.context().pageText(),
                """
                        Reformation is described in the {0}.\\
                        Fermentation and Digestion both happen in special vats, which are standalone objects that need no particular structure.
                        """,
                this.entryLink("Reformation Array Entry", ApparatusCategory.CATEGORY_ID, ReformationArrayEntry.ENTRY_ID)
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
