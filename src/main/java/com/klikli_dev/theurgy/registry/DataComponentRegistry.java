/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.logistics.WireEndPoint;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DataComponentRegistry {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Theurgy.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MERCURY_FLUX_STORAGE = register("mercury_flux_storage", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MERCURY_FLUX_TO_CONVERT = register("mercury_flux_to_convert", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CURRENT_MERCURY_FLUX_PER_TICK = register("current_mercury_flux_per_tick", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> DIVINATION_DISTANCE = register("divination_distance", builder -> builder
            .persistent(Codec.FLOAT)
            .networkSynchronized(ByteBufCodecs.FLOAT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> DIVINATION_POS = register("divination_pos", builder -> builder
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WireEndPoint>> WIRE_END_POINT = register("wire_end_point", builder -> builder
            .persistent(WireEndPoint.CODEC.codec())
            .networkSynchronized(WireEndPoint.STREAM_CODEC)
            .cacheEncoding()
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return DATA_COMPONENTS.register(name, () -> unaryOperator.apply(DataComponentType.builder()).build());
    }
}
