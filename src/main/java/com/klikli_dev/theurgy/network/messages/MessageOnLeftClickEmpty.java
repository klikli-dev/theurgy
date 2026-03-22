// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.HandlesOnLeftClick;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

public class MessageOnLeftClickEmpty implements Message {
    public static final Type<MessageOnLeftClickEmpty> TYPE = new Type<>(Theurgy.loc("on_left_click_empty"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageOnLeftClickEmpty> STREAM_CODEC =
            StreamCodec.composite(
                    NeoForgeStreamCodecs.enumCodec(InteractionHand.class),
                    (m) -> m.hand,
                    MessageOnLeftClickEmpty::new
            );

    InteractionHand hand;

    public MessageOnLeftClickEmpty(InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(this.hand);
        if (stack.getItem() instanceof HandlesOnLeftClick leftClickableItem) {
            leftClickableItem.onLeftClickEmpty(player.level(), player, this.hand);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
