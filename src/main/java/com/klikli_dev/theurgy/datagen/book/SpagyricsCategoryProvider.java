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
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraftforge.common.data.LanguageProvider;

public class SpagyricsCategoryProvider implements MacroLangCategoryProvider {

    public static final String CATEGORY_ID = "spagyrics";
    private LanguageProvider lang;

    public SpagyricsCategoryProvider() {
        this.registerDefaultMacros();
    }

    public BookCategoryModel.Builder make(BookLangHelper helper, LanguageProvider lang) {
        this.setLanguageProvider(lang);

        helper.category(CATEGORY_ID);
        this.add(helper.categoryName(), "Spagyrics");

        var entryHelper = ModonomiconAPI.get().getEntryLocationHelper();
        entryHelper.setMap(
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________i_p_____________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________"
        );

        var introEntry = this.makeIntroEntry(helper, entryHelper, 'i');
        var principlesEntry = this.makePrinciplesEntry(helper, entryHelper, 'p');
        principlesEntry.withParent(introEntry);

        return BookCategoryModel.builder()
                .withId(Theurgy.loc((helper.category)))
                .withName(helper.categoryName())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withEntries(
                        introEntry.build(),
                        principlesEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        this.add(helper.entryName(), "Spagyrics");
        this.add(helper.entryDescription(), "Power over the Three Principles");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Spagyrics");
        this.add(helper.pageText(),
                """
                        Spagyrics is derived from Greek for "to separate and reunite". As such, it is the process of separating, purifying and recombining the *three principles*, or "elements", of matter: Alchemical **Salt**, **Sulfur** and **Mercury**.
                        """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Benefits");
        this.add(helper.pageText(),
                """
                        The inquisitive mind may ask: "Why would one want to do that?". The answer lies in the promise of total control over all aspects of matter, including the ability to create any type of matter from any other type.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        intro2
                );
    }

    private BookEntryModel.Builder makePrinciplesEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("principles");
        this.add(helper.entryName(), "The Three Principles");
        this.add(helper.entryDescription(), "An Introduction to Alchemical Elements");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "The Three Principles");
        this.add(helper.pageText(),
                """
                        The [#]($PURPLE)Principles[#](), or Essentials, are the three basic elements all things are made of.
                        \\
                        \\
                        Despite the name, they are unrelated to the common materials often associated with these words, such as table salt, metallic mercury and the mineral sulfur.
                        """);

        helper.page("salt");
        var salt = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Salt");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Salt[#]() is the principle representing the **Body** of a thing. It provides the matrix wherein Sulfur and Mercury can act. As such it is associated with materiality, stability and manifestation in the physical world.
                        """);

        helper.page("sulfur");
        var sulfur = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Sulfur");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Sulfur[#]() is the **Soul** of a thing. It represents the unique properties of a piece of matter, such as how it will look, feel, and how it interacts with other things.
                        \\
                        \\
                        Transforming the Sulfur of one thing is the underlying idea of *transmutation*.
                        """);

        helper.page("mercury");
        var mercury = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Mercury");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Mercury[#]() is the **Energy** or Life Force of a thing. It is the most elusive of the three principles, and enables the other two principles to function.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.MERCURY_CRYSTAL.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        salt,
                        sulfur,
                        mercury
                );
    }

    @Override
    public LanguageProvider getLanguageProvider() {
        return this.lang;
    }

    @Override
    public void setLanguageProvider(LanguageProvider languageProvider) {
        this.lang = languageProvider;
    }
}
