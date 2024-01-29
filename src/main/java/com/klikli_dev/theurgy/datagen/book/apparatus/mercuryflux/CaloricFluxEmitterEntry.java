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
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class CaloricFluxEmitterEntry extends EntryProvider {

    public static final String ENTRY_ID = "caloric_flux_emitter";

    public CaloricFluxEmitterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.CALORIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                       This devices converts raw mercury flux into [#]($PURPLE)Caloric Flux[#](), or simply: transferable heat. It can be used to power other alchemical apparatuses that would usually need a {0} below them.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Usage");
        this.pageText(
                """
                        Right-click the target block with the {0} until it is highlighted. Then place the Emitter onto a Mercury Flux source, such as a {1}.\\
                        The maximum range is **8** blocks.\\
                        As long as mercury flux is provided to it, the emitter will send caloric flux to the target block and keep it heated.
                        """,
                this.itemLink(ItemRegistry.CALORIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("redstone", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Redstone");
        this.pageText(
                """
                        By default the block is enabled.
                        \\
                        \\
                        You can disable it with an active redstone signal.
                        \\
                        \\
                        When disabled, the emitter will not send caloric flux to the target block.
                        """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/caloric_flux_emitter_from_campfire"))
                .withRecipeId2(Theurgy.loc("crafting/shaped/caloric_flux_emitter_from_lava_bucket"))
                .build());
    }

    @Override
    protected String entryName() {
        return "Caloric Flux Emitter";
    }

    @Override
    protected String entryDescription() {
        return "Efficiently powering alchemical Apparatus";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.CALORIC_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
