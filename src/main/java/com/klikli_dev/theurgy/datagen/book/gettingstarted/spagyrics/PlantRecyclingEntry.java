// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePageModel;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class PlantRecyclingEntry extends EntryProvider {

    public static final String ENTRY_ID = "plant_recycling";

    public PlantRecyclingEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SaltRegistry.MINERAL.get()))
                .withText(this.context().pageText()));
        this.pageTitle("Recycling Plants");
        this.pageText(
                """
                        Like {0}, {1} can be made more useful by refining it into a somewhat more desirable Alchemical Salt: {2}.
                        """,
                this.itemLink("Strata Salt", SaltRegistry.STRATA.get()),
                this.itemLink("Plant Salt", SaltRegistry.PLANT.get()),
                this.itemLink("Creature Salt", SaltRegistry.CREATURE.get())
        );

        this.page("refining", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Refining Plant Salt");
        this.pageText(
                """
                        This is a two-step process:
                        1. First we calcinate Plants such as Crops or Trees to obtain {0}.
                        2. Then we calcinate 2 of these again to receive a small amount of {1}.
                        """,
                this.itemLink("Alchemical Salt - Plants", SaltRegistry.PLANT.get()),
                this.itemLink("Alchemical Salt - Creatures", SaltRegistry.CREATURE.get())
        );

        this.page("recipe1", () -> BookCalcinationRecipePageModel.create()
                .withRecipeId1("theurgy:calcination/alchemical_salt_plant_from_crops")
                .withRecipeId2("theurgy:calcination/alchemical_salt_creature_from_plant_salt")
                );

        this.page("recipe2", () -> BookCalcinationRecipePageModel.create()
                .withRecipeId1("theurgy:calcination/alchemical_salt_plant_from_logs")
                .withRecipeId2("theurgy:calcination/alchemical_salt_plant_from_leaves")
        );

        this.page("recipe3", () -> BookCalcinationRecipePageModel.create()
                .withRecipeId1("theurgy:calcination/alchemical_salt_plant_from_saplings")
        );
    }

    @Override
    protected String entryName() {
        return "Recycling Plants";
    }

    @Override
    protected String entryDescription() {
        return "Using Plants to create Creature Salt";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SaltRegistry.PLANT);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}