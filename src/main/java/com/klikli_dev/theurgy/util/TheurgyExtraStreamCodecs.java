// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.function.IntFunction;

public class TheurgyExtraStreamCodecs {

    public static final StreamCodec<ByteBuf, Vec3> VEC3_FLOAT = new StreamCodec<>() {
        public Vec3 decode(ByteBuf buf) {
            return new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        public void encode(ByteBuf buf, Vec3 vec) {
            buf.writeFloat((float) vec.x);
            buf.writeFloat((float) vec.y);
            buf.writeFloat((float) vec.z);
        }
    };
    private static final IntFunction<Tiers> TIERS_BY_ID = ByIdMap.continuous(Enum::ordinal, Tiers.values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, Tiers> TIERS_STREAM_CODEC = ByteBufCodecs.idMapper(TIERS_BY_ID, Tiers::ordinal);

    public static <B, F, S> StreamCodec<B, Pair<F, S>> pair(StreamCodec<B, F> firstCodec, StreamCodec<B, S> secondCodec) {
        return StreamCodec.composite(
                firstCodec,
                Pair::getFirst,
                secondCodec,
                Pair::getSecond,
                Pair::of
        );
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> pCodec1,
            final Function<C, T1> pGetter1,
            final StreamCodec<? super B, T2> pCodec2,
            final Function<C, T2> pGetter2,
            final StreamCodec<? super B, T3> pCodec3,
            final Function<C, T3> pGetter3,
            final StreamCodec<? super B, T4> pCodec4,
            final Function<C, T4> pGetter4,
            final StreamCodec<? super B, T5> pCodec5,
            final Function<C, T5> pGetter5,
            final StreamCodec<? super B, T6> pCodec6,
            final Function<C, T6> pGetter6,
            final StreamCodec<? super B, T7> pCodec7,
            final Function<C, T7> pGetter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> pFactory
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = pCodec1.decode(p_330310_);
                T2 t2 = pCodec2.decode(p_330310_);
                T3 t3 = pCodec3.decode(p_330310_);
                T4 t4 = pCodec4.decode(p_330310_);
                T5 t5 = pCodec5.decode(p_330310_);
                T6 t6 = pCodec6.decode(p_330310_);
                T7 t7 = pCodec7.decode(p_330310_);
                return pFactory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                pCodec1.encode(p_332052_, pGetter1.apply(p_331912_));
                pCodec2.encode(p_332052_, pGetter2.apply(p_331912_));
                pCodec3.encode(p_332052_, pGetter3.apply(p_331912_));
                pCodec4.encode(p_332052_, pGetter4.apply(p_331912_));
                pCodec5.encode(p_332052_, pGetter5.apply(p_331912_));
                pCodec6.encode(p_332052_, pGetter6.apply(p_331912_));
                pCodec7.encode(p_332052_, pGetter7.apply(p_331912_));
            }
        };
    }
}