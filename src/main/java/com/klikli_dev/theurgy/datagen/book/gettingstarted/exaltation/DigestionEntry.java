package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class DigestionEntry extends EntryProvider {
    public static final String ENTRY_ID = "digestion_exaltation";

    public DigestionEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Digestion");
        this.pageText("""
                The key process of Exaltation is the Digestion of lower Niter into higher Niter (or vice versa).
                \\
                \\
                We need 4 {0} to create 1 {1}.
                """,
                this.itemLink("Alchemical Niter: Common Metals",SulfurRegistry.METALS_COMMON.get()),
                this.itemLink("Alchemical Niter: Rare Metals",SulfurRegistry.METALS_RARE.get())
        );

        this.page("metals", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.METALS_RARE.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Alchemical Niter: Rare Metals");
        this.pageText("""
                [#]($INPUT)Right-click[#]() the {0} with the ingredients to place them inside:
                - 4x {1}
                - {2}
                - {3}

                (Check JEI for more recipes)
                """,
                this.itemLink(ItemRegistry.DIGESTION_VAT.get()),
                this.itemLink("Alchemical Niter: Common Metals",SulfurRegistry.METALS_COMMON.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET.get()),
                this.itemLink(ItemRegistry.PURIFIED_GOLD.get())
        );

        this.page("start", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                [#]($INPUT)Shift-right-click[#]() the {0} with an [#]($INPUT)empty hand[#]() to close the vat and start the digestion.
                \\
                \\
                Once it opens again the process is complete.
                \\
                \\
                [#]($INPUT)Right-click[#]() with an empty hand to retrieve the {1}.
                """,
                this.itemLink(ItemRegistry.DIGESTION_VAT.get()),
                this.itemLink("Alchemical Niter: Rare Metals",SulfurRegistry.METALS_RARE.get())
        );


        this.page("bulk", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Bulk Processing");
        this.pageText("""
                You can provide all these ingredients in bulk, the process will then repeat until all ingredients are consumed.
                \\
                \\
                You can also [#]($INPUT)shift-right-click[#]() with an [#]($INPUT)empty hand[#]() to open the vat and interrupt processing to take out the intermediate result.
                """
        );

        this.page("bulk2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Bulk Processing");
        this.pageText("""
                Remember: the more Rare Metals Niter you produce from Common Metals Niter (via Iron Sulfur) the more Gold you can get in the end.
                """
        );

    }

    @Override
    protected String entryName() {
        return "Digestion";
    }

    @Override
    protected String entryDescription() {
        return "Convert Common Metals Niter into Rare Metals Niter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.METALS_RARE.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
