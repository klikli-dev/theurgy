// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.filter.ListFilterMenu;
import com.klikli_dev.theurgy.network.Message;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;


public class MessageSetListFilterScreenOption implements Message {
    public static final Type<MessageSetListFilterScreenOption> TYPE = new Type<>(Theurgy.loc("set_list_filter_screen_option"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSetListFilterScreenOption> STREAM_CODEC =
            StreamCodec.composite(
                    Option.STREAM_CODEC,
                    (m) -> m.option,
                    MessageSetListFilterScreenOption::new
            );
    public final Option option;

    public MessageSetListFilterScreenOption(Option option) {
        this.option = option;
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        if (player.containerMenu instanceof ListFilterMenu menu) {
            if (this.option == Option.ACCEPT_LIST)
                menu.isDenyList = false;
            if (this.option == Option.DENY_LIST)
                menu.isDenyList = true;
            if (this.option == Option.RESPECT_DATA)
                menu.respectDataComponents = true;
            if (this.option == Option.IGNORE_DATA)
                menu.respectDataComponents = false;
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Option {
        ACCEPT_LIST, DENY_LIST, RESPECT_DATA, IGNORE_DATA, UPDATE_FILTER_ITEM;

        private static final IntFunction<Option> BY_ID = ByIdMap.continuous(Enum::ordinal, Option.values(), ByIdMap.OutOfBoundsStrategy.WRAP);

        public static final StreamCodec<ByteBuf, Option> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Option::ordinal);
    }
}
