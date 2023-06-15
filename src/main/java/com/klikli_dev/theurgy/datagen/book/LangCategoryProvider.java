/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import net.minecraftforge.common.data.LanguageProvider;

public interface LangCategoryProvider {

    LanguageProvider getLanguageProvider();

    default void add(String key, String value) {
        this.getLanguageProvider().add(key, value);
    }
}
