// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
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

/**
 * A dummy provider to separate the generation of divination rod entries from the rest of the getting started category
 */
public class DivinationRodEntryProvider extends CategoryProvider {


    public DivinationRodEntryProvider(TheurgyBookProvider parent, CategoryEntryMap entryMap) {
        super(parent, "dummy");

        this.entryMap = entryMap;
    }

    public TheurgyBookProvider parent() {
        return (TheurgyBookProvider) this.parent;
    }


    @Override
    protected String[] generateEntryMap() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    @Override
    protected void generateEntries() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    @Override
    protected BookCategoryModel generateCategory() {
        throw new UnsupportedOperationException("This is a dummy provider to help generate entries, it should not be used to generate a Category.");
    }

    public BookEntryModel aboutDivinationRods(char location) {
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
                            """, this.entryLink("Spagyrics", GettingStartedCategoryProvider.CATEGORY_ID, "spagyrics"));


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


        return this.entry(location)
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        intro2,
                        sulfurAttunedRods,
                        usage,
                        usage2
                );
    }

    public BookEntryModel t1DivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.DIVINATION_ROD_T1.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        supportedBlocks,
                        recipe
                );
    }

    public BookEntryModel abundantAndCommonSulfurAttunedDivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        sulfur,
                        recipe_abundant,
                        recipe_common
                );
    }

    public BookEntryModel amethystDivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.AMETHYST_DIVINATION_ROD.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    public BookEntryModel t2DivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.DIVINATION_ROD_T2.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    public BookEntryModel t3DivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.DIVINATION_ROD_T3.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    public BookEntryModel t4DivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.DIVINATION_ROD_T4.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    public BookEntryModel rareSulfurAttunedDivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }

    public BookEntryModel preciousSulfurAttunedDivinationRodEntry(char location) {
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

        return this.entry(location)
                .withIcon(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get())
                .withEntryBackground(EntryBackground.DEFAULT)
                .withPages(
                        intro,
                        recipe
                );
    }
}