// SPDX-FileCopyrightText: 2026 klikli-dev
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
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import net.minecraft.world.item.crafting.Ingredient;

public class LogisticsNexusEntry extends EntryProvider {
    public static final String ENTRY_ID = "logistics_nexus";

    public LogisticsNexusEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("nexus", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_NEXUS.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                The Logistics Nexus pairs with another nexus that shares the same Nexus ID. Paired nexuses connect the logistics networks attached to either nexus, even across long distances and dimensions.
                """);

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_nexus")));

        this.page("pairing", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Pairing and ID");
        this.pageText("""
                The recipe yields two Nexuses, giving you a matched pair right away. Each Nexus keeps its Nexus ID in the item and on placement. A pair only links if both placed Nexuses share the same ID.
                """);

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Using the Nexus");
        this.pageText("""
                Attach Connection Nodes to the Nexus to join it to a Mercurial Logistics network. Any blocks connected to those nodes become part of the paired network, letting the two sides act as one connected system.
                """);
    }

    @Override
    protected String entryName() {
        return "Logistics Nexus";
    }

    @Override
    protected String entryDescription() {
        return "Bridge two logistics networks over any distance.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_NEXUS.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
