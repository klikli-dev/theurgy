// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.network.Message;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class MessageSetDivinationResult implements Message {
    public static final Type<MessageSetDivinationResult> TYPE = new Type<>(Theurgy.loc("set_divination_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSetDivinationResult> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
                    (m) -> Optional.ofNullable(m.pos),
                    ByteBufCodecs.BYTE,
                    (m) -> m.distance,
                    (pos, distance) -> new MessageSetDivinationResult(pos.orElse(null), distance)
            );

    public final BlockPos pos;
    public final byte distance;

    public MessageSetDivinationResult(BlockPos pos, float distance) {
        this.pos = pos;
        this.distance = (byte) Math.min(256, distance);
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof DivinationRodItem) {
            var tag = stack.getOrCreateTag();
            tag.putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, this.distance);
            if (this.pos != null) {
                tag.putLong(TheurgyConstants.Nbt.Divination.POS, this.pos.asLong());
            }
            player.inventoryMenu.broadcastChanges();
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
