// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;


public class BookIncubationRecipePageModel extends BookRecipePageModel<BookIncubationRecipePageModel> {
    protected BookIncubationRecipePageModel() {
        super(BookIncubationRecipePage.ID);
    }

    public static BookIncubationRecipePageModel create() {
        return new BookIncubationRecipePageModel();
    }

    @Override
    protected BookPage createPage(BookRecipePage.JsonDataHolder common) {
        return new BookIncubationRecipePage(common);
    }
}
