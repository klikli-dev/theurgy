// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.calcination;

import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconPageTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;


public class BookCalcinationRecipePage extends BookProcessingRecipePage<CalcinationRecipe> {
    public static final Identifier ID = Theurgy.loc("calcination_recipe");
    public static final MapCodec<BookCalcinationRecipePage> CODEC = codec(BookCalcinationRecipePage::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BookCalcinationRecipePage> STREAM_CODEC = streamCodec(BookCalcinationRecipePage::new);

    public BookCalcinationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookCalcinationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    @Override
    public BookPageType<?> type() {
        return TheurgyModonomiconPageTypeRegistry.CALCINATION_RECIPE;
    }

}
