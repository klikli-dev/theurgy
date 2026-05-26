// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.LiquefactionCauldronEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class AboutDivinationRodsEntry extends EntryProvider {
    public static final String ENTRY_ID = "about_divination_rods";
    public AboutDivinationRodsEntry(CategoryProvider parent) { super(parent); }
    @Override protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create().withItem(Ingredient.of(ItemRegistry.DIVINATION_ROD_T1.get())).withTitle(this.context().pageTitle()).withText(this.context().pageText()));
        this.pageTitle("About Divination Rods"); this.pageText("""
                Divination Rods, also known as Dowsing Rods, are a valuable instrument used to locate ores and other valuable blocks. In order to show the location of a block, it must first be "attuned" to it.
                """);
        this.page("intro2", () -> BookTextPageModel.create().withTitle(this.context().pageTitle()).withText(this.context().pageText()));
        this.pageTitle("Rod Attunement"); this.pageText("""
                There are two methods of attuning, depending on the type of rod: by *crafting* a rod with the []($PURPLE)alchemical sulfur[](#) of the desired block, or by *using* a rod on the desired block.
                \\
                \\
                Attuning a rod to a block will cause it to point towards the nearest block of that type.
                """);
        this.page("sulfur_attuned_rods", () -> BookTextPageModel.create().withTitle(this.context().pageTitle()).withText(this.context().pageText()));
        this.pageTitle("Pre-Attuned Rods"); this.pageText("""
                Pre-Attuned rods, those that are crafted with alchemical sulfur, are more stable, and thus have a much higher durability, but require more effort to craft.
                \\
                \\
                Research {0} (specifically, *Liquefaction*) to continue on this path.
                """, this.entryLink("Spagyrics", GettingStartedCategoryProvider.CATEGORY_ID, "spagyrics"));
        this.page("usage", () -> BookTextPageModel.create().withTitle(this.context().pageTitle()).withText(this.context().pageText()));
        this.pageTitle("Usage"); this.pageText("""
                - **Shift-Click** a block to attune the rod to it (unless it is pre-attuned).
                - **Right-Click and hold** to let the rod search for blocks.
                - **Right-Click without holding** after a successful search will let the rod show the last found block without consuming durability.
                """);
        this.page("usage2", () -> BookTextPageModel.create().withTitle(this.context().pageTitle()).withText(this.context().pageText()));
        this.pageTitle("Usage"); this.pageText("""
                The rod will indicate that it found a block by *changing color* to be partially or fully purple, and by emitting a *glowing ball* that will fly towards the block, when right-clicked without holding.
                """);
    }
    @Override protected String entryName() { return "About Divination Rods"; }
    @Override protected String entryDescription() { return "An Introduction to Ore-Finding"; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.DIVINATION_ROD_T1.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
