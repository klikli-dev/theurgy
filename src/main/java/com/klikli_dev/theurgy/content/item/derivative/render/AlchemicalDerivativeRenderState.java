// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.derivative.render;

import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable render state for alchemical derivative items (sulfurs, niters).
 * <p>
 * This record holds only non-level-owned data so that changes (e.g. shift key state)
 * are detected by the renderer's equality check and trigger a re-render.
 *
 * @param sourceItem   the source item holder, if specified via data component (may be null)
 * @param sourceTag    the source tag, if specified via data component (may be null)
 * @param tier         the derivative tier (ABUNDANT, COMMON, RARE, PRECIOUS)
 * @param jarIconItem  the empty jar icon item for this derivative type
 * @param renderSource whether the source item should be rendered on the jar
 * @param shiftDown    whether the shift key is currently held
 */
public record AlchemicalDerivativeRenderState(
        @Nullable Holder<Item> sourceItem,
        @Nullable TagKey<Item> sourceTag,
        AlchemicalDerivativeTier tier,
        Item jarIconItem,
        boolean renderSource,
        boolean shiftDown
) {
}
