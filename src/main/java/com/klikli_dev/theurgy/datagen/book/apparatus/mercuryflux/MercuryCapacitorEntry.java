// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryCapacitorEntry extends EntryProvider {

    public static final String ENTRY_ID = "mercury_capacitor";

    public MercuryCapacitorEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_CAPACITOR.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                        The {0} is a large mercury flux storage unit that can store up to 10x the mercury flux of a Mercury Catalyst. It can receive and send mercury flux to adjacent blocks.
                        """,
                this.itemLink(ItemRegistry.MERCURY_CAPACITOR.get())
        );

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        The capacitor displays its fill level through its color:
                          \s
                          \s
                        - [#](ff0000)Red[#]() = Close to empty
                        - [#](ad03fc)Purple[#]() = Around halfway
                        - [#](00ff00)Green[#]() = Full
                        """,
                this.itemLink(ItemRegistry.MERCURY_CAPACITOR.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercury_capacitor"))
        );
    }

    @Override
    protected String entryName() {
        return "Mercury Capacitor";
    }

    @Override
    protected String entryDescription() {
        return "Storing Large Amounts of Energy";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_CAPACITOR.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
