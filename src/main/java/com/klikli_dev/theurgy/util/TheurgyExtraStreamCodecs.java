// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.codec.StreamCodec;

public class TheurgyExtraStreamCodecs {

    public static <B, F, S> StreamCodec<B, Pair<F, S>> pair(StreamCodec<B, F> firstCodec, StreamCodec<B, S> secondCodec) {
        return StreamCodec.composite(
                firstCodec,
                Pair::getFirst,
                secondCodec,
                Pair::getSecond,
                Pair::of
        );
    }
}