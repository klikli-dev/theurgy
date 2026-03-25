// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Sent from server to client after datapack sync to inform which sulfur item stacks
 * should not be rendered in modonomicon (because they have no liquefaction recipe).
 */
public record MessageSyncSulfursWithoutRecipe(List<ItemStack> sulfursWithoutRecipe) implements Message {

    public static final Type<MessageSyncSulfursWithoutRecipe> TYPE = new Type<>(Theurgy.loc("sync_sulfurs_without_recipe"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSyncSulfursWithoutRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
                    MessageSyncSulfursWithoutRecipe::sulfursWithoutRecipe,
                    MessageSyncSulfursWithoutRecipe::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        this.sulfursWithoutRecipe.forEach(PageRendererRegistry::registerItemStackNotToRender);
    }
}
