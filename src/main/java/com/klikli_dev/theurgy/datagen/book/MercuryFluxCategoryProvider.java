// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryFluxCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "mercury_flux";

    public MercuryFluxCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "____________ć_____________________",
                "__________________________________",
                "__________i_c_____________________",
                "__________________________________",
                "____________s_____________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {
        var intro = this.add(this.intro('i'));
        var mercuryCatalyst = this.add(this.mercuryCatalyst('c'));
        var caloricFluxEmitter = this.add(this.caloricFluxEmitter('ć'));
        var sulfuricFluxEmitter = this.add(this.sulfuricFluxEmitter('s'));

        mercuryCatalyst.addParent(this.parent(intro));
        caloricFluxEmitter.addParent(this.parent(mercuryCatalyst));
        sulfuricFluxEmitter.addParent(this.parent(mercuryCatalyst));
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Mercury Flux");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"))
                .withIcon(ItemRegistry.MERCURY_SHARD.get());
    }


    private BookEntryModel intro(char location) {
        this.context().entry("intro");
        this.add(this.context().entryName(), "Mercury Flux");
        this.add(this.context().entryDescription(), "Raw Energy Manipulation");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Mercury Flux");
        this.add(this.context().pageText(),
                """
                        **Mercury** represents the Energy contained in all matter. The Spagyrics processes for Mercury extraction yield {0}. In this Form it is not immediately useful as an energy source, so it must first be transformed - catalyzed - into [#]($PURPLE)Mercury Flux[#](), which is Mercury in it's natural Form.
                        """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.MERCURY_SHARD.get())
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro
                );
    }

    private BookEntryModel mercuryCatalyst(char location) {
        this.context().entry("mercury_catalyst");
        this.add(this.context().entryName(), "Mercury Catalyst");
        this.add(this.context().entryDescription(), "Accessing Raw Energy");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_CATALYST.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        This apparatus slowly converts {0} into [#]($PURPLE)Mercury Flux[#](), which can be used as a source of Energy. It is the first step towards using the Energy contained in matter.
                        """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0}, then insert {1} by right-clicking the oven with it. The Catalyst will begin to slowly fill up with [#]($PURPLE)Mercury Flux[#]().
                        \\
                        \\
                        It's sides will turn more and more blue as it fills up.
                        """,
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercury_catalyst"))
                .build();

        return this.entry(location)
                .withIcon(ItemRegistry.MERCURY_CATALYST.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        usage,
                        recipe
                );
    }

    private BookEntryModel caloricFluxEmitter(char location) {
        this.context().entry("caloric_flux_emitter");
        this.add(this.context().entryName(), "Caloric Flux Emitter");
        this.add(this.context().entryDescription(), "Efficiently powering alchemical Apparatus");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.CALORIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        This devices converts raw mercury flux into [#]($PURPLE)Caloric Flux[#](), or simply: transferable heat. It can be used to power other alchemical apparatuses that would usually need a {0} below them.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Right-click the target block with the {0} until it is highlighted. Then place the Emitter onto a Mercury Flux source, such as a {1}.\\
                        The maximum range is **8** blocks.\\
                        As long as mercury flux is provided to it, the emitter will send caloric flux to the target block and keep it heated.
                        """,
                this.itemLink(ItemRegistry.CALORIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/caloric_flux_emitter_from_campfire"))
                .withRecipeId2(Theurgy.loc("crafting/shaped/caloric_flux_emitter_from_lava_bucket"))
                .build();

        return this.entry(location)
                .withIcon(ItemRegistry.CALORIC_FLUX_EMITTER.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        usage,
                        recipe
                );
    }

    private BookEntryModel sulfuricFluxEmitter(char location) {
        this.context().entry("sulfuric_flux_emitter");
        this.add(this.context().entryName(), "Sulfuric Flux Emitter");
        this.add(this.context().entryDescription(), "Transporting Sulfuric Information");

        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageText(),
                """
                        This devices converts raw mercury flux into [#]($PURPLE)Sulfuric Flux[#](), which allows to transport the information inherent in sulfur over a distance. It can be used to convert one type of matter into another.\\
                        See {0} for more information.
                        """,
                this.categoryLink("Reformation", ReformationCategoryProvider.CATEGORY_ID)
        );

        return this.entry(location)
                .withIcon(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
                .withEntryBackground(EntryBackground.DEFAULT);
    }
}
