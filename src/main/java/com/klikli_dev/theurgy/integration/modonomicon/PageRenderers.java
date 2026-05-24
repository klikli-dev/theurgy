// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePageRenderer;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePageRenderer;
import com.klikli_dev.theurgy.integration.modonomicon.page.distillation.BookDistillationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.distillation.BookDistillationRecipePageRenderer;
import com.klikli_dev.theurgy.integration.modonomicon.page.incubation.BookIncubationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.incubation.BookIncubationRecipePageRenderer;
import com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction.BookLiquefactionRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction.BookLiquefactionRecipePageRenderer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class PageRenderers {

    public static void onClientSetup(FMLClientSetupEvent event) {
        PageRendererRegistry.registerPageRenderer(
                BookAccumulationRecipePage.ID,
                p -> new BookAccumulationRecipePageRenderer((BookAccumulationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                BookCalcinationRecipePage.ID,
                p -> new BookCalcinationRecipePageRenderer((BookCalcinationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                BookDistillationRecipePage.ID,
                p -> new BookDistillationRecipePageRenderer((BookDistillationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                BookIncubationRecipePage.ID,
                p -> new BookIncubationRecipePageRenderer((BookIncubationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                BookLiquefactionRecipePage.ID,
                p -> new BookLiquefactionRecipePageRenderer((BookLiquefactionRecipePage) p));
    }

}
