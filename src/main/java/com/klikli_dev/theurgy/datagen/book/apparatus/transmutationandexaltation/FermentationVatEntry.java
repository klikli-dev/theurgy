// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation;

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

public class FermentationVatEntry extends EntryProvider {

    public static final String ENTRY_ID = "fermentation_vat";

    public FermentationVatEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.FERMENTATION_VAT.get()))
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageText(),
                """
                        Fermentation vats enable a process called Alchemical fermentation. This allows converting Alchemical Sulfur into Alchemical Niter, enabling [#]($PURPLE)Transmutation[#]().
                        """
        );

        //TODO: link to the page that talks about the fermentation process

        this.page("uses", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Uses");
        this.add(this.context().pageText(),
                """
                        Fermentation is required to convert objects into other types of objects, such as gems into metals.
                         """
        );

        this.page("process", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Process");
        this.add(this.context().pageText(),
                """
                        Unlike other apparatus the fermentation vat only works when closed.\\
                        Shift-right-click with an empty hand to open or close the vat.\\
                        When closed, no items or fluids can be put in or taken out.
                        """
        );

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the vat on the ground.\\
                        Then right-click it with the ingredients (usually an Alchemical Sulfur and a source of sugar, such as a crop)\\
                        Right-click the block with a water bucket (or supply via pipes)\\
                        Shift-right-click the vat with an empty hand to close it.
                        """
        );


        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/fermentation_vat"))
                .build());

    }

    @Override
    protected String entryName() {
        return "Fermentation Vat";
    }

    @Override
    protected String entryDescription() {
        return "Convert Alchemical Sulfur into malleable Alchemical Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.FERMENTATION_VAT.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
