// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.salt.render;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable render state for alchemical salt items.
 * <p>
 * When shift is held, the source item is rendered instead of the salt icon.
 *
 * @param sourceItem the source item holder, if specified via data component (may be null)
 * @param sourceTag  the source tag, if specified via data component (may be null)
 * @param shiftDown  whether the shift key is currently held
 */
public record AlchemicalSaltRenderState(
        @Nullable Holder<Item> sourceItem,
        @Nullable TagKey<Item> sourceTag,
        boolean shiftDown
) {
}
