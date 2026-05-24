// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;


public class BookCalcinationRecipePageModel extends BookRecipePageModel<BookCalcinationRecipePageModel> {
    protected BookCalcinationRecipePageModel() {
        super(BookCalcinationRecipePage.ID);
    }

    public static BookCalcinationRecipePageModel create() {
        return new BookCalcinationRecipePageModel();
    }

    @Override
    protected BookPage createPage(BookRecipePage.JsonDataHolder common) {
        return new BookCalcinationRecipePage(common);
    }
}
