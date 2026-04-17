// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import java.util.List;

/**
 * Marker interface for blockentities that represent have a leaf node behaviour.
 * Such BEs are always logistics nodes.
 */
public interface HasLeafNodeBehaviour<T, C> extends LogisticsNode {
    LeafNodeBehaviour<T, C> leafNode();

    default List<? extends LeafNodeBehaviour<?, ?>> leafNodes() {
        return List.of(this.leafNode());
    }
}
