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
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class NeededApparatusEntry extends EntryProvider {

    public static final String ENTRY_ID = "needed_apparatus_spagyrics";

    public NeededApparatusEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Required Apparatus");
        this.pageText("""
                Ore Refining needs all of the Spagyrics Apparatus to extract the needed materials and recombine them.\\
                The following pages list all the needed machinery.
                \\
                \\
                View the {0} Category on how to craft and use them.
                 """, this.categoryLink("Apparatus", ApparatusCategory.CATEGORY_ID));

        this.page("pyromantic_brazier", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.PYROMANTIC_BRAZIER.get()))
                .withText(this.context().pageText()));
        this.pageTitle("Pyromantic Brazier");
        this.pageText("You will need 4 of these to power the other Apparatus.");

        this.page("calcination_oven", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.CALCINATION_OVEN.get()))
                .withText(this.context().pageText()));
        this.pageText(
                """
                        The Calcination Oven will allow you to create the {0}.
                        """,
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );

        this.page("sal_ammoniac_accumulator", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()))
                .withText(this.context().pageText()));
        this.pageText(
                """
                        The Sal Ammoniac Accumulator will fill the Sal Ammoniac Tank with solvent.
                        """
        );

        this.page("sal_ammoniac_tank", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_TANK.get()))
                .withText(this.context().pageText()));
        this.pageText(
                """
                        The Sal Ammoniac Tank stores the solvent - Sal Ammoniac - for use in the Liquefaction Cauldron.
                        """
        );

        this.page("liquefaction_cauldron", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LIQUEFACTION_CAULDRON.get()))
                .withText(this.context().pageText()));

        this.pageText(
                """
                        The Liquefaction Cauldron uses Sal Ammoniac to extract Alchemical Sulfur.
                        """
        );


        this.page("distiller", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.DISTILLER.get()))
                .withText(this.context().pageText()));

        this.pageText(
                """
                        The Distiller creates {0}.
                        """,
                this.itemLink("Mercury Shards", ItemRegistry.MERCURY_SHARD.get())
        );

        this.page("incubator", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.INCUBATOR.get()))
                .withText(this.context().pageText()));

        this.pageText(
                """
                        The Incubator recombines the Salt, Sulfur, and Mercury into the refined item, {4} in our case.
                        It needs one each of {0}, {1}, {2} to hold the input materials.
                        """,
                this.itemLink("Salt Vessel", ItemRegistry.INCUBATOR_SALT_VESSEL.get()),
                this.itemLink("Mercury Vessel", ItemRegistry.INCUBATOR_MERCURY_VESSEL.get()),
                this.itemLink("Sulfur Vessel", ItemRegistry.INCUBATOR_SULFUR_VESSEL.get()),
                this.itemLink("Iron Ingots", Items.IRON_INGOT)
        );

        this.page("next_steps", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Next Steps");
        this.pageText("""
                        Craft all the apparatus and place them.
                        \\
                        \\
                        The {0} needs to be placed on top of a {1}.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get())

        );
        this.page("next_steps2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Next Steps");
        this.pageText("""
                        Those that need heating need to be placed on the {0}.
                        \\
                        Put some fuel, such as {1}, into the braziers to heat them.
                        """,
                this.itemLink("Pyromantic Braziers", ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(Items.COAL)

        );
    }

    @Override
    protected String entryName() {
        return "Required Apparatus";
    }

    @Override
    protected String entryDescription() {
        return "The apparatus needed to refine raw metals";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.DISTILLER.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}