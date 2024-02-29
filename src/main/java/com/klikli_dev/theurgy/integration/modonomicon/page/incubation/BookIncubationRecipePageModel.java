// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.api.datagen.book.condition.BookConditionModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.integration.modonomicon.page.distillation.BookDistillationRecipePageModel;
import org.jetbrains.annotations.NotNull;


public class BookIncubationRecipePageModel extends BookRecipePageModel<BookIncubationRecipePageModel> {
    protected BookIncubationRecipePageModel() {
        super(TheurgyModonomiconConstants.Page.INCUBATION_RECIPE);
    }

    public static BookIncubationRecipePageModel create() {
        return new BookIncubationRecipePageModel();
    }
}
