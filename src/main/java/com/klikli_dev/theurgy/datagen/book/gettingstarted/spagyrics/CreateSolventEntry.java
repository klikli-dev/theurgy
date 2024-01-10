package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.ApparatusCategory;
import com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics.SalAmmoniacAccumulatorEntry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class CreateSolventEntry extends EntryProvider {

    public static final String ENTRY_ID = "create_solvent";

    public CreateSolventEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.SAL_AMMONIAC_BUCKET.get()))
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Solvents");
        this.pageText("""
                Solvents strip Matter of their Salt and Mercury, leaving the Alchemical Sulfur behind.
                \\
                \\
                The most common Solvent is Sal Ammoniac, in liquid form.
                """
        );

        this.page("obtain", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Obtaining liquid Sal Ammoniac");
        this.pageText("""
                        Traces of Sal Ammoniac can be extracted from Water.
                        1. Set up the {0} with a Tank below.
                        2. Fill the Accumulator with water.
                        3. Wait!
                        """,
                this.entryLink("Sal Ammoniac Accumulator", ApparatusCategory.CATEGORY_ID, SalAmmoniacAccumulatorEntry.ENTRY_ID)
        );

        this.page("faster", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Speeding it up");
        this.pageText("""
                        Instead of waiting for trace amounts to accumulate, you can dissolve a {0} in the water to speed up the process significantly.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.page("step1", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Filling the Accumulator");
        this.pageText("After placing the {0} on a {1}, [#]($INPUT)right-click[#]() the {0} with water buckets (up to 10) to fill it.",
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get())
        );

        this.page("step2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Adding Crystals");
        this.pageText("""
                        Optionally you can now [#]($INPUT)right-click[#]() the {0} with a {1} (obtained by mining). You will get Sal Ammoniac regardless, but the crystal will speed up the process significantly.
                        """,
                this.itemLink(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );

        this.page("step3", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Taking the Sal Ammoniac");
        this.pageText("Once the {0} has filled up sufficiently, you can [#]($INPUT)right-click[#]() it with an empty bucket to obtain a {1}.",
                this.itemLink(ItemRegistry.SAL_AMMONIAC_TANK.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1("theurgy:crafting/shapeless/sal_ammoniac_crystal_from_sal_ammoniac_bucket")
                .withText(this.context().pageText())
                .build());
        this.pageText("You can reverse this by filling the fluid Sal Ammoniac in a bucket and then crafting the bucket with no other item. You will receive a {0} and an empty bucket.",
                this.itemLink(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get())
        );
    }

    @Override
    protected String entryName() {
        return "Accumulating Solvent";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining Sal Ammoniac - they key to Sulfur Extraction";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.CATEGORY_START;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.SAL_AMMONIAC_BUCKET.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}