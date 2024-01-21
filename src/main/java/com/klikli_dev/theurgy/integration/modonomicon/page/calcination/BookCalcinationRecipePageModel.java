// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePageModel;
import org.jetbrains.annotations.NotNull;


public class BookCalcinationRecipePageModel extends BookRecipePageModel<BookCalcinationRecipePageModel> {
    protected BookCalcinationRecipePageModel() {
        super(TheurgyModonomiconConstants.Page.CALCINATION_RECIPE);
    }

    public static BookCalcinationRecipePageModel create() {
        return new BookCalcinationRecipePageModel();
    }
}
