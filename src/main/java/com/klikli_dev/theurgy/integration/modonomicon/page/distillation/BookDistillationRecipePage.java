// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;


public class BookDistillationRecipePage extends BookProcessingRecipePage<DistillationRecipe> {
    public BookDistillationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookDistillationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    public static BookDistillationRecipePage fromJson(Identifier entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(entryId, json, provider);
        return new BookDistillationRecipePage(common);
    }

    public static BookDistillationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        return new BookDistillationRecipePage(common);
    }
    
    @Override
    public Identifier getType() {
        return TheurgyModonomiconConstants.Page.DISTILLATION_RECIPE;
    }

}
