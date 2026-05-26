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

public class AmethystDivinationRodEntry extends EntryProvider {

    public static final String ENTRY_ID = "amethyst_divination_rod";

    public AmethystDivinationRodEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.AMETHYST_DIVINATION_ROD.get()))
                .withText(this.context().pageText()));

        this.pageText("""
                Crystals are especially useful for attuning divination rods, and many more advanced rod designs require {0} specifically. This rod is pre-attuned to locate budding amethyst blocks to make it easier to obtain these helpful crystals.
                """, this.itemLink("amethyst shards", Items.AMETHYST_SHARD));

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/amethyst_divination_rod"))
                .withText(this.context().pageText()));
        this.pageText("""
                A pre-attuned rod that only finds {0}.
                """, this.itemLink("amethyst", Items.BUDDING_AMETHYST));
    }

    @Override protected String entryName() { return "Amethyst Divination Rod"; }
    @Override protected String entryDescription() { return "A pre-attuned rod to find budding amethyst blocks."; }
    @Override protected GuiSprite entryBackground() { return EntryBackground.DEFAULT; }
    @Override protected BookIconModel entryIcon() { return BookIconModel.create(ItemRegistry.AMETHYST_DIVINATION_ROD.get()); }
    @Override protected String entryId() { return ENTRY_ID; }
}
