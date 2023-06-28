/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.*;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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
        spagyricsCategory.withCondition(this.entryReadCondition(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/spagyrics")));

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

    public BookAdvancementConditionModel advancementCondition(ResourceLocation advancementId) {
        return BookAdvancementConditionModel.builder().withAdvancementId(advancementId).build();
    }

    public BookEntryReadConditionModel entryReadCondition(ResourceLocation entryId) {
        return BookEntryReadConditionModel.builder().withEntry(entryId).build();
    }

    public BookEntryReadConditionModel entryReadCondition(BookEntryModel.Builder entry) {
        return BookEntryReadConditionModel.builder().withEntry(entry.getId()).build();
    }

    public BookAndConditionModel and(BookConditionModel... children) {
        return BookAndConditionModel.builder().withChildren(children).build();
    }

    public BookOrConditionModel or(BookConditionModel... children) {
        return BookOrConditionModel.builder().withChildren(children).build();
    }
}
