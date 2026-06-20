// SPDX-FileCopyrightText: 2025 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.research.ResearchNodeRef;
import com.klikli_dev.modonomicon.api.datagen.research.SingleResearchSubProvider;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.DigestionVatEntry;
import com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation.FermentationVatEntry;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation.IncubationEntry;
import net.minecraft.resources.Identifier;

public class TheurgyResearch extends SingleResearchSubProvider {

    public static final ResearchNodeRef GETTING_STARTED_INCUBATION = node("research/entry_viewed/getting_started/incubation");
    public static final ResearchNodeRef GETTING_STARTED_INCUBATION_AFTER_REFORMATION = node("research/entry_viewed/getting_started/incubation_after_reformation");
    public static final ResearchNodeRef GETTING_STARTED_INCUBATION_AFTER_TRANSMUTATION = node("research/entry_viewed/getting_started/incubation_after_transmutation");
    public static final ResearchNodeRef GETTING_STARTED_INCUBATION_AFTER_EXALTATION = node("research/entry_viewed/getting_started/incubation_after_exaltation");
    public static final ResearchNodeRef APPARATUS_DIGESTION_VAT = node("research/entry_viewed/apparatus/digestion_vat");
    public static final ResearchNodeRef APPARATUS_FERMENTATION_VAT = node("research/entry_viewed/apparatus/fermentation_vat");

    public TheurgyResearch() {
        super("the_hermetica", Theurgy.MODID);
    }

    /**
     * Creates a minimal BookEntryModel with just the correct name for tooltip purposes.
     * Used for cross-category researchNodeEntryViewedOnce calls where the entry model
     * is not available as a local variable.
     */
    public static BookEntryModel entryModel(String categoryId, String entryId) {
        return BookEntryModel.create(
                Identifier.fromNamespaceAndPath(Theurgy.MODID, categoryId + "/" + entryId),
                "book.theurgy.the_hermetica." + categoryId + "." + entryId + ".name"
        );
    }

    @Override
    protected void generateResearch() {
        this.node(GETTING_STARTED_INCUBATION, this.ingress()
                .onEntryViewedOnce(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/" + com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics.IncubationEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/getting_started/incubation"));
        this.node(GETTING_STARTED_INCUBATION_AFTER_REFORMATION, this.ingress()
                .onEntryViewedOnce(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/" + com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.IncubationEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/getting_started/incubation_after_reformation"));
        this.node(GETTING_STARTED_INCUBATION_AFTER_TRANSMUTATION, this.ingress()
                .onEntryViewedOnce(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/" + com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.IncubationEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/getting_started/incubation_after_transmutation"));
        this.node(GETTING_STARTED_INCUBATION_AFTER_EXALTATION, this.ingress()
                .onEntryViewedOnce(this.modLoc(GettingStartedCategoryProvider.CATEGORY_ID + "/" + IncubationEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/getting_started/incubation_after_exaltation"));

        this.node(APPARATUS_DIGESTION_VAT, this.ingress()
                .onEntryViewedOnce(this.modLoc(ApparatusCategory.CATEGORY_ID + "/" + DigestionVatEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/apparatus/digestion_vat"));
        this.node(APPARATUS_FERMENTATION_VAT, this.ingress()
                .onEntryViewedOnce(this.modLoc(ApparatusCategory.CATEGORY_ID + "/" + FermentationVatEntry.ENTRY_ID))
                .declareFact("theurgy/research/entry_viewed/apparatus/fermentation_vat"));

        this.researchNodeName(GETTING_STARTED_INCUBATION, "The Philosopher's Incubator");
        this.researchNodeName(GETTING_STARTED_INCUBATION_AFTER_REFORMATION, "Incubation After Reformation");
        this.researchNodeName(GETTING_STARTED_INCUBATION_AFTER_TRANSMUTATION, "Incubation After Transmutation");
        this.researchNodeName(GETTING_STARTED_INCUBATION_AFTER_EXALTATION, "Incubation After Exaltation");
        this.researchNodeName(APPARATUS_DIGESTION_VAT, "The Alchemist's Crucible");
        this.researchNodeName(APPARATUS_FERMENTATION_VAT, "The Fermentation Vat");
    }

    static ResearchNodeRef node(String path) {
        return ResearchNodeRef.of(Identifier.fromNamespaceAndPath(Theurgy.MODID, path));
    }
}
