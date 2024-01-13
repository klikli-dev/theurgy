// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CreativeModeTabRegistry;
import net.minecraft.data.PackOutput;

public class TheurgyBookProvider extends BookProvider {
    public TheurgyBookProvider(PackOutput packOutput, ModonomiconLanguageProvider lang) {
        super("the_hermetica", packOutput, Theurgy.MODID, lang);
    }

    @Override
    protected void registerDefaultMacros() {
        this.registerDefaultMacro("$PURPLE", "ad03fc");
        this.registerDefaultMacro("$INPUT", "008080");
    }

    @Override
    protected BookModel generateBook() {
        this.lang.add(this.context().bookName(), "The Hermetica");
        this.lang.add(this.context().bookTooltip(), "A treatise on the Ancient Art of Alchemy.\n§o(In-Game Guide for Theurgy)§r");

        int categorySortNum = 1;
        var gettingStartedCategory = new GettingStartedCategoryProvider(this).generate().withSortNumber(categorySortNum++);

        var apparatusCategory = new ApparatusCategory(this).generate().withSortNumber(categorySortNum++);

        //TODO: entry read condition

        var book = BookModel.create(
                        this.modLoc(this.context().bookId()),
                        this.context().bookName()
                )
                .withTooltip(this.context().bookTooltip())
                .withCategories(
                        gettingStartedCategory,
                        apparatusCategory
                )
                .withGenerateBookItem(true)
                .withModel(this.modLoc("the_hermetica_icon"))
                .withCreativeTab(CreativeModeTabRegistry.THEURGY.getId())
                .withAutoAddReadConditions(true);
        return book;
    }
}
