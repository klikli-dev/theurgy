// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.CreateSulfurEntry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RequiredItemsEntry extends EntryProvider {

    public static final String ENTRY_ID = "required_items";

    public RequiredItemsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("source", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.LAPIS.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Source Sulfur");
        this.pageText("""
                        First you of course need some sulfur to convert into your desired sulfur. For this demonstration we will use sulfur of lapis lazuli.
                        \\
                        \\
                        It is a good idea to collect a few of these in order to have a steady supply to convert.
                         """
        );

        this.page("source2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Source Sulfur");
        this.pageText("""
                        View {0} to refresh how to obtain sulfur from an item, or use the Quartz Sulfur you obtained in the Reformation experiment.
                         """,
                this.entryLink("Extracting Sulfur", GettingStartedCategoryProvider.CATEGORY_ID, CreateSulfurEntry.ENTRY_ID)
        );

        this.page("target", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.QUARTZ.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Target Sulfur");
        this.pageText("""
                Then you also need *one* sulfur of the target type. For this demonstration we will use sulfur of quartz.
                 """
        );
    }

    @Override
    protected String entryName() {
        return "Required Items";
    }

    @Override
    protected String entryDescription() {
        return "The items needed for this process";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.LAPIS_LAZULI);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}