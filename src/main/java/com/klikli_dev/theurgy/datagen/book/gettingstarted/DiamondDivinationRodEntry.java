// SPDX-FileCopyrightText: 2023 klikli-dev
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class DiamondDivinationRodEntry extends EntryProvider {

    public static final String ENTRY_ID = "t3_divination_rod";

    public DiamondDivinationRodEntry(CategoryProvider parent) { super(parent); }

    @Override protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create().withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T3.get())).withText(this.context().pageText()));
        this.pageText("""
                A further improved attunable divination rod to locate high tier ores, such as {0}.
                """, this.itemLink("diamond", Items.DIAMOND_ORE));
        this.page("recipe", () -> BookCraftingRecipePageModel.create().withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t3")).withText(this.context().pageText()));
        this.pageText("""
                A further improved attunable divination rod, much more durable and able to detect most ores.
                """);
    }

    @Override protected String entryName() { return "The Diamond Divination Rod"; }
    @Override protected String entryDescription() { return "A superior-grade attunable rod for locating precious ores."; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.DIVINATION_ROD_T3.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
