/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.modonomicon.api.ModonomiconAPI;
import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class TheurgyBookProvider extends BookProvider {
    public TheurgyBookProvider(PackOutput packOutput, String modid, LanguageProvider lang, LanguageProvider... translations) {
        super(packOutput, modid, lang, translations);
    }

    @Override
    protected void generate() {
        var theHermetica = this.makeTheHermetica();
        this.add(theHermetica);
    }

    private BookModel makeTheHermetica() {
        var helper = ModonomiconAPI.get().getLangHelper(this.modid);
        helper.book("the_hermetica");

        this.lang().add(helper.bookName(), "The Hermetica");
        this.lang().add(helper.bookTooltip(), "A treatise on the Ancient Art of Alchemy.\n§o(In-Game Guide for Theurgy)§r");

        int categorySortNum = 1;
        var gettingStartedCategory = new GettingStartedCategoryProvider().make(helper, this.lang).withSortNumber(categorySortNum++);

        var book = BookModel.create(
                        this.modLoc("the_hermetica"),
                        helper.bookName()
                )
                .withTooltip(helper.bookTooltip())
                .withCategories(
                        gettingStartedCategory
                )
                .withGenerateBookItem(true)
                .withModel(this.modLoc("the_hermetica_icon"))
                .withAutoAddReadConditions(true);
        return book;
    }
}
