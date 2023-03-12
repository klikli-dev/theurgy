/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

public interface MacroLangCategoryProvider extends LangCategoryProvider, MacroCategoryProvider {
    @Override
    default void add(String key, String value) {
        this.getLanguageProvider().add(key, this.macro(value));
    }
}
