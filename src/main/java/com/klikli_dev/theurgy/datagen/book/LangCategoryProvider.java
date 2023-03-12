/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import net.minecraftforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public interface LangCategoryProvider {

    LanguageProvider getLanguageProvider();

    void setLanguageProvider(LanguageProvider languageProvider);


    default void add(String key, String value) {
      this.getLanguageProvider().add(key, value);
    }
}
