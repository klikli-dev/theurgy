// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.page.accumulation.BookAccumulationRecipePageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class SpagyricsCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "spagyrics";

    public SpagyricsCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "________________c_________________",
                "__________________________________",
                "__________i_p_b___s_l_r_o_________",
                "__________________________________",
                "________________d_________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {

        var introEntry = this.add(this.makeIntroEntry('i'));
        var principlesEntry = this.add(this.makePrinciplesEntry('p'));
        principlesEntry.withParent(introEntry);

//        var solventEntry = this.add(this.makeSolventsEntry('s'));
//        solventEntry.withParent(pyromanticBrazierEntry);
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Spagyrics");

        return BookCategoryModel.create(
                        Theurgy.loc(this.context().categoryId()),
                        this.context().categoryName()
                )
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky2.png"))
                .withIcon(ItemRegistry.CALCINATION_OVEN.get());
    }


    private BookEntryModel makeIntroEntry(char location) {
        this.context().entry("intro");
        this.add(this.context().entryName(), "Spagyrics");
        this.add(this.context().entryDescription(), "Power over the Three Principles");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Spagyrics");
        this.add(this.context().pageText(),
                """
                        Spagyrics is derived from Greek for "to separate and reunite". As such, it is the process of separating, purifying and recombining the *three principles*, or "elements", of matter: Alchemical **Salt**, **Sulfur** and **Mercury**.
                        """);

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Benefits");
        this.add(this.context().pageText(),
                """
                        The inquisitive mind may ask: "Why would one want to do that?". The answer lies in the promise of total control over all aspects of matter, including the ability to create any type of matter from any other type.
                        """);

        return this.entry(location)
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        intro2
                );
    }

    private BookEntryModel makePrinciplesEntry(char location) {
        this.context().entry("principles");
        this.add(this.context().entryName(), "The Three Principles");
        this.add(this.context().entryDescription(), "An Introduction to Alchemical Elements");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Three Principles");
        this.add(this.context().pageText(),
                """
                        The [#]($PURPLE)Principles[#](), or Essentials, are the three basic elements all things are made of.
                        \\
                        \\
                        Despite the name, they are unrelated to the common materials often associated with these words, such as table salt, metallic mercury and the mineral sulfur.
                        """);

        this.context().page("salt");
        var salt = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Alchemical Salt");
        this.add(this.context().pageText(),
                """
                        [#]($PURPLE)Alchemical Salt[#]() is the principle representing the **Body** of a thing. It provides the matrix wherein Sulfur and Mercury can act. As such it is associated with materiality, stability and manifestation in the physical world.
                        """);

        this.context().page("sulfur");
        var sulfur = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Alchemical Sulfur");
        this.add(this.context().pageText(),
                """
                        [#]($PURPLE)Alchemical Sulfur[#]() is the **Soul** of a thing. It represents the unique properties of a piece of matter, such as how it will look, feel, and how it interacts with other things.
                        \\
                        \\
                        Transforming the Sulfur of one thing is the underlying idea of *transmutation*.
                        """);

        this.context().page("mercury");
        var mercury = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Alchemical Mercury");
        this.add(this.context().pageText(),
                """
                        [#]($PURPLE)Alchemical Mercury[#]() is the **Energy** or Life Force of a thing. It is the most elusive of the three principles, and enables the other two principles to function.
                        """);

        return this.entry(location)
                .withIcon(ItemRegistry.MERCURY_CRYSTAL.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        salt,
                        sulfur,
                        mercury
                );
    }

    private BookEntryModel makeSolventsEntry(char location) {
        //TODO: this entry, minus crafting of the accumulator and tank will go into the spagyrics category
        this.context().entry("solvents");
        this.add(this.context().entryName(), "Solvents");
        this.add(this.context().entryDescription(), "Solving all your problems?");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_BUCKET.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Solvents");
        this.add(this.context().pageText(),
                """
                        Solvents are required for the process of Liquefaction, by which [#]($PURPLE)Alchemical Sulfur[#]() is extracted from matter. Usually they are a type of acid. The following solvents are available:
                        - Sal Ammoniac
                        - Alkahest *(not yet implemented)*
                        """);
        //TODO: Update entry once alkahest is available

        this.context().page("crafting");
        var crafting = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Crafting");
        this.add(this.context().pageText(),
                """
                        Sal Ammoniac is crafted in a {0}. It has two modes of operation: It can increase the concentration of naturally occurring Sal Ammoniac in water to a usable level via evaporation, which is a rather slow and inefficient process, or it can enrich water with {1} to produce a usable solvent much quicker.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink("Sal Ammoniac Crystals", ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/sal_ammoniac_accumulator"))
                .build();

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0} on top of a {1}, and fill it with a Water by right-clicking with a water bucket.
                        \\
                        \\
                        Optionally insert a {2} by right-clicking the cauldron with it to speed up the process.
                        \\
                        \\
                        See also {3}.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe1");
        var recipe1 = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sal_ammoniac_accumulator"))
                .build();
        //no text

        this.context().page("recipe2");
        var recipe2 = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sal_ammoniac_tank"))
                .build();
        //no text

        this.context().page("working");
        var working = BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/sal_ammoniac_accumulator_working.png"))
                .build();
        this.add(this.context().pageTitle(), "Working Correctly");
        this.add(this.context().pageText(),
                """
                        If working properly, it will show blue or yellow (if using Sal Ammoniac Crystals) bubbles.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get())
        );

        this.context().page("sal_ammoniac_crystal");
        var crystal = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get()))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The crystals can be obtained by mining {0}.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ORE.get())
        );

        this.context().page("sal_ammoniac_fluid_recipe");
        var salAmmoniacFluidRecipe = BookAccumulationRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("accumulation/sal_ammoniac_from_water"))
                .withRecipeId2(Theurgy.loc("accumulation/sal_ammoniac_from_water_and_sal_ammoniac_crystal"))
                .withTitle2(this.context().pageTitle() + ".2")
                .build();
        this.add(this.context().pageTitle() + ".2", "... using Crystal");
        //no text

        return this.entry(location)
                .withIcon(ItemRegistry.SAL_AMMONIAC_TANK.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        crafting,
                        multiblock,
                        usage,
                        recipe1,
                        recipe2,
                        working,
                        crystal,
                        salAmmoniacFluidRecipe
                );
    }

}
