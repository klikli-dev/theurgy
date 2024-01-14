// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class IncubationEntry extends EntryProvider {

    public static final String ENTRY_ID = "incubation_after_reformation";

    public IncubationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.QUARTZ))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Incubating Quartz");
        this.pageText(
                """
                        Finally, with the Quartz Sulfur at hand, we can incubate it into Quartz.
                        \\
                        \\
                        Simply follow the steps you already know from {0}.
                            """,
                this.entryLink("Incubation", GettingStartedCategoryProvider.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.IncubationEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Incubating the Quartz";
    }

    @Override
    protected String entryDescription() {
        return "Finally creating our Quartz";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.QUARTZ);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}