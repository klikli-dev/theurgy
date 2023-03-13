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

    /**
     * Adds a new translation with a pattern and arguments, internally using MessageFormat to format the pattern.
     */
    default void add(String key, String pattern, Object... args) {
        this.add(key, this.format(pattern, args));
    }
}
