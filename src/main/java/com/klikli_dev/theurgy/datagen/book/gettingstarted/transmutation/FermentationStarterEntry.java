// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class FermentationStarterEntry extends EntryProvider {

    public static final String ENTRY_ID = "fermentation_starter";

    public FermentationStarterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("fermentation_starter", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.FERMENTATION_STARTER.get()))
                .withText(this.context().pageText())
        );
        this.pageText("""
                When trying to obtain large amounts of niter, a lot of fermentation ingredients, sugar and crops, are used up. Consequently it is all the more important to find a way to reduce the amount of crops needed to ferment niter.
                \\
                By pre-fermenting crops with sugar in Sal Ammoniac, a highly concentrated material called {0} is obtained. As only a very small amount of this material is needed to ferment niter, it is many times more efficient than using raw crops or sugar.
                """,
                this.itemLink(ItemRegistry.FERMENTATION_STARTER.get())
        );

        this.page("extraction", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Extraction");
        this.pageText("""
                        [#]($INPUT)Right-click[#]() the {0} with the ingredients to place them inside:
                        - {1}
                        - {2}
                        - any Crops
                        
                        \\
                        \\
                        (Check JEI/EMI for recipe details)
                        \\
                        \\
                        Then simply proceed with the fermentation process as usual.
                        """,
                this.itemLink(ItemRegistry.FERMENTATION_VAT.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET),
                this.itemLink(Items.SUGAR)
        );


        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
                {0} will be produced in large batches consisting of many pieces, but consuming only one sugar and one crop each.
                \\
                \\
                You can now use one piece of {0} per niter fermentation process, instead of one crop or one sugar.
                """,
                this.itemLink(ItemRegistry.FERMENTATION_STARTER.get())
        );
    }

    @Override
    protected String entryName() {
        return "Efficient Fermentation";
    }

    @Override
    protected String entryDescription() {
        return "Using less crops for more Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.FERMENTATION_STARTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}