// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.apparatus.transmutationandexaltation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.datagen.book.LogisticsCategory;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;

public class BasicVatAutomationEntry extends EntryProvider {

    public static final String ENTRY_ID = "basic_vat_automation";

    public BasicVatAutomationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Basic Vat Automation");
        this.add(this.context().pageText(),
                """
                        Without automation, vats need to be opened and closed manually.\\
                        Because items and fluids can only be inserted or extracted while open, and processing only happens while closed, this requires - potentially tedious - manual supervision.
                        \\
                        \\
                        Using redstone, the opening and closing of the vat can be automated, so that pipes or a {0} can insert and extract items.
                        """,
                this.categoryLink("Mercurial Logistics System", LogisticsCategory.CATEGORY_ID)
        );

        this.page("setup", () -> BookImagePageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()))
                .withImages(
                        this.modLoc("textures/gui/book/vat_automation_0.png"),
                        this.modLoc("textures/gui/book/vat_automation_1.png"),
                        this.modLoc("textures/gui/book/vat_automation_2.png"),
                        this.modLoc("textures/gui/book/vat_automation_3.png")
                );

        ;
        this.add(this.context().pageTitle(), "Block Setup");
        this.add(this.context().pageText(),
                """
                        Click through the pictures to see how to set up the automation.\\
                        Make sure the "front" (the side with the red dot) is facing the redstone wire that is off (not powered by the redstone torch).
                        """
        );

        this.page("usage", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        1. After setting up the blocks as shown, the vat will be open, and the redstone torch will be on.
                        2. Now insert the desired recipe ingredients into the vat.
                        3. As soon as a valid recipe is present, the vat will close, due to the active redstone signal.
                        4. The closed vat will start processing.
                        """
        );

        this.page("usage2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Usage");
        this.add(this.context().pageText(),
                """
                        5. Once the vat produced an output item, it will output a redstone signal.
                        6. This will turn off the redstone torch, opening the vat again.
                        8. The item can now be extracted from the vat.
                        7. Once extracted the output signal turns off, causing the torch and input signal to turn on and the vat to close.
                        """
        );

        this.page("logistics", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "Logistics");
        this.add(this.context().pageText(),
                """
                        The most convenient way to extract the results and refill the ingredients is to set up a basic {0}.
                        1. Set up an *extractor* that targets the *bottom* of the vat and connect it to an inserter that is placed on a chest which will hold the output.
                        2. Set up an *inserter* that targets the *top* of the vat and connect it to an extractor that is placed on a chest which will hold the recipe input.
                        """,
                this.categoryLink("Mercurial Logistics System", LogisticsCategory.CATEGORY_ID)
        );

        this.page("hopper", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "A note on hoppers");
        this.add(this.context().pageText(),
                """
                        Hoppers can be used to insert and extract items from the vat, however the redstone setup needed might be more challenging, as the hopper will be powered (and thus locked) if any adjacent block has redstone. This leads to the problem that the vat powering the redstone diagonally above the hopper will lock the hopper.
                        """
        );

        this.page("insertion", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.add(this.context().pageTitle(), "A note on insertion");
        this.add(this.context().pageText(),
                """
                        Once the vat has no valid recipe any more it will no longer automatically close, allowing the automatic (or also manual) insertion of ingredients.
                        \\
                        \\
                        After sufficient ingredients are in the vat it will close again and continue the automatic process.
                        """
        );

    }

    @Override
    protected String entryName() {
        return "Basic Vat Automation";
    }

    @Override
    protected String entryDescription() {
        return "Automate your vats with redstone signals";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.LEVER);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
