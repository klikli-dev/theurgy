// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.integration.modonomicon.page.BookAccumulationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.BookAccumulationRecipePageRenderer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PageRenderers {

    public static void onClientSetup(FMLClientSetupEvent event) {
        PageRendererRegistry.registerPageRenderer(
                TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE,
                p -> new BookAccumulationRecipePageRenderer((BookAccumulationRecipePage) p));
    }

}
