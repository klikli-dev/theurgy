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
import com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction.BookLiquefactionRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.liquefaction.BookLiquefactionRecipePageRenderer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PageRenderers {

    public static void onClientSetup(FMLClientSetupEvent event) {
        PageRendererRegistry.registerPageRenderer(
                TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE,
                p -> new BookAccumulationRecipePageRenderer((BookAccumulationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                TheurgyModonomiconConstants.Page.CALCINATION_RECIPE,
                p -> new BookCalcinationRecipePageRenderer((BookCalcinationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                TheurgyModonomiconConstants.Page.DISTILLATION_RECIPE,
                p -> new BookDistillationRecipePageRenderer((BookDistillationRecipePage) p));

        PageRendererRegistry.registerPageRenderer(
                TheurgyModonomiconConstants.Page.LIQUEFACTION_RECIPE,
                p -> new BookLiquefactionRecipePageRenderer((BookLiquefactionRecipePage) p));
    }

}
