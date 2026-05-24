// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;


public class BookLiquefactionRecipePageModel extends BookRecipePageModel<BookLiquefactionRecipePageModel> {
    protected BookLiquefactionRecipePageModel() {
        super(BookLiquefactionRecipePage.ID);
    }

    public static BookLiquefactionRecipePageModel create() {
        return new BookLiquefactionRecipePageModel();
    }

    @Override
    protected BookPage createPage(BookRecipePage.JsonDataHolder common) {
        return new BookLiquefactionRecipePage(common);
    }
}
