// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ReformationArrayEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class DigestionVatEntry extends EntryProvider {

    public static final String ENTRY_ID = "digestion_vat";

    public DigestionVatEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Exaltation Apparatus");
        this.pageText("""
                        As discussed, Exaltation consists of Digestion and the already known Fermentation and Reformation.
                        \\
                        \\
                        View the {0} Category on how to craft and use the required apparatus.
                         """,
                this.entryLink("Apparatus", ApparatusCategory.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.TransmutationAndExaltationEntry.ENTRY_ID)
        );

        this.page("reformation_array", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Reformation Array");
        this.pageText("""
                        Once again you need to set up a reformation array, the usage was already discussed in the {0} and following entries.
                         """,
                this.entryLink("Reformation Array", GettingStartedCategoryProvider.CATEGORY_ID, ReformationArrayEntry.ENTRY_ID)
        );

        this.page("fermentation_vat", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.FERMENTATION_VAT.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                You also need at least one fermentation vat. Multiple vats can be used to process multiple sulfurs in parallel.
                """);

        this.page("digestion_vat", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIGESTION_VAT.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                Finally you also need at least one digestion vat. Multiple vats can be used to process multiple niters in parallel.
                """);
    }

    @Override
    protected String entryName() {
        return "Required Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "The apparatus needed for exaltation";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.DIGESTION_VAT.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}