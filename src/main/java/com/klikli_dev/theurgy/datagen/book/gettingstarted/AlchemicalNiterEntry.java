// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class AlchemicalNiterEntry extends EntryProvider {
    public static final String ENTRY_ID = "alchemical_niter";

    public AlchemicalNiterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SulfurRegistry.IRON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Alchemical Sulfur");
        this.pageText("""
                Alchemical Sulfur, as the "soul" of matter is an integral part of the transformation of matter.
                \\
                \\
                As seen in the Spagyrics chapter, it is Sulfur that determines the type of matter.
                """
        );

        this.page("intro2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Transformation");
        this.pageText("""
                The key to transforming matter is then to first transform the sulfur, after which the Sagyrics processes can be applied as before to reassemble the Principles into an object.
                \\
                \\
                There is one issue: Sulfur is not malleable, it resists transformation.
                """
        );

        this.page("niter", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SulfurRegistry.METALS_COMMON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Alchemical Niter");
        this.pageText("""
                The solution is to create Alchemical Niter from the Sulfur.
                \\
                \\
                Niter represents the "concept" behind a type of matter (such as "common metal" or "precious gem" instead of "iron" or "diamond"), and is much more malleable.
                """
        );

        this.page("niter2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Alchemical Niter");
        this.pageText("""
                For instance, Alchemical Niter of Common Gems can be transformed into any Common Gem Sulfur, but also into Common Metal Niter and with the right process even into Rare or Abundant Gem Niter. In short, Alchemical Niter is the key to true mastery over matter. 
                """
        );

        this.page("processes", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Transformation Processes");
        this.pageText("""
                All alchemical transformation processes operate on Sulfur or Niter, depending on the desired result, one or more of the following may be used and combined:
                - Reformation
                - Fermentation
                - Digestion
                """
        );

        //TODO: that should probably be a separate entry, either part of the 3 "paths", or a line of info-only entries
        this.page("reformation", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Reformation");
        this.pageText("""
                Reformation allows to change the type of sulfur or niter, however due to the properties of these substances, there are limitations.
                \\
                \\
                Sulfurs can be changed into another sulfur of the same type (e.g. "gem") and tier (e.g. "rare").\\
                Niters can be changed into a sulfur 
                """
        );
    }

    @Override
    protected String entryName() {
        return "Alchemical Sulfur and Niter";
    }

    @Override
    protected String entryDescription() {
        return "Two aspects of the \"Soul\" of Matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.EMPTY_CERAMIC_JAR_LABELED_ICON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
