// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.logistics.Wire;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Message;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MessageAddWires implements Message {

    public static final Type<MessageAddWires> TYPE = new Type<>(Theurgy.loc("add_wires"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageAddWires> STREAM_CODEC =
            StreamCodec.composite(
                    Wire.STREAM_CODEC.apply(ByteBufCodecs.collection(ObjectOpenHashSet::new)),
                    (m) -> m.wires,
                    MessageAddWires::new
            );

    public final Set<Wire> wires;

    public MessageAddWires(Set<Wire> wires) {
        this.wires = wires;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        this.wires.forEach(Wires.get(player.level())::addWire);
    }
}
