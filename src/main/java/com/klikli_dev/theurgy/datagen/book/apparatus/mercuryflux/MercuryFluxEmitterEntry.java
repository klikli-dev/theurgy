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
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryFluxEmitterEntry extends EntryProvider {

    public static final String ENTRY_ID = "mercury_flux_emitter";

    public MercuryFluxEmitterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_FLUX_EMITTER.get()))
                .withText(this.context().pageText()));
        this.pageText(
                """
                        The {0} transfers [#]($PURPLE)Mercury Flux[#]() from the block it is attached to into another linked block.
                        \\
                        It can also transfer NeoForge Energy / FE, but won't convert between the two.
                        """,
                this.itemLink(ItemRegistry.MERCURY_FLUX_EMITTER.get())
        );

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText(
                """
                        Right-click the target block with the {0} until it is highlighted. Then place the Emitter onto a Mercury Flux source (**Shift-Right-Click**!), such as a {1}.\\
                        The maximum range is **8** blocks.\\
                        As long as the attached block can provide mercury flux and the target can receive it, the emitter will transfer mercury flux between them.
                        """,
                this.itemLink(ItemRegistry.MERCURY_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("usage2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Placement Hint");
        this.pageText(
                """
                        When placing the emitter onto a block that already contains mercury flux, use **Shift-Right-Click**.\\
                        A normal right-click would instead try to select that block as the emitter's target.
                        """
        );

        this.page("redstone", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        );
        this.pageTitle("Redstone");
        this.pageText(
                """
                        By default the block is enabled.
                        \\
                        \\
                        You can disable it with an active redstone signal.
                        \\
                        \\
                        When disabled, the emitter will not transfer mercury flux or NeoForge Energy / FE to the target block.
                        """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercury_flux_emitter")));
    }

    @Override
    protected String entryName() {
        return "Mercury Flux Emitter";
    }

    @Override
    protected String entryDescription() {
        return "Transporting raw mercury flux";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
