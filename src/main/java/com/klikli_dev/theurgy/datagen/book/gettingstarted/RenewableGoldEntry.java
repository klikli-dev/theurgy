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
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RenewableGoldEntry extends EntryProvider {

    public static final String ENTRY_ID = "renewable_gold";

    public RenewableGoldEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.GOLD_INGOT))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()).build());
        this.pageTitle("Renewable Riches");
        this.pageText("""
                The idea of the Goose that Lays the Golden Eggs is as familiar as it is ridiculous, as is the notion of Gold growing on Trees.
                \\
                \\
                Yet our discoveries have opened a path to just such a possibility...
                """);

        this.page("coal", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.CHARCOAL))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Charcoal as Mineral");
        this.pageText("""
                Charcoal is considered a mineral ("other minerals") in Alchemy. It is obviously renewable.
                \\
                \\
                Coal Sulfur can be obtained from Charcoal.
                """);

        this.page("conversion", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(Items.CHARCOAL))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Conversion of Coal");
        this.pageText("""
                When applying our knowlege from the previous experiments, we realize we can convert Charcoal into Gold, or even Diamonds.
                \\
                \\
                All we need to do is apply the same steps as before to convert between types and tiers as needed.
                """);

        this.page("tips", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Recipes");
        this.pageText("""
                (View JEI for help with the recipes)
                """);
    }

    @Override
    protected String entryName() {
        return "Renewable Riches";
    }

    @Override
    protected String entryDescription() {
        return "Gold Growing on Trees?";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/renewable_gold.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}