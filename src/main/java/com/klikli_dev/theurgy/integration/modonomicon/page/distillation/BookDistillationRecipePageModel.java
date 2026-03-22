// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;


public class BookDistillationRecipePageModel extends BookRecipePageModel<BookDistillationRecipePageModel> {
    protected BookDistillationRecipePageModel() {
        super(TheurgyModonomiconConstants.Page.DISTILLATION_RECIPE);
    }

    public static BookDistillationRecipePageModel create() {
        return new BookDistillationRecipePageModel();
    }
}
