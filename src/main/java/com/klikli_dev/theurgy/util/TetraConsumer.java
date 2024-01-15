// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

@FunctionalInterface
public interface TetraConsumer<P, Q, S, T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param p the first input argument
     * @param q the second input argument
     * @param s the third input argument
     * @param t the third input argument
     */
    void accept(P p, Q q, S s, T t);
}