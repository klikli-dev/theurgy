// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction;

import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconPageTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;


public class BookLiquefactionRecipePage extends BookProcessingRecipePage<LiquefactionRecipe> {
    public static final Identifier ID = Theurgy.loc("liquefaction_recipe");
    public static final MapCodec<BookLiquefactionRecipePage> CODEC = codec(BookLiquefactionRecipePage::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BookLiquefactionRecipePage> STREAM_CODEC = streamCodec(BookLiquefactionRecipePage::new);

    public BookLiquefactionRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookLiquefactionRecipePage(NetworkDataHolder common) {
        super(common);
    }

    @Override
    public BookPageType<?> type() {
        return TheurgyModonomiconPageTypeRegistry.LIQUEFACTION_RECIPE;
    }
}
