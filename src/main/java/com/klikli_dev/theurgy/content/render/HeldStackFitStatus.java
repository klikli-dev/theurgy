// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum HeldStackFitStatus {
    FITS,
    DOES_NOT_FIT,
    NOT_APPLICABLE;

    public static final IntFunction<HeldStackFitStatus> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, HeldStackFitStatus> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, HeldStackFitStatus::ordinal);

    public static HeldStackFitStatus of(boolean fits) {
        return fits ? FITS : DOES_NOT_FIT;
    }
}
