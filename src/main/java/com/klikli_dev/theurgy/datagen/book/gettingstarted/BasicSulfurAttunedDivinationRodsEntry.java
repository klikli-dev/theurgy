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
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.LiquefactionCauldronEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class BasicSulfurAttunedDivinationRodsEntry extends EntryProvider {

    public static final String ENTRY_ID = "abundant_and_common_sulfur_attuned_divination_rod";

    public BasicSulfurAttunedDivinationRodsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get(), ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()))
                .withText(this.context().pageText()));

        this.pageText("""
                This divination rod is the most basic variant of pre-attuned rod. While it cannot be attuned to a new block after crafting, it has a much higher durability, and is generally more convenient to use.
                """);

        this.page("sulfur", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Sulfur");
        this.pageText("""
                To obtain sulfur for crafting this rod you first need to obtain an ore, ingot or gem of the type of material you want the rod to be attuned to. Then, you need to obtain it''s sulfur by melting it down in a {0}.
                """, this.entryLink("Liquefaction Cauldron", ApparatusCategory.CATEGORY_ID, LiquefactionCauldronEntry.ENTRY_ID));

        this.page("recipe_abundant", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_abundant"))
                .withText(this.context().pageText()));
        this.pageText("""
                A rod pre-attuned to abundant ores, such as {0} or {1}.
                """, this.itemLink("copper", Items.COPPER_ORE), this.itemLink("coal", Items.COAL_ORE));

        this.page("recipe_common", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfur_attuned_divination_rod_common"))
                .withText(this.context().pageText()));
        this.pageText("""
                A rod pre-attuned to common ores, such as {0} or {1}.
                """, this.itemLink("iron", Items.IRON_ORE), this.itemLink("lapis", Items.LAPIS_ORE));
    }

    @Override
    protected String entryName() {
        return "Basic Sulfur-Attuned Divination Rods";
    }

    @Override
    protected String entryDescription() {
        return "Pre-attuned rods for locating abundant and common ores.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
