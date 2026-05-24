// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.accumulation;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;


public class BookAccumulationRecipePageModel extends BookRecipePageModel<BookAccumulationRecipePageModel> {
    protected BookAccumulationRecipePageModel() {
        super(BookAccumulationRecipePage.ID);
    }

    public static BookAccumulationRecipePageModel create() {
        return new BookAccumulationRecipePageModel();
    }

    @Override
    protected BookPage createPage(BookRecipePage.JsonDataHolder common) {
        return new BookAccumulationRecipePage(common);
    }
}
