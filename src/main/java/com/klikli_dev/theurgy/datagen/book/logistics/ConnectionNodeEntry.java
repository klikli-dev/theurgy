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
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class ConnectionNodeEntry extends EntryProvider {
    public ConnectionNodeEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("node", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_CONNECTION_NODE.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                The node allows connecting wires over long distances without placing an inserter or extractor.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_connector_node")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               1. Right-click any block to attach the node to it.
               2. Then connect the node to other blocks using wires.
               3. Any block connected directly or indirectly will be part of the same network.
                """
        );

        this.page("image", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withImages(Theurgy.loc("textures/gui/book/connection_node_example.png")));
        this.pageTitle("Demonstration");

        this.page("large_networks", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Complex Networks");
        this.pageText("""
               In networks with multiple inserters and extractors the default behaviour is round-robin. That means each extractor will attempt to split the items evenly between all connected inserters.
               \\
               \\
               In the future more advanced configurations will be possible.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Connection Node";
    }

    @Override
    protected String entryDescription() {
        return "Extend the Range of Logistics Networks";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_CONNECTION_NODE.get());
    }

    @Override
    protected String entryId() {
        return "connection_node";
    }
}
