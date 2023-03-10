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

public class SpagyricsCategoryProvider {

    public static final String CATEGORY_ID = "spagyrics";

    public BookCategoryModel.Builder make(BookLangHelper helper, LanguageProvider lang) {
        helper.category(CATEGORY_ID);
        lang.add(helper.categoryName(), "Spagyrics");

        var entryHelper = ModonomiconAPI.get().getEntryLocationHelper();
        entryHelper.setMap(
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________i_______________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________"
        );

        var introEntry = this.makeIntroEntry(helper, lang, entryHelper, 'i');

        return BookCategoryModel.builder()
                .withId(Theurgy.loc((helper.category)))
                .withName(helper.categoryName())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withEntries(
                        introEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, LanguageProvider lang, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        lang.add(helper.entryName(), "Spagyrics");
        lang.add(helper.entryDescription(), "Power over the Three Principles");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "Spagyrics");
        lang.add(helper.pageText(),
                """
                        Spagyrics is derived from Greek for "to separate and reunite". As such, it is the process of separating, purifying and recombining the *three principles*, or "elements", of matter: **alchemical salt**, **sulfur** and **mercury**.
                        """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "Benefits");
        lang.add(helper.pageText(),
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
}
