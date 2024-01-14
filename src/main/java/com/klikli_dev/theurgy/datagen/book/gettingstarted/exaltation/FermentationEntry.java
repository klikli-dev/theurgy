package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.GettingStartedCategoryProvider;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class FermentationEntry extends EntryProvider {
    public static final String ENTRY_ID = "fermentation_exaltation";

    public FermentationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Fermentation");
        this.pageText("""
                Once again we need niter, this time only from our source Sulfur: {0}.
                \\
                \\
                We do not need a target niter, because we will create the Rare Metals Niter using Digestion and will use Reformation only for the creation of Gold Sulfur.
                """,
                this.itemLink("Alchemical Niter: Common Metals",SulfurRegistry.METALS_COMMON.get())
        );


        this.page("metals", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.METALS_COMMON.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Alchemical Niter: Common Metals");
        this.pageText("""
                View {0} to refresh your memory on how to use the Fermentation Vat.
                """,
                this.entryLink("Fermentation", GettingStartedCategoryProvider.CATEGORY_ID, com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation.FermentationEntry.ENTRY_ID)
        );
    }

    @Override
    protected String entryName() {
        return "Fermentation";
    }

    @Override
    protected String entryDescription() {
        return "Convert Iron Sulfur into Common Metals Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.METALS_COMMON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
