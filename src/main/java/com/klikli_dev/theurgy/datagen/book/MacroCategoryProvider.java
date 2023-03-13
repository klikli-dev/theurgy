/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.book;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public interface MacroCategoryProvider {

    Map<String, String> MACROS = new HashMap<>();

    String COLOR_PURPLE = "ad03fc";

    default void registerMacro(String macro, String value){
        MACROS.put(macro, value);
    }

    default void registerDefaultMacros(){
        this.registerMacro("$PURPLE", COLOR_PURPLE);
    }

    default String macro(String input){
        for (var entry : MACROS.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }

    default String format(String pattern, Object... arguments){
        return MessageFormat.format(pattern, arguments);
    }

    default String entryLink(String category, String entry){
        return this.entryLink("", category, entry);
    }
    default String entryLink(String text, String category, String entry){
        return this.format("[{0}](entry://{1}/{2})", text, category, entry);
    }
}
