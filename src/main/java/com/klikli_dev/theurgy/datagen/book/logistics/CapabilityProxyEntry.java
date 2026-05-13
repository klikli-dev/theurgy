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
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.world.item.crafting.Ingredient;

public class CapabilityProxyEntry extends EntryProvider {
    public static final String ENTRY_ID = "capability_proxy";

    public CapabilityProxyEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("proxy", () -> BookSpotlightPageModel.create()
                .withItem(Ingredient.of(ItemRegistry.LOGISTICS_CAPABILITY_PROXY.get()))
                .withText(this.context().pageText()));
        this.pageText("""
                The Logistics Capability Proxy gives you a new place to connect to a block that already has a probe on it.
                \\
                \\
                Hoppers, pipes, and Mercurial logistics inserters or extractors connected to the proxy behave as if they were connected directly to the block targeted by the probe.
                """
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.create()
                .withRecipeId1(Theurgy.loc("crafting/shaped/logistics_capability_proxy")));

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Usage");
        this.pageText("""
                Place the proxy anywhere convenient, then connect it with Mercurial Wires to one or more [Logistics Capability Probes](entry://logistics/capability_probe).
                \\
                \\
                The proxy does not need to touch the target block. It only needs to share a network with the probe.
                """
        );

        this.page("how_it_works", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("How it Works");
        this.pageText("""
                The main use of the proxy is to get more than six usable sides for a machine.
                \\
                \\
                One probe uses one side of the real block, and then every proxy connected to that probe gives you another full set of six sides to build with.
                """
        );

        this.page("multiple_probes", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Multiple Probes");
        this.pageText("""
                One proxy can be connected to several probes. If more than one of them can be used, the proxy will alternate between them.
                \\
                \\
                One probe can also feed many proxies, so a single machine side can be turned into as many remote connection points as you need.
                """
        );

        this.page("inspection", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Inspection");
        this.pageText("""
                Use an empty hand on the proxy to highlight its connected probes and their current targets.
                \\
                \\
                This helps you check which machine the proxy is acting for before you connect automation to it.
                """
        );
    }

    @Override
    protected String entryName() {
        return "Logistics Capability Proxy";
    }

    @Override
    protected String entryDescription() {
        return "Expose remote block capabilities through the network.";
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LOGISTICS_CAPABILITY_PROXY.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
