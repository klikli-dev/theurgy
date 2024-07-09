// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.modonomicon.book.page.BookImagePage;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class VatRedstoneEntry extends EntryProvider {

    public static final String ENTRY_ID = "vat_redstone";

    public VatRedstoneEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("redstone_control", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Redstone Control");
        this.add(this.context().pageText(),
                """
                        Vats can be closed by applying a redstone signal (if a valid recipe is present).\\
                        If a redstone signal is applied and ingredients for a valid recipe are inserted, the vat will automatically close.\\
                        \\
                        \\
                        If the redstone signal is turned off the vat will open again.
                        """
        );

        this.page("sides", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()))
                .withImages(
                        this.modLoc("textures/gui/book/redstone_digestion_vat.png"),
                        this.modLoc("textures/gui/book/redstone_fermentation_vat.png")
                );

        ;
        this.add(this.context().pageTitle(), "Sided Behaviour");
        this.add(this.context().pageText(),
                """
                        The side with the dark red (light red if it outputs a redstone signal) dot is where the vat **outputs** redstone. On all other side it accepts redstone input.
                        """
        );

        this.page("redstone_output", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Redstone Output");
        this.add(this.context().pageText(),
                """
                        Vats output a redstone signal when they have at least one item in the output slot.
                        \\
                        \\
                        This can be used unlock the vat when it produced a result to extract it.
                        """
        );

        this.page("analog_output", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Analog Output");
        this.add(this.context().pageText(),
                """
                        Vats output a comparator signal based on the amount of items in the output slot.
                        \\
                        \\
                        This can be used for advanced automation that only unlocks the vat when a certain amount of items is produced.
                        """
        );

    }

    @Override
    protected String entryName() {
        return "Vat Redstone Control";
    }

    @Override
    protected String entryDescription() {
        return "Redstone input and output for fermentation and digestion vats.";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.REDSTONE_TORCH);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
