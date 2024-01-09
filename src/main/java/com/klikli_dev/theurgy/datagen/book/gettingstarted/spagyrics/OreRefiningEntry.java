package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.LiquefactionCauldronEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;

public class OreRefiningEntry extends EntryProvider {

    public static final String ENTRY_ID = "ore_refining";

    public OreRefiningEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Spagyrics for Refining");
        this.pageText("""
                The easiest application of Spagyrics is the refining of ores and raw metals. The process of smelting metal in a furnace is wasteful, as it only yields a single ingot per ore, losing a lot of the precious raw materials in the process. Alchemists can extract even the last iota of value from ores, but the process is somewhat more complex.
                """);

        this.page("intro2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Ore Duplication");
        this.pageText("""
                        The first application of Spagyrics thus we will be the efficient refining of raw metals into multiple ingots.
                        In the following pages and entries we will attempt to create *three* {0} out of just *one* {1} using Spagyrics processes.
                        """,
                this.itemLink("Iron Ingots", Items.IRON_INGOT),
                this.itemLink("Raw Iron", Items.RAW_IRON)
        );


        this.page("overview", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("The Process");
        this.pageText("""
                        To refine an Ore or Raw Metal you first need to {0} it, which will yield multiple Metal Sulfur. Additionally you need to obtain multiple heaps of {1} to provide a body for this multiplied Sulfur, and some {2} to provide the mercury for the soul of the resulting refined ingots.
                        """, this.entryLink("liquefy", ApparatusCategory.CATEGORY_ID, LiquefactionCauldronEntry.ENTRY_ID),
                this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get()),
                this.itemLink("Mercury Shards", ItemRegistry.MERCURY_SHARD.get()));

        //TODO: use in the other entries
//        this.page("cost", () -> BookTextPageModel.builder()
//                .withTitle(this.context().pageTitle())
//                .withText(this.context().pageText())
//                .build());
//        this.pageTitle("The Cost");
//        this.pageText("""
//                To bring it to a point, the cost of significantly increasing ore yield with Spagyrics is having to obtain high amounts of {0} which is mainly sourced from ... Metals.
//                """, this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get()));
//
//        this.page("cost2", () -> BookTextPageModel.builder()
//                .withTitle(this.context().pageTitle())
//                .withText(this.context().pageText())
//                .build());
//        this.pageTitle("The Cost");
//        this.pageText("""
//                The first and obvious solution to this chicken-and-egg problem is to {0} abundant metals, such as {1}, and use the salt to {2} more valuable materials such as {3} or even {4} as output.
//                """, this.entryLink("calcinate", ApparatusCategory.CATEGORY_ID, CalcinationOvenEntry.ENTRY_ID),
//                this.itemLink(Items.RAW_COPPER),
//                this.entryLink("incubate", ApparatusCategory.CATEGORY_ID, IncubatorEntry.ENTRY_ID),
//                this.itemLink(Items.IRON_INGOT),
//                this.itemLink(Items.DIAMOND));
//
//        this.page("silver_lining", () -> BookTextPageModel.builder()
//                .withTitle(this.context().pageTitle())
//                .withText(this.context().pageText())
//                .build());
//        this.pageTitle("The Silver Lining");
//        this.pageText("""
//                As luck would have it another option to obtain {0} is to calcinate it from {1}, which is in turn obtained by calcinating Sand, Cobblestone, etc. This is a somewhat lossy process as it requires a lot of {1}, but it does have the upside of giving those abundant materials a use.
//                """, this.itemLink("Mineral Salt", SaltRegistry.MINERAL.get()),
//                this.itemLink("Strata Salt", SaltRegistry.STRATA.get()));
//
//        this.page("soul", () -> BookTextPageModel.builder()
//                .withTitle(this.context().pageTitle())
//                .withText(this.context().pageText())
//                .build());
//        this.pageTitle("The Soul");
//        this.pageText("""
//                Obtaining {0} is usually not much of an issue as a wide variety of materials can be {1} to obtain in.
//                """, this.itemLink(ItemRegistry.MERCURY_SHARD.get()),
//                this.entryLink("distilled", ApparatusCategory.CATEGORY_ID, DistillerEntry.ENTRY_ID));

        this.page("next", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("How?");
        this.pageText("""
                The next entries will guide you through the process of obtaining all the materials and creating your iron ingots.
                """);
    }

    @Override
    protected String entryName() {
        return "Ore Refining";
    }

    @Override
    protected String entryDescription() {
        return "Triple your ore yield";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/raw_metal_x3.png"), 32, 32);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}