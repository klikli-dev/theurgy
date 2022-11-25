/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.lang;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.data.LanguageProvider;

public class ENUSProvider extends LanguageProvider {
    public ENUSProvider(DataGenerator gen) {
        super(gen, Theurgy.MODID, "en_us");
    }

    private void addItems() {
        this.add(((TranslatableContents)Theurgy.CREATIVE_MODE_TAB.getDisplayName().getContents()).getKey(), "Empty Jar");

        this.addItem(ItemRegistry.EMPTY_JAR, "Empty Jar");
        this.addItem(ItemRegistry.ALCHEMICAL_SULFUR, "Alchemical Sulfur");
    }

    @Override
    protected void addTranslations() {
        this.addItems();
    }
}
