/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.modonomicon.api.ModonomiconAPI;
import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class TheurgyBookProvider extends BookProvider {
    public TheurgyBookProvider(DataGenerator generator, String modid, LanguageProvider lang) {
        super(generator, modid, lang);
    }

    @Override
    protected void generate() {
        var theHermetica = this.makeTheHermetica();
        this.add(theHermetica);
    }

    private BookModel makeTheHermetica() {
        var helper = ModonomiconAPI.get().getLangHelper(this.modid);
        helper.book("the_hermetica");

        this.lang.add(helper.bookName(), "The Hermetica");
        this.lang.add(helper.bookTooltip(), "A treatise on the Ancient Art of Alchemy");

        int categorySortNum = 1;

        var book = BookModel.builder()
                .withId(this.modLoc("the_hermetica"))
                .withName(helper.bookName())
                .withTooltip(helper.bookTooltip())
                .withCategories(

                )
                .withGenerateBookItem(true)
                .withAutoAddReadConditions(true)
                .build();
        return book;
    }
}
