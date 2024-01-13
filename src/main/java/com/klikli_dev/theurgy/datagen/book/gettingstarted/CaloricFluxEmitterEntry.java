// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

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

public class CaloricFluxEmitterEntry extends EntryProvider {

    public static final String ENTRY_ID = "caloric_flux_emitter";

    public CaloricFluxEmitterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.CALORIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                        Using coal as a heat is positively *barbaric*. Alchemists can do better.
                        To heat your apparatus more efficiently and even from a distance, you can use a Caloric Flux Emitter.
                        The following pages list all the needed machinery.
                         """,
                this.entryLink("Caloric Flux Emitter", ApparatusCategory.CATEGORY_ID, CaloricFluxEmitterEntry.ENTRY_ID),
                this.categoryLink("Apparatus", ApparatusCategory.CATEGORY_ID)
        );

        this.page("intro2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Caloric Flux Emitter");
        this.pageText("""
                        View {0} in the {1} Category on how to craft and use them.
                         """,
                this.entryLink("Caloric Flux Emitter", ApparatusCategory.CATEGORY_ID, CaloricFluxEmitterEntry.ENTRY_ID),
                this.categoryLink("Apparatus", ApparatusCategory.CATEGORY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Caloric Flux";
    }

    @Override
    protected String entryDescription() {
        return "A more convenient heat source.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.CALORIC_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}