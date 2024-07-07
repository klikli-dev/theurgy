// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.logistics.*;
import com.klikli_dev.theurgy.registry.ItemRegistry;


public class LogisticsCategory extends CategoryProvider {

    public static final String CATEGORY_ID = "logistics";

    public LogisticsCategory(TheurgyBookProvider parent) {
        super(parent);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "________________l___í_____________",
                "__________________________________",
                "________________i_____ŵ_n_ň_______",
                "__________________________________",
                "________________w___e___f_________",
                "__________________________________",
                "________________________a_________",
                "__________________________________"

        };
    }

    @Override
    protected void generateEntries() {
        var introEntry = new IntroEntry(this).generate('i');
        var loreEntry = new LoreEntry(this).generate('l');
        loreEntry.withParent(introEntry);

        var wandEntry = new MercurialWandEntry(this).generate('w');
        wandEntry.withParent(introEntry);
        //TODO: add a brief tutorial entry for reconfiguring e.g. an oven setup with the wand to not use the default direction?
        //      maybe after the network entry

        var wireEntry = new MercurialWireEntry(this).generate('ŵ');
        wireEntry.withParent(introEntry);

        var extractorEntry = new ItemExtractorEntry(this).generate('e');
        extractorEntry.withParent(introEntry);

        var inserterEntry = new ItemInserterEntry(this).generate('í');
        inserterEntry.withParent(introEntry);

        var networkEntry = new LogisticsNetworkEntry(this).generate('n');
        networkEntry.withParent(wireEntry);
        networkEntry.withParent(extractorEntry);
        networkEntry.withParent(inserterEntry);

        var nodeEntry = new ConnectionNodeEntry(this).generate('ň');
        nodeEntry.withParent(networkEntry);

        var listFilterEntry = new ListFilterEntry(this).generate('f');
        listFilterEntry.withParent(networkEntry);

        var attributeFilterEntry = new AttributeFilterEntry(this).generate('a');
        attributeFilterEntry.withParent(listFilterEntry);
    }

    @Override
    protected String categoryName() {
        return "Mercurial Logistics";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURIAL_WAND.get());
    }

    @Override
    public String categoryId() {
        return CATEGORY_ID;
    }

    @Override
    protected BookCategoryModel additionalSetup(BookCategoryModel category) {
        return super.additionalSetup(category).withBackground(Theurgy.loc("textures/gui/book/bg_nightsky2.png"));
    }
}
