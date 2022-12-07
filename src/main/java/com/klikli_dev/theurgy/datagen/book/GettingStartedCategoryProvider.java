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
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.getId().toString())
                .withEntries(
                        introEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, LanguageProvider lang, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        lang.add(helper.entryName(), "About");
        lang.add(helper.entryDescription(), "About using The Hermetica");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        lang.add(helper.pageTitle(), "About");
        lang.add(helper.pageText(),
                """
                        The following pages will lead the novice alchemist on their journey through the noble art of the transformation of matter and mind. This humble author will share his experiences, thoughts and research notes to guide the valued reader in as safe a manner as the subject matter allows.
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

}
