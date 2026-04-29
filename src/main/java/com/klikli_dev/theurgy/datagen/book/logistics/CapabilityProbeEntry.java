// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.logistics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import net.minecraft.world.item.crafting.Ingredient;

public class CapabilityProbeEntry extends EntryProvider {
    public static final String ENTRY_ID = "capability_probe";

    public CapabilityProbeEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("probe", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_CAPABILITY_PROBE.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                The Logistics Capability Probe marks one side of a block for remote access through the Mercurial Logistics network.
                \\
                \\
                Its main purpose is to turn that one used-up side into many more usable sides by feeding one or more [Logistics Capability Proxies](entry://logistics/capability_proxy).
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_capability_probe")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
                Right-click the face of a block whose capabilities you want to expose to the network.
                \\
                \\
                The probe always targets the block it is attached to. Then connect the probe to the rest of the network with Mercurial Wires.
                """
        );

        this.page("how_it_works", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("How it Works");
        this.pageText("""
                A probe uses one side of the target block.
                \\
                \\
                You can then connect that single probe to as many proxies as you need, and each proxy gives you another six sides that can act like that probed side.
                """
        );

        this.page("inspection", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Inspection");
        this.pageText("""
                Use an empty hand on the probe to highlight its current target.
                \\
                \\
                This makes it easy to check which block face the probe is using before you build proxies around it.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Logistics Capability Probe";
    }

    @Override
    protected String entryDescription() {
        return "Mark a block as a remote capability endpoint.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_CAPABILITY_PROBE.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
