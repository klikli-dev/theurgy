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
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.checkerframework.checker.units.qual.A;

public class GettingStartedCategoryProvider extends CategoryProvider {

    public static final String CATEGORY_ID = "getting_started";

    public GettingStartedCategoryProvider(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {

        return new String[]{
                "__________________________________",
                "__________________ḍ___ď_ḑ_ḓ_______",
                "__________________________________",
                "________________d___ḋ_____________",
                "______________________ɖ_ᶑ_________",
                "__________________đ_______________",
                "__________________________________",
                "__________i_a_____________________",
                "__________________________________",
                "______________s_š_________________",
                "__________________________________",
                "______________u___________________"
        };
    }

    @Override
    protected BookCategoryModel generateCategory() {
        this.add(this.context().categoryName(), "Getting Started");

        var introEntry = this.makeIntroEntry('i');
        var aboutModEntry = this.makeAboutModEntry('a');

        var aboutDivinationRods = this.makeAboutDivinationRodsEntry('d');
        var t1DivinationRod = this.makeT1DivinationRodEntry('ḍ');
        var abundantAndCommonSulfurAttunedDivinationRod = this.makeAbundantAndCommonSulfurAttunedDivinationRodEntry('đ');
        var amethystDivinationRod = this.makeAmethystDivinationRodEntry('ḋ');
        var t2DivinationRod = this.makeT2DivinationRodEntry('ď');
        var t3DivinationRod = this.makeT3DivinationRodEntry('ḑ');
        var t4DivinationRod = this.makeT4DivinationRodEntry('ḓ');
        var rareSulfurAttunedDivinationRod = this.makeRareSulfurAttunedDivinationRodEntry('ɖ');
        var preciousSulfurAttunedDivinationRod = this.makePreciousSulfurAttunedDivinationRodEntry('ᶑ');

        var spagyricsEntry = this.makeSpagyricsEntry('s');
        var apparatusHowToEntry = this.makeApparatusHowToEntry('u');
        var spagyricsLinkEntry = this.makeSpagyricsLinkEntry('š');


        aboutModEntry.withParent(introEntry);
        aboutDivinationRods.withParent(aboutModEntry);
        t1DivinationRod.withParent(aboutDivinationRods);
        abundantAndCommonSulfurAttunedDivinationRod.withParent(aboutDivinationRods);
        abundantAndCommonSulfurAttunedDivinationRod.withParent(spagyricsLinkEntry);
        amethystDivinationRod.withParent(t1DivinationRod);
        amethystDivinationRod.withParent(abundantAndCommonSulfurAttunedDivinationRod);
        t2DivinationRod.withParent(amethystDivinationRod);
        t3DivinationRod.withParent(t2DivinationRod);
        t4DivinationRod.withParent(t3DivinationRod);
        rareSulfurAttunedDivinationRod.withParent(amethystDivinationRod);
        preciousSulfurAttunedDivinationRod.withParent(rareSulfurAttunedDivinationRod);


        spagyricsEntry.withParent(aboutModEntry);
        apparatusHowToEntry.withParent(spagyricsEntry);
        spagyricsLinkEntry.withParent(spagyricsEntry);

        //TODO: Conditions
        //  amethyst entry should NOT depend on spagyrisc -> hence not on abundant sulfur rod


        return BookCategoryModel.create(
                        Theurgy.loc((this.context().categoryId())),
                        this.context().categoryName()
                )
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get())
                .withBackground(Theurgy.loc("textures/gui/book/bg_nightsky.png"))
                .withEntries(
                        introEntry.build(),
                        aboutModEntry.build(),
                        aboutDivinationRods.build(),
                        t1DivinationRod.build(),
                        abundantAndCommonSulfurAttunedDivinationRod.build(),
                        amethystDivinationRod.build(),
                        t2DivinationRod.build(),
                        t3DivinationRod.build(),
                        t4DivinationRod.build(),
                        rareSulfurAttunedDivinationRod.build(),
                        preciousSulfurAttunedDivinationRod.build(),
                        apparatusHowToEntry.build(),
                        spagyricsEntry.build(),
                        spagyricsLinkEntry.build()
                );
    }

