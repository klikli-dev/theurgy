package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

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

public class CreateSulfurEntry extends EntryProvider {

    public static final String ENTRY_ID = "create_sulfur";

    public CreateSulfurEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(SulfurRegistry.IRON.get()))
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Solvents");
        this.pageText("""
                Sulfur extraction is the part of ore purification that leads to [#]($PURPLE)multiplication[#](). One Ore or Raw Metal yields multiple sulfurs, which then each can be refined into an ingot.
                    """
        );

        this.page("step1", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Filling the Liquefaction Cauldron");
        this.pageText("""
                        [#]($INPUT)Right-click[#]() the {0} with a {1} to fill it.
                            """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(ItemRegistry.SAL_AMMONIAC_BUCKET.get())
        );

        this.page("step2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Adding Raw Materials");
        this.pageText("""
                        Now [#]($INPUT)right-click[#]() the {0} with the item you want to extract sulfur from, such as {1}. The item will be placed inside.
                            """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(Items.RAW_IRON)
        );

        this.page("step3", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Providing Heat");
        this.pageText("""
                        Now add fuel, such as Coal, to the {0} below the Cauldron to heat it up.
                            """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get()),
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get())
        );

        this.page("step4", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Extracting the Sulfur");
        this.pageText("""
                        After a while some sulfur will have been extracted, you can [#]($INPUT)right-click[#]() the {0} with an empty hand to obtain {1}.
                            """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink("Alchemical Sulfur", SulfurRegistry.IRON.get())
        );
    }

    @Override
    protected String entryName() {
        return "Extracting Sulfur";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining Sulfur - the \"Soul\"";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.CATEGORY_START;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(SulfurRegistry.IRON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}