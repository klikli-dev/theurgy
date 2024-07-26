// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class FluidInserterEntry extends EntryProvider {
    public static final String ENTRY_ID = "fluid_inserter";

    public FluidInserterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("inserter", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_FLUID_INSERTER.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                When attached to a block, the inserter will insert fluids from the Mercurial Logistics System into the block. The fluids will be extracted from blocks that have extractors attached to them, if the extractors are part of the same network.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_fluid_inserter")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               Right-click a block that has a fluid tank to attach the inserter to it.
               \\
               \\
               The inserter will by default insert into the face it is attached to. This only matters if the block has multiple fluid tanks accessible from different block faces.
                """
        );

        this.page("identification", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Identification");
        this.pageText("""
               The inserter features a green band on the side attached to the target block.
                """
        );

        this.page("direction", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Changing Direction");
        this.pageText("""
               Inserters can also insert into faces they are not attached to. Use the {0} in the "Select Direction" mode to cycle through the insert directions.
                """,
                this.entryLink("Mercurial Wand", LogisticsCategory.CATEGORY_ID, MercurialWandEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Fluid Inserter";
    }

    @Override
    protected String entryDescription() {
        return "Inserting fluids into Blocks from the Mercurial Logistics System.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_FLUID_INSERTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
