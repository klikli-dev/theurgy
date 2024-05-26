// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;


public class MessageRequestSulfuricFluxEmitterSelection implements Message {
    public static final Type<MessageRequestSulfuricFluxEmitterSelection> TYPE = new Type<>(Theurgy.loc("request_sulfuric_flux_emitter_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageRequestSulfuricFluxEmitterSelection> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    (m) -> m.blockPos,
                    MessageRequestSulfuricFluxEmitterSelection::new
            );

    private final BlockPos blockPos;

    public MessageRequestSulfuricFluxEmitterSelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour().onPlace(this.blockPos, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