    private BookEntryModel.Builder makeIntroEntry(char icon) {
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
                        [To get help, join us at https://invite.gg/klikli](https://invite.gg/klikli)
                        """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.THE_HERMETICA_ICON.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.CATEGORY_START)
                .withPages(
                        intro,
                        help
                );
    }

    private BookEntryModel.Builder makeAboutModEntry(char icon) {
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

        this.context().page("about3");
        var about3 = BookTextPageModel.builder()
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        As a final note, alchemists are guided by reason and logic, not superstition or magic. Our experiments are based on careful observation, meticulous record-keeping, and rigorous testing. We do not claim to possess supernatural powers, but rather seek to harness the natural forces of the world around us to achieve our goals.
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


        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(Items.NETHER_STAR)
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        about,
                        about2,
                        about3,
                        features,
                        features2
                );
    }

    private BookEntryModel.Builder makeAboutDivinationRodsEntry(char icon) {
        this.context().entry("about_divination_rods");
        this.add(this.context().entryName(), "About Divination Rods");
        this.add(this.context().entryDescription(), "An Introduction to Ore-Finding");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "About Divination Rods");
        this.add(this.context().pageText(),
                """
                        Divination Rods, also known as Dowsing Rods, are a valuable instrument used to locate ores and other valuable blocks. In order to show the location of a block, it must first be "attuned" to it.
                                 """);

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Rod Attunement");
        this.add(this.context().pageText(),
                """
                        There are two methods of attuning, depending on the type of rod: by *crafting* a rod with the []($PURPLE)alchemical sulfur[](#) of the desired block, or by *using* a rod on the desired block.
                        \\
                        \\
                        Attuning a rod to a block will cause it to point towards the nearest block of that type.
                            """);

        this.context().page("sulfur_attuned_rods");
        var sulfurAttunedRods = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Pre-Attuned Rods");
        this.add(this.context().pageText(),
                """
                        Pre-Attuned rods, those that are crafted with alchemical sulfur, are more stable, and thus have a much higher durability, but require more effort to craft.
                        \\
                        \\
                        Research {0} (specifically, *Liquefaction*) to continue on this path.
                            """, this.entryLink("Spagyrics", this.categoryId(), "spagyrics"));


        this.context().page("usage");
        var usage = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        - **Shift-Click** a block to attune the rod to it (unless it is pre-attuned).
                        - **Right-Click and hold** to let the rod search for blocks.
                        - **Right-Click without holding** after a successful search will let the rod show the last found block without consuming durability.
                        """);

        this.context().page("usage2");
        var usage2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        The rod will indicate that it found a block by *changing color* to be partially or fully purple, and by emitting a *glowing ball* that will fly towards the block, when right-clicked without holding.
                           """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2,
                        sulfurAttunedRods,
                        usage,
                        usage2
                );
    }

    private BookEntryModel.Builder makeT1DivinationRodEntry(char icon) {
        this.context().entry("t1_divination_rod");
        this.add(this.context().entryName(), "The Glass Divination Rod");
        this.add(this.context().entryDescription(), "A basic attunable rod for locating abundant and common ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        This divination rod is the most basic variant of attunable rods. It does not require Alchemical Sulfur and can be attuned to a variety of blocks by using it on them, even after it has previously been attuned. However, it has a lower durability than pre-attuned rods.
                                 """);

        this.context().page("supported_blocks");
        var supportedBlocks = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Attunable Materials");
        this.add(this.context().pageText(),
                """
                        Rods can be attuned to a wide variety of useful blocks, including various types of ores and wood. Basic divination rods will be sufficient to locate common ores such as {0} or {1}, but more rare and precious materials such as {2} and {3} will require a higher tier rod to detect.
                         """,
                this.itemLink("iron", Items.IRON_ORE),
                this.itemLink("coal", Items.COAL_ORE),
                this.itemLink("gold", Items.GOLD_ORE),
                this.itemLink("diamonds", Items.DIAMOND_ORE)
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t1"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The most basic tier of divination rods, brittle and limited in it's application, but powerful nonetheless.
                           """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        supportedBlocks,
                        recipe
                );
    }

    private BookEntryModel.Builder makeAbundantAndCommonSulfurAttunedDivinationRodEntry(char icon) {
        this.context().entry("abundant_and_common_sulfur_attuned_divination_rod");
        this.add(this.context().entryName(), "Basic Sulfur-Attuned Divination Rods");
        this.add(this.context().entryDescription(), "Pre-attuned rods for locating abundant and common ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get(), ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        This divination rod is the most basic variant of pre-attuned rod. While it cannot be attuned to a new block after crafting, it has a much higher durability, and is generally more convenient to use.
                                 """);

        this.context().page("sulfur");
        var sulfur = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Sulfur");
        this.add(this.context().pageText(),
                """
                        To obtain sulfur for crafting this rod you first need to obtain an ore, ingot or gem of the type of material you want the rod to be attuned to. Then, you need to obtain it''s sulfur by melting it down in a {0}.
                                 """, this.entryLink("Liquefaction Cauldron", "spagyrics", "liquefaction_cauldron"));


        this.context().page("recipe_abundant");
        var recipe_abundant = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_abundant"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A rod pre-attuned to abundant ores, such as {0} or {1}.
                               """, this.itemLink("copper", Items.COPPER_ORE), this.itemLink("coal", Items.COAL_ORE));

        this.context().page("recipe_common");
        var recipe_common = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_common"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A rod pre-attuned to common ores, such as {0} or {1}.
                               """, this.itemLink("iron", Items.IRON_ORE), this.itemLink("lapis", Items.LAPIS_ORE));

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        sulfur,
                        recipe_abundant,
                        recipe_common
                );
    }

    private BookEntryModel.Builder makeAmethystDivinationRodEntry(char icon) {
        this.context().entry("amethyst_divination_rod");
        this.add(this.context().entryName(), "Amethyst Divination Rod");
        this.add(this.context().entryDescription(), "A pre-attuned rod to find budding amethyst blocks.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.AMETHYST_DIVINATION_ROD.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        Crystals are especially useful for attuning divination rods, and many more advanced rod designs require {0} specifically. This rod is pre-attuned to locate budding amethyst blocks to make it easier to obtain these helpful crystals.
                        """, this.itemLink("amethyst shards", Items.AMETHYST_SHARD));

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/amethyst_divination_rod"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A pre-attuned rod that only finds {0}.
                               """, this.itemLink("amethyst", Items.BUDDING_AMETHYST));

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.AMETHYST_DIVINATION_ROD.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makeT2DivinationRodEntry(char icon) {
        this.context().entry("t2_divination_rod");
        this.add(this.context().entryName(), "The Iron Divination Rod");
        this.add(this.context().entryDescription(), "An improved attunable rod for locating rare ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T2.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        An improved attunable divination rod to locate higher tier ores, such as {0}.
                                 """,
                this.itemLink("gold", Items.GOLD_ORE)
                );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t2"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        An improved attunable divination rod, more durable and broader in it's application.
                           """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T2.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makeT3DivinationRodEntry(char icon) {
        this.context().entry("t3_divination_rod");
        this.add(this.context().entryName(), "The Diamond Divination Rod");
        this.add(this.context().entryDescription(), "A superior-grade attunable rod for locating precious ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T3.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        A further improved attunable divination rod to locate high tier ores, such as {0}.
                                 """,
                this.itemLink("diamond", Items.DIAMOND_ORE)
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t3"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A further improved attunable divination rod, much more durable and able to detect most ores.
                           """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T3.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makeT4DivinationRodEntry(char icon) {
        this.context().entry("t4_divination_rod");
        this.add(this.context().entryName(), "The Netherite Divination Rod");
        this.add(this.context().entryDescription(), "A very high durability attunable rod for locating precious ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T4.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        The very high durability attunable divination rod to locate ores of any tier, such as {0}.
                                 """,
                this.itemLink("diamond", Items.DIAMOND_ORE)
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t4"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        The highest tier attunable divination rod, incredibly durable and able to detect all ores.
                           """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.DIVINATION_ROD_T4.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makeRareSulfurAttunedDivinationRodEntry(char icon) {
        this.context().entry("rare_sulfur_attuned_divination_rod");
        this.add(this.context().entryName(), "Sulfur-Attuned Divination Rods for Rare Materials");
        this.add(this.context().entryDescription(), "An improved design of Sulfur-Attuned Divination rods, allowing to locate rare ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        This divination rod is an improved variant of pre-attuned rod that is more durable allows to locate rare ores, such as {0}.
                                 """,
                this.itemLink("gold", Items.GOLD_ORE)
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_rare"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A rod pre-attuned to rare ores, such as {0}.
                               """, this.itemLink("gold", Items.GOLD_ORE));

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makePreciousSulfurAttunedDivinationRodEntry(char icon) {
        this.context().entry("precious_sulfur_attuned_divination_rod");
        this.add(this.context().entryName(), "Sulfur-Attuned Divination Rods for Precious Materials");
        this.add(this.context().entryDescription(), "An intricately crafted Sulfur-Attuned Divination rod, allowing to locate precious ores.");

        this.context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()))
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageText(),
                """
                        This divination rod is the most powerful variant of pre-attuned rod, it is highly durable and allows to locate precious ores, such as {0}.
                                 """,
                this.itemLink("diamond", Items.DIAMOND_ORE)
        );

        this.context().page("recipe");
        var recipe = BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_precious"))
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        A rod pre-attuned to precious ores, such as {0}.
                               """, this.itemLink("diamond", Items.DIAMOND_ORE));

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    private BookEntryModel.Builder makeApparatusHowToEntry(char icon) {
        this.context().entry("apparatus_how_to");
        this.add(this.context().entryName(), "Alchemical Apparatus");
        this.add(this.context().entryDescription(), "How to interact with the tools of the trade");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Alchemical Apparatus");
        this.add(this.context().pageText(),
                """
                        Alchemist use a variety of tools and devices to aid them in their work. These devices are collectively referred to as apparatus.
                        \\
                        \\
                        It is important to understand that each apparatus should only have one specific function, such as generating heat or melting items.
                                 """);

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        By adhering to this principle, we can create a modular system that allows for greater flexibility and efficiency in our work.
                        \\
                        \\
                        Further, all apparatus follow a standardized interaction pattern that makes it easier to use them both for manual interactions and for automation.
                                 """);

        this.context().page("manual_interaction");
        var manualInteraction = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Manual Interaction");
        this.add(this.context().pageText(),
                """
                        To interact with an apparatus, approach it and right-click on it.
                        \\
                        \\
                        **Taking Output Items**\\
                        If you have an empty hand, the machine will first try to take the contents of its output slot and place them in your inventory.
                                    """);

        this.context().page("manual_interaction2");
        var manualInteraction2 = BookTextPageModel.builder()
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageText(),
                """
                        **Taking Input Items**\\
                        If there are no output items, it will instead try to place the contents of its input slot into your inventory, effectively emptying it.
                        \\
                        \\
                        **Inserting Items**\\
                        If you have an item in your hand, the apparatus will automatically try to insert it into the input slot.
                                    """);

        this.context().page("fluid_interaction");
        var fluidInteraction = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Fluids");
        this.add(this.context().pageText(),
                """
                        If you click on an apparatus with a filled fluid container in your hand, it will try to empty the container into the device.
                        \\
                        \\
                        If you click on an apparatus with an empty fluid container in your hand, it will instead try to fill the container from the device.
                                    """);

        this.context().page("automatic_interaction");
        var automaticInteraction = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();

        this.add(this.context().pageTitle(), "Automatic Interaction");
        this.add(this.context().pageText(),
                """
                        Automatic interactions also use a standardized pattern.
                        \\
                        \\
                        **Input** slots can be accessed from the **top**, while **output** slots are available at the **bottom**.\\
                        \\
                        A **combined inventory** can be found at the horizontal **sides**.
                                  """);


        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(BlockRegistry.PYROMANTIC_BRAZIER.get())
                .withLocation(this.entryMap().get(icon))
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

    private BookEntryModel.Builder makeSpagyricsEntry(char icon) {
        this.context().entry("spagyrics");
        this.add(this.context().entryName(), "Spagyrics");
        this.add(this.context().entryDescription(), "Mastery over Matter");

        this.context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Spagyrics");
        this.add(this.context().pageText(),
                """
                        While divination rods are a useful tool to obtain *more* materials, they rely on the natural abundance of such materials.
                        \\
                        \\
                        Spagyrics pursue the goal of *creating* materials out of other, possibly more abundant, materials.""");

        this.context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build();
        this.add(this.context().pageTitle(), "Learn More");
        this.add(this.context().pageText(),
                """
                        Open the Spagyrics Category to learn more about the various required alchemical processes.
                        """);

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(BlockRegistry.CALCINATION_OVEN.get())
                .withLocation(this.entryMap().get(icon))
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2
                );
    }

    private BookEntryModel.Builder makeSpagyricsLinkEntry(char icon) {
        this.context().entry("spagyrics_link");
        this.add(this.context().entryName(), "Spagyrics");
        this.add(this.context().entryDescription(), "View the Spagyrics Category");

        return BookEntryModel.builder()
                .withId(Theurgy.loc(this.context().categoryId() + "/" + this.context().entryId()))
                .withName(this.context().entryName())
                .withDescription(this.context().entryDescription())
                .withIcon(BlockRegistry.CALCINATION_OVEN.get())
                .withLocation(this.entryMap().get(icon))
                .withCategoryToOpen(Theurgy.loc(SpagyricsCategoryProvider.CATEGORY_ID))
                .withEntryBackground(EntryBackground.LINK_TO_CATEGORY);
    }


}
