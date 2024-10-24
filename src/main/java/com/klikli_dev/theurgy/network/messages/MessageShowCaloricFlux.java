// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class MessageShowCaloricFlux implements Message {
    public static final Type<MessageShowCaloricFlux> TYPE = new Type<>(Theurgy.loc("show_caloric_flux"));

    public static final Color COLOR = new Color(0xdb3f07, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowCaloricFlux> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.from,
                    BlockPos.STREAM_CODEC,
                    (m) -> m.to,
                    Direction.STREAM_CODEC,
                    (m) -> m.emitterDirection,
                    MessageShowCaloricFlux::new
            );

    private final BlockPos from;
    private final BlockPos to;

    private final Direction emitterDirection;

    public MessageShowCaloricFlux(BlockPos from, BlockPos to, Direction emitterDirection) {
        this.from = from;
        this.to = to;
        this.emitterDirection = emitterDirection;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        var level = player.level();
        var normal = Vec3.atLowerCornerOf(this.emitterDirection.getNormal());
        var from = Vec3.atCenterOf(this.from).subtract(normal.scale(0.5));
        var to = Vec3.atCenterOf(this.to);

        //discard the message if source/target are not loaded or if the player is too far
        if (level.isLoaded(this.to) && level.isLoaded(this.from) && level.isClientSide && player.getOnPos().distSqr(this.from) < 15 * 15) {
            FollowProjectile projectile = new FollowProjectile(level, from, to, COLOR, 0.1f);
            projectile.setDeltaMovement(normal.scale(0.3f));

            EntityUtil.spawnEntityClientSide(level, projectile);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
