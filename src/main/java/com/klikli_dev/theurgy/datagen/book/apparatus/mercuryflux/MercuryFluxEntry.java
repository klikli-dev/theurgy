package com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.SpagyricsCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class MercuryFluxEntry extends EntryProvider {
    public static final String ENTRY_ID = "mercury_flux";
    public MercuryFluxEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Mercury Flux");
        this.pageText("""
                **Mercury** represents the Energy contained in all matter. The Spagyrics processes for Mercury extraction yield {0}. In this Form it is not immediately useful as an energy source, so it must first be transformed - catalyzed - into [#]($PURPLE)Mercury Flux[#](), which is Mercury in it's natural Form.
                """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );
    }

    @Override
    protected String entryName() {
        return "Mercury Flux";
    }

    @Override
    protected String entryDescription() {
        return "Raw Energy Manipulation";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_SHARD.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
