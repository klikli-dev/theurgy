// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;


public class BookCalcinationRecipePage extends BookProcessingRecipePage<CalcinationRecipe> {
    public BookCalcinationRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(RecipeTypeRegistry.CALCINATION.get(), title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }

    public static BookCalcinationRecipePage fromJson(JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(json, provider);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
        return new BookCalcinationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static BookCalcinationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookCalcinationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.CALCINATION_RECIPE;
    }

}
