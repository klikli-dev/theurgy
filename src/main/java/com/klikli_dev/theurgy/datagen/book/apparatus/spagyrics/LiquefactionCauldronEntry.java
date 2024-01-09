package com.klikli_dev.theurgy.datagen.book.apparatus.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class LiquefactionCauldronEntry extends EntryProvider {

    public static final String ENTRY_ID = "liquefaction_cauldron";

    public LiquefactionCauldronEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.LIQUEFACTION_CAULDRON.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                 Liquefaction allows the extraction of [#]($PURPLE)Alchemical Sulfur[#]() from matter. In the this cauldron a [#]($PURPLE)Solvent[#](), usually a type of acid, is used to dissolve the target object, then the resulting solution is heated to evaporate the solvent and leave behind the Sulfur.
                """);

        this.page("multiblock", () -> BookMultiblockPageModel.builder()
                .withMultiblockId(Theurgy.loc("placement/liquefaction_cauldron"))
                .build());

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0} on top of a {1}, and fill it with a Solvent by right-clicking with a solvent-filled bucket.
                        \\
                        \\
                        Then insert the item to liquefy by right-clicking the cauldron with it.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get()),
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/liquefaction_cauldron"))
                .build());

        this.page("working", () -> BookImagePageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withImages(this.modLoc("textures/gui/book/liquefaction_cauldron_working.png"))
                .build());
        this.pageTitle("Working Correctly");
        this.pageText(
                """
                        If the {0} is working properly, it will show bubbles.
                        """,
                this.itemLink(ItemRegistry.LIQUEFACTION_CAULDRON.get())
        );

    }

    @Override
    protected String entryName() {
        return "Liquefaction Cauldron";
    }

    @Override
    protected String entryDescription() {
        return "Extracting ALchemical Sulfur from Matter";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.LIQUEFACTION_CAULDRON.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
