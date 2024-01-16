// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;


public class BookLiquefactionRecipePage extends BookProcessingRecipePage<LiquefactionRecipe> {
    public BookLiquefactionRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor) {
        super(RecipeTypeRegistry.LIQUEFACTION.get(), title1, recipeId1, title2, recipeId2, text, anchor);
    }

    public static BookLiquefactionRecipePage fromJson(JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        return new BookLiquefactionRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor);
    }

    public static BookLiquefactionRecipePage fromNetwork(FriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        return new BookLiquefactionRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.LIQUEFACTION_RECIPE;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeUtf(this.anchor);
    }
}
