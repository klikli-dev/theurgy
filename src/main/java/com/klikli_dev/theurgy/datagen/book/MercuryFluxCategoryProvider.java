// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryFluxCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "mercury_flux";

    public MercuryFluxCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________i_______________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {
        var introEntry = this.add(this.makeIntroEntry('i'));
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Mercury Flux");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"))
                .withIcon(ItemRegistry.MERCURY_SHARD.get());
    }


    private BookEntryModel makeIntroEntry(char location) {
        this.context().entry("intro");
        this.add(this.context().entryName(), "Mercury Flux");
        this.add(this.context().entryDescription(), "Raw Energy Manipulation");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Mercury Flux");
        this.add(this.context().pageText(),
                """
                        **Mercury** represents the Energy contained in all matter. The Spagyrics processes for Mercury extraction yield {0}. In this Form it is not immediately useful as an energy source, so it must first be transformed - catalyzed - into [#]($PURPLE)Mercury Flux[#](), which is Mercury in it's natural Form.
                        """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.MERCURY_SHARD.get())
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro
                );
    }
}
