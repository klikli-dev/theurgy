// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.MercurialWandItemMode;
import com.klikli_dev.theurgy.content.item.mercurialwand.mode.SelectDirectionMode;
import com.klikli_dev.theurgy.content.item.mode.ModeItem;
import com.klikli_dev.theurgy.logistics.Wire;
import com.klikli_dev.theurgy.network.Message;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Direction;
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


public class MessageItemModeSelectDirection implements Message {
    public static final Type<MessageItemModeSelectDirection> TYPE = new Type<>(Theurgy.loc("select_direction"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageItemModeSelectDirection> STREAM_CODEC =
            StreamCodec.composite(
                    Direction.STREAM_CODEC,
                    (m) -> m.direction,
                    MessageItemModeSelectDirection::new
            );

    public final Direction direction;

    public MessageItemModeSelectDirection(Direction direction) {
        this.direction = direction;
    }


    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof ModeItem modeItem) {
            //theoretically the current mode should be the select direction mode,
            // but if we run into some sort of race condition it is better to just get it directly
            var mode = (SelectDirectionMode) MercurialWandItemMode.Type.SELECT_DIRECTION.mode();
            mode.setDirection(stack, this.direction);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
