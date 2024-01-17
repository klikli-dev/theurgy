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

public class ResultPedestalEntry extends EntryProvider {

    public static final String ENTRY_ID = "result_pedestal";

    public ResultPedestalEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()))
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageText(),
                """
                        This pedestal will be filled with the Sulfur resulting from the reformation process.
                        """
        );

        this.page("structure", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Structure");
        this.add(this.context().pageText(),
                """
                        Reformation recipes have only one output. Correspondingly, your reformation array needs only one result pedestal. Additional pedestals will not be linked to the array.
                        """
        );


        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the pedestal on the ground.\\
                        After the reformation is complete, the pedestal will be filled with the resulting sulfur.\\
                        Right-click the pedestal with an empty hand to retrieve the sulfur.
                        """
        );

        this.page("visuals", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Visuals");
        this.add(this.context().pageText(),
                """
                        A glowing orb will show above the pedestal if it contains sulfur.
                        """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/reformation_result_pedestal"))
                .build());

    }

    @Override
    protected String entryName() {
        return "Reformation Result Pedestal";
    }

    @Override
    protected String entryDescription() {
        return "Holds the replicated resulting Sulfur, identical to the Target Sulfur";
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
