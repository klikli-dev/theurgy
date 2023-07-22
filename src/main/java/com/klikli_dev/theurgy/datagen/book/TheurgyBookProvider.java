/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class TheurgyBookProvider extends BookProvider {
    public TheurgyBookProvider(PackOutput packOutput, LanguageProvider lang, LanguageProvider... translations) {
        super("the_hermetica", packOutput, Theurgy.MODID, lang, translations);
    }

    @Override
    protected void registerDefaultMacros() {
        this.registerDefaultMacro("$PURPLE", "ad03fc");
    }

    @Override
    protected BookModel generateBook() {
        this.lang.add(this.context().bookName(), "The Hermetica");
        this.lang.add(this.context().bookTooltip(), "A treatise on the Ancient Art of Alchemy.\n§o(In-Game Guide for Theurgy)§r");

        int categorySortNum = 1;
        var gettingStartedCategory = new GettingStartedCategoryProvider(this).generate().withSortNumber(categorySortNum++);
        var spagyricsCategory = new SpagyricsCategoryProvider(this).generate().withSortNumber(categorySortNum++);
        spagyricsCategory.withCondition(this.condition().entryRead(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/spagyrics")));

        var book = BookModel.create(
                        this.modLoc(this.context().bookId()),
                        this.context().bookName()
                )
                .withTooltip(this.context().bookTooltip())
                .withCategories(
                        gettingStartedCategory,
                        spagyricsCategory
                )
                .withGenerateBookItem(true)
                .withModel(this.modLoc("the_hermetica_icon"))
                .withAutoAddReadConditions(true);
        return book;
    }
}
