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

        var pyromanticBrazierEntry = this.add(this.makePyromanticBrazierEntry('b'));
        pyromanticBrazierEntry.withParent(principlesEntry);

        var calcinationOvenEntry = this.add(this.makeCalcinationOvenEntry('c'));
        calcinationOvenEntry.withParent(pyromanticBrazierEntry);

        var solventEntry = this.add(this.makeSolventsEntry('s'));
        solventEntry.withParent(pyromanticBrazierEntry);

        var liquefactionCauldronEntry = this.add(this.makeLiquefactionCauldronEntry('l'));
        liquefactionCauldronEntry.withParent(solventEntry);

        var distillerEntry = this.add(this.makeDistillerEntry('d'));
        distillerEntry.withParent(pyromanticBrazierEntry);

        var incubatorEntry = this.add(this.makeIncubatorEntry('r'));
        incubatorEntry.withParent(calcinationOvenEntry);
        incubatorEntry.withParent(liquefactionCauldronEntry);
        incubatorEntry.withParent(distillerEntry);

        var oreRefiningLinkEntry = this.add(this.makeOreRefiningLinkEntry('o'));
        oreRefiningLinkEntry.withParent(incubatorEntry);
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

    private BookEntryModel makePyromanticBrazierEntry(char location) {
        this.context().entry("pyromantic_brazier");
        this.add(this.context().entryName(), "Pyromantic Brazier");
        this.add(this.context().entryDescription(), "Heating your Alchemical Devices");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Pyromantic Brazier");
        this.add(this.context().pageText(),
                """
                        The {0} is a simple heating apparatus that can be used to power other Alchemical Devices. It is powered by burning furnace fuel, such as wood, coal, or charcoal.
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
                        Place the {0} below the Alchemical Device you want to power, then insert a fuel item by right-clicking the brazier with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert fuel items.
                        \\
                        \\
                        See also {1}.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/pyromantic_brazier"))
                .build();
        //no text

        return this.entry(location)
                .withIcon(ItemRegistry.PYROMANTIC_BRAZIER.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        usage,
                        recipe
                );
    }

    private BookEntryModel makeCalcinationOvenEntry(char location) {
        this.context().entry("calcination_oven");
        this.add(this.context().entryName(), "Calcination Oven");
        this.add(this.context().entryDescription(), "Making Salt");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Calcination Oven");
        this.add(this.context().pageText(),
                """
                        Calcination is the process whereby [#]($PURPLE)Alchemical Salt[#]() is extracted from matter. The {0} is a simple device that can be used to perform this process by applying consistent high heat to the target object.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        this.context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/calcination_oven"))
                .build();

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0} on top of a {1}, then insert the item to calcinate by right-clicking the oven with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.
                        \\
                        \\
                        See also {2}.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/calcination_oven"))
                .build();
        //no text

//        this.context().page("recipe_ore");
//        var recipeOre = BookCalcinationRecipePageModel.builder()
//                .withTitle1(this.context().pageTitle())
//                .withRecipeId1(Theurgy.loc("calcination/ore"))
//                .build();
//        this.add(this.context().pageTitle(), "Sample Recipe");
        //no text

        this.context().page("working");
        var working = BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/calcination_oven_working.png"))
                .build();
        this.add(this.context().pageTitle(), "Working Correctly");
        this.add(this.context().pageText(),
                """
                        If the {0} is working properly, it will show the orange rings moving slightly.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock,
                        usage,
                        recipe,
//                        recipeOre
                        working
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

    private BookEntryModel makeLiquefactionCauldronEntry(char location) {
        this.context().entry("liquefaction_cauldron");
        this.add(this.context().entryName(), "Liquefaction Cauldron");
        this.add(this.context().entryDescription(), "Making Sulfur");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Liquefaction Cauldron");
        this.add(this.context().pageText(),
                """
                        Liquefaction allows the extraction of [#]($PURPLE)Alchemical Sulfur[#]() from matter. In the {0} a [#]($PURPLE)Solvent[#](), usually a type of acid, is used to dissolve the target object, then the resulting solution is heated to evaporate the solvent and leave behind the Sulfur.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get())
        );

        this.context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/liquefaction_cauldron"))
                .build();

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0} on top of a {1}, and fill it with a Solvent by right-clicking with a solvent-filled bucket.
                        \\
                        \\
                        Then insert the item to liquefy by right-clicking the cauldron with it.
                        \\
                        \\
                        See also {2}.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/liquefaction_cauldron"))
                .build();
        //no text

//        this.context().page("sample_recipe");
//        var sampleRecipe = BookLiquefactionRecipePageModel.builder()
//                .withTitle1(this.context().pageTitle())
//                .withRecipeId1(Theurgy.loc("liquefaction/wheat"))
//                .build();
//        this.add(this.context().pageTitle(), "Sample Recipe");
//        //no text

        this.context().page("working");
        var working = BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/liquefaction_cauldron_working.png"))
                .build();
        this.add(this.context().pageTitle(), "Working Correctly");
        this.add(this.context().pageText(),
                """
                        If the {0} is working properly, it will show bubbles.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.LIQUEFACTION_CAULDRON.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock,
                        usage,
                        recipe,
//                        sampleRecipe
                        working
                );
    }

    private BookEntryModel makeDistillerEntry(char location) {
        this.context().entry("distiller");
        this.add(this.context().entryName(), "Distiller");
        this.add(this.context().entryDescription(), "Making Mercury");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Distiller");
        this.add(this.context().pageText(),
                """
                        Distillation allows to obtain purified [#]($PURPLE)Alchemical Mercury[#]() from matter. To this end the object is heated until it dissolves into a gaseous form and the resulting vapour is condensed into crystals. The Mercury obtained this way is stable and can be used in alchemical recipes.
                        """);
        //TODO: link to mercury energy stuff
        //TODO: Link to matter teleportation u sing mercury

        this.context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/distiller"))
                .build();

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0} on top of a {1}, then insert the item to distill by right-clicking the Distiller with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.
                        \\
                        \\
                        See also {2}.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/distiller"))
                .build();
        //no text

//        this.context().page("sample_recipe");
//        var sampleRecipe = BookDistillationRecipePageModel.builder()
//                .withTitle1(this.context().pageTitle())
//                .withRecipeId1(Theurgy.loc("distillation/stone"))
//                .build();
//        this.add(this.context().pageTitle(), "Sample Recipe");
//        //no text

        this.context().page("working");
        var working = BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/distiller_working.png"))
                .build();
        this.add(this.context().pageTitle(), "Working Correctly");
        this.add(this.context().pageText(),
                """
                        If the {0} is working properly, it will float with a bobbing motion.
                        """,
                this.itemLink(ItemRegistry.DISTILLER.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.DISTILLER.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock,
                        usage,
                        recipe,
//                        sampleRecipe
                        working
                );
    }

    private BookEntryModel makeIncubatorEntry(char location) {
        this.context().entry("incubator");
        this.add(this.context().entryName(), "Incubator");
        this.add(this.context().entryDescription(), "Making Matter");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Incubator");
        this.add(this.context().pageText(),
                """
                        Incubation is the process of *recombination* of the Principles of Matter into actual objects.\\
                        The Incubator has one vessel for each of the Principles, and a central chamber where the recombination takes place.
                        """);

        this.context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/incubator"))
                .build();

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Place the {0} on top of a {1} and one of each of the three vessels next to it. Insert the items to process by right-clicking the vessels with them.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process.\\
                        See also {2}.
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        this.context().page("recipe_incubator");
        var recipeIncubator = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator"))
                .build();
        //no text

        this.context().page("recipe_mercury_vessel");
        var recipeMercuryVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_mercury_vessel"))
                .build();
        //no text

        this.context().page("recipe_salt_vessel");
        var recipeSaltVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_salt_vessel"))
                .build();
        //no text

        this.context().page("recipe_sulfur_vessel");
        var recipeSulfurVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/incubator_sulfur_vessel"))
                .build();
        //no text

//        this.context().page("sample_recipe");
//        var sampleRecipe = BookIncubationRecipePageModel.builder()
//                .withTitle1(this.context().pageTitle())
//                .withRecipeId1(Theurgy.loc("incubation/wheat"))
//                .build();
//        this.add(this.context().pageTitle(), "Sample Recipe");
//        //no text

        this.context().page("working");
        var working = BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/incubator_working.png"))
                .build();
        this.add(this.context().pageTitle(), "Working Correctly");
        this.add(this.context().pageText(),
                """
                        If the {0} is working properly, it will show smoke.
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get())
        );

        return this.entry(location)
                .withIcon(ItemRegistry.INCUBATOR.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock,
                        usage,
                        recipeIncubator,
                        recipeMercuryVessel,
                        recipeSaltVessel,
                        recipeSulfurVessel,
//                        sampleRecipe
                        working
                );
    }

    private BookEntryModel makeOreRefiningLinkEntry(char location) {
        this.context().entry("about_ore_refining_link");
        this.add(this.context().entryName(), "Ore Refining");
        this.add(this.context().entryDescription(), "Return to the Getting Started Category to learn about Ore Refining");

        return this.entry(location).withIcon(Items.RAW_IRON)
                .withCategoryToOpen(Theurgy.loc(GettingStartedCategoryProvider.CATEGORY_ID))
                .withEntryBackground(EntryBackground.LINK_TO_CATEGORY);
    }
}
