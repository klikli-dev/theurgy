// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class LogisticsNetworkEntry extends EntryProvider {
    public static final String ENTRY_ID = "logistics_network";

    public LogisticsNetworkEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("network", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Network Formation");
        this.pageText("""
                The most basic mercurial logistics network consists of one inserter and one extractor, connected by a wire.
                \\
                \\
                You can test this with a setup that inserts the contents of a chest into a furnace to be smelted.
                """
        );

        this.page("setup", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Place the Blocks");
        this.pageText("""
                1. Place a chest and a furnace near each other.
                2. Place an extractor on any face of the chest.
                3. Place an inserter on the top of the furnace (where the input items go).
                4. Connect inserter and extractor with a wire.
                """
        );

        this.page("image", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withImages(Theurgy.loc("textures/gui/book/furnace_automation.png")));
        this.pageTitle("Demonstration");

        this.page("extending", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Extending the Setup");
        this.pageText("""
                To improve the setup you can now place an extractor at the bottom of the furnace (or any other face and use the {0} to change the extract direction to bottom) and an inserter at any face of a second chest. Connect the two with a wire, and your furnace will now automatically deposit smelted items into the second chest.
                """
        );

        this.page("extending2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Extending the Setup");
        this.pageText("""
                You could add a third chest with fuel and connect it to an inserter placed on the side of the furnace to automatically refuel it.
                """
        );

        this.page("advanced", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Advanced Networks");
        this.pageText("""
                More complex network behaviours can be achieved with the use of filters (see further entries to learn more). Additionally, in the future more advanced routing options will be available.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Network Formation";
    }

    @Override
    protected String entryDescription() {
        return "Putting it all together.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(
                Theurgy.loc("textures/gui/book/logistics_network.png")
        );
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
