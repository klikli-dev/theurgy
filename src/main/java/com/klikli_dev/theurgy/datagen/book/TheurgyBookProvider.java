// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CreativeModeTabRegistry;


public class TheurgyBookProvider extends SingleBookSubProvider {
    public TheurgyBookProvider(ModonomiconLanguageProvider lang) {
        super("the_hermetica", Theurgy.MODID, lang);
    }

    @Override
    protected void registerDefaultMacros() {
        this.registerDefaultMacro("$PURPLE", "ad03fc");
        this.registerDefaultMacro("$INPUT", "008080");
    }

    @Override
    protected void generateCategories() {
        //TODO: setup real entry conditions

        var gettingStartedCategory = this.add(new GettingStartedCategoryProvider(this).generate());

        var apparatusCategory = this.add(new ApparatusCategory(this).generate());

        var logisticsCategory = this.add(new LogisticsCategory(this).generate());
    }

    @Override
    protected String bookName() {
        return "The Hermetica";
    }

    @Override
    protected String bookTooltip() {
        return "A treatise on the Ancient Art of Alchemy.\n§o(In-Game Guide for Theurgy)§r";
    }

    @Override
    protected BookModel additionalSetup(BookModel book) {
        return super.additionalSetup(book)
                .withModel(this.modLoc("the_hermetica_icon"))
                .withGenerateBookItem(true)
                .withCreativeTab(CreativeModeTabRegistry.THEURGY.getId())
                .withAutoAddReadConditions(true);
    }
}
