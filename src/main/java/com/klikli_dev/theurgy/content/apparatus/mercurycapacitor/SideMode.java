// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycapacitor;

import com.mojang.serialization.Codec;

/**
 * Represents the configuration of a single side of a mercury capacitor.
 * Controls whether the side can receive flux, push flux, both, or neither.
 */
public enum SideMode {
    /**
     * Side does not interact with mercury flux at all.
     */
    NONE,

    /**
     * Side can only receive flux (input).
     * Other blocks can push into this side, but this side won't push out.
     */
    INPUT,

    /**
     * Side can only push flux (output).
     * This side will export flux, but won't accept incoming flux.
     */
    OUTPUT,

    /**
     * Side can both receive and push flux.
     * Acts as a bidirectional connection.
     */
    BOTH;

    public static final Codec<SideMode> CODEC = Codec.STRING.xmap(
            SideMode::fromString,
            SideMode::name
    );

    public static SideMode fromString(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}