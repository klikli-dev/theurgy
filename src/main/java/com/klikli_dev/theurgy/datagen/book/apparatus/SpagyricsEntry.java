package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.SpagyricsCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class SpagyricsEntry extends EntryProvider {
    public SpagyricsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Spagyrics Apparatus");
        this.pageText("""
                Spagyrics Apparatus enable to separate and recombine the three principles of a substance.
                """);

        this.page("further_reading", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Further Reading");
        this.pageText("""
                        See {0}.
                        """,
                this.categoryLink("Spagyircs", SpagyricsCategoryProvider.CATEGORY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Spagyrics Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "Separating and Recombining the Three Principles";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    }

    @Override
    protected String entryId() {
        return "spagyrics";
    }
}
