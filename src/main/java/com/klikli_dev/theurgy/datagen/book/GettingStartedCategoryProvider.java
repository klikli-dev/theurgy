/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class GettingStartedCategoryProvider extends CategoryProvider {

    public GettingStartedCategoryProvider(BookProvider parent) {
        super(parent, "getting_started");
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__________________________________",
                "__________________________________",
                "____________u_____š_______________",
                "__________________________________",
                "__________i___d___s_______________",
                "__________________________________",
                "__________a_______________________",
                "__________________________________",
                "__________________________________"
        };
    }

    @Override
    protected void generateEntries() {
        var introEntry = this.add(this.makeIntroEntry('i'));
        var aboutModEntry = this.add(this.makeAboutModEntry('a'));
        aboutModEntry.withParent(introEntry);

        var divinationRodEntry = this.add(this.makeDivinationRodEntry('d'));
        divinationRodEntry.withParent(introEntry);
        //TODO: higher tier div rod entries explaining how they work
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Getting Started");

        return BookCategoryModel.create(
                        Theurgy.loc((this.context().categoryId())),
                        this.context().categoryName()
                )
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get());
    }


    private BookEntryModel makeIntroEntry(char icon) {
        this.context().entry("intro");
        this.add(this.context().entryName(), "About this Work");
        this.add(this.context().entryDescription(), "About using The Hermetica");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "About this Work");
        this.add(this.context().pageText(),
                """
                        The following pages will lead the novice alchemist on their journey through the noble art of the transformation of matter and mind. This humble author will share their experiences, thoughts and research notes to guide the valued reader in as safe a manner as the subject matter allows.
                        """);

        this.context().page("help");
        var help = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Seeking Counsel");
        this.add(this.context().pageText(),
                """
                        If the reader finds themselves in trouble of any kind, prompt assistance will be provided at the Council of Alchemists, known also as Kli Kli's Discord Server.
                        \\
                        \\
                        [To get help, join us at https://dsc.gg/klikli](https://discord.gg/trE4SHRXvb)
                        """);


        return BookEntryModel.create(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()), this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(0, 1)
                .withPages(
                        intro,
                        help
                );
    }

    private BookEntryModel makeAboutModEntry(char icon) {
        this.context().entry("about_mod");
        this.add(this.context().entryName(), "The Art of Alchemy");
        this.add(this.context().entryDescription(), "About this Mod");

        this.context().page("about");
        var about = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "The Art of Alchemy");
        this.add(this.context().pageText(),
                """
                        Welcome, dear reader, to Theurgy, a mod that explores the ancient and revered art of classical alchemy. As you embark on your journey through the noble art of transformation, you will be equipped with divination rods to make finding resources in the world easier.
                        """);
        this.context().page("about2");
        var about2 = BookTextPageModel.builder()
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        Through diligent study and practice, you will then learn to use alchemical devices to refine, replicate, and transform resources into new and useful materials. Along the way, you will have the opportunity to craft alchemical devices and equipment to aid you in your endeavors.
                        """);

        this.context().page("features");
        var features = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Features");
        this.add(this.context().pageText(),
                """
                        - Divination rods to find ores
                        - Future: Ore refining (= more ingots per ore)
                        - Future: Item replication (create duplicates of items you have)
                        - Future: Item transformation (create new items from other items)
                        """);

        this.context().page("features2");
        var features2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "More Features");
        this.add(this.context().pageText(),
                """
                        - Future: Weapons and Equipment
                        - Future: Devices to assist in common tasks
                        """);


        return BookEntryModel.create(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()), this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(Items.NETHER_STAR)
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(0, 0)
                .withPages(
                        about,
                        about2,
                        features,
                        features2
                );
    }

    private BookEntryModel makeDivinationRodEntry(char icon) {
        this.context().entry("divination_rod");
        this.add(this.context().entryName(), "Divination Rods");
        this.add(this.context().entryDescription(), "An Introduction to Ore-Finding");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        As a novice alchemist, it is important to familiarize yourself with the various tools and techniques at your disposal. One such tool is the divination rod, a valuable instrument used to locate hidden ores in the world.
                             """);

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                         By attuning your senses and your rod to the elemental energies present in the earth, you can detect the presence of ore deposits and guide yourself to their location. With practice, the use of divination rods can greatly aid you in your quest for the resources necessary for your alchemical pursuits.
                        """);

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t1"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The most basic tier of divination rods, brittle and limited in it's application, but powerful nonetheless.
                           """);

        this.context().page("supported_blocks");
        var supported_blocks = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Attunable Materials");
        this.add(this.context().pageText(),
                """
                        Rods can be attuned to a wide variety of useful blocks, including various types of ores and wood. Basic divination rods will be sufficient to locate common ores such as [iron](item://minecraft:iron_ore) or [coal](item://minecraft:coal_ore), but more rare and precious materials such as [diamonds](item://minecraft:diamond_ore) will require a higher tier rod to detect.
                         """);

        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        - **Shift-Click** a block to attune the rod to it.
                        - **Right-Click and hold** to let the rod search for blocks.
                        - **"Right-Click without holding**: after a successful search to let the rod show the last found block without consuming durability.
                        """);


        return BookEntryModel.create(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()), this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(0, 0)
                .withPages(
                        intro,
                        intro2,
                        recipe,
                        supported_blocks,
                        usage
                );
    }
}
