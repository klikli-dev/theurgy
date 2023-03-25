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
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.LanguageProvider;

public class GettingStartedCategoryProvider implements MacroLangCategoryProvider {

    public static final String CATEGORY_ID = "getting_started";
    private LanguageProvider lang;

    public BookCategoryModel make(BookLangHelper helper, LanguageProvider lang) {
        this.lang = lang;
        helper.category(CATEGORY_ID);
        this.add(helper.categoryName(), "Getting Started");

        var entryHelper = ModonomiconAPI.get().getEntryLocationHelper();
        entryHelper.setMap(
                "__________________________________",
                "__________________________________",
                "____________u_____š_______________",
                "__________________________________",
                "__________i___d___s_______________",
                "__________________________________",
                "__________a_______________________",
                "__________________________________",
                "__________________________________"
        );

        var introEntry = this.makeIntroEntry(helper, entryHelper, 'i');
        var aboutModEntry = this.makeAboutModEntry(helper, entryHelper, 'a');
        aboutModEntry.withParent(introEntry);

        var divinationRodEntry = this.makeDivinationRodEntry(helper, entryHelper, 'd');
        divinationRodEntry.withParent(introEntry);
        //TODO: higer tier div rod entries explaining how they work

        var apparatusHowToEntry = this.makeApparatusHowToEntry(helper, entryHelper, 'u');
        apparatusHowToEntry.withParent(introEntry);

        var spagyricsEntry = this.makeSpagyricsEntry(helper, entryHelper, 's');
        spagyricsEntry.withParent(divinationRodEntry);

        var spagyricsLinkEntry = this.makeSpagyricsLinkEntry(helper, entryHelper, 'š');
        spagyricsLinkEntry.withParent(spagyricsEntry);

        return BookCategoryModel.create(
                        Theurgy.loc((helper.category)),
                        helper.categoryName()
                )
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get())
                .withEntries(
                        introEntry.build(),
                        aboutModEntry.build(),
                        divinationRodEntry.build(),
                        apparatusHowToEntry.build(),
                        spagyricsEntry.build(),
                        spagyricsLinkEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("intro");
        this.add(helper.entryName(), "About this Work");
        this.add(helper.entryDescription(), "About using The Hermetica");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "About this Work");
        this.add(helper.pageText(),
                """
                        The following pages will lead the novice alchemist on their journey through the noble art of the transformation of matter and mind. This humble author will share their experiences, thoughts and research notes to guide the valued reader in as safe a manner as the subject matter allows.
                        """);

        helper.page("help");
        var help = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Seeking Counsel");
        this.add(helper.pageText(),
                """
                        If the reader finds themselves in trouble of any kind, prompt assistance will be provided at the Council of Alchemists, known also as Kli Kli's Discord Server.
                        \\
                        \\
                        [To get help, join us at https://invite.gg/klikli](https://invite.gg/klikli)
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        help
                );
    }

    private BookEntryModel.Builder makeAboutModEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("about_mod");
        this.add(helper.entryName(), "The Art of Alchemy");
        this.add(helper.entryDescription(), "About this Mod");

        helper.page("about");
        var about = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "The Art of Alchemy");
        this.add(helper.pageText(),
                """
                        Welcome, dear reader, to Theurgy, a mod that explores the ancient and revered art of classical alchemy. As you embark on your journey through the noble art of transformation, you will be equipped with divination rods to make finding resources in the world easier.
                        """);
        helper.page("about2");
        var about2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                        Through diligent study and practice, you will then learn to use alchemical devices to refine, replicate, and transform resources into new and useful materials. Along the way, you will have the opportunity to craft alchemical devices and equipment to aid you in your endeavors.
                        """);

