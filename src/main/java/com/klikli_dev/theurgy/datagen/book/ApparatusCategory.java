// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
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
                "______________a_ï_č_______________",
                "__________________________________",
                "______________t_ç_d_______________",
                "__________________________________",
                "____________ć_l___b_______________",
                "__________________________________",
                "__________m_ì___i_________________",
                "__________________________________",
                "____________ș___h_________________",
                "__________________________________",
                "__________________________________"

        };
    }

    @Override
    protected void generateEntries() {

        var introEntry = new IntroEntry(this).generate('i');

        var howToEntry = new HowToEntry(this).generate('h');
        howToEntry.addParent(this.parent(introEntry));

       this.spagyricsEntries(introEntry);
       this.mercuryFluxEntries(introEntry);
    }

    private void mercuryFluxEntries(BookEntryModel parent){
        var mercuryFluxEntry = new MercuryFluxEntry(this).generate('ì');
        mercuryFluxEntry.addParent(this.parent(parent));

        var mercuryCatalystEntry = new MercuryCatalystEntry(this).generate('m');
        mercuryCatalystEntry.addParent(this.parent(mercuryFluxEntry));

        var caloricFluxEmitterEntry = new CaloricFluxEmitterEntry(this).generate('ć');
        caloricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));

        var sulfuricFluxEmitterEntry = new SulfuricFluxEmitterEntry(this).generate('ș');
        sulfuricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));
    }

    private void spagyricsEntries(BookEntryModel parent){
        var spagyricsEntry = new SpagyricsEntry(this).generate('ç');
        spagyricsEntry.addParent(this.parent(parent));

        var pyromanticBrazierEntry = new PyromanticBrazierEntry(this).generate('b');
        pyromanticBrazierEntry.addParent(this.parent(spagyricsEntry));

        var salAmmoniacTankEntry = new SalAmmoniacTankEntry(this).generate('t');
        salAmmoniacTankEntry.addParent(this.parent(spagyricsEntry));

        var salAmmoniacAccumulatorEntry = new SalAmmoniacAccumulatorEntry(this).generate('a');
        salAmmoniacAccumulatorEntry.addParent(this.parent(salAmmoniacTankEntry));

        var liquefactionCauldronEntry = new LiquefactionCauldronEntry(this).generate('l');
        liquefactionCauldronEntry.addParent(this.parent(spagyricsEntry));

        var calcinationOvenEntry = new CalcinationOvenEntry(this).generate('č');
        calcinationOvenEntry.addParent(this.parent(spagyricsEntry));

        var distillerEntry = new DistillerEntry(this).generate('d');
        distillerEntry.addParent(this.parent(spagyricsEntry));

        var incubatorEntry = new IncubatorEntry(this).generate('ï');
        incubatorEntry.addParent(this.parent(spagyricsEntry));
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
