// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation;

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

public class DigestionVatEntry extends EntryProvider {

    public static final String ENTRY_ID = "digestion_vat";

    public DigestionVatEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.DIGESTION_VAT.get()))
                .withText(this.context().pageText()));
        this.add(this.context().pageText(),
                """
                        Digestion vats are required for Alchemical Digestion, whereby Alchemical Niters can be converted into higher or lower tiers.\\
                        This is required to convert objects of different value into each other, such as Iron into Gold - also known as [#]($PURPLE)Exaltation[#]().
                         """
        );

        //TODO: create an entry about pure gold in the category/entry/graph that discusses exaltation
        //TODO: link to the page that talks about the digestion process

        this.page("process", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Process");
        this.add(this.context().pageText(),
                """
                        Unlike other apparatus the digestion vat only works when closed.\\
                        Shift-right-click with an empty hand to open or close the vat.\\
                        When closed, no items or fluids can be put in or taken out.
                        """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/digestion_vat")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the vat on the ground.\\
                        Then right-click it with the ingredients (usually an Alchemical Niter and {0})\\
                        Right-click the block with a Sal Ammoniac bucket (or supply via pipes)\\
                        Shift-right-click the vat with an empty hand to close it.
                        """,
                this.itemLink(ItemRegistry.PURIFIED_GOLD.get())
        );

        this.page("usage2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Both items and fluids can also be piped in and out of the vat, or hoppers can be used.
                        """
        );

        this.page("redstone", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Redstone");
        this.add(this.context().pageText(),
                """
                        The vat can be closed by applying a redstone signal (if a valid recipe is present).\\
                        If a redstone signal is applied and ingredients for a valid recipe are inserted, the vat will automatically close.
                        \\
                        \\
                        If the redstone signal is turned off the vat will open again.
                        """
        );

    }

    @Override
    protected String entryName() {
        return "Digestion Vat";
    }

    @Override
    protected String entryDescription() {
        return "Convert Alchemical Niter from one tier to another";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.DIGESTION_VAT.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
