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
    protected void generateEntries() {
        var introEntry = this.add(new IntroEntry(this).generate());
        this.layout().entry(introEntry).at(-1, -1);

        var loreEntry = this.add(new LoreEntry(this).generate());
        loreEntry.withParent(introEntry);
        this.layout().entry(loreEntry).above(introEntry, 2);

        var wandEntry = this.add(new MercurialWandEntry(this).generate());
        wandEntry.withParent(introEntry);
        //TODO: add a brief tutorial entry for reconfiguring e.g. an oven setup with the wand to not use the default direction?
        //      maybe after the network entry
        this.layout().entry(wandEntry).below(introEntry, 2);

        var wireEntry = this.add(new MercurialWireEntry(this).generate());
        wireEntry.withParent(introEntry);
        this.layout().entry(wireEntry).rightOf(introEntry, 6);

        var extractorEntry = this.add(new ItemExtractorEntry(this).generate());
        extractorEntry.withParent(introEntry);
        this.layout().entry(extractorEntry).rightOf(introEntry, 4).below(2);

        var inserterEntry = this.add(new ItemInserterEntry(this).generate());
        inserterEntry.withParent(introEntry);
        this.layout().entry(inserterEntry).rightOf(introEntry, 4).above(2);

        var fluidExtractorEntry = this.add(new FluidExtractorEntry(this).generate());
        fluidExtractorEntry.withParent(extractorEntry);
        this.layout().entry(fluidExtractorEntry).below(extractorEntry, 2);

        var fluidInserterEntry = this.add(new FluidInserterEntry(this).generate());
        fluidInserterEntry.withParent(inserterEntry);
        this.layout().entry(fluidInserterEntry).above(inserterEntry, 2);

        var networkEntry = this.add(new LogisticsNetworkEntry(this).generate());
        networkEntry.withParent(wireEntry);
        networkEntry.withParent(extractorEntry);
        networkEntry.withParent(inserterEntry);
        this.layout().entry(networkEntry).rightOf(wireEntry, 2);

        var nodeEntry = this.add(new ConnectionNodeEntry(this).generate());
        nodeEntry.withParent(networkEntry);
        this.layout().entry(nodeEntry).rightOf(networkEntry, 2).above(2);

        var nexusEntry = this.add(new LogisticsNexusEntry(this).generate());
        nexusEntry.withParent(nodeEntry);
        this.layout().entry(nexusEntry).above(nodeEntry, 2);

        var listFilterEntry = this.add(new ListFilterEntry(this).generate());
        listFilterEntry.withParent(networkEntry);
        this.layout().entry(listFilterEntry).rightOf(networkEntry, 2).below(2);

        var attributeFilterEntry = this.add(new AttributeFilterEntry(this).generate());
        attributeFilterEntry.withParent(listFilterEntry);
        this.layout().entry(attributeFilterEntry).below(listFilterEntry, 2);

        var fluidListFilterEntry = this.add(new FluidListFilterEntry(this).generate());
        fluidListFilterEntry.withParent(attributeFilterEntry);
        this.layout().entry(fluidListFilterEntry).below(attributeFilterEntry, 2);

        var frequencyEntry = this.add(new FrequencyEntry(this).generate());
        frequencyEntry.withParent(networkEntry);
        this.layout().entry(frequencyEntry).rightOf(networkEntry, 4);

        var mercuryFluxConnectorEntry = this.add(new MercuryFluxConnectorEntry(this).generate());
        mercuryFluxConnectorEntry.withParent(frequencyEntry);
        this.layout().entry(mercuryFluxConnectorEntry).below(frequencyEntry, 2);

        var capabilityProbeEntry = this.add(new CapabilityProbeEntry(this).generate());
        capabilityProbeEntry.withParent(frequencyEntry);
        this.layout().entry(capabilityProbeEntry).rightOf(frequencyEntry, 2).below(2);

        var capabilityProxyEntry = this.add(new CapabilityProxyEntry(this).generate());
        capabilityProxyEntry.withParent(capabilityProbeEntry);
        this.layout().entry(capabilityProxyEntry).below(capabilityProbeEntry, 2);
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
        return super.additionalSetup(category).withBackground(Theurgy.loc("textures/gui/book/bg_nightsky4_small.png"));
    }
}
