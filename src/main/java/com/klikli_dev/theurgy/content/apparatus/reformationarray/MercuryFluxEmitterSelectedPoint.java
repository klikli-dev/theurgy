// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

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

public class MercuryFluxEmitterSelectedPoint extends SelectedPoint<MercuryFluxEmitterSelectedPoint> {

    public static final Codec<MercuryFluxEmitterSelectedPoint> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            BlockPos.CODEC.fieldOf("blockPos").forGetter(SelectedPoint::getBlockPos)
                    )
                    .apply(instance, MercuryFluxEmitterSelectedPoint::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MercuryFluxEmitterSelectedPoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SelectedPoint::getBlockPos,
            MercuryFluxEmitterSelectedPoint::new
    );

    public static final Codec<List<MercuryFluxEmitterSelectedPoint>> LIST_CODEC = Codec.list(CODEC);

    public static final Color color = new Color(0xDDC166, false);

    public MercuryFluxEmitterSelectedPoint(BlockPos blockPos) {
        super(blockPos);
    }

    protected MercuryFluxEmitterSelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        super(level, blockPos, blockState);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_MERCURY_FLUX_EMITTER);
    }

    @Override
    public boolean cycleMode() {
        return false;
    }

    @Override
    public Codec<MercuryFluxEmitterSelectedPoint> codec() {
        return CODEC;
    }
}
