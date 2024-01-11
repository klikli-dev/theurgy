package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.page.calcination.BookCalcinationRecipePageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class StrataRecyclingEntry extends EntryProvider {

    public static final String ENTRY_ID = "strata_recycling";

    public StrataRecyclingEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SaltRegistry.MINERAL.get()))
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Expensive Salt");
        this.pageText(
                """
                        As discovered in the previous entry, alchemical processes that should create Metals (or Gems) need Alchemical Salt created from these types of matter - Minerals. This, of course, is somewhat expensive, as it consumes valuable Minerals that we could use otherwise.
                        """
        );

        this.page("solution", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Recycling Strata");
        this.pageText(
                """
                        Sedimentary Strata, such as {0}, {1}, {2} but also {3} and many others, contains trace amounts of minerals.
                        \\
                        \\
                        With the right process, we can obtain Alchemical Salt from them.
                        """,
                this.itemLink(Items.DIRT),
                this.itemLink(Items.COBBLESTONE),
                this.itemLink(Items.SAND),
                this.itemLink(Items.CLAY)
        );

        this.page("refining", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Refining Strata Salt");
        this.pageText(
                """
                        This is a two-step process:
                        1. First we calcinate the Strata to obtain {0}.
                        2. Then we calcinate 20 of these again to receive a small amount of {1}.
                        """,
                this.itemLink("Alchemical Salt - Strata", SaltRegistry.STRATA.get()),
                this.itemLink("Alchemical Salt - Minerals", SaltRegistry.MINERAL.get())
        );

        this.page("recipe1", () -> BookCalcinationRecipePageModel.builder()
                .withRecipeId1("theurgy:calcination/strata_from_cobblestone")
                .withRecipeId2("theurgy:calcination/mineral")
                .withText(this.context().pageText())
                .build());
        this.pageText(
                """
                        Sample extraction from Cobblestone.
                        """
        );
    }

    @Override
    protected String entryName() {
        return "Recycling Strata";
    }

    @Override
    protected String entryDescription() {
        return "Using Stone, Sand and Dirt to create Mineral Salt";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/cobble_to_salt.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}