// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.reformation;

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

public class SourcePedestalEntry extends EntryProvider {

    public static final String ENTRY_ID = "source_pedestal";

    public SourcePedestalEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()))
                .withText(this.context().pageText()));
        this.add(this.context().pageText(),
                """
                        Reformation requires a source of sulfur that will be transformed, or "converted", into another type of sulfur.
                        \\
                        This type of pedestal holds these source sulfurs that **will be consumed** in the process.
                        """
        );

        this.page("structure", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Structure");
        this.add(this.context().pageText(),
                """
                        Reformation recipes require one or more source sulfurs. Correspondingly, your reformation array needs at least one source pedestal, but if the recipe requires more sources, you need to place more.
                        """
        );

        this.page("sulfur_consumption", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Sulfur consumption");
        this.add(this.context().pageText(),
                """
                        JEI and EMI will show exactly how many source sulfurs need to be placed per pedestal and how many pedestals are required. \\
                        Usually one sulfur is consumed per pedestal. However, for very expensive recipes that use a total of 8 pedestals, it is possible that more than one sulfur must be placed per pedestal.
                        """
        );


        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the pedestal on the ground.\\
                        Then right-click it with the sulfur you want to use as a source.
                        """
        );


        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/reformation_source_pedestal")));

    }

    @Override
    protected String entryName() {
        return "Reformation Source Pedestal";
    }

    @Override
    protected String entryDescription() {
        return "Holds Sulfur to be consumed and transformed into another type of Sulfur";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
