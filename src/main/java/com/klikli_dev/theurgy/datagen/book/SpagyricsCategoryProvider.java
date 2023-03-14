/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.ModonomiconAPI;
import com.klikli_dev.modonomicon.api.datagen.BookLangHelper;
import com.klikli_dev.modonomicon.api.datagen.EntryLocationHelper;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraftforge.common.data.LanguageProvider;

public class SpagyricsCategoryProvider implements MacroLangCategoryProvider {

    public static final String CATEGORY_ID = "spagyrics";
    private LanguageProvider lang;

    public SpagyricsCategoryProvider() {
        this.registerDefaultMacros();
    }

    public BookCategoryModel.Builder make(BookLangHelper helper, LanguageProvider lang) {
        this.setLanguageProvider(lang);

        helper.category(CATEGORY_ID);
        this.add(helper.categoryName(), "Spagyrics");

        var entryHelper = ModonomiconAPI.get().getEntryLocationHelper();
        entryHelper.setMap(
                "__________________________________",
                "__________________________________",
                "________________c_________________",
                "__________________________________",
                "__________i_p_b___l_r_____________",
                "__________________________________",
                "________________d_________________",
                "__________________________________",
                "__________________________________"
        );

        var introEntry = this.makeIntroEntry(helper, entryHelper, 'i');
        var principlesEntry = this.makePrinciplesEntry(helper, entryHelper, 'p');
        principlesEntry.withParent(introEntry);

        var pyromanticBrazierEntry = this.makePyromanticBrazierEntry(helper, entryHelper, 'b');
        pyromanticBrazierEntry.withParent(principlesEntry);

        var calcinationOvenEntry = this.makeCalcinationOvenEntry(helper, entryHelper, 'c');
        calcinationOvenEntry.withParent(pyromanticBrazierEntry);

        var liquefactionCauldronEntry = this.makeLiquefactionCauldronEntry(helper, entryHelper, 'l');
        liquefactionCauldronEntry.withParent(pyromanticBrazierEntry);

        var distillerEntry = this.makeDistillerEntry(helper, entryHelper, 'd');
        distillerEntry.withParent(pyromanticBrazierEntry);

        var incubatorEntry = this.makeIncubatorEntry(helper, entryHelper, 'r');
        incubatorEntry.withParent(calcinationOvenEntry);
        incubatorEntry.withParent(liquefactionCauldronEntry);
        incubatorEntry.withParent(distillerEntry);

        return BookCategoryModel.builder()
                .withId(Theurgy.loc((helper.category)))
                .withName(helper.categoryName())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withEntries(
                        introEntry.build(),
                        principlesEntry.build(),
                        pyromanticBrazierEntry.build(),
                        calcinationOvenEntry.build(),
                        liquefactionCauldronEntry.build(),
                        distillerEntry.build(),
                        incubatorEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        this.add(helper.entryName(), "Spagyrics");
        this.add(helper.entryDescription(), "Power over the Three Principles");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Spagyrics");
        this.add(helper.pageText(),
                """
                        Spagyrics is derived from Greek for "to separate and reunite". As such, it is the process of separating, purifying and recombining the *three principles*, or "elements", of matter: Alchemical **Salt**, **Sulfur** and **Mercury**.
                        """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Benefits");
        this.add(helper.pageText(),
                """
                        The inquisitive mind may ask: "Why would one want to do that?". The answer lies in the promise of total control over all aspects of matter, including the ability to create any type of matter from any other type.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        intro2
                );
    }

    private BookEntryModel.Builder makePrinciplesEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("principles");
        this.add(helper.entryName(), "The Three Principles");
        this.add(helper.entryDescription(), "An Introduction to Alchemical Elements");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "The Three Principles");
        this.add(helper.pageText(),
                """
                        The [#]($PURPLE)Principles[#](), or Essentials, are the three basic elements all things are made of.
                        \\
                        \\
                        Despite the name, they are unrelated to the common materials often associated with these words, such as table salt, metallic mercury and the mineral sulfur.
                        """);

        helper.page("salt");
        var salt = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Salt");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Salt[#]() is the principle representing the **Body** of a thing. It provides the matrix wherein Sulfur and Mercury can act. As such it is associated with materiality, stability and manifestation in the physical world.
                        """);

        helper.page("sulfur");
        var sulfur = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Sulfur");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Sulfur[#]() is the **Soul** of a thing. It represents the unique properties of a piece of matter, such as how it will look, feel, and how it interacts with other things.
                        \\
                        \\
                        Transforming the Sulfur of one thing is the underlying idea of *transmutation*.
                        """);

        helper.page("mercury");
        var mercury = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Mercury");
        this.add(helper.pageText(),
                """
                        [#]($PURPLE)Alchemical Mercury[#]() is the **Energy** or Life Force of a thing. It is the most elusive of the three principles, and enables the other two principles to function.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.MERCURY_CRYSTAL.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        salt,
                        sulfur,
                        mercury
                );
    }

    private BookEntryModel.Builder makePyromanticBrazierEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("pyromantic_brazier");
        this.add(helper.entryName(), "Pyromantic Brazier");
        this.add(helper.entryDescription(), "Heating your Alchemical Devices");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Pyromantic Brazier");
        this.add(helper.pageText(),
                """
                        The Pyromantic Brazier is a simple heating apparatus that can be used to power other Alchemical Devices. It is powered by burning furnace fuel, such as wood, coal, or charcoal.
                        """);


        helper.page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Usage");
        this.add(helper.pageText(),
                """
                        Place the brazier below the Alchemical Device you want to power, then insert a fuel item by right-clicking the brazier with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert fuel items.
                        \\
                        \\
                        See also {0}.
                        """,
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        helper.page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/pyromantic_brazier"))
                .build();
        //no text

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.PYROMANTIC_BRAZIER.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        usage,
                        recipe
                );
    }

    private BookEntryModel.Builder makeCalcinationOvenEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("calcination_oven");
        this.add(helper.entryName(), "Calcination Oven");
        this.add(helper.entryDescription(), "Making Salt");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Calcination Oven");
        this.add(helper.pageText(),
                """
                        Calcination is the process whereby Alchemical Salt is extracted from matter. The Calcination Oven is a simple device that can be used to perform this process by applying consistent high heat to the target object.
                        """);

        //multiblock page
        helper.page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/calcination_oven"))
                .build();

        helper.page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Usage");
        this.add(helper.pageText(),
                """
                        Place the oven on top of a Pyromantic Brazier, then insert the item to calcinate by right-clicking the oven with it.
                        \\
                        \\
                        Alternatively a hopper can be used to insert items to process
                        \\
                        \\
                        See also {0}.
                        """,
                this.entryLink("Alchemical Apparatus", GettingStartedCategoryProvider.CATEGORY_ID, "apparatus_how_to")
        );

        helper.page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/calcination_oven"))
                .build();
        //no text

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.CALCINATION_OVEN.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock,
                        usage,
                        recipe
                );
    }

