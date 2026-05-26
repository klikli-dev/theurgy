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

public class PreciousSulfurAttunedDivinationRodsEntry extends EntryProvider {

    public static final String ENTRY_ID = "precious_sulfur_attuned_divination_rod";

    public PreciousSulfurAttunedDivinationRodsEntry(CategoryProvider parent) { super(parent); }

    @Override protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create().withItem(Ingredient.of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get())).withText(this.context().pageText()));
        this.pageText("""
                This divination rod is the most powerful variant of pre-attuned rod, it is highly durable and allows to locate precious ores, such as {0}.
                """, this.itemLink("diamond", Items.DIAMOND_ORE));
        this.page("recipe", () -> BookCraftingRecipePageModel.create().withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_precious")).withText(this.context().pageText()));
        this.pageText("""
                A rod pre-attuned to precious ores, such as {0}.
                """, this.itemLink("diamond", Items.DIAMOND_ORE));
    }

    @Override protected String entryName() { return "Sulfur-Attuned Divination Rods for Precious Materials"; }
    @Override protected String entryDescription() { return "An intricately crafted Sulfur-Attuned Divination rod, allowing to locate precious ores."; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
