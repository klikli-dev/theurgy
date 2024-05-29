/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.logistics.WireEndPoint;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DataComponentRegistry {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Theurgy.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MERCURY_FLUX_STORAGE = DATA_COMPONENTS.registerComponentType("mercury_flux_storage", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MERCURY_FLUX_TO_CONVERT = DATA_COMPONENTS.registerComponentType("mercury_flux_to_convert", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CURRENT_MERCURY_FLUX_PER_TICK = DATA_COMPONENTS.registerComponentType("current_mercury_flux_per_tick", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> DIVINATION_DISTANCE = DATA_COMPONENTS.registerComponentType("divination_distance", builder -> builder
            .persistent(Codec.FLOAT)
            .networkSynchronized(ByteBufCodecs.FLOAT)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> DIVINATION_POS = DATA_COMPONENTS.registerComponentType("divination_pos", builder -> builder
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WireEndPoint>> WIRE_END_POINT = DATA_COMPONENTS.registerComponentType("wire_end_point", builder -> builder
            .persistent(WireEndPoint.CODEC)
            .networkSynchronized(WireEndPoint.STREAM_CODEC)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MercurialWandItemMode>> MERCURIAL_WAND_ITEM_MODE = DATA_COMPONENTS.registerComponentType("mercurial_wand_item_mode", builder -> builder
            .persistent(MercurialWandItemMode.CODEC)
            .networkSynchronized(MercurialWandItemMode.STREAM_CODEC)
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Item>>> SULFUR_SOURCE_ITEM = DATA_COMPONENTS.registerComponentType("sulfur_source_item", builder -> builder
            .persistent(BuiltInRegistries.ITEM.holderByNameCodec())
            .networkSynchronized(ByteBufCodecs.holderRegistry(Registries.ITEM))
            .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TagKey<Item>>> SULFUR_SOURCE_TAG = DATA_COMPONENTS.registerComponentType("sulfur_source_tag", builder -> builder
            .persistent(TagKey.codec(Registries.ITEM))
            .networkSynchronized(ByteBufCodecs.fromCodec(TagKey.codec(Registries.ITEM)))
            .cacheEncoding()
    );
}
