package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class IntroEntry extends EntryProvider {
    public IntroEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Alchemical Apparatus");
        this.pageText("""
                Alchemists use a variety of tools and devices to aid them in their work. These devices are collectively referred to as apparatus.
                \\
                \\
                The Apparatus category contains crafting and basic usage instructions for each apparatus.
                """);

        this.page("further_reading", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Further Reading");
        this.pageText("""
                To find out how to use the apparatus to achieve specific ends, view the other categories, especially {0}.
                """,
                this.categoryLink("Getting Started", GettingStartedCategoryProvider.CATEGORY_ID)
                );
    }

    @Override
    protected String entryName() {
        return "Alchemical Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "How to interact with the tools of the trade";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.CATEGORY_START;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.CALCINATION_OVEN.get());
    }

    @Override
    protected String entryId() {
        return "intro";
    }
}
