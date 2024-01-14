// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.CaloricFluxEmitterEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class PyromanticBrazierEntry extends EntryProvider {
    public PyromanticBrazierEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.PYROMANTIC_BRAZIER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                A simple heating apparatus that can be used to power other Alchemical Devices. It is powered by burning furnace fuel, such as wood, coal, or charcoal.
                """);

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());

        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} below the Alchemical Device you want to power, then insert a fuel item by right-clicking the brazier with it.
                        \\
                        \\
                        Alternatively a hopper (or pipes) can be used to insert fuel items.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/pyromantic_brazier"))
                .build());

        this.pageTitle("Upgrades");
        this.pageText(
                """
                        To become independent from furnace fuel you can instead use a {0} to power your Alchemical Devices.\\
                        Another advantage of using Caloric Flux is the ability to transfer heat over long distances.
                        """,
                this.entryLink("Caloric Flux Emitter", ApparatusCategory.CATEGORY_ID, CaloricFluxEmitterEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Pyromantic Brazier";
    }

    @Override
    protected String entryDescription() {
        return "Heating your Alchemical Devices";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.PYROMANTIC_BRAZIER.get());
    }

    @Override
    protected String entryId() {
        return "pyromantic_brazier";
    }
}
