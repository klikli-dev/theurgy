// SPDX-FileCopyrightText: 2023 klikli-dev
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class GlassDivinationRodEntry extends EntryProvider {

    public static final String ENTRY_ID = "t1_divination_rod";

    public GlassDivinationRodEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get()))
                .withText(this.context().pageText()));

        this.pageText("""
                This divination rod is the most basic variant of attunable rods. It does not require Alchemical Sulfur and can be attuned to a variety of blocks by using it on them, even after it has previously been attuned. However, it has a lower durability than pre-attuned rods.
                """);

        this.page("supported_blocks", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Attunable Materials");
        this.pageText("""
                Rods can be attuned to a wide variety of useful blocks, including various types of ores and wood. Basic divination rods will be sufficient to locate common ores such as {0} or {1}, but more rare and precious materials such as {2} and {3} will require a higher tier rod to detect.
                """,
                this.itemLink("iron", Items.IRON_ORE),
                this.itemLink("coal", Items.COAL_ORE),
                this.itemLink("gold", Items.GOLD_ORE),
                this.itemLink("diamonds", Items.DIAMOND_ORE));

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t1"))
                .withText(this.context().pageText()));
        this.pageText("""
                The most basic tier of divination rods, brittle and limited in it's application, but powerful nonetheless.
                """);
    }

    @Override
    protected String entryName() {
        return "The Glass Divination Rod";
    }

    @Override
    protected String entryDescription() {
        return "A basic attunable rod for locating abundant and common ores.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.DIVINATION_ROD_T1.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
