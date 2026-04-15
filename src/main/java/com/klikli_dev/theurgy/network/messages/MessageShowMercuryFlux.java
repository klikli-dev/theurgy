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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class MessageShowMercuryFlux implements Message {
    public static final Type<MessageShowMercuryFlux> TYPE = new Type<>(Theurgy.loc("show_mercury_flux"));

    public static final Color COLOR = new Color(0x00FFFF, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowMercuryFlux> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.from,
                    BlockPos.STREAM_CODEC,
                    (m) -> m.to,
                    MessageShowMercuryFlux::new
            );

    private final BlockPos from;
    private final BlockPos to;

    public MessageShowMercuryFlux(BlockPos from, BlockPos to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        var level = player.level();
        var from = Vec3.atCenterOf(this.from);
        var to = Vec3.atCenterOf(this.to);

        //discard the message if source/target are not loaded or if the player is too far
        if (level.isLoaded(this.to) && level.isLoaded(this.from) && level.isClientSide() && player.getOnPos().distSqr(this.from) < 15 * 15) {
            FollowProjectile projectile = new FollowProjectile(level, from, to, COLOR, 0.1f);
            var direction = to.subtract(from).normalize();
            projectile.setDeltaMovement(direction.scale(0.3f));

            EntityUtil.spawnEntityClientSide(level, projectile, true);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}