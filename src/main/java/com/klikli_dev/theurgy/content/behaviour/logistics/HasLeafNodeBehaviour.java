package com.klikli_dev.theurgy.content.behaviour.logistics;

/**
 * Marker interface for blockentities that represent have a leaf node behaviour.
 * Such BEs are always logistics nodes.
 */
public interface HasLeafNodeBehaviour<T, C> extends LogisticsNode {
    LeafNodeBehaviour<T, C> leafNode();
}
