// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon;

import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.modonomicon.registry.BookPageTypeRegistry;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.distillation.BookDistillationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.incubation.BookIncubationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction.BookLiquefactionRecipePage;

public final class TheurgyModonomiconPageTypeRegistry {

    public static final BookPageType<BookAccumulationRecipePage> ACCUMULATION_RECIPE = BookPageTypeRegistry.register(BookAccumulationRecipePage.ID, BookAccumulationRecipePage.CODEC, BookAccumulationRecipePage.STREAM_CODEC);
    public static final BookPageType<BookCalcinationRecipePage> CALCINATION_RECIPE = BookPageTypeRegistry.register(BookCalcinationRecipePage.ID, BookCalcinationRecipePage.CODEC, BookCalcinationRecipePage.STREAM_CODEC);
    public static final BookPageType<BookDistillationRecipePage> DISTILLATION_RECIPE = BookPageTypeRegistry.register(BookDistillationRecipePage.ID, BookDistillationRecipePage.CODEC, BookDistillationRecipePage.STREAM_CODEC);
    public static final BookPageType<BookIncubationRecipePage> INCUBATION_RECIPE = BookPageTypeRegistry.register(BookIncubationRecipePage.ID, BookIncubationRecipePage.CODEC, BookIncubationRecipePage.STREAM_CODEC);
    public static final BookPageType<BookLiquefactionRecipePage> LIQUEFACTION_RECIPE = BookPageTypeRegistry.register(BookLiquefactionRecipePage.ID, BookLiquefactionRecipePage.CODEC, BookLiquefactionRecipePage.STREAM_CODEC);

    private TheurgyModonomiconPageTypeRegistry() {
    }

    public static void bootstrap() {
    }
}
