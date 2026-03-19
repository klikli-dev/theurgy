// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


public class BookCalcinationRecipePage extends BookProcessingRecipePage<CalcinationRecipe> {
    public BookCalcinationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookCalcinationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    public static BookCalcinationRecipePage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(entryId, json, provider);
        return new BookCalcinationRecipePage(common);
    }

    public static BookCalcinationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        return new BookCalcinationRecipePage(common);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.CALCINATION_RECIPE;
    }

}
