// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;


public class BookIncubationRecipePage extends BookProcessingRecipePage<IncubationRecipe> {
    public BookIncubationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookIncubationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    public static BookIncubationRecipePage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(entryId, json, provider);
         return new BookIncubationRecipePage(common);
    }

    public static BookIncubationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        return new BookIncubationRecipePage(common);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.INCUBATION_RECIPE;
    }

}
