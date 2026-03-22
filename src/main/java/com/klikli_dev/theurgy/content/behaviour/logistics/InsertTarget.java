// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.logistics;

import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

public record InsertTarget<T, C>(InserterNodeBehaviour<T, C> inserter, BlockCapabilityCache<T, C> capability) {
}
