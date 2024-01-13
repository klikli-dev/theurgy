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
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.datagen.book.gettingstarted.reformation.ConvertWithinTypeAndTierEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class SulfuricFluxEmitterEntry extends EntryProvider {

    public static final String ENTRY_ID = "sulfuric_flux_emitter";

    public SulfuricFluxEmitterEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                         This devices converts raw mercury flux into [#]($PURPLE)Sulfuric Flux[#](), a type of energy that can carry information. It can be used to transform Sulfur into other types of Sulfur by linking it to a Reformation Array.\\
                        See {0} for more information.
                        """,
                this.entryLink("Reformation", GettingStartedCategoryProvider.CATEGORY_ID, ConvertWithinTypeAndTierEntry.ENTRY_ID)
        );

        this.page("targets", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Target Blocks");
        this.add(this.context().pageText(),
                """
                        Valid target blocks that will form a Reformation Array are {0},  {1}, and {2}.
                        """,
                this.itemLink(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()),
                this.itemLink(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get())
        );


        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        Right-click the target blocks (the pedestals) with the {0} so they get highlighted.\\
                        Then place the Emitter onto a Mercury Flux source, such as a {1}.\\
                        The maximum range is **8** blocks.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("usage2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        The emitter is the central controlling element of the reformation array.\\
                        If a valid array is linked to the emitter, and a valid recipe is present in the pedestals, the emitter will start emitting sulfuric flux and transform the sulfur.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get()),
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get())
        );

        this.page("usage3", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.add(this.context().pageTitle(), "Checking Validity");
        this.add(this.context().pageText(),
                """
                        Right-click the placed {0} to check if it is linked to a valid reformation array.\\
                        If it is not, destroy it, link it to the pedestals again, and place it.
                        """,
                this.itemLink(ItemRegistry.SULFURIC_FLUX_EMITTER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/sulfuric_flux_emitter"))
                .build());
    }

    @Override
    protected String entryName() {
        return "Sulfuric Flux Emitter";
    }

    @Override
    protected String entryDescription() {
        return "Transporting Sulfuric Information";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SULFURIC_FLUX_EMITTER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
