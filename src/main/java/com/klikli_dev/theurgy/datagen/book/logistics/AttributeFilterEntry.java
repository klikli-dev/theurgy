// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

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

public class AttributeFilterEntry extends EntryProvider {
    public static final String ENTRY_ID = "attribute_filter";

    public AttributeFilterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("filter", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.ATTRIBUTE_FILTER.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                Unlike List Filters, Attribute Filters allow filtering items based on their attributes, such as their tag, damage, or enchantments.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/attribute_filter")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
               1. Right-click with the filter in hand to open the filter GUI.
               2. Place an item with the desired attribute in the filter.
               3. Scroll to select the right attribute.
               4. Click the "Add" or "Add Inverted" button to add the attribute or it's opposite to the filter.
               5. Optionally repeat with the same or other items to combine multiple attributes.
               6. Right-click an inserter or extractor with the filter to apply it.
               """
        );

        this.page("removal", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Removal");
        this.pageText("""
               1. Right-click with an empty hand on an inserter or extractor to remove the filter.
               2. The filter will be returned to the player's inventory.
               """
        );

        this.page("credit", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Credit where Credit is Due");
        this.pageText("""
               Filter functionality, including all textures, is largely based on the Filters found in the Mod "Create". [Click here to learn more about Create](https://github.com/Creators-of-Create/Create).
               \\
               \\
               This adaption is possible due to Create being Open Source, licensed under the MIT License. A sincere thanks to the creators of create for their foundational work.
               """
        );
    }

    @Override
    protected String entryName() {
        return "Mercurial Attribute Filter";
    }

    @Override
    protected String entryDescription() {
        return "Allows limiting the insertion and extraction to items with specific attributes.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.ATTRIBUTE_FILTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
