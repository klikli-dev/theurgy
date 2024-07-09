// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookTrueConditionModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.apparatus.HowToEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.IntroEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.CaloricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.MercuryCatalystEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.MercuryFluxEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.SulfuricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.reformation.*;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.*;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;

public class ApparatusCategory extends CategoryProvider {

    public static final String CATEGORY_ID = "apparatus";

    public ApparatusCategory(TheurgyBookProvider parent) {
        super(parent);
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
                "__________________________________",
                "______________________ṟ___________",
                "_________________________________",
                "______________________ṛ___________"

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

    @Override
    protected String categoryName() {
        return "About Apparatus";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(ItemRegistry.CALCINATION_OVEN.get());
    }

    @Override
    public String categoryId() {
        return CATEGORY_ID;
    }

    @Override
    protected BookCategoryModel additionalSetup(BookCategoryModel category) {
        return super.additionalSetup(category).withBackground(Theurgy.loc("textures/gui/book/bg_nightsky2.png"));
    }

    private void exaltationEntries(BookEntryModel parent) {
        var transmutationAndExaltation = new TransmutationAndExaltationEntry(this).generate('é');
        transmutationAndExaltation.addParent(this.parent(parent));
        transmutationAndExaltation.withCondition(BookTrueConditionModel.create());

        var fermentationVatEntry = new FermentationVatEntry(this).generate('f');
        fermentationVatEntry.addParent(this.parent(transmutationAndExaltation));

        var digestionVatEntry = new DigestionVatEntry(this).generate('ď');
        digestionVatEntry.addParent(this.parent(transmutationAndExaltation));

        var vatRedstoneEntry = new VatRedstoneEntry(this).generate('ṟ');
        vatRedstoneEntry.addParent(this.parent(digestionVatEntry).withLineReversed(true));
        vatRedstoneEntry.addParent(this.parent(fermentationVatEntry).withLineReversed(true));
        vatRedstoneEntry.withCondition(this.condition().or(
                this.condition().entryRead(digestionVatEntry),
                this.condition().entryRead(fermentationVatEntry)
        ));

        var basicVatAutomationEntry = new BasicVatAutomationEntry(this).generate('ṛ');
        basicVatAutomationEntry.addParent(this.parent(vatRedstoneEntry));
    }

    private void reformationEntries(BookEntryModel parent) {
        var reformationArrayEntry = new ReformationArrayEntry(this).generate('r');
        reformationArrayEntry.addParent(this.parent(parent));
        reformationArrayEntry.withCondition(BookTrueConditionModel.create());

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
        mercuryFluxEntry.withCondition(BookTrueConditionModel.create());

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
        spagyricsEntry.withCondition(BookTrueConditionModel.create());

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
}
