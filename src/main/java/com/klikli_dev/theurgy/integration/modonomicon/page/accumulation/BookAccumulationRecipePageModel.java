// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.accumulation;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;


public class BookAccumulationRecipePageModel extends BookRecipePageModel<BookAccumulationRecipePageModel> {
    protected BookAccumulationRecipePageModel() {
        super(TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE);
    }

    public static BookAccumulationRecipePageModel create() {
        return new BookAccumulationRecipePageModel();
    }
}
