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
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryFluxConnectorEntry extends EntryProvider {
    public static final String ENTRY_ID = "mercury_flux_connector";

    public MercuryFluxConnectorEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("connector", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                The Mercurial Flux Connector attaches to a block and links it to the Mercurial Logistics network.
                \
                \
                It can move both Mercury Flux and NeoForge Energy / FE between connected blocks.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_mercury_flux_connector")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
                Right-click the target block with the connector to attach it.
                \
                \
                Then connect it to the rest of the network with Mercurial Wires.
                """
        );

        this.page("transfer", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Transfer");
        this.pageText("""
                Connectors automatically balance transfer across other connected Mercurial Flux Connectors in the same network.
                \
                \
                Mercury Flux and NeoForge Energy / FE use separate channels, so no conversion happens between them.
                """
        );

        this.page("direction", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Changing Targets");
        this.pageText("""
                        Like other logistics connectors, it uses the face it is attached to by default.
                        Use the {0} in the "Select Direction" mode to target a different face of the same block.
                        """,
                this.entryLink("Mercurial Wand", LogisticsCategory.CATEGORY_ID, MercurialWandEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Flux Connector";
    }

    @Override
    protected String entryDescription() {
        return "Move Mercury Flux or NeoForge Energy / FE through the logistics network.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
