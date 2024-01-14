// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class SalAmmoniacAccumulatorEntry extends EntryProvider {
    public static final String ENTRY_ID = "sal_ammoniac_accumulator";

    public SalAmmoniacAccumulatorEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                The first half of the Sal Ammoniac generation process.
                \\
                \\
                **Needs** to be placed on top of a {0}.
                """,
                this.entryLink("Sal Ammoniac Tank", this.parent.categoryId(), SalAmmoniacTankEntry.ENTRY_ID)
        );

        this.page("multiblock", () -> BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/sal_ammoniac_accumulator"))
                .build());

        //TODO: link to spagyrics solvent entry, mention that the recipes and all is there

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} on top of a {1}, and fill it with water by right-clicking with a water bucket.
                        \\
                        \\
                        Optionally insert a {2} by right-clicking the cauldron with it to speed up the process.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.page("recipe1", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sal_ammoniac_accumulator"))
                .build());

        this.page("working", () -> BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/sal_ammoniac_accumulator_working.png"))
                .build());
        this.pageTitle("Working Correctly");
        this.pageText("""
                If working properly, it will show blue or yellow (if using Sal Ammoniac Crystals) bubbles.
                """);
    }

    @Override
    protected String entryName() {
        return "Sal Ammoniac Accumulator";
    }

    @Override
    protected String entryDescription() {
        return "Creating liquid Sal Ammoniac";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
