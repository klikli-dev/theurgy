// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookTrueConditionModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.apparatus.HowToEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.InsertHelperEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.IntroEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.*;
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
    protected void generateEntries() {
        // Main entries
        var introEntry = this.add(new IntroEntry(this).generate());
        this.layout().entry(introEntry).at(-1, -1);

        var howToEntry = this.add(new HowToEntry(this).generate());
        howToEntry.addParent(this.parent(introEntry));
        this.layout().entry(howToEntry).below(introEntry, 2);

        var insertHelperEntry = this.add(new InsertHelperEntry(this).generate());
        insertHelperEntry.addParent(this.parent(howToEntry));
        this.layout().entry(insertHelperEntry).rightOf(howToEntry, 2);

        // Spagyrics entries
        var spagyricsEntry = this.add(new SpagyricsEntry(this).generate());
        spagyricsEntry.addParent(this.parent(introEntry));
        spagyricsEntry.withCondition(BookTrueConditionModel.create());
        this.layout().entry(spagyricsEntry).above(introEntry, 4);

        var pyromanticBrazierEntry = this.add(new PyromanticBrazierEntry(this).generate());
        pyromanticBrazierEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(pyromanticBrazierEntry).rightOf(spagyricsEntry, 2).below(2);

        var salAmmoniacTankEntry = this.add(new SalAmmoniacTankEntry(this).generate());
        salAmmoniacTankEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(salAmmoniacTankEntry).leftOf(spagyricsEntry, 2);

        var salAmmoniacAccumulatorEntry = this.add(new SalAmmoniacAccumulatorEntry(this).generate());
        salAmmoniacAccumulatorEntry.addParent(this.parent(salAmmoniacTankEntry));
        this.layout().entry(salAmmoniacAccumulatorEntry).above(salAmmoniacTankEntry, 2);

        var liquefactionCauldronEntry = this.add(new LiquefactionCauldronEntry(this).generate());
        liquefactionCauldronEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(liquefactionCauldronEntry).leftOf(spagyricsEntry, 2).below(2);

        var calcinationOvenEntry = this.add(new CalcinationOvenEntry(this).generate());
        calcinationOvenEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(calcinationOvenEntry).rightOf(spagyricsEntry, 2).above(2);

        var distillerEntry = this.add(new DistillerEntry(this).generate());
        distillerEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(distillerEntry).rightOf(spagyricsEntry, 2);

        var incubatorEntry = this.add(new IncubatorEntry(this).generate());
        incubatorEntry.addParent(this.parent(spagyricsEntry));
        this.layout().entry(incubatorEntry).above(spagyricsEntry, 2);

        // Mercury flux entries
        var mercuryFluxEntry = this.add(new MercuryFluxEntry(this).generate());
        mercuryFluxEntry.addParent(this.parent(introEntry));
        mercuryFluxEntry.withCondition(BookTrueConditionModel.create());
        this.layout().entry(mercuryFluxEntry).leftOf(introEntry, 4);

        var mercuryCatalystEntry = this.add(new MercuryCatalystEntry(this).generate());
        mercuryCatalystEntry.addParent(this.parent(mercuryFluxEntry));
        this.layout().entry(mercuryCatalystEntry).leftOf(mercuryFluxEntry, 2);

        var mercuryFluxEmitterEntry = this.add(new MercuryFluxEmitterEntry(this).generate());
        mercuryFluxEmitterEntry.addParent(this.parent(mercuryCatalystEntry));
        this.layout().entry(mercuryFluxEmitterEntry).above(mercuryCatalystEntry, 2);

        var mercuryCapacitorEntry = this.add(new MercuryCapacitorEntry(this).generate());
        mercuryCapacitorEntry.addParent(this.parent(mercuryCatalystEntry));
        this.layout().entry(mercuryCapacitorEntry).below(mercuryCatalystEntry, 2);

        var caloricFluxEmitterEntry = this.add(new CaloricFluxEmitterEntry(this).generate());
        caloricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));
        this.layout().entry(caloricFluxEmitterEntry).above(mercuryFluxEntry, 2);

        var sulfuricFluxEmitterEntry = this.add(new SulfuricFluxEmitterEntry(this).generate());
        sulfuricFluxEmitterEntry.addParent(this.parent(mercuryFluxEntry));
        this.layout().entry(sulfuricFluxEmitterEntry).below(mercuryFluxEntry, 2);

        // Reformation entries
        var reformationArrayEntry = this.add(new ReformationArrayEntry(this).generate());
        reformationArrayEntry.addParent(this.parent(introEntry));
        reformationArrayEntry.withCondition(BookTrueConditionModel.create());
        this.layout().entry(reformationArrayEntry).rightOf(introEntry, 6).above(4);

        var sulfuricFluxEmitterReferenceEntry = this.add(new SulfuricFluxEmitterReferenceEntry(this).generate());
        sulfuricFluxEmitterReferenceEntry.addParent(this.parent(reformationArrayEntry));
        this.layout().entry(sulfuricFluxEmitterReferenceEntry).rightOf(reformationArrayEntry, 2).below(2);

        var targetPedestalEntry = this.add(new TargetPedestalEntry(this).generate());
        targetPedestalEntry.addParent(this.parent(reformationArrayEntry));
        this.layout().entry(targetPedestalEntry).above(reformationArrayEntry, 2);

        var sourcePedestalEntry = this.add(new SourcePedestalEntry(this).generate());
        sourcePedestalEntry.addParent(this.parent(reformationArrayEntry));
        this.layout().entry(sourcePedestalEntry).leftOf(reformationArrayEntry, 2);

        var resultPedestalEntry = this.add(new ResultPedestalEntry(this).generate());
        resultPedestalEntry.addParent(this.parent(reformationArrayEntry));
        this.layout().entry(resultPedestalEntry).rightOf(reformationArrayEntry, 2);

        // Exaltation entries
        var transmutationAndExaltation = this.add(new TransmutationAndExaltationEntry(this).generate());
        transmutationAndExaltation.addParent(this.parent(introEntry));
        transmutationAndExaltation.withCondition(BookTrueConditionModel.create());
        this.layout().entry(transmutationAndExaltation).rightOf(introEntry, 6).below(4);

        var fermentationVatEntry = this.add(new FermentationVatEntry(this).generate());
        fermentationVatEntry.addParent(this.parent(transmutationAndExaltation));
        this.layout().entry(fermentationVatEntry).leftOf(transmutationAndExaltation, 2);

        var digestionVatEntry = this.add(new DigestionVatEntry(this).generate());
        digestionVatEntry.addParent(this.parent(transmutationAndExaltation));
        this.layout().entry(digestionVatEntry).rightOf(transmutationAndExaltation, 2);

        var vatRedstoneEntry = this.add(new VatRedstoneEntry(this).generate());
        vatRedstoneEntry.addParent(this.parent(digestionVatEntry).withLineReversed(true));
        vatRedstoneEntry.addParent(this.parent(fermentationVatEntry).withLineReversed(true));
        vatRedstoneEntry.withCondition(this.condition().or(
                this.condition().entryRead(digestionVatEntry),
                this.condition().entryRead(fermentationVatEntry)
        ));
        this.layout().entry(vatRedstoneEntry).below(transmutationAndExaltation, 2);

        var basicVatAutomationEntry = this.add(new BasicVatAutomationEntry(this).generate());
        basicVatAutomationEntry.addParent(this.parent(vatRedstoneEntry));
        this.layout().entry(basicVatAutomationEntry).below(vatRedstoneEntry, 2);
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
}
