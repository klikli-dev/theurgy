// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.TheurgyResearch;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AboutDivinationRodsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AboutModEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AlchemicalNiterEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AmethystDivinationRodEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.BasicSulfurAttunedDivinationRodsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.CaloricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.CreditsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.DiamondDivinationRodEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.GlassDivinationRodEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.IntroEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.IronDivinationRodEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.LogisticsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.NetheriteDivinationRodEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.PreciousSulfurAttunedDivinationRodsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.RareSulfurAttunedDivinationRodsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.ReplicationEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.RenewableGoldEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.ConvertToOtherTierEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.DigestionEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.DigestionVatEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.PurifiedGoldEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.RequiredItemsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.IncubationEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;


public class GettingStartedCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "getting_started";

    public GettingStartedCategoryProvider(TheurgyBookProvider parent) {
        super(parent);
    }

    public TheurgyBookProvider parent() {
        return (TheurgyBookProvider) this.parent;
    }

    @Override
    protected void generateEntries() {
        // Main entries
        var introEntry = this.add(new IntroEntry(this).generate());
        this.layout().entry(introEntry).at(-7, -4);

        var aboutModEntry = this.add(new AboutModEntry(this).generate());
        aboutModEntry.withParent(introEntry);
        this.layout().entry(aboutModEntry).rightOf(introEntry, 2);

        var creditsEntry = this.add(new CreditsEntry(this).generate());
        creditsEntry.withParent(aboutModEntry);
        this.layout().entry(creditsEntry).above(aboutModEntry, 2);

        var aboutDivinationRods = this.add(new AboutDivinationRodsEntry(this).generate());
        aboutDivinationRods.withParent(aboutModEntry);
        this.layout().entry(aboutDivinationRods).rightOf(aboutModEntry, 4).above(4);

        var glassDivinationRod = this.add(new GlassDivinationRodEntry(this).generate());
        glassDivinationRod.withParent(aboutDivinationRods);
        this.layout().entry(glassDivinationRod).rightOf(aboutDivinationRods, 2).above(2);

        var basicSulfurAttunedDivinationRods = this.add(new BasicSulfurAttunedDivinationRodsEntry(this).generate());
        basicSulfurAttunedDivinationRods.withParent(aboutDivinationRods);
        this.layout().entry(basicSulfurAttunedDivinationRods).rightOf(aboutDivinationRods, 2).below(2);

        var amethystDivinationRod = this.add(new AmethystDivinationRodEntry(this).generate());
        amethystDivinationRod.withParent(glassDivinationRod);
        amethystDivinationRod.withParent(basicSulfurAttunedDivinationRods);
        this.layout().entry(amethystDivinationRod).rightOf(aboutDivinationRods, 4);

        var ironDivinationRod = this.add(new IronDivinationRodEntry(this).generate());
        ironDivinationRod.withParent(amethystDivinationRod);
        this.layout().entry(ironDivinationRod).rightOf(amethystDivinationRod, 2).above(2);

        var diamondDivinationRod = this.add(new DiamondDivinationRodEntry(this).generate());
        diamondDivinationRod.withParent(ironDivinationRod);
        this.layout().entry(diamondDivinationRod).rightOf(ironDivinationRod, 2);

        var netheriteDivinationRod = this.add(new NetheriteDivinationRodEntry(this).generate());
        netheriteDivinationRod.withParent(diamondDivinationRod);
        this.layout().entry(netheriteDivinationRod).rightOf(diamondDivinationRod, 2);

        var rareSulfurAttunedDivinationRods = this.add(new RareSulfurAttunedDivinationRodsEntry(this).generate());
        rareSulfurAttunedDivinationRods.withParent(amethystDivinationRod);
        this.layout().entry(rareSulfurAttunedDivinationRods).rightOf(amethystDivinationRod, 2).below(2);

        var preciousSulfurAttunedDivinationRods = this.add(new PreciousSulfurAttunedDivinationRodsEntry(this).generate());
        preciousSulfurAttunedDivinationRods.withParent(rareSulfurAttunedDivinationRods);
        this.layout().entry(preciousSulfurAttunedDivinationRods).rightOf(rareSulfurAttunedDivinationRods, 2);

        // Spagyrics entries (inlined from generateSpagyricsEntries)
        var spagyrics = this.add(new SpagyricsEntry(this).generate());
        spagyrics.withParent(aboutModEntry);
        this.layout().entry(spagyrics).rightOf(aboutModEntry, 2).below(2);

        var oreRefining = this.add(new OreRefiningEntry(this).generate());
        oreRefining.withParent(spagyrics);
        this.layout().entry(oreRefining).below(spagyrics, 2);

        var neededApparatus = this.add(new NeededApparatusEntry(this).generate());
        neededApparatus.withParent(oreRefining);
        this.layout().entry(neededApparatus).below(oreRefining, 2);

        var createSolvent = this.add(new CreateSolventEntry(this).generate());
        createSolvent.withParent(neededApparatus);
        this.layout().entry(createSolvent).leftOf(neededApparatus, 2).below(2);

        var createSulfur = this.add(new CreateSulfurEntry(this).generate());
        createSulfur.withParent(createSolvent);
        this.layout().entry(createSulfur).below(createSolvent, 2);

        var createSalt = this.add(new CreateSaltEntry(this).generate());
        createSalt.withParent(neededApparatus);
        this.layout().entry(createSalt).rightOf(createSulfur, 4);

        var recycleStrata = this.add(new StrataRecyclingEntry(this).generate());
        recycleStrata.withParent(createSalt);
        this.layout().entry(recycleStrata).rightOf(createSalt, 2);

        var recyclePlants = this.add(new PlantRecyclingEntry(this).generate());
        recyclePlants.withParent(recycleStrata);
        this.layout().entry(recyclePlants).below(recycleStrata, 4);

        var createMercury = this.add(new CreateMercuryEntry(this).generate());
        createMercury.withParent(neededApparatus);
        this.layout().entry(createMercury).below(neededApparatus, 2);

        var logistics = this.add(new LogisticsEntry(this).generate());
        logistics.withParent(createMercury);
        this.layout().entry(logistics).rightOf(createMercury, 4);

        var incubation = this.add(new IncubationEntry(this).generate());
        incubation.withParent(createMercury)
                .withParent(createSalt)
                .withParent(createSulfur);
        this.layout().entry(incubation).below(createMercury, 4);

        // Post-spagyrics entries
        var replication = this.add(new ReplicationEntry(this).generate());
        replication.withParent(spagyrics);
        replication.withParent(incubation);
        replication.withCondition(this.condition().researchNodeEntryViewedOnce(TheurgyResearch.GETTING_STARTED_INCUBATION, incubation));
        replication.showWhenAnyParentUnlocked(true);
        this.layout().entry(replication).rightOf(spagyrics, 6);

        var niter = this.add(new AlchemicalNiterEntry(this).generate());
        niter.withParent(replication);
        this.layout().entry(niter).rightOf(replication, 2);

        var caloricFlux = this.add(new CaloricFluxEmitterEntry(this).generate());
        caloricFlux.withParent(niter);
        this.layout().entry(caloricFlux).above(niter, 2);

        // Reformation entries (inlined from generateReformationEntries)
        var convertWithinTypeAndTier = this.add(new ConvertWithinTypeAndTierEntry(this).generate());
        convertWithinTypeAndTier.withParent(niter);
        this.layout().entry(convertWithinTypeAndTier).rightOf(niter, 2);

        var reformationArray = this.add(new ReformationArrayEntry(this).generate());
        reformationArray.withParent(convertWithinTypeAndTier);
        this.layout().entry(reformationArray).rightOf(convertWithinTypeAndTier, 2);

        var requiredItems = this.add(new RequiredItemsEntry(this).generate());
        requiredItems.withParent(this.parent(reformationArray).withDrawArrow(false));
        this.layout().entry(requiredItems).above(reformationArray, 2);

        var source = this.add(new SourceEntry(this).generate());
        source.withParent(this.parent(reformationArray));
        this.layout().entry(source).rightOf(requiredItems, 2);

        var target = this.add(new TargetEntry(this).generate());
        target.withParent(reformationArray);
        this.layout().entry(target).below(source, 4);

        var sulfuricFluxEmitter = this.add(new SulfuricFluxEmitterEntry(this).generate());
        sulfuricFluxEmitter.withParent(source);
        sulfuricFluxEmitter.withParent(target);
        this.layout().entry(sulfuricFluxEmitter).rightOf(source, 2).below(2);

        var result = this.add(new ResultEntry(this).generate());
        result.withParent(sulfuricFluxEmitter);
        this.layout().entry(result).rightOf(sulfuricFluxEmitter, 2);

        var reformationIncubation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.IncubationEntry(this).generate());
        reformationIncubation.withParent(result);
        this.layout().entry(reformationIncubation).rightOf(result, 2);

        // Transmutation entries (inlined from generateTransmutationEntries)
        var convertToOtherType = this.add(new ConvertToOtherTypeEntry(this).generate());
        convertToOtherType.withParent(convertWithinTypeAndTier);
        convertToOtherType.withCondition(this.condition().researchNodeEntryViewedOnce(TheurgyResearch.GETTING_STARTED_INCUBATION_AFTER_REFORMATION, reformationIncubation));
        convertToOtherType.hideWhileLocked(true);
        this.layout().entry(convertToOtherType).below(convertWithinTypeAndTier, 6);

        var fermentationVatEntry = this.add(new FermentationVatEntry(this).generate());
        fermentationVatEntry.withParent(convertToOtherType);
        this.layout().entry(fermentationVatEntry).below(reformationArray, 6);

        var requiredItemsTransmutation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.RequiredItemsEntry(this).generate());
        requiredItemsTransmutation.withParent(this.parent(fermentationVatEntry).withDrawArrow(false));
        this.layout().entry(requiredItemsTransmutation).below(reformationArray, 4);

        var fermentationTransmutation = this.add(new FermentationEntry(this).generate());
        fermentationTransmutation.withParent(fermentationVatEntry);
        this.layout().entry(fermentationTransmutation).rightOf(fermentationVatEntry, 2);

        var fermentationStarter = this.add(new FermentationStarterEntry(this).generate());
        fermentationStarter.withParent(fermentationTransmutation);
        this.layout().entry(fermentationStarter).above(fermentationTransmutation, 2);

        var niterToNiterReformation = this.add(new NiterToNiterReformationEntry(this).generate());
        niterToNiterReformation.withParent(fermentationTransmutation);
        this.layout().entry(niterToNiterReformation).rightOf(fermentationTransmutation, 2);

        var niterToSulfurReformation = this.add(new NiterToSulfurReformationEntry(this).generate());
        niterToSulfurReformation.withParent(niterToNiterReformation);
        this.layout().entry(niterToSulfurReformation).rightOf(niterToNiterReformation, 2);

        var incubation_transmutation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.IncubationEntry(this).generate());
        incubation_transmutation.withParent(niterToSulfurReformation);
        this.layout().entry(incubation_transmutation).rightOf(niterToSulfurReformation, 2);

        // Exaltation entries (inlined from generateExaltationEntries)
        var convertToOtherTier = this.add(new ConvertToOtherTierEntry(this).generate());
        convertToOtherTier.withParent(convertToOtherType);
        convertToOtherTier.withCondition(this.condition().researchNodeEntryViewedOnce(TheurgyResearch.GETTING_STARTED_INCUBATION_AFTER_TRANSMUTATION, incubation_transmutation));
        convertToOtherTier.hideWhileLocked(true);
        this.layout().entry(convertToOtherTier).below(convertToOtherType, 4);

        var digestionVat = this.add(new DigestionVatEntry(this).generate());
        digestionVat.withParent(convertToOtherTier);
        this.layout().entry(digestionVat).below(fermentationVatEntry, 4);

        var requiredItems_exaltation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.RequiredItemsEntry(this).generate());
        requiredItems_exaltation.withParent(this.parent(digestionVat));
        this.layout().entry(requiredItems_exaltation).above(digestionVat, 2);

        var purifiedGold = this.add(new PurifiedGoldEntry(this).generate());
        purifiedGold.withParent(requiredItems_exaltation);
        this.layout().entry(purifiedGold).rightOf(requiredItems_exaltation, 2);

        var fermentation_exaltation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.FermentationEntry(this).generate());
        fermentation_exaltation.withParent(digestionVat);
        this.layout().entry(fermentation_exaltation).rightOf(digestionVat, 2);

        var digestion_exaltation = this.add(new DigestionEntry(this).generate());
        digestion_exaltation.withParent(fermentation_exaltation);
        digestion_exaltation.withParent(purifiedGold);
        this.layout().entry(digestion_exaltation).rightOf(fermentation_exaltation, 2);

        var niterToSulfurReformation_exaltation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.NiterToSulfurReformationEntry(this).generate());
        niterToSulfurReformation_exaltation.withParent(digestion_exaltation);
        this.layout().entry(niterToSulfurReformation_exaltation).rightOf(digestion_exaltation, 2);

        var incubation_exaltation = this.add(new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.IncubationEntry(this).generate());
        incubation_exaltation.withParent(niterToSulfurReformation_exaltation);
        this.layout().entry(incubation_exaltation).rightOf(niterToSulfurReformation_exaltation, 2);

        // Final entry
        var renewableGold = this.add(new RenewableGoldEntry(this).generate());
        renewableGold.withParent(convertToOtherTier);
        renewableGold.withCondition(this.condition().researchNodeEntryViewedOnce(TheurgyResearch.GETTING_STARTED_INCUBATION_AFTER_EXALTATION, incubation_exaltation));
        renewableGold.hideWhileLocked(true);
        this.layout().entry(renewableGold).rightOf(digestionVat, -2).below(2);
    }

    @Override
    protected String categoryName() {
        return "Getting Started";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(ItemRegistry.THE_HERMETICA_ICON.get());
    }

    @Override
    public String categoryId() {
        return CATEGORY_ID;
    }

    @Override
    protected BookCategoryModel additionalSetup(BookCategoryModel category) {
        return super.additionalSetup(category).withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"));
    }
}
