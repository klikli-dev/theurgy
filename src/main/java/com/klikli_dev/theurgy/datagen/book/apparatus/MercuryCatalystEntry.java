package com.klikli_dev.theurgy.datagen.book.apparatus;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.crafting.Ingredient;

public class MercuryCatalystEntry extends EntryProvider {

    public static final String ENTRY_ID = "mercury_catalyst";

    public MercuryCatalystEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(ItemRegistry.MERCURY_CATALYST.get()))
                .withText(this.context().pageText())
                .build());
        this.pageText("""
                        This apparatus slowly converts {0} into [#]($PURPLE)Mercury Flux[#](), which can be used as a source of Energy. It is the first step towards using the Energy contained in matter.
                        """,
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.page("usage", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Usage");
        this.pageText(
                """
                        Place the {0}, then insert {1} by right-clicking the oven with it. The Catalyst will begin to slowly fill up with [#]($PURPLE)Mercury Flux[#]().
                        \\
                        \\
                        It's sides will turn more and more blue as it fills up.
                        """,
                this.itemLink(ItemRegistry.MERCURY_CATALYST.get()),
                this.itemLink(ItemRegistry.MERCURY_SHARD.get())
        );

        this.page("recipe", () -> BookCraftingRecipePageModel.builder()
                .withRecipeId1(Theurgy.loc("crafting/shaped/mercury_catalyst"))
                .build());
    }

    @Override
    protected String entryName() {
        return "Mercury Catalyst";
    }

    @Override
    protected String entryDescription() {
        return "Accessing Raw Energy";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(ItemRegistry.MERCURY_CATALYST.get());
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
