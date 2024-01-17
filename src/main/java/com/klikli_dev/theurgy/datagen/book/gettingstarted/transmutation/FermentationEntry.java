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
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class FermentationEntry extends EntryProvider {
    public static final String ENTRY_ID = "fermentation_transmutation";

    public FermentationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Fermentation");
        this.pageText("""
                        We need two different Niters: some {0} as source that we want to convert and at least one {1} as conversion target.
                        """,
                this.itemLink("Alchemical Niter: Common Gems", SulfurRegistry.GEMS_COMMON.get()),
                this.itemLink("Alchemical Niter: Common Metals", SulfurRegistry.METALS_COMMON.get())
        );

        this.page("gems", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.GEMS_COMMON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Alchemical Niter: Common Gems");
        this.pageText("""
                        [#]($INPUT)Right-click[#]() the {0} with the ingredients to place them inside:
                        - {1}
                        - {2}
                        - {3} or any Crops
                                        
                        (Check JEI for more recipes)
                        """,
                this.itemLink(ItemRegistry.FERMENTATION_VAT.get()),
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get()),
                this.itemLink(Items.WATER_BUCKET),
                this.itemLink(Items.SUGAR)
        );

        this.page("metals", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.METALS_COMMON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Alchemical Niter: Common Metals");
        this.pageText("""
                        [#]($INPUT)Right-click[#]() the {0} with the ingredients to place them inside:
                        - {1}
                        - {2}
                        - {3} or any Crops

                        (Check JEI for more recipes)
                        """,
                this.itemLink(ItemRegistry.FERMENTATION_VAT.get()),
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get()),
                this.itemLink(Items.WATER_BUCKET),
                this.itemLink(Items.SUGAR)
        );

        this.page("start", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                        [#]($INPUT)Shift-right-click[#]() the {0} with an [#]($INPUT)empty hand[#]() to close the vat and start the fermentation.
                        \\
                        \\
                        Once it opens again the process is complete.
                        \\
                        \\
                        [#]($INPUT)Right-click[#]() with an empty hand to retrieve the {1}.
                        """,
                this.itemLink(ItemRegistry.FERMENTATION_VAT.get()),
                this.itemLink("Alchemical Niter: Common Gems", SulfurRegistry.GEMS_COMMON.get())
        );


        this.page("bulk", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Bulk Processing");
        this.pageText("""
                You can provide all these ingredients in bulk, the process will then repeat until all ingredients are consumed.
                \\
                \\
                You can also [#]($INPUT)shift-right-click[#]() with an [#]($INPUT)empty hand[#]() to open the vat and interrupt processing to take out the intermediate result.
                """
        );

        this.page("bulk2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Bulk Processing");
        this.pageText("""
                Remember: the more Niter you produce from Quartz (via Quartz Sulfur) the more Iron you can get in the end.
                """
        );

    }

    @Override
    protected String entryName() {
        return "Fermentation";
    }

    @Override
    protected String entryDescription() {
        return "Convert Quartz Sulfur into Common Gems Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.GEMS_COMMON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
