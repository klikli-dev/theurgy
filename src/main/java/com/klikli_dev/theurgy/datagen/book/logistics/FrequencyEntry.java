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
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class FrequencyEntry extends EntryProvider {
    public static final String ENTRY_ID = "frequency";
    public FrequencyEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Node Frequency");
        this.pageText("""
                Leaf Nodes in the same network, such as inserter and extractor nodes, by default are all connected. Every extractor will attempt to insert into every inserter.
                \\
                \\
                This can be counteracted using filters, but sometimes it is desirable to have multiple *logical* sub-networks without having to physically separate them. This is where node frequencies come in.
                """
        );

        this.page("subnetworks", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Sub-Networks");
        this.pageText("""
                By setting the frequency on an inserter or extractor, it will only interact with other nodes that have the same frequency.
                \\
                \\
                This means, you could theoretically connect all the blocks in your base into a single network, but by using different frequencies you can decide which blocks actually interact with each other.
                """
        );

        this.page("select", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Select a Frequency");
        this.pageText("""
                 First, you need to select a frequency in your {0}.
                 1. Equip your {0}.
                 2. Sneak+Scroll until you are in the "Select Frequency" mode.
                 3. Right and Left click to increase/decrease the selected frequency.
                 """,
                this.itemLink(ItemRegistry.MERCURIAL_WAND.get())
        );

        this.page("set", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Set a Frequency");
        this.pageText("""
                 Then you can apply the frequency to a leaf node.
                 1. Equip your {0}.
                 2. Right-Click an inserter or extractor to set its frequency.
                 3. Repeat for all blocks that should be in the same sub-network.
                 4. Right-Click any of the blocks with an empty hand to show all the blocks in the same sub-network.
                 """,
                this.itemLink(ItemRegistry.MERCURIAL_WAND.get())
        );
    }

    @Override
    protected String entryName() {
        return "Node Frequency";
    }

    @Override
    protected String entryDescription() {
        return "Creating sub-networks by setting node frequencies";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURIAL_WAND.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
