// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class ListFilterEntry extends EntryProvider {
    public static final String ENTRY_ID = "list_filter";

    public ListFilterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("filter", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LIST_FILTER.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                Filters can be used to limit what items are inserted or extracted from a block.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/list_filter")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               1. Right-click with the filter in hand to open the filter GUI.
               2. Place items in the filter and set it to either allow or deny these items.
               3. Optionally set the filter whether or not to match data components.
               4. Right-click an inserter or extractor with the filter to apply it.
               """
        );

        this.page("removal", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Removal");
        this.pageText("""
               1. Shift-right-click with an empty hand on an inserter or extractor to remove the filter.
               2. The filter will be returned to the player's inventory.
               """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial List Filter";
    }

    @Override
    protected String entryDescription() {
        return "Allows limiting the insertion and extraction to specific items.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LIST_FILTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
