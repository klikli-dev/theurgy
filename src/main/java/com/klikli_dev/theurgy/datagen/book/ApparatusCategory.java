// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookTrueConditionModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.apparatus.*;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.DigestionVatEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.TransmutationAndExaltationEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.FermentationVatEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.CaloricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.MercuryCatalystEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.MercuryFluxEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.SulfuricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.reformation.*;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;


public class ApparatusCategory extends CategoryProvider {

    public static final String CATEGORY_ID = "apparatus";

    public ApparatusCategory(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "______________a_ï_č___ț___________",
                "__________________________________",
                "______________t_ç_d_ʂ_r_ŕ_________",
                "__________________________________",
                "____________ć_l___b_____ş_________",
                "__________________________________",
                "__________m_ì___i_________________",
                "__________________________________",
                "____________ș___h_________________",
                "__________________________________",
                "____________________f_é_ď_________",
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
        this.reformationEntries(introEntry);
        this.exaltationEntries(introEntry);
    }

    private void exaltationEntries(BookEntryModel parent) {
        var transmutationAndExaltation = new TransmutationAndExaltationEntry(this).generate('é');
        transmutationAndExaltation.addParent(this.parent(parent));
        transmutationAndExaltation.withCondition(BookTrueConditionModel.builder().build());

        var fermentationVatEntry = new FermentationVatEntry(this).generate('f');
        fermentationVatEntry.addParent(this.parent(transmutationAndExaltation));

        var digestionVatEntry = new DigestionVatEntry(this).generate('ď');
        digestionVatEntry.addParent(this.parent(transmutationAndExaltation));
    }

    private void reformationEntries(BookEntryModel parent) {
        var reformationArrayEntry = new ReformationArrayEntry(this).generate('r');
        reformationArrayEntry.addParent(this.parent(parent));
        reformationArrayEntry.withCondition(BookTrueConditionModel.builder().build());

        var sulfuricFluxEmitterEntry = new SulfuricFluxEmitterReferenceEntry(this).generate('ş');
        sulfuricFluxEmitterEntry.addParent(this.parent(reformationArrayEntry));

        var targetPedestalEntry = new TargetPedestalEntry(this).generate('ț');
        targetPedestalEntry.addParent(this.parent(reformationArrayEntry));

        var sourcePedestalEntry = new SourcePedestalEntry(this).generate('ʂ');
        sourcePedestalEntry.addParent(this.parent(reformationArrayEntry));

        var resultPedestalEntry = new ResultPedestalEntry(this).generate('ŕ');
        resultPedestalEntry.addParent(this.parent(reformationArrayEntry));
    }

    private void mercuryFluxEntries(BookEntryModel parent) {
        var mercuryFluxEntry = new MercuryFluxEntry(this).generate('ì');
        mercuryFluxEntry.addParent(this.parent(parent));
        mercuryFluxEntry.withCondition(BookTrueConditionModel.builder().build());

        var mercuryCatalystEntry = new MercuryCatalystEntry(this).generate('m');
        mercuryCatalystEntry.addParent(this.parent(mercuryFluxEntry));

        var caloricFluxEmitterEntry = new CaloricFluxEmitterEntry(this).generate('ć');
        caloricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));

        var sulfuricFluxEmitterEntry = new SulfuricFluxEmitterEntry(this).generate('ș');
        sulfuricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));
    }

    private void spagyricsEntries(BookEntryModel parent) {
        var spagyricsEntry = new SpagyricsEntry(this).generate('ç');
        spagyricsEntry.addParent(this.parent(parent));
        spagyricsEntry.withCondition(BookTrueConditionModel.builder().build());

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
