// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.ConvertToOtherTierEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.DigestionEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.DigestionVatEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.PurifiedGoldEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.RequiredItemsEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.IncubationEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;


public class GettingStartedCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "getting_started";

    public GettingStartedCategoryProvider(TheurgyBookProvider parent) {
        super(parent);
    }

    public TheurgyBookProvider parent() {
        return (TheurgyBookProvider) this.parent;
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "___________________________________",
                "__________________ḍ___ď_ḑ_ḓ________",
                "___________________________________",
                "________________d___ḋ______________",
                "___________________________________",
                "____________ç_____đ___ɖ_ᶑ__________",
                "___________________________________",
                "__________i_a_________c___ṛ_ŕ______",
                "___________________________________",
                "______________s_____ṟ_n_r_ȓ___ŗ_ʀ_ȑ",
                "___________________________________",
                "______________o_____________ř______",
                "___________________________________",
                "______________ó___________ț________",
                "___________________________________",
                "____________ő_ò___ǒ_____ť_ţ_ƭ_ʈ_ṫ_ṭ",
                "___________________________________",
                "____________ö___ô_ơ_______ê_ě______",
                "___________________________________",
                "______________õ_________é_è_ë_ē_ė_ę",
                "___________________________________",
                "________________________ƒ__________"
        };
    }


    @Override
    protected void generateEntries() {
        var introEntry = new IntroEntry(this).generate('i');
        var aboutModEntry = new AboutModEntry(this).generate('a');
        aboutModEntry.withParent(introEntry);

        var creditsEntry = new CreditsEntry(this).generate('ç');
        creditsEntry.withParent(aboutModEntry);

        this.generateDivinationRodEntries(aboutModEntry);

        var spagyrics = this.generateSpagyricsEntries(aboutModEntry); //spagyrics, incubation

        var replication = new ReplicationEntry(this).generate('ṟ');
        replication.withParent(spagyrics.getFirst());
        replication.withParent(spagyrics.getSecond());
        replication.withCondition(this.condition().entryRead(spagyrics.getSecond()));
        replication.showWhenAnyParentUnlocked(true);

        var niter = new AlchemicalNiterEntry(this).generate('n');
        niter.withParent(replication);

        var caloricFlux = new CaloricFluxEmitterEntry(this).generate('c');
        caloricFlux.withParent(niter);

        var reformation = this.generateReformationEntries(niter); //convertWithinTypeAndTier, reformationIncubation

        var transmutation = this.generateTransmutationEntries(reformation);  //convertToOtherType, reformationIncubation

        var exaltation = this.generateExaltationEntries(transmutation); //convertToOtherTier, incubation

        var renewableGold = new RenewableGoldEntry(this).generate('ƒ');
        renewableGold.withParent(exaltation.getFirst());
        renewableGold.withCondition(this.condition().entryRead(exaltation.getSecond()));
        renewableGold.hideWhileLocked(true);
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

    protected Pair<BookEntryModel, BookEntryModel> generateExaltationEntries(Pair<BookEntryModel, BookEntryModel> transmutation) {
        var convertToOtherTier = new ConvertToOtherTierEntry(this).generate('é');
        convertToOtherTier.withParent(transmutation.getFirst());
        convertToOtherTier.withCondition(this.condition().entryRead(transmutation.getSecond()));
        convertToOtherTier.hideWhileLocked(true);

        var digestionVat = new DigestionVatEntry(this).generate('è');
        digestionVat.withParent(convertToOtherTier);

        var requiredItems = new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.RequiredItemsEntry(this).generate('ê');
        requiredItems.withParent(this.parent(digestionVat));

        var purifiedGold = new PurifiedGoldEntry(this).generate('ě');
        purifiedGold.withParent(requiredItems);

        var fermentation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.FermentationEntry(this).generate('ë');
        fermentation.withParent(digestionVat);

        var digestion = new DigestionEntry(this).generate('ē');
        digestion.withParent(fermentation);
        digestion.withParent(purifiedGold);

        var niterToSulfurReformation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.NiterToSulfurReformationEntry(this).generate('ė');
        niterToSulfurReformation.withParent(digestion);

        var incubation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.IncubationEntry(this).generate('ę');
        incubation.withParent(niterToSulfurReformation);

        return Pair.of(convertToOtherTier, incubation);
    }

    protected Pair<BookEntryModel, BookEntryModel> generateTransmutationEntries(Pair<BookEntryModel, BookEntryModel> reformation) {
        var convertToOtherType = new ConvertToOtherTypeEntry(this).generate('ť');
        convertToOtherType.withParent(reformation.getFirst());
        convertToOtherType.withCondition(this.condition().entryRead(reformation.getSecond()));
        convertToOtherType.hideWhileLocked(true);

        var fermentationVatEntry = new FermentationVatEntry(this).generate('ţ');
        fermentationVatEntry.withParent(convertToOtherType);

        var requiredItemsTransmutation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.RequiredItemsEntry(this).generate('ț');
        requiredItemsTransmutation.withParent(this.parent(fermentationVatEntry).withDrawArrow(false));

        var fermentationTransmutation = new FermentationEntry(this).generate('ƭ');
        fermentationTransmutation.withParent(fermentationVatEntry);

        var niterToNiterReformation = new NiterToNiterReformationEntry(this).generate('ʈ');
        niterToNiterReformation.withParent(fermentationTransmutation);

        var niterToSulfurReformation = new NiterToSulfurReformationEntry(this).generate('ṫ');
        niterToSulfurReformation.withParent(niterToNiterReformation);

        var incubation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.IncubationEntry(this).generate('ṭ');
        incubation.withParent(niterToSulfurReformation);

        return Pair.of(convertToOtherType, incubation);
    }

    protected Pair<BookEntryModel, BookEntryModel> generateReformationEntries(BookEntryModel parent) {
        var convertWithinTypeAndTier = new ConvertWithinTypeAndTierEntry(this).generate('r');
        convertWithinTypeAndTier.withParent(parent);
        var reformationArray = new ReformationArrayEntry(this).generate('ȓ');
        reformationArray.withParent(convertWithinTypeAndTier);
        var requiredItems = new RequiredItemsEntry(this).generate('ṛ');
        requiredItems.withParent(this.parent(reformationArray).withDrawArrow(false));

        var source = new SourceEntry(this).generate('ŕ');
        source.withParent(this.parent(reformationArray));
        var target = new TargetEntry(this).generate('ř');
        target.withParent(reformationArray);
        var sulfuricFluxEmitter = new SulfuricFluxEmitterEntry(this).generate('ŗ');
        sulfuricFluxEmitter.withParent(source);
        sulfuricFluxEmitter.withParent(target);
        var result = new ResultEntry(this).generate('ʀ');
        result.withParent(sulfuricFluxEmitter);
        var reformationIncubation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.IncubationEntry(this).generate('ȑ');
        reformationIncubation.withParent(result);

        return Pair.of(convertWithinTypeAndTier, reformationIncubation);
    }

    protected Pair<BookEntryModel, BookEntryModel> generateSpagyricsEntries(BookEntryModel parent) {
        var spagyrics = new SpagyricsEntry(this).generate('s');
        spagyrics.withParent(parent);
        var oreRefining = new OreRefiningEntry(this).generate('o');
        oreRefining.withParent(spagyrics);
        var neededApparatus = new NeededApparatusEntry(this).generate('ó');
        neededApparatus.withParent(oreRefining);
        var createSolvent = new CreateSolventEntry(this).generate('ő');
        createSolvent.withParent(neededApparatus);
        var createSulfur = new CreateSulfurEntry(this).generate('ö');
        createSulfur.withParent(createSolvent);
        var createSalt = new CreateSaltEntry(this).generate('ô');
        createSalt.withParent(neededApparatus);
        var recycleStrata = new StrataRecyclingEntry(this).generate('ơ');
        recycleStrata.withParent(createSalt);
        var createMercury = new CreateMercuryEntry(this).generate('ò');
        createMercury.withParent(neededApparatus);
        var logistics = new LogisticsEntry(this).generate('ǒ');
        logistics.withParent(createMercury);
        var incubation = new IncubationEntry(this).generate('õ');
        incubation
                .withParent(createMercury)
                .withParent(createSalt)
                .withParent(createSulfur);

        return Pair.of(spagyrics, incubation);
    }

    protected void generateDivinationRodEntries(BookEntryModel parent) {
        var rods = new DivinationRodEntryProvider(this.parent(), this.entryMap());
        var aboutDivinationRods = this.add(rods.aboutDivinationRods('d'));
        aboutDivinationRods.withParent(parent);

        var t1DivinationRod = this.add(rods.t1DivinationRodEntry('ḍ'));
        var abundantAndCommonSulfurAttunedDivinationRod = this.add(rods.abundantAndCommonSulfurAttunedDivinationRodEntry('đ'));
        //TODO: should be child of spagyrics / sulfur

        var amethystDivinationRod = this.add(rods.amethystDivinationRodEntry('ḋ'));
        var t2DivinationRod = this.add(rods.t2DivinationRodEntry('ď'));
        var t3DivinationRod = this.add(rods.t3DivinationRodEntry('ḑ'));
        var t4DivinationRod = this.add(rods.t4DivinationRodEntry('ḓ'));
        var rareSulfurAttunedDivinationRod = this.add(rods.rareSulfurAttunedDivinationRodEntry('ɖ'));
        var preciousSulfurAttunedDivinationRod = this.add(rods.preciousSulfurAttunedDivinationRodEntry('ᶑ'));

        t1DivinationRod.withParent(aboutDivinationRods);

        abundantAndCommonSulfurAttunedDivinationRod.withParent(aboutDivinationRods);
//        abundantAndCommonSulfurAttunedDivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(aboutDivinationRods),
//                        this.parent().advancementCondition(this.modLoc("has_liquefaction_cauldron"))
//                )
//        );

        amethystDivinationRod.withParent(t1DivinationRod);
        amethystDivinationRod.withParent(abundantAndCommonSulfurAttunedDivinationRod);
//        amethystDivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().or(
//                                this.parent().entryReadCondition(t1DivinationRod),
//                                this.parent().entryReadCondition(abundantAndCommonSulfurAttunedDivinationRod)
//                        ),
//                        this.parent().advancementCondition(this.modLoc("has_basic_rod")
//                        )
//                )
//        );

        t2DivinationRod.withParent(amethystDivinationRod);
//        t2DivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(amethystDivinationRod),
//                        this.parent().advancementCondition(this.modLoc("has_amethyst_rod"))
//                )
//        );

        t3DivinationRod.withParent(t2DivinationRod);
//        t3DivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(t2DivinationRod),
//                        this.parent().advancementCondition(this.modLoc("has_t2_rod"))
//                )
//        );

        t4DivinationRod.withParent(t3DivinationRod);
//        t4DivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(t3DivinationRod),
//                        this.parent().advancementCondition(this.modLoc("has_t3_rod"))
//                )
//        );

        rareSulfurAttunedDivinationRod.withParent(amethystDivinationRod);
//        rareSulfurAttunedDivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(amethystDivinationRod),
//                        this.parent().advancementCondition(this.modLoc("has_amethyst_rod"))
//                )
//        );

        preciousSulfurAttunedDivinationRod.withParent(rareSulfurAttunedDivinationRod);
//        preciousSulfurAttunedDivinationRod.withCondition(
//                this.parent().and(
//                        this.parent().entryReadCondition(rareSulfurAttunedDivinationRod),
//                        this.parent().advancementCondition(this.modLoc("has_rare_rod"))
//                )
//        );


        //TODO: Conditions
        //  amethyst entry should NOT depend on spagyrics -> hence not on abundant sulfur rod
    }

}
