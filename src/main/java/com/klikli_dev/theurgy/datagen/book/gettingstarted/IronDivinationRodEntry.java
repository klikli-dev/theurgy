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

public class IronDivinationRodEntry extends EntryProvider {

    public static final String ENTRY_ID = "t2_divination_rod";

    public IronDivinationRodEntry(CategoryProvider parent) { super(parent); }

    @Override protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create().withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T2.get())).withText(this.context().pageText()));
        this.pageText("""
                An improved attunable divination rod to locate higher tier ores, such as {0}.
                """, this.itemLink("gold", Items.GOLD_ORE));
        this.page("recipe", () -> BookCraftingRecipePageModel.create().withRecipeId1(Theurgy.loc("crafting/shaped/divination_rod_t2")).withText(this.context().pageText()));
        this.pageText("""
                An improved attunable divination rod, more durable and broader in it's application.
                """);
    }

    @Override protected String entryName() { return "The Iron Divination Rod"; }
    @Override protected String entryDescription() { return "An improved attunable rod for locating rare ores."; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.DIVINATION_ROD_T2.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
