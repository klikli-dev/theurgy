// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

@FunctionalInterface
public interface TriConsumer<T, U, S> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param s the third input argument
     */
    void accept(T t, U u, S s);
}