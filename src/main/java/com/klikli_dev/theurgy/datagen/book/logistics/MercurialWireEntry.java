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
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class MercurialWireEntry extends EntryProvider {
    public MercurialWireEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("wire", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.COPPER_WIRE.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                Mercurial Wires can connect different parts of your Mercurial Logistics system. Substances will move freely between any blocks connected by wires.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/copper_wire")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               Right-click one block, then right-click another block to connect them with the wire.
               \\
               \\
               Only mercurial logistics blocks, such as inserters, extractors or connection nodes, can be connected with wires.
                """
        );

        this.page("removal", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Removing Wires");
        this.pageText("""
               To remove a wire, break one of the blocks the wire is connected to.
               \\
               \\
               Alternatively, click both blocks connected by the wire with the wire item once more to remove just the one wire.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Wire";
    }

    @Override
    protected String entryDescription() {
        return "Item-Over-Wire Transport";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.COPPER_WIRE.get());
    }

    @Override
    protected String entryId() {
        return "mercurial_wire";
    }
}
