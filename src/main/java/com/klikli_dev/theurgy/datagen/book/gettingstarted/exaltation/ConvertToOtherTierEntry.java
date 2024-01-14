package com.klikli_dev.theurgy.datagen.book.gettingstarted.exaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class ConvertToOtherTierEntry extends EntryProvider {
    public static final String ENTRY_ID = "convert_to_other_tier";

    public ConvertToOtherTierEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Exaltation");
        this.pageText("""
                Exaltation is the elevation of matter from one tier to another (e.g. Common to Rare).
                \\
                \\
                Thus, exaltation breaches the final barrier of transformation, allowing you to obtain any material you desire.
                """
        );

        this.page("intro2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Exaltation");
        this.pageText("""
                Due to the nature of exaltation, it is a very costly process. Matter cannot simply be made more valuable.\\
                Thus there is a loss or gain, depending on the direction of conversion.\\
                Generally, 4 units of the lower tier are required to obtain 1 unit of the higher tier and vice versa.
                """
        );

        this.page("digestion", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Digestion");
        this.pageText("""
                The core of the Exaltation Process is the Digestion of Niter into other Niter, in a {0}.
                \\
                \\
                Digestion needs a Catalysator in the form of {1}, which will be explained soon.
                """,
                this.itemLink(ItemRegistry.DIGESTION_VAT.get()),
                this.itemLink(ItemRegistry.PURIFIED_GOLD.get())
        );

        this.page("process", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                Exaltation consists of three steps.
                \\
                \\
                First, the Sulfur to be transformed is fermented alchemically into Niter (e.g. Iron Sulfur into Common Metals Niter).\\
                Then that 4 of that Niter can be digested into 1 Niter of the desired type (e.g. Common Metals Niter into Rare Metals Niter).
                """
        );

        this.page("process2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                *(Optionally that Niter can be reformed into a Niter of another type (e.g. Rare Metals Niter into Rare Gems Niter))*
                \\
                \\
                Finally that Niter can be reformed again into the desired Sulfur (e.g. Rare Metals Niter into Gold Sulfur).
                """
        );

        this.page("instructions", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Instructions");
        this.pageText("""
                        The following entries will guide you through the conversion of {0} into {1}. *This assumes you already obtained at least 4 Iron Sulfur and one Gold Sulfur*.
                        \\
                        \\
                        The instructions also apply to all other conversions between different tiers.
                        """,
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get()),
                this.itemLink("Alchemical Sulfur: Gold", SulfurRegistry.GOLD.get())
        );
    }

    @Override
    protected String entryName() {
        return "Replication by Exaltation";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining more of an item by converting items of a different tier.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/convert_tiers.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