        helper.page("about3");
        var about3 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                        As a final note, alchemists are guided by reason and logic, not superstition or magic. Our experiments are based on careful observation, meticulous record-keeping, and rigorous testing. We do not claim to possess supernatural powers, but rather seek to harness the natural forces of the world around us to achieve our goals.
                        """);

        helper.page("features");
        var features = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Features");
        this.add(helper.pageText(),
                """
                        - Divination rods to find ores
                        - Future: Ore refining (= more ingots per ore)
                        - Future: Item replication (create duplicates of items you have)
                        - Future: Item transformation (create new items from other items)
                        """);

        helper.page("features2");
        var features2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "More Features");
        this.add(helper.pageText(),
                """
                        - Future: Weapons and Equipment
                        - Future: Devices to assist in common tasks
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(Items.NETHER_STAR)
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        about,
                        about2,
                        about3,
                        features,
                        features2
                );
    }

    private BookEntryModel.Builder makeDivinationRodEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("divination_rod");
        this.add(helper.entryName(), "Divination Rods");
        this.add(helper.entryDescription(), "An Introduction to Ore-Finding");

        helper.page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withText(helper.pageText())
                .build();

        this.add(helper.pageText(),
                """
                        As a novice alchemist, it is important to familiarize yourself with the various tools and techniques at your disposal. One such tool is the divination rod, a valuable instrument used to locate hidden ores in the world.
                             """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                         By attuning your senses and your rod to the elemental energies present in the earth, you can detect the presence of ore deposits and guide yourself to their location. With practice, the use of divination rods can greatly aid you in your quest for the resources necessary for your alchemical pursuits.
                        """);

        helper.page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t1"))
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                        The most basic tier of divination rods, brittle and limited in it's application, but powerful nonetheless.
                           """);

        helper.page("supported_blocks");
        var supported_blocks = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Attunable Materials");
        this.add(helper.pageText(),
                """
                        Rods can be attuned to a wide variety of useful blocks, including various types of ores and wood. Basic divination rods will be sufficient to locate common ores such as [iron](item://minecraft:iron_ore) or [coal](item://minecraft:coal_ore), but more rare and precious materials such as [diamonds](item://minecraft:diamond_ore) will require a higher tier rod to detect.
                         """);

        helper.page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Usage");
        this.add(helper.pageText(),
                """
                        - **Shift-Click** a block to attune the rod to it.
                        - **Right-Click and hold** to let the rod search for blocks.
                        - **"Right-Click without holding**: after a successful search to let the rod show the last found block without consuming durability.
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2,
                        recipe,
                        supported_blocks,
                        usage
                );
    }

    private BookEntryModel.Builder makeApparatusHowToEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("apparatus_how_to");
        this.add(helper.entryName(), "Alchemical Apparatus");
        this.add(helper.entryDescription(), "How to interact with the tools of the trade");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Alchemical Apparatus");
        this.add(helper.pageText(),
                """
                        Alchemist use a variety of tools and devices to aid them in their work. These devices are collectively referred to as apparatus.
                        \\
                        \\
                        It is important to understand that each apparatus should only have one specific function, such as generating heat or melting items.
                                 """);

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                        By adhering to this principle, we can create a modular system that allows for greater flexibility and efficiency in our work.
                        \\
                        \\
                        Further, all apparatus follow a standardized interaction pattern that makes it easier to use them both for manual interactions and for automation.
                                 """);

        helper.page("manual_interaction");
        var manualInteraction = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Manual Interaction");
        this.add(helper.pageText(),
                """
                        To interact with an apparatus, approach it and right-click on it.
                        \\
                        \\
                        **Taking Output Items**\\
                        If you have an empty hand, the machine will first try to take the contents of its output slot and place them in your inventory.
                                    """);

        helper.page("manual_interaction2");
        var manualInteraction2 = BookTextPageModel.builder()
                .withText(helper.pageText())
                .build();
        this.add(helper.pageText(),
                """
                        **Taking Input Items**\\
                        If there are no output items, it will instead try to place the contents of its input slot into your inventory, effectively emptying it.
                        \\
                        \\
                        **Inserting Items**\\
                        If you have an item in your hand, the apparatus will automatically try to insert it into the input slot.
                                    """);

        helper.page("fluid_interaction");
        var fluidInteraction = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Fluids");
        this.add(helper.pageText(),
                """
                        If you click on an apparatus with a filled fluid container in your hand, it will try to empty the container into the device.
                        \\
                        \\
                        If you click on an apparatus with an empty fluid container in your hand, it will instead try to fill the container from the device.
                                    """);

        helper.page("automatic_interaction");
        var automaticInteraction = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();

        this.add(helper.pageTitle(), "Automatic Interaction");
        this.add(helper.pageText(),
                """
                        Automatic interactions also use a standardized pattern.
                        \\
                        \\
                        **Input** slots can be accessed from the **top**, while **output** slots are available at the **bottom**.\\
                        \\
                        A **combined inventory** can be found at the horizontal **sides**.
                                  """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(BlockRegistry.PYROMANTIC_BRAZIER.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2,
                        manualInteraction,
                        manualInteraction2,
                        fluidInteraction,
                        automaticInteraction
                );
    }

    private BookEntryModel.Builder makeSpagyricsEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("spagyrics");
        this.add(helper.entryName(), "Spagyrics");
        this.add(helper.entryDescription(), "Mastery over Matter");

        helper.page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Spagyrics");
        this.add(helper.pageText(),
                """
                        While divination rods are a useful tool to obtain *more* materials, they rely on the natural abundance of such materials.
                        \\
                        \\
                        Spagyrics pursue the goal of *creating* materials out of other, possibly more abundant, materials.""");

        helper.page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(helper.pageTitle())
                .withText(helper.pageText())
                .build();
        this.add(helper.pageTitle(), "Learn More");
        this.add(helper.pageText(),
                """
                        Open the Spagyrics Category to learn more about the various required alchemical processes.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(BlockRegistry.CALCINATION_OVEN.get())
                .withLocation(entryHelper.get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2
                );
    }

    private BookEntryModel.Builder makeSpagyricsLinkEntry(BookLangHelper helper, EntryLocationHelper entryHelper, char icon) {
        helper.entry("spagyrics_link");
        this.add(helper.entryName(), "Spagyrics");
        this.add(helper.entryDescription(), "View the Spagyrics Category");

        return BookEntryModel.builder()
                .withId(Theurgy.loc(helper.category + "/" + helper.entry))
                .withName(helper.entryName())
                .withDescription(helper.entryDescription())
                .withIcon(BlockRegistry.CALCINATION_OVEN.get())
                .withLocation(entryHelper.get(icon))
                .withCategoryToOpen(Theurgy.loc(SpagyricsCategoryProvider.CATEGORY_ID))
                .withEntryBackground(EntryBackground.LINK_TO_CATEGORY);
    }

    @Override
    public LanguageProvider getLanguageProvider() {
        return this.lang;
    }
}
