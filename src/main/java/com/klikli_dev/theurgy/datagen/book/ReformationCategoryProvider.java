// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class ReformationCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "reformation";

    public ReformationCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________i_r_s___________________",
                "__________________________________",
                "__________________________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {
        var intro = this.add(this.intro('i'));
        var reformationArray = this.add(this.reformationArray('r'));
        var sulfuricFluxEmitter = this.add(this.sulfuricFluxEmitter('s'));

        reformationArray.addParent(this.parent(intro));
        sulfuricFluxEmitter.addParent(this.parent(reformationArray));
        //TODO: link from flux page to here
        //TODO: link from getting started to here -> use three ingots icon
        //TODO: one entry per apparatus
        //TODO: then a few entries to show the process (based on iron, for example)
        //TODO: use an icon that indicates duplication of eg ingots -> use three ingots icon
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Reformation");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"))
                .withIcon(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get());
    }


    private BookEntryModel intro(char location) {
        this.context().entry("intro");
        this.add(this.context().entryName(), "Reformation");
        this.add(this.context().entryDescription(), "Replication of Matter");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Reformation");
        this.add(this.context().pageText(),
                """
                        Reformation is the process of replicating matter. However, matter cannot be created from nothing, so the process requires a source of matter to be converted into the desired matter. Furthermore, matter in its complete form consisting of Soul, Spirit and Body resists change.
                        """
        );

        this.context().page("process");
        var process = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Process");
        this.add(this.context().pageText(),
                """
                        Therefore, the process of reformation requires the matter to be broken down into its components first, and only the Soul - the Sulfur - is malleable and may be transformed.
                        """

        );

        this.context().page("theory");
        var theory = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Theoretical Underpinnings");
        this.add(this.context().pageText(),
                """
                        This Reformation of one sulfur into the other can be achieved by the application of Sulfuric Flux. This is a type of Mercury flux that has been infused with the essence of one type of Sulfur, allowing it to transform other Sulfur it gets in contact with into the same type.
                        """

        );

        this.context().page("reassembly");
        var reassembly = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Reassembly");
        this.add(this.context().pageText(),
                """
                        Once the desired Sulfur has been produced, {0} can be used to reassemble the matter into the desired form.
                        """,
                this.categoryLink("Spagyrics", SpagyricsCategoryProvider.CATEGORY_ID)
        );


        return this.entry(location)
                .withIcon(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get())
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        process,
                        theory,
                        reassembly
                );
    }

    private BookEntryModel reformationArray(char location) {
        this.context().entry("reformation_array");
        this.add(this.context().entryName(), "Reformation Array");
        this.add(this.context().entryDescription(), "Conversion of Sulfur");

        this.page("about", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Reformation Array");
        this.add(this.context().pageText(),
                """
                        The reformation array provides the necessary framework for to attune Sulfuric Flux to a target sulfur and use it to transform source sulfur into that target type of sulfur.
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

        return this.entry(location)
                .withIcon(Theurgy.loc("textures/gui/book/convert_sulfur.png"), 64, 64)
                .withEntryBackground(EntryBackground.DEFAULT);
    }

    private BookEntryModel sulfuricFluxEmitter(char location) {
        this.context().entry("sulfuric_flux_emitter");
        this.add(this.context().entryName(), "Sulfuric Flux Emitter");
        this.add(this.context().entryDescription(), "Flux that can transform Sulfur");

        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageText(),
                """
                        This devices converts raw mercury flux into [#]($PURPLE)Sulfuric Flux[#](), a type of energy that can carry information. It can be used to transform Sulfur into other types of Sulfur by linking it to a Reformation Array.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get())
        );

        this.page("targets", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Target Blocks");
        this.add(this.context().pageText(),
                """
                        Valid target blocks that will form a Reformation Array are {0},  {1}, and {2}.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get())
        );


        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Right-click the target blocks (the pedestals) with the {0} so they get highlighted.\\
                        Then place the Emitter onto a Mercury Flux source, such as a {1}.\\
                        The maximum range is **8** blocks.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("usage2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        If a valid reformation array is linked to the emitter, and a valid recipe is present in the pedestals, the emitter will start emitting sulfuric flux and transform the sulfur.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("usage3", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Checking Validity");
        this.add(this.context().pageText(),
                """
                        Right-click the placed {0} to check if it is linked to a valid reformation array.\\
                        If it is not, destroy it, link it to the pedestals again, and place it.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfuric_flux_emitter"))
                .build());

        return this.entry(location)
                .withIcon(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
                .withEntryBackground(EntryBackground.DEFAULT);
    }

}
