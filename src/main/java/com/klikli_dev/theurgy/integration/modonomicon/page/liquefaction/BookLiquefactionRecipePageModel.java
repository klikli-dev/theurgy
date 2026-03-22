// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;


public class BookLiquefactionRecipePageModel extends BookRecipePageModel<BookLiquefactionRecipePageModel> {
    protected BookLiquefactionRecipePageModel() {
        super(TheurgyModonomiconConstants.Page.LIQUEFACTION_RECIPE);
    }

    public static BookLiquefactionRecipePageModel create() {
        return new BookLiquefactionRecipePageModel();
    }
}
