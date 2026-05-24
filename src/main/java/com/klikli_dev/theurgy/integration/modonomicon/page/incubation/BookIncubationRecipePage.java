// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.incubation;

import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconPageTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;


public class BookIncubationRecipePage extends BookProcessingRecipePage<IncubationRecipe> {
    public static final Identifier ID = Theurgy.loc("incubation_recipe");
    public static final MapCodec<BookIncubationRecipePage> CODEC = codec(BookIncubationRecipePage::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BookIncubationRecipePage> STREAM_CODEC = streamCodec(BookIncubationRecipePage::new);

    public BookIncubationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookIncubationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    @Override
    public BookPageType<?> type() {
        return TheurgyModonomiconPageTypeRegistry.INCUBATION_RECIPE;
    }

}
