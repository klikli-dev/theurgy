// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;


public class MessageRequestCaloricFluxEmitterSelection implements Message {
    public static final Type<MessageRequestCaloricFluxEmitterSelection> TYPE = new Type<>(Theurgy.loc("request_caloric_flux_emitter_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageRequestCaloricFluxEmitterSelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    MessageRequestCaloricFluxEmitterSelection::new
            );

    private final BlockPos blockPos;

    public MessageRequestCaloricFluxEmitterSelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onPlace(this.blockPos, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
