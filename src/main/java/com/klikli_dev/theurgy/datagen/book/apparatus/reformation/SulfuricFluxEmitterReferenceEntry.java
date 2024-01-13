// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.reformation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.SulfuricFluxEmitterEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class SulfuricFluxEmitterReferenceEntry extends EntryProvider {

    public static final String ENTRY_ID = "sulfuric_flux_emitter_reference";

    public SulfuricFluxEmitterReferenceEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                        See the {0} in the "Mercury Flux" part of this category.
                        """,
                this.entryLink("Sulfuric Flux Emitter Entry", ApparatusCategory.CATEGORY_ID, SulfuricFluxEmitterEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Sulfuric Flux Emitter";
    }

    @Override
    protected String entryDescription() {
        return "Transporting Sulfuric Information";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
