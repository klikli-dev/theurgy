// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.distillation;

import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconPageTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;


public class BookDistillationRecipePage extends BookProcessingRecipePage<DistillationRecipe> {
    public static final Identifier ID = Theurgy.loc("distillation_recipe");
    public static final MapCodec<BookDistillationRecipePage> CODEC = codec(BookDistillationRecipePage::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BookDistillationRecipePage> STREAM_CODEC = streamCodec(BookDistillationRecipePage::new);

    public BookDistillationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookDistillationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    @Override
    public BookPageType<?> type() {
        return TheurgyModonomiconPageTypeRegistry.DISTILLATION_RECIPE;
    }

}
