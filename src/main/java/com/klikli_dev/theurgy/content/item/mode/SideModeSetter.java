// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.SideMode;
import net.minecraft.core.Direction;

/**
 * Interface for blocks that allow configuring per-side mercury flux modes.
 * Used by the Mercurial Wand to cycle side configurations.
 */
public interface SideModeSetter {
    /**
     * Get the current mode for a specific side.
     */
    SideMode getSideMode(Direction direction);

    /**
     * Set the mode for a specific side.
     */
    void setSideMode(Direction direction, SideMode mode);

    /**
     * Cycle to the next mode for a specific side.
     * Returns the new mode after cycling.
     */
    default SideMode cycleSideMode(Direction direction) {
        var current = this.getSideMode(direction);
        var next = switch (current) {
            case NONE -> SideMode.INPUT;
            case INPUT -> SideMode.OUTPUT;
            case OUTPUT -> SideMode.BOTH;
            case BOTH -> SideMode.NONE;
        };
        this.setSideMode(direction, next);
        return next;
    }
}