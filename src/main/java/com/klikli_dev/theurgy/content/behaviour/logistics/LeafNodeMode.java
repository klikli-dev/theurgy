// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

public enum LeafNodeMode {
    INSERT,
    EXTRACT
    //TODO: we may need a mode "mirror" that just mirrors the capability to another node.
    //      in this node if we have multiple source/target nodes we'd need a function to e.g. round-robin between them,
    //      or allow the player to select a fixed target.
}
