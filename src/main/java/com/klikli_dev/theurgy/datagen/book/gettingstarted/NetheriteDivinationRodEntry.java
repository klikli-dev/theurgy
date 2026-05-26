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

public class NetheriteDivinationRodEntry extends EntryProvider {

    public static final String ENTRY_ID = "t4_divination_rod";

    public NetheriteDivinationRodEntry(CategoryProvider parent) { super(parent); }

    @Override protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create().withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T4.get())).withText(this.context().pageText()));
        this.pageText("""
                The very high durability attunable divination rod to locate ores of any tier, such as {0}.
                """, this.itemLink("diamond", Items.DIAMOND_ORE));
        this.page("recipe", () -> BookCraftingRecipePageModel.create().withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t4")).withText(this.context().pageText()));
        this.pageText("""
                The highest tier attunable divination rod, incredibly durable and able to detect all ores.
                """);
    }

    @Override protected String entryName() { return "The Netherite Divination Rod"; }
    @Override protected String entryDescription() { return "A very high durability attunable rod for locating precious ores."; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.DIVINATION_ROD_T4.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
