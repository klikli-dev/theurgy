// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted.transmutation;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;

public class ConvertToOtherTypeEntry extends EntryProvider {
    public static final String ENTRY_ID = "convert_to_other_type";

    public ConvertToOtherTypeEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Transmutation");
        this.pageText("""
                Transmutation is the conversion of one type of matter into another (e.g. gems into metals).
                \\
                \\
                Thus, transmutation deals with the already discussed challenge of Sulfur resisting significant change.
                """
        );

        this.page("intro2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Transmutation");
        this.pageText("""
                Due to inherent differences of the types of matter there is often a loss or gain, depending on the direction of conversion.\\
                Generally, 2 metals convert into 1 gem and vice versa.
                Similarly, 2 other minerals convert into 1 metal and vice versa.
                Consequently 4 other minerals convert into 1 gem and vice versa.
                """
        );

        this.page("process", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                Transmutation consists of three steps.
                \\
                \\
                First, the Sulfur to be transformed is fermented alchemically into Niter (e.g. Quartz Sulfur into Common Gems Niter).\\
                Then that malleable Niter can be reformed into another Niter of the desired type (e.g. Common Gems Niter into Common Metals Niter).
                """
        );

        this.page("process2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Process");
        this.pageText("""
                Finally that Niter can be reformed again into the desired Sulfur (e.g. Common Metals Niter into Iron Sulfur).
                """
        );

        this.page("limitations", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Limitations");
        this.pageText("""
                While fermentation into niter overcomes the limitation of *type*, the limitation of *tier* (Abundant, Common, Rare, ... ) remains.
                \\
                Conversion of Iron into Gold remains out of reach just yet.
                \\
                \\
                This process will be tackled after you have succeeded in transmutation.
                """
        );

        this.page("instructions", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build()
        );
        this.pageTitle("Instructions");
        this.pageText("""
                        The following entries will guide you through the conversion of {0} into {1}, *assuming you already obtained at least one Sulfur of each*.
                        \\
                        \\
                        The instructions also apply to all other conversions within the same tier.
                        """,
                this.itemLink("Alchemical Sulfur: Quartz", SulfurRegistry.QUARTZ.get()),
                this.itemLink("Alchemical Sulfur: Iron", SulfurRegistry.IRON.get())
        );
    }

    @Override
    protected String entryName() {
        return "Replication by Transmutation";
    }

    @Override
    protected String entryDescription() {
        return "Obtaining more of an item by converting items of the same tier, but another type";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(0, 2); //the third type of background which has no shorthand in EntryBackground
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(this.modLoc("textures/gui/book/convert_types.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}
