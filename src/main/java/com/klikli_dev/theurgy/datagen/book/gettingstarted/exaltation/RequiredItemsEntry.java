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
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.CreateSulfurEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RequiredItemsEntry extends EntryProvider {

    public static final String ENTRY_ID = "required_items_exaltation";

    public RequiredItemsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("source", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SulfurRegistry.IRON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Source Sulfur");
        this.pageText("""
                Again we need some sulfur to convert into your desired sulfur. For this demonstration we will use sulfur of Iron.
                \\
                \\
                We need 4 iron sulfur per 1 gold sulfur we want to create.
                  """
        );

        this.page("source2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Source Sulfur");
        this.pageText("""
                        It is a good idea to collect a few of these in order to have a steady supply to convert.
                        \\
                        \\
                        View {0} to refresh how to obtain sulfur from an item, or use the Iron Sulfur you obtained in the Transmutation experiment.
                         """,
                this.entryLink("Extracting Sulfur", GettingStartedCategoryProvider.CATEGORY_ID, CreateSulfurEntry.ENTRY_ID)
        );

        this.page("target", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SulfurRegistry.GOLD.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Target Sulfur");
        this.pageText("""
                Because we are converting within the same type (metals) you only need *one* sulfur of the target type.
                \\
                \\
                It will be used for the final reformation of all our Niter into the target sulfur.
                   """
        );
        this.page("target2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Target Sulfur");
        this.pageText("""
                For this demonstration we will use sulfur of gold.
                \\
                \\
                *Note: If you want to switch both tier and type you will need two sulfurs of the target type, like in the previous experiment.*
                       """
        );

        this.page("catalyst", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.PURIFIED_GOLD.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("catalyst");
        this.pageText("""
                        Finally we need purified gold as a catalyst for the Digestion process.
                        \\
                        \\
                        The next entry will describe how to obtain it from {0}, {1} and {2}.
                        """,
                this.itemLink(Items.GOLD_INGOT),
                this.itemLink("any Alchemical Salt", SaltRegistry.MINERAL.get()),
                this.itemLink(Items.WATER_BUCKET)
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
        return BookIconModel.create(Items.IRON_INGOT);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}