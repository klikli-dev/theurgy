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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;


public class MessageRequestCaloricFluxEmitterSelection implements Message {

    public static final ResourceLocation ID = Theurgy.loc("request_caloric_flux_emitter_selection");

    private BlockPos blockPos;

    public MessageRequestCaloricFluxEmitterSelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public MessageRequestCaloricFluxEmitterSelection(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        BlockRegistry.CALORIC_FLUX_EMITTER.get().selectionBehaviour().onPlace(this.blockPos, player);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
