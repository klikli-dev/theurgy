// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectedPoint;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.logistics.Wire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CaloricFluxEmitterSelectedPoint extends SelectedPoint<CaloricFluxEmitterSelectedPoint> {

    public static final Codec<CaloricFluxEmitterSelectedPoint> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            BlockPos.CODEC.fieldOf("blockPos").forGetter(SelectedPoint::getBlockPos)
                    )
                    .apply(instance, CaloricFluxEmitterSelectedPoint::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CaloricFluxEmitterSelectedPoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SelectedPoint::getBlockPos,
            CaloricFluxEmitterSelectedPoint::new
    );

    public static final Codec<List<CaloricFluxEmitterSelectedPoint>> LIST_CODEC = Codec.list(CODEC);

    public static final Color color = new Color(0xDDC166, false);

    public CaloricFluxEmitterSelectedPoint(BlockPos blockPos) {
        super(blockPos);
    }

    protected CaloricFluxEmitterSelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        super(level, blockPos, blockState);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_CALORIC_FLUX_EMITTER);
    }

    @Override
    public boolean cycleMode() {
        //has no mode
        //but double-selecting removes the point
        return false;
    }

    @Override
    public Codec<CaloricFluxEmitterSelectedPoint> codec() {
        return CODEC;
    }
}