    private BookEntryModel.Builder makeLiquefactionCauldronEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("liquefaction_cauldron");
        this.add(helper.entryName(), "Liquefaction Cauldron");
        this.add(helper.entryDescription(), "Making Sulfur");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Liquefaction Cauldron");
        this.add(helper.pageText(),
                """
                        """);

        //intro page
        //multiblock page
        helper.page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/liquefaction_cauldron"))
                .build();

        //usage page

        helper.page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/liquefaction_cauldron"))
                .build();
        //no text

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.LIQUEFACTION_CAULDRON.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock
                );
    }

    private BookEntryModel.Builder makeDistillerEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("distiller");
        this.add(helper.entryName(), "Distiller");
        this.add(helper.entryDescription(), "Making Mercury");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Distiller");
        this.add(helper.pageText(),
                """
                        """);

        //intro page
        //multiblock page
        helper.page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/distiller"))
                .build();

        //usage page
        helper.page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/distiller"))
                .build();
        //no text

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.DISTILLER.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock
                );
    }

    private BookEntryModel.Builder makeIncubatorEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("incubator");
        this.add(helper.entryName(), "Incubator");
        this.add(helper.entryDescription(), "Making Matter");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Incubator");
        this.add(helper.pageText(),
                """
                        """);

        //intro page
        //multiblock page
        helper.page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/incubator"))
                .build();

        //usage page
        //block recipe pages

        helper.page("recipe_incubator");
        var recipeIncubator = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/incubator"))
                .build();
        //no text

        helper.page("recipe_mercury_vessel");
        var recipeMercuryVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/incubator_mercury_vessel"))
                .build();
        //no text

        helper.page("recipe_salt_vessel");
        var recipeSaltVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/incubator_salt_vessel"))
                .build();
        //no text

        helper.page("recipe_sulfur_vessel");
        var recipeSulfurVessel = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("shaped/incubator_sulfur_vessel"))
                .build();
        //no text

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.INCUBATOR.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        multiblock
                );
    }

    @Override
    public LanguageProvider getLanguageProvider() {
        return this.lang;
    }

    @Override
    public void setLanguageProvider(LanguageProvider languageProvider) {
        this.lang = languageProvider;
    }
}
