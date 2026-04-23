// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record LogisticsNexusPairing(UUID id, GlobalPos pos) {
    public static final Codec<LogisticsNexusPairing> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(LogisticsNexusPairing::id),
            GlobalPos.CODEC.fieldOf("pos").forGetter(LogisticsNexusPairing::pos)
    ).apply(instance, LogisticsNexusPairing::new));
}
