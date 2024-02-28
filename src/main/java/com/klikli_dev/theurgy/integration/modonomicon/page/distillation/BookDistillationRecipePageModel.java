// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.api.datagen.book.condition.BookConditionModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookRecipePageModel;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import org.jetbrains.annotations.NotNull;

public class BookDistillationRecipePageModel extends BookRecipePageModel {
    protected BookDistillationRecipePageModel(@NotNull String anchor, @NotNull BookConditionModel condition) {
        super(TheurgyModonomiconConstants.Page.DISTILLATION_RECIPE, anchor, condition);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BookRecipePageModel.Builder<Builder> {
        protected Builder() {
            super();
        }

        public BookDistillationRecipePageModel build() {
            var model = new BookDistillationRecipePageModel(this.anchor, this.condition);
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
