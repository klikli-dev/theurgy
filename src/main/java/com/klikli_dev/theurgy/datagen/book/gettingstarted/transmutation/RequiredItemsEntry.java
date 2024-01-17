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
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.CreateSulfurEntry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RequiredItemsEntry extends EntryProvider {

    public static final String ENTRY_ID = "required_items_transmutation";

    public RequiredItemsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("source", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.QUARTZ.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Source Sulfur");
        this.pageText("""
                Again we need some sulfur to convert into your desired sulfur. For this demonstration we will use sulfur of Quartz.
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
                .withItem(Ingredient.of(SulfurRegistry.IRON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Target Sulfur");
        this.pageText("""
                Now you need *two* sulfur of the target type.
                \\
                \\
                One sulfur that will be converted into Niter for our intermediate reformation and one sulfur for the final reformation of all our Niter into the target sulfur.
                   """
        );
        this.page("target2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Target Sulfur");
        this.pageText("""
                For this demonstration we will use sulfur of iron.
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
        return BookIconModel.create(Items.QUARTZ);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}