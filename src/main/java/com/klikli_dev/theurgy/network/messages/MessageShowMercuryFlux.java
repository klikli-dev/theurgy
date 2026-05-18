// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.magicparticleslib.premade.projectile.VisualEntitySpawner;
import com.klikli_dev.magicparticleslib.premade.projectile.glowtrail.GlowTrailProjectile;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class MessageShowMercuryFlux implements Message {
    public static final Type<MessageShowMercuryFlux> TYPE = new Type<>(Theurgy.loc("show_mercury_flux"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowMercuryFlux> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.from,
                    BlockPos.STREAM_CODEC,
                    (m) -> m.to,
                    Direction.STREAM_CODEC,
                    (m) -> m.emitterDirection,
                    MessageShowMercuryFlux::new
            );

    private final BlockPos from;
    private final BlockPos to;
    private final Direction emitterDirection;

    public MessageShowMercuryFlux(BlockPos from, BlockPos to, Direction emitterDirection) {
        this.from = from;
        this.to = to;
        this.emitterDirection = emitterDirection;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        var level = player.level();
        var normal = Vec3.atLowerCornerOf(this.emitterDirection.getUnitVec3i());
        var from = Vec3.atCenterOf(this.from).subtract(normal.scale(0.5));
        var to = Vec3.atCenterOf(this.to);

        //discard the message if source/target are not loaded or if the player is too far
        if (level.isLoaded(this.to) && level.isLoaded(this.from) && level.isClientSide() && player.getOnPos().distSqr(this.from) < 15 * 15) {
            GlowTrailProjectile projectile = new GlowTrailProjectile(level, from, to)
                    .color(ARGB.opaque(0x00FFFF))
                    .size(0.1f)
                    .initialVelocity(normal.scale(0.3f));

            VisualEntitySpawner.spawn(level, projectile, true);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
