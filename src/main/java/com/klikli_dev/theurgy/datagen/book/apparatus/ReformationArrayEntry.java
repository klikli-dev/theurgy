package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;

public class ReformationArrayEntry extends EntryProvider {

    public static final String ENTRY_ID = "reformation_array";

    public ReformationArrayEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("about", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Reformation Array");
        this.pageText(
                """
                        Reformation is the process of converting sulfurs into other Sulfurs of the same type and tier (such as common gems to common gems).\\
                        Further, it can be used to convert between different types of Niters of the same tier, and to convert Niters into Sulfurs.
                        The reformation array provides the necessary framework to perform this process.
                         """
        );
        this.page("structure", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Structure");
        this.add(this.context().pageText(),
                """
                        The array has no fixed structure, you simply need to place a {0}, at least one {1}, and a {2} within a few blocks of each other.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()),
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("structure2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Structure");
        this.add(this.context().pageText(),
                """
                        To set up the array, right-click each of the pedestals with the {3} to link it. Finally place the {3} near the pedestals and supply it with mercury flux, e.g. by attaching it to a {4}.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()),
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.entryLink("Sulfuric Flux Emitter", ApparatusCategory.CATEGORY_ID, SulfuricFluxEmitterEntry.ENTRY_ID),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("functions", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Functions");
        this.add(this.context().pageText(),
                """
                        - {0} will hold the sulfur you want to create more of.
                        - {1} will hold the input sulfur you want to use up. Multiple may be required.
                        - {2} will be filled with the created sulfur.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get())
        );

        this.page("placement", () -> BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/reformation_array"))
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageText(),
                """
                        An example setup for a Reformation Array. Note that other placements are possible.
                        """
        );

        this.page("visuals", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Visuals");
        this.add(this.context().pageText(),
                """
                        A glowing orb will show above a pedestal if it contains sulfur.\\
                        Flux particles will fly from the Emitter to the Pedestals if the array is active.
                        """
        );
    }

    @Override
    protected String entryName() {
        return "Reformation Array";
    }

    @Override
    protected String entryDescription() {
        return "Conversion of Sulfur into other Sulfurs of the same type";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/convert_sulfur.png"), 64, 64);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
