// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;


public class BookIncubationRecipePage extends BookProcessingRecipePage<IncubationRecipe> {
    public BookIncubationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookIncubationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    public static BookIncubationRecipePage fromJson(Identifier entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(entryId, json, provider);
        return new BookIncubationRecipePage(common);
    }

    public static BookIncubationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        return new BookIncubationRecipePage(common);
    }

    @Override
    public Identifier getType() {
        return TheurgyModonomiconConstants.Page.INCUBATION_RECIPE;
    }

}
