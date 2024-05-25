// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.logistics.IntroEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;


public class LogisticsCategory extends CategoryProvider {

    public static final String CATEGORY_ID = "logistics";

    public LogisticsCategory(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "________________i_________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________"

        };
    }

    @Override
    protected void generateEntries() {
        var introEntry = new IntroEntry(this).generate('i');
        //finish intro entry with some more info - or create lore entry

        //entry for wand
        //entry for inserter and extractor and wire, that then all lead to an entry that shows how to set them up
        //then an entry for the node
        //then entries for configuration, maybe as followups to the inserter/extractor entries
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Mercurial Logistics");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky2.png"))
                .withIcon(ItemRegistry.MERCURIAL_WAND.get());
    }

}
