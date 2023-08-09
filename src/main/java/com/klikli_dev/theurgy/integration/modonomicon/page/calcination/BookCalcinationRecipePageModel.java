// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import org.jetbrains.annotations.NotNull;

public class BookCalcinationRecipePageModel extends BookRecipePageModel {
    protected BookCalcinationRecipePageModel(@NotNull String anchor) {
        super(TheurgyModonomiconConstants.Page.CALCINATION_RECIPE, anchor);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BookRecipePageModel.Builder<Builder> {
        protected Builder() {
            super();
        }

        public BookCalcinationRecipePageModel build() {
            var model = new BookCalcinationRecipePageModel(this.anchor);
            model.title1 = this.title1;
            model.recipeId1 = this.recipeId1;
            model.title2 = this.title2;
            model.recipeId2 = this.recipeId2;
            model.text = this.text;
            return model;
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
