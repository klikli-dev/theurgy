// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;

import java.util.List;

/**
 * Marker interface for blockentities that represent a logistics node
 */
public interface LogisticsNode {

    /**
     * Visualises the status of the logistics node.
     * Should be called server-side only as the information may not be available client side.
     */
    List<Pair<BlockPos, Integer>> getStatusHighlights();

}
