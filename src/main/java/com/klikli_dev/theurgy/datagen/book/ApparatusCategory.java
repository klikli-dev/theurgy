// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.apparatus.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;

public class ApparatusCategory extends CategoryProvider {

    public static final String CATEGORY_ID = "apparatus";

    public ApparatusCategory(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "____________a_t_b_________________",
                "__________________________________",
                "______________l_ç_________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "________________i_________________",
                "__________________________________",
                "________________h_________________",
                "__________________________________",
                "__________________________________"

        };
    }

    @Override
    protected void generateEntries() {

        var introEntry = new IntroEntry(this).generate('i');

        var howToEntry = new HowToEntry(this).generate('h');
        howToEntry.addParent(this.parent(introEntry));

        var spagyricsEntry = new SpagyricsEntry(this).generate('ç');
        spagyricsEntry.addParent(this.parent(introEntry));

        var pyromanticBrazierEntry = new PyromanticBrazierEntry(this).generate('b');
        pyromanticBrazierEntry.addParent(this.parent(spagyricsEntry));

        var salAmmoniacTankEntry = new SalAmmoniacTankEntry(this).generate('t');
        salAmmoniacTankEntry.addParent(this.parent(spagyricsEntry));

        var salAmmoniacAccumulatorEntry = new SalAmmoniacAccumulatorEntry(this).generate('a');
        salAmmoniacAccumulatorEntry.addParent(this.parent(salAmmoniacTankEntry));

        var liquefactionCauldronEntry = new LiquefactionCauldronEntry(this).generate('l');
        liquefactionCauldronEntry.addParent(this.parent(spagyricsEntry));
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "About Apparatus");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky2.png"))
                .withIcon(ItemRegistry.CALCINATION_OVEN.get());
    }

}
