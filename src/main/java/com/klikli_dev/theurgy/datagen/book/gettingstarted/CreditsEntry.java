// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.book.gettingstarted;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;

public class CreditsEntry extends EntryProvider {

    public static final String ENTRY_ID = "credits";

    public CreditsEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Credit where Credit is Due");
        this.pageText("""
                As any mod, Theurgy would not have been possible without the help of a large community. I would ike to thank all the modders, artists, packmakers and players who have contributed to the Minecraft modding community.
                """);

        this.page("open_source", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Open Source");
        this.pageText("""
                Beyond the direct contributions to the ecosystem and to this mod directly, Theurgy also uses a number of Open Source libraries and tools, as well as source code from other Open Source mods.
                \\
                \\
                I also encourage everyone to check out the [Source Code of Theurgy on GitHub](https://github.com/klikli-dev/theurgy). Feel free to contribute - or see if anything in the code can help you with your own projects. 
                """);

        this.page("create", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Thank you, Creators of Create!");
        this.pageText("""
               Filter functionality, including all textures, is largely based on the Filters found in the Mod "Create". [Click here to learn more about Create](https://github.com/Creators-of-Create/Create).
               \\
               \\
               This adaption is possible due to Create being Open Source, licensed under the MIT License. A sincere thank you to the creators of create for their foundational work.
               """
        );
    }

    @Override
    protected String entryName() {
        return "Credits";
    }

    @Override
    protected String entryDescription() {
        return "Credit where Credit is Due";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return Pair.of(1, 0);
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.DIAMOND);
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}