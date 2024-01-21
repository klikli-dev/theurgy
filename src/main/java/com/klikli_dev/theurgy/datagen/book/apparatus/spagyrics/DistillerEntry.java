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

public class DistillerEntry extends EntryProvider {

    public static final String ENTRY_ID = "distiller";

    public DistillerEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.DISTILLER.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                         Distillation allows to obtain purified [#]($PURPLE)Alchemical Mercury[#]() from matter. To this end the object is heated until it dissolves into a gaseous form and the resulting vapour is condensed into crystals. The Mercury obtained this way is stable and can be used in alchemical recipes.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        //TODO: link to mercury energy stuff
        //TODO: Link to matter teleportation u sing mercury


        this.page("multiblock", () -> BookMultiblockPageModel.create()
                .withMultiblockId(Theurgy.loc("placement/distiller")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} on top of a {1}, then insert the item to distill by right-clicking the Distiller with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/distiller")));

        this.page("working", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/distiller_working.png")));
        this.pageTitle("Working Correctly");
        this.pageText(
                """
                        If the {0} is working properly, it will float with a bobbing motion.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get())
        );
    }

    @Override
    protected String entryName() {
        return "Distiller";
    }

    @Override
    protected String entryDescription() {
        return "Extracting Alchemical Mercury from Matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.DISTILLER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
