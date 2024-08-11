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

public class CalcinationOvenEntry extends EntryProvider {

    public static final String ENTRY_ID = "calcination_oven";

    public CalcinationOvenEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.CALCINATION_OVEN.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                         Calcination is the process whereby [#]($PURPLE)Alchemical Salt[#]() is extracted from matter. The {0} is a simple device that can be used to perform this process by applying consistent high heat to the target object.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        this.page("multiblock", () -> BookMultiblockPageModel.create()
                .withVisualizeButton(false)
                .withMultiblockId(Theurgy.loc("placement/calcination_oven")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} on top of a {1}, then insert the item to calcinate by right-clicking the oven with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/calcination_oven")));

        this.page("working", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/calcination_oven_working.png")));
        this.pageTitle("Working Correctly");
        this.pageText(
                """
                        If the {0} is working properly, it will show the orange rings moving slightly.
                                      """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );
    }

    @Override
    protected String entryName() {
        return "Calcination Oven";
    }

    @Override
    protected String entryDescription() {
        return "Extracting Alchemical Salt from Matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.CALCINATION_OVEN.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
