// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.mojang.datafixers.util.Pair;

public class HowToEntry extends EntryProvider {
    public HowToEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("basics", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Basic Concepts");
        this.pageText("""
                Generally, each apparatus only has one specific function, such as generating heat, or performing a specific processing operation.
                \\
                \\
                Further, all apparatus follow a standardized interaction pattern that makes it easier to use them both for manual interactions and for automation.
                """);

        this.page("manual_interaction", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Manual Interaction");
        this.pageText("""
                To interact with an apparatus, approach it and right-click on it.
                \\
                \\
                **Taking Output Items**\\
                If you have an empty hand, the machine will first try to take the contents of its output slot and place them in your inventory.
                """);


        this.page("manual_interaction2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Manual Interaction");
        this.pageText("""
                **Taking Input Items**\\
                If there are no output items, it will instead try to place the contents of its input slot into your inventory, effectively emptying it.
                \\
                \\
                **Inserting Items**\\
                If you have an item in your hand, the apparatus will automatically try to insert it into the input slot.
                """);

        this.page("fluid_interaction", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Fluids");
        this.pageText("""
                If you click on an apparatus with a filled fluid container in your hand, it will try to empty the container into the device.
                \\
                \\
                If you click on an apparatus with an empty fluid container in your hand, it will instead try to fill the container from the device.
                """);

        this.page("emptying_fluids", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Emptying Fluids");
        this.pageText("""
                Crouch and Right-Click on an apparatus to empty all fluids from it.
                \\
                \\
                This is particularly useful if small amounts of fluid are left but you want to add a full bucket.
                """);
    }

    @Override
    protected String entryName() {
        return "How to use Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "How to interact with the tools of the trade.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/apparatus_howto.png"), 64, 64);
    }

    @Override
    protected String entryId() {
        return "how_to";
    }
}
