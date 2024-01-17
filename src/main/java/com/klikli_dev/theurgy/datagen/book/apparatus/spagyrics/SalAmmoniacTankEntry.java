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
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class SalAmmoniacTankEntry extends EntryProvider {
    public static final String ENTRY_ID = "sal_ammoniac_tank";

    public SalAmmoniacTankEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_TANK.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                        The second half of the Sal Ammoniac generation process.
                        \\
                        \\
                        **Needs** to be placed below a {0}.
                        """,
                this.entryLink("Sal Ammoniac Accumulator", this.parent.categoryId(), SalAmmoniacAccumulatorEntry.ENTRY_ID)
        );

        this.page("recipe2", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sal_ammoniac_tank"))
                .build());
    }

    @Override
    protected String entryName() {
        return "Sal Ammoniac Tank";
    }

    @Override
    protected String entryDescription() {
        return "Storing liquid Sal Ammoniac";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SAL_AMMONIAC_TANK.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
