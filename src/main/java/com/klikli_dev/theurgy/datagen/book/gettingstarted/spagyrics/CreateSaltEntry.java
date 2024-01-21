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
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CreateSaltEntry extends EntryProvider {

    public static final String ENTRY_ID = "create_salt";

    public CreateSaltEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(SaltRegistry.MINERAL.get()))
                .withText(this.context().pageText()));
        this.pageTitle("Extracting Salt");
        this.pageText(
                """
                        Salt is needed in order to create items from Alchemical Sulfur. The type of salt needs to match the type of sulfur - for our project that involves {0} we need {1}, which covers all types of ores and metals.
                        """,
                this.itemLink("Iron Sulfur", SulfurRegistry.IRON.get()),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );

        this.page("step1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Calcinating Minerals");
        this.pageText(
                """
                        [#]($INPUT)Right-click[#]() the {0} with any Mineral such as Ores, Raw Metals or Ingots to calcinate it.
                        \\
                        \\
                        {2} is a great source, as it is renewable.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink("Strata Salt", SaltRegistry.STRATA.get()),
                this.itemLink(Items.CHARCOAL)
        );


        this.page("step2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Providing Heat");
        this.pageText(
                """
                        Now add fuel, such as Coal, to the {0} below the oven to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get())
        );

        this.page("step3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Obtaining the Salt");
        this.pageText(
                """
                        After a while some salt will have been created, you can [#]($INPUT)right-click[#]() the {0} with an empty hand to obtain {1}.
                        """,
                this.itemLink(ItemRegistry.CALCINATION_OVEN.get()),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get())
        );
    }

    @Override
    protected String entryName() {
        return "Extracting Salt";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining Salt - the \"Body\"";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SaltRegistry.MINERAL.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}