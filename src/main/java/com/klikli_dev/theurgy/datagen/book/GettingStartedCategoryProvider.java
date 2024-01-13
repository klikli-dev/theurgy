// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AboutModEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.CaloricFluxEmitterEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.IntroEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.*;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.IncubationEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;

import java.lang.annotation.Target;

public class GettingStartedCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "getting_started";

    public GettingStartedCategoryProvider(TheurgyBookProvider parent) {
        super(parent, CATEGORY_ID);
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
                "__________________đ___ɖ_ᶑ__________",
                "___________________________________",
                "__________i_a_________c_____ŕ______",
                "___________________________________",
                "______________s_______n_r_ȓ___ŗ_ʀ_ȑ",
                "___________________________________",
                "______________o_____________ř______",
                "___________________________________",
                "______________ó____________________",
                "___________________________________",
                "____________ő_ò____________________",
                "___________________________________",
                "____________ö___ô_ơ________________",
                "___________________________________",
                "______________õ____________________"
        };
    }

    @Override
    protected void generateEntries() {
        var rods = new DivinationRodEntryProvider(this.parent(), this.entryMap());

        var introEntry = new IntroEntry(this).generate('i');
        var aboutModEntry = new AboutModEntry(this).generate('a');
        aboutModEntry.withParent(introEntry);

        var spagyrics = new SpagyricsEntry(this).generate('s');
        spagyrics.withParent(aboutModEntry);
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
        var incubation = new IncubationEntry(this).generate('õ');
        incubation
                .withParent(createMercury)
                .withParent(createSalt)
                .withParent(createSulfur);

        var niter = new AlchemicalNiterEntry(this).generate('n');
        niter.withParent(spagyrics);
        niter.withParent(incubation);
        niter.withCondition(this.condition().entryRead(incubation));
        niter.showWhenAnyParentUnlocked(true);

        var caloricFlux = new CaloricFluxEmitterEntry(this).generate('c');
        caloricFlux.withParent(niter);

        var convertWithinTypeAndTier = new ConvertWithinTypeAndTierEntry(this).generate('r');
        convertWithinTypeAndTier.withParent(niter);
        var reformationArray = new ReformationArrayEntry(this).generate('ȓ');
        reformationArray.withParent(convertWithinTypeAndTier);

        var source = new SourceEntry(this).generate('ŕ');
        source.withParent(reformationArray);
        var target = new TargetEntry(this).generate('ř');
        target.withParent(reformationArray);
        var sulfuricFluxEmitter = new SulfuricFluxEmitterEntry(this).generate('ŗ');
        sulfuricFluxEmitter.withParent(source);
        sulfuricFluxEmitter.withParent(target);
        var result = new ResultEntry(this).generate('ʀ');
        result.withParent(sulfuricFluxEmitter);
        var reformationIncubation = new com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.IncubationEntry(this).generate('ȑ');
        reformationIncubation.withParent(result);

        //TODO: also make two branches for the other conversion types using fermentation and digestion, but possibly after the reformation chain

        //TODO: the two other branches should be children of the replication by reformation entry, but should be unlocked by the reformation incubation entry

        //TODO: First the cross-type conversion, then the cross-tier

        var aboutDivinationRods = this.add(rods.aboutDivinationRods('d'));
        var t1DivinationRod = this.add(rods.t1DivinationRodEntry('ḍ'));
        var abundantAndCommonSulfurAttunedDivinationRod = this.add(rods.abundantAndCommonSulfurAttunedDivinationRodEntry('đ'));
        //TODO: should be child of spagyrics / sulfur

        var amethystDivinationRod = this.add(rods.amethystDivinationRodEntry('ḋ'));
        var t2DivinationRod = this.add(rods.t2DivinationRodEntry('ď'));
        var t3DivinationRod = this.add(rods.t3DivinationRodEntry('ḑ'));
        var t4DivinationRod = this.add(rods.t4DivinationRodEntry('ḓ'));
        var rareSulfurAttunedDivinationRod = this.add(rods.rareSulfurAttunedDivinationRodEntry('ɖ'));
        var preciousSulfurAttunedDivinationRod = this.add(rods.preciousSulfurAttunedDivinationRodEntry('ᶑ'));
        aboutDivinationRods.withParent(aboutModEntry);

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

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Getting Started");

        return BookCategoryModel.create(Theurgy.loc((this.context().categoryId())), this.context().categoryName())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get()).withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"));
    }
}
