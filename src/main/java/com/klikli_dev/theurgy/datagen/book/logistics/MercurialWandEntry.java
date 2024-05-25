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

public class MercurialWandEntry extends EntryProvider {

    public static final String ENTRY_ID = "mercurial_wand";
    public MercurialWandEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("conversion", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.MERCURIAL_WAND.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                Mercurial Logistics apparatus can be controlled and configured using the Mercurial Wand.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercurial_wand")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               Crouch and Scroll to change the mode of the wand.
               \\
               \\
               Right-click to interact with the target apparatus/block.
                """
        );

        this.page("rotate", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Rotate Selected Direction");
        this.pageText("""
               With this mode, right-clicking on a block will cycle the selected direction of the target block.
               \\
               \\
               The "selected direction" is the direction the block will insert/extract to/from.
               \\
               \\
               The default selected direction is the face the block is attached to.
                """
        );

        this.page("rotate_visuals", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Rotate Selected Direction");
        this.pageText("""
               When looking at a logistics block with this mode selected, the block will highlight the selected direction on its target block.
               \\
               \\
               Yellow is the current direction, green is the direction that will be set if you right-click.\\
               Make sure that the side you want to insert/extract from is yellow!
                """
        );

        this.page("enable", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Enable/Disable");
        this.pageText("""
               With this mode, right-clicking on a block will enable or disable it.
               \\
               \\
               A disabled block will no longer insert or extract from the block it is attached to.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Wand";
    }

    @Override
    protected String entryDescription() {
        return "Controlling Mercurial Logistics";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURIAL_WAND.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
