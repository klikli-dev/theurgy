// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;


public class BookDistillationRecipePageModel extends BookRecipePageModel<BookDistillationRecipePageModel> {
    protected BookDistillationRecipePageModel() {
        super(BookDistillationRecipePage.ID);
    }

    public static BookDistillationRecipePageModel create() {
        return new BookDistillationRecipePageModel();
    }

    @Override
    protected BookPage createPage(BookRecipePage.JsonDataHolder common) {
        return new BookDistillationRecipePage(common);
    }
}
