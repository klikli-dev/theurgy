/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.ModonomiconAPI;
import com.klikli_dev.modonomicon.api.datagen.BookLangHelper;
import com.klikli_dev.modonomicon.api.datagen.EntryLocationHelper;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryParentModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class GettingStartedCategoryProvider {

    public BookCategoryModel.Builder make(BookLangHelper helper, LanguageProvider lang) {
        helper.category("getting_started");
        lang.add(helper.categoryName(), "Getting Started");

        var entryHelper = ModonomiconAPI.get().getEntryLocationHelper();
        entryHelper.setMap(
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________i_d_____________________",
                "__________________________________",
                "__________a_______________________",
                "__________________________________",
                "__________________________________"
        );

        var introEntry = this.makeIntroEntry(helper, lang, entryHelper, 'i');
        var aboutModEntry = this.makeAboutModEntry(helper, lang, entryHelper, 'a');
        aboutModEntry.withParent(introEntry);

        var divinationRodEntry = this.makeDivinationRodEntry(helper, lang, entryHelper, 'd');
        divinationRodEntry.withParent(introEntry);

        return BookCategoryModel.builder()
                .withId(Theurgy.loc((helper.category)))
                .withName(helper.categoryName())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.getId().toString())
                .withEntries(
                        introEntry.build(),
                        aboutModEntry.build(),
                        divinationRodEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, LanguageProvider lang, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        lang.add(helper.entryName(), "About this Work");
        lang.add(helper.entryDescription(), "About using The Hermetica");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "About this Work");
        lang.add(helper.pageText(),
                """
                        The following pages will lead the novice alchemist on their journey through the noble art of the transformation of matter and mind. This humble author will share their experiences, thoughts and research notes to guide the valued reader in as safe a manner as the subject matter allows.
                        """);

        helper.page("help");
        var help = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "Seeking Counsel");
        lang.add(helper.pageText(),
                """
                        If the reader finds themselves in trouble of any kind, prompt assistance will be provided at the Council of Alchemists, known also as Kli Kli's Discord Server.
                        \\
                        \\
                        [To get help, join us at https://invite.gg/klikli](https://invite.gg/klikli)
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.getId().toString())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(0, 1)
                .withPages(
                        intro,
                        help
                );
    }

    private BookEntryModel.Builder makeAboutModEntry(BookLangHelper helper, LanguageProvider lang, EntryLocationHelper entryHelper, char icon) {
        helper.entry("about_mod");
        lang.add(helper.entryName(), "The Art of Alchemy");
        lang.add(helper.entryDescription(), "About this Mod");

        helper.page("about");
        var about = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "The Art of Alchemy");
        lang.add(helper.pageText(),
                """
                   Welcome, dear reader, to Theurgy, a mod that explores the ancient and revered art of classical alchemy. As you embark on your journey through the noble art of transformation, you will be equipped with divination rods to make finding resources in the world easier.
                   """);
        helper.page("about2");
        var about2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageText(),
                """
                   Through diligent study and practice, you will then learn to use alchemical devices to refine, replicate, and transform resources into new and useful materials. Along the way, you will have the opportunity to craft alchemical devices and equipment to aid you in your endeavors.
                   """);

        helper.page("features");
        var features = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "Features");
        lang.add(helper.pageText(),
                """
                        - Divination rods to find ores
                        - Future: Ore refining (= more ingots per ore)
                        - Future: Item replication (create duplicates of items you have)
                        - Future: Item transformation (create new items from other items)
                        """);

        helper.page("features2");
        var features2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "More Features");
        lang.add(helper.pageText(),
                """
                        - Future: Weapons and Equipment
                        - Future: Devices to assist in common tasks
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ForgeRegistries.ITEMS.getKey(Items.NETHER_STAR).toString())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(0, 0)
                .withPages(
                        about,
                        about2,
                        features,
                        features2
                );
    }

    private BookEntryModel.Builder makeDivinationRodEntry(BookLangHelper helper, LanguageProvider lang, EntryLocationHelper entryHelper, char icon) {
        helper.entry("divination_rod");
        lang.add(helper.entryName(), "Divination Rods");
        lang.add(helper.entryDescription(), "An Introduction to Ore-Finding");

        helper.page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withText(helper.pageText())
                .build();

        lang.add(helper.pageText(),
                """
                   As a novice alchemist, it is important to familiarize yourself with the various tools and techniques at your disposal. One such tool is the divination rod, a valuable instrument used to locate hidden ores in the world. By attuning your senses and your rod to the elemental energies present in the earth, you can detect the presence of ore deposits and guide yourself to their location.
                        """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageText(),
                """
                   With practice, the use of divination rods can greatly aid you in your quest for the resources necessary for your alchemical pursuits. Do not underestimate the power of this simple tool, for it is the first step on your path to mastery of the alchemical arts.
                   """);

        //TODO: usage
//        helper.page("features");
//        var features = BookTextPageModel.builder()
//                .withTitle(helper.pageTitle())
//                .withText(helper.pageText())
//                .build();
//        lang.add(helper.pageTitle(), "Features");
//        lang.add(helper.pageText(),
//                """
//                        - Divination rods to find ores
//                        - Future: Ore refining (= more ingots per ore)
//                        - Future: Item replication (create duplicates of items you have)
//                        - Future: Item transformation (create new items from other items)
//                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ForgeRegistries.ITEMS.getKey(Items.NETHER_STAR).toString())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(0, 0)
                .withPages(
                        intro,
                        intro2
                );
    }
}
