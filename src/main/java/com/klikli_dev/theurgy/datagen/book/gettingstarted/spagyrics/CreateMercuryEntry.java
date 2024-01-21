// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class CreateMercuryEntry extends EntryProvider {

    public static final String ENTRY_ID = "create_mercury";

    public CreateMercuryEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_SHARD.get()))
                .withText(this.context().pageText()));
        this.pageTitle("Extracting Mercury");
        this.pageText(
                """
                        Mercury is the spirit, or energy, contained within matter. Like Salt, it is needed to create items from Alchemical Sulfur. Almost all items yield Mercury, but the more valuable and the more refined the item, the more mercury they will yield.
                        """
        );

        this.page("step1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Distilling Mercury");
        this.pageText(
                """
                        Right-click the {0} with any mercury-yielding item, such as Crops, Food, Ores, Raw Metals, but also Stone, Sand, Glass, ...\\
                        For low-value items you may need to add multiple items at once to obtain even one {1}.
                         """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.page("step2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Providing Heat");
        this.pageText(
                """
                        Now add fuel, such as Coal, to the {0} below the {1} to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.DISTILLER.get())
        );

        this.page("step3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Obtaining the Mercury");
        this.pageText(
                """
                        After a while some mercury shards will have been created, you can right-click the {0} with an empty hand to obtain {1}.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );
    }

    @Override
    protected String entryName() {
        return "Extracting Mercury";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining Mercury - the \"Spirit\"";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
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