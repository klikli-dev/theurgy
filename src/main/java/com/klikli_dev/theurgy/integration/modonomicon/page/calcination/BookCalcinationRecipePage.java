/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class BookCalcinationRecipePage extends BookProcessingRecipePage<CalcinationRecipe> {
    public BookCalcinationRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor) {
        super(RecipeTypeRegistry.CALCINATION.get(), title1, recipeId1, title2, recipeId2, text, anchor);
    }

    public static BookCalcinationRecipePage fromJson(JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        return new BookCalcinationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor);
    }

    public static BookCalcinationRecipePage fromNetwork(FriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        return new BookCalcinationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.CALCINATION_RECIPE;
    }


    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeUtf(this.anchor);
    }
}
