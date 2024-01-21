// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class PurifiedGoldEntry extends EntryProvider {

    public static final String ENTRY_ID = "purified_gold";

    public PurifiedGoldEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("gold", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.PURIFIED_GOLD.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageText("""
                Purified Gold acts as a catalyst for Niter Digestion, making the already malleable Niter even more so. This allows Niter to be processed into a different tier by combining or splitting it.
                  """
        );

        this.page("purifying", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Purifying Gold");
        this.pageText("""
                        Purification is achieved - somewhat counterintuitively - by Digestion. However, luckily, it does not need {0} as catalyst.
                        \\
                        \\
                        Instead, any {1} can be used. The Salt will connect with the impurities in the Gold and draw them out.
                         """,
                this.itemLink(ItemRegistry.PURIFIED_GOLD.get()),
                this.itemLink("Alchemical Salt", SaltRegistry.MINERAL.get())
        );

        this.page("digestion", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.DIGESTION_VAT.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Digestion of Gold");
        this.pageText("""
                        [#]($INPUT)Right-click[#]() the {0} with the ingredients to place them inside:
                        - {1}
                        - {2}
                        - {3}
                        """,
                this.itemLink(ItemRegistry.DIGESTION_VAT.get()),
                this.itemLink(Items.GOLD_INGOT),
                this.itemLink("Any Alchemical Salt", SaltRegistry.MINERAL.get()),
                this.itemLink(Items.WATER_BUCKET)
        );

        this.page("start", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Process");
        this.pageText("""
                        [#]($INPUT)Shift-right-click[#]() the {0} with an [#]($INPUT)empty hand[#]() to close the vat and start the digestion.
                        \\
                        \\
                        Once it opens again the process is complete.
                        \\
                        \\
                        [#]($INPUT)Right-click[#]() with an empty hand to retrieve the {1}.
                        """,
                this.itemLink(ItemRegistry.DIGESTION_VAT.get()),
                this.itemLink(ItemRegistry.PURIFIED_GOLD.get())
        );
    }

    @Override
    protected String entryName() {
        return "Purified Gold";
    }

    @Override
    protected String entryDescription() {
        return "The Catalysator for Niter Digestion";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.PURIFIED_GOLD.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}