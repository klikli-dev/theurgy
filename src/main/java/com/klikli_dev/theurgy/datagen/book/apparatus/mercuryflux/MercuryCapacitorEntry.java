// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.mercuryflux;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryCapacitorEntry extends EntryProvider {

    public static final String ENTRY_ID = "mercury_capacitor";

    public MercuryCapacitorEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_CAPACITOR.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                        The {0} is a large mercury flux storage unit that can store up to 10x the mercury flux of a Mercury Catalyst. It can receive and send mercury flux to adjacent blocks.
                        """,
                this.itemLink(ItemRegistry.MERCURY_CAPACITOR.get())
        );

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        The capacitor displays its fill level through its color:
                          \s
                          \s
                        - [#](ff0000)Red[#]() = Close to empty
                        - [#](ad03fc)Purple[#]() = Around halfway
                        - [#](00ff00)Green[#]() = Full
                        """,
                this.itemLink(ItemRegistry.MERCURY_CAPACITOR.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercury_capacitor"))
        );

        this.page("sides", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Side Configuration");
        this.pageText("""
                        Each side of the {0} can be configured to control how it interacts with adjacent blocks:
                        
                        - [#](00ff00)OUTPUT[#]() - Pushes mercury flux out of this side
                        - [#](00ffff)INPUT[#]() - Receives mercury flux from this side
                        - [#](808080)NONE[#]() - No interaction
                        
                        By default:
                        - [#](00ff00)UP/DOWN[#]() are set to OUTPUT
                        - [#](00ffff)NORTH/SOUTH/EAST/WEST[#]() are set to INPUT
                        """,
                this.itemLink(ItemRegistry.MERCURY_CAPACITOR.get())
        );

        this.page("wand", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Configuring with the Mercurial Wand");
        this.pageText("""
                        Use the {0} to change a capacitor's side configuration:
                        
                        1. Switch to the "Cycle Capacitor Side Mode" wand mode
                        2. Right-click on a capacitor face to cycle through: INPUT → OUTPUT → BOTH → NONE → INPUT
                        
                        Note: The wand cycles through modes in a specific order, so you may need to click multiple times to reach your desired configuration.
                        """,
                this.itemLink(ItemRegistry.MERCURIAL_WAND.get())
        );
    }

    @Override
    protected String entryName() {
        return "Mercury Capacitor";
    }

    @Override
    protected String entryDescription() {
        return "Storing Large Amounts of Energy";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_CAPACITOR.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
