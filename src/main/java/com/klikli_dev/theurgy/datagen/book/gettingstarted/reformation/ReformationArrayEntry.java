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
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class ReformationArrayEntry extends EntryProvider {

    public static final String ENTRY_ID = "reformation_array";

    public ReformationArrayEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Reformation Array");
        this.pageText("""
                        Reformation is performed in a so-called "Reformation Array", a complex combination of apparatus that guide Sulfuric Flux to transform Sulfur.
                        \\
                        \\
                        View the {0} Category on how to craft and use the required apparatus.
                         """,
                this.entryLink("Apparatus", ApparatusCategory.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.apparatus.reformation.ReformationArrayEntry.ENTRY_ID)
        );

        this.page("target", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                You need one target pedestal, which will hold the sulfur you want to create more of.
                """);

        this.page("result", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                Further, you need one result pedestal, in which the output sulfur will end up.
                """);

        this.page("source", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                Then you need at least one source pedestal, which will hold the sulfur that will be consumed to be converted.
                \\
                \\
                It is recommended to have at least 2 or more such pedestals, as some future recipes require more than one sulfur to be consumed.
                """);

        this.page("emitter", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                Finally, you need a sulfuric flux emitter that will drive the actual conversion.
                """);

        this.page("next_steps", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Next Steps");
        this.pageText("""
                        Craft all the apparatus and place and link them as described in {0}.\\
                        After, [#]($INPUT)right-click[#]() the {1} with an empty hand to see if it is linked to all the pedestals.
                        \\
                        \\
                        Then move on to the next entry.
                        """,
                this.entryLink("Reformation Array", ApparatusCategory.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.apparatus.reformation.ReformationArrayEntry.ENTRY_ID),
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
        );
    }

    @Override
    protected String entryName() {
        return "Required Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "The apparatus needed for reformation";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}