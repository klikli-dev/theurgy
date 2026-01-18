// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.renderer;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DivinationDistanceProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<DivinationDistanceProperty> MAP_CODEC = MapCodec.unit(new DivinationDistanceProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (stack.getOrDefault(DataComponentRegistry.DIVINATION_DISTANCE, -1.0f) < 0)
            return 0.0f; // Default or 'not found' value
        return stack.get(DataComponentRegistry.DIVINATION_DISTANCE);
    }

    @Override
    public @NotNull MapCodec<DivinationDistanceProperty> type() {
        return MAP_CODEC;
    }
}
