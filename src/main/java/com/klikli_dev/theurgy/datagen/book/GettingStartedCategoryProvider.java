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
import com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux.MercuryCatalystEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.AboutModEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.IntroEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.AlchemicalNiterEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ConvertWithinTypeAndTierEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ReformationArrayEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.*;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;

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
                "__________________________________",
                "__________________ḍ___ď_ḑ_ḓ_______",
                "__________________________________",
                "________________d___ḋ_____________",
                "__________________________________",
                "__________________đ___ɖ_ᶑ_________",
                "__________________________________",
                "__________i_a_____________________",
                "__________________________________",
                "______________s_______n_r_ȓ_______",
                "__________________________________",
                "______________o___________________",
                "__________________________________",
                "______________ó___________________",
                "__________________________________",
                "____________ő_ò___________________",
                "__________________________________",
                "____________ö___ô_ơ_______________",
                "__________________________________",
                "______________õ___________________"
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


//        var reformation = this.add(this.reformation('r'));
//        reformation.withParent(incubation);

        var convertWithinTypeAndTier = new ConvertWithinTypeAndTierEntry(this).generate('r');
        convertWithinTypeAndTier.withParent(niter);
        var reformationArray = new ReformationArrayEntry(this).generate('ȓ');
        reformationArray.withParent(convertWithinTypeAndTier);
        //from this entry start an explanatory entry chain for reformation
        //and also make two branches for the other conversion types using fermentation and digestion, but possibly after the reformation chain
        //TODO: we need a "needed apparatus" entry once again

        //TODO: link to sulfuric flux emitter for crafting

        //TODO: add an entry to create a caloric flux emmitter

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

    private BookEntryModel reformation(char location) {
        this.context().entry("reformation");
        this.add(this.context().entryName(), "Reformation");
        this.add(this.context().entryDescription(), "Further Duplication of Matter");

        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.add(this.context().pageTitle(), "Reformation");
        this.add(this.context().pageText(),
                """
                        To further duplicate matter, we can use the process of Reformation to convert one material into another.
                        \\
                        \\
                        See the Category for {0} on how to achieve that.
                        """,
                this.categoryLink("Reformation", ReformationCategoryProvider.CATEGORY_ID)
        );


        return this.entry(location)
                .withIcon(this.modLoc("textures/gui/book/three_iron_ingots.png"), 32, 32)
                .withEntryBackground(EntryBackground.DEFAULT);
    }
}
