// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectedPoint;
import com.klikli_dev.theurgy.content.render.Color;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LogisticsCapabilityProxySelectedPoint extends SelectedPoint<LogisticsCapabilityProxySelectedPoint> {

    public static final Codec<LogisticsCapabilityProxySelectedPoint> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            BlockPos.CODEC.fieldOf("blockPos").forGetter(SelectedPoint::getBlockPos)
                    )
                    .apply(instance, LogisticsCapabilityProxySelectedPoint::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LogisticsCapabilityProxySelectedPoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SelectedPoint::getBlockPos,
            LogisticsCapabilityProxySelectedPoint::new
    );

    public static final Codec<List<LogisticsCapabilityProxySelectedPoint>> LIST_CODEC = Codec.list(CODEC);

    public static final Color COLOR = new Color(0x00FFFF, false);

    public LogisticsCapabilityProxySelectedPoint(BlockPos blockPos) {
        super(blockPos);
    }

    protected LogisticsCapabilityProxySelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        super(level, blockPos, blockState);
    }

    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_LOGISTICS_CAPABILITY_PROXY);
    }

    @Override
    public boolean cycleMode() {
        return false;
    }

    @Override
    public Codec<LogisticsCapabilityProxySelectedPoint> codec() {
        return CODEC;
    }
}
