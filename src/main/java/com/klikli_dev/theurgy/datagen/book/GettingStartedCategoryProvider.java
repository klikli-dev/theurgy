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
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.NeededApparatusEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.OreRefiningEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.SpagyricsEntry;
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
                "______________________ɖ_ᶑ_________",
                "__________________đ_______________",
                "__________________________________",
                "__________i_a___________ő_ö_______",
                "__________________________________",
                "______________s___________ô_õ_r___",
                "__________________________________",
                "______________o_________ò_________",
                "__________________________________",
                "______________ó___________________"
        };
    }

    @Override
    protected void generateEntries() {
        var rods = new DivinationRodEntryProvider(this.parent(), this.entryMap());
        var ore = new OreRefiningEntryProvider(this.parent(), this.entryMap());

        var introEntry = new IntroEntry(this).generate('i');
        var aboutModEntry = new AboutModEntry(this).generate('a');

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

        var spagyrics = new SpagyricsEntry(this).generate('s');
        spagyrics.withParent(aboutModEntry);

        var oreRefining = new OreRefiningEntry(this).generate('o');
        oreRefining.withParent(spagyrics);

        var neededApparatus = new NeededApparatusEntry(this).generate('ó');
        neededApparatus.withParent(oreRefining);

        var createSolvent = this.add(ore.createSolventEntry('ő'));
        var createSulfur = this.add(ore.createSulfurEntry('ö'));
        var createSalt = this.add(ore.createSaltEntry('ô'));
        var createMercury = this.add(ore.createMercuryEntry('ò'));
        var incubation = this.add(ore.incubationEntry('õ'));
        var reformation = this.add(this.reformation('r'));

        //TODO: add an entry to create a caloric flux emmitter


        //links and conditions
        aboutModEntry.withParent(introEntry);

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


        createSolvent.withParent(neededApparatus);
        createSulfur.withParent(createSolvent);
        createSalt.withParent(neededApparatus);
        createMercury.withParent(neededApparatus);
        incubation
                .withParent(createMercury)
                .withParent(createSalt)
                .withParent(createSulfur);
        reformation.withParent(incubation);

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
