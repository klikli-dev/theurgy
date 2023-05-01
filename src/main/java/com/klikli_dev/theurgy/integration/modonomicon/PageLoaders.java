// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon;

import com.klikli_dev.modonomicon.data.LoaderRegistry;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePage;
import com.klikli_dev.theurgy.integration.modonomicon.page.distillation.BookDistillationRecipePage;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class PageLoaders {

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        LoaderRegistry.registerPageLoader(TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE, BookAccumulationRecipePage::fromJson, BookAccumulationRecipePage::fromNetwork);
        LoaderRegistry.registerPageLoader(TheurgyModonomiconConstants.Page.CALCINATION_RECIPE, BookCalcinationRecipePage::fromJson, BookCalcinationRecipePage::fromNetwork);
        LoaderRegistry.registerPageLoader(TheurgyModonomiconConstants.Page.DISTILLATION_RECIPE, BookDistillationRecipePage::fromJson, BookDistillationRecipePage::fromNetwork);
    }


}
