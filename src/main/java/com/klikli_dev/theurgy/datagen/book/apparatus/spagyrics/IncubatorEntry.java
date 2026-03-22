// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class IncubatorEntry extends EntryProvider {

    public static final String ENTRY_ID = "incubator";

    public IncubatorEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.INCUBATOR.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                Incubation is the process of *recombination* of the Principles of Matter into actual objects.\\
                The Incubator has one vessel for each of the Principles, and a central chamber where the recombination takes place.
                """
        );

        this.page("multiblock", () -> BookMultiblockPageModel.create()
                .withVisualizeButton(false)
                .withMultiblockId(Theurgy.loc("placement/incubator")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} on top of a {1} and one of each of the three vessels next to it. Insert the items to process by right-clicking the vessels with them.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("recipe_incubator", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator")));

        this.page("recipe_mercury_vessel", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_mercury_vessel")));

        this.page("recipe_salt_vessel", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_salt_vessel")));

        this.page("recipe_sulfur_vessel", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_sulfur_vessel")));

        this.page("working", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/incubator_working.png")));
        this.pageTitle("Working Correctly");
        this.pageText(
                """
                        If the {0} is working properly, it will show smoke.
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get())
        );
    }

    @Override
    protected String entryName() {
        return "Incubator";
    }

    @Override
    protected String entryDescription() {
        return "Reassembling Matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.INCUBATOR.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
