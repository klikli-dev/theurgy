// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record InWorldHUDSnapshot(Optional<Component> title, List<Component> lines, List<ItemStack> items) {

    public static final InWorldHUDSnapshot EMPTY = new InWorldHUDSnapshot(Optional.empty(), List.of(), List.of());

    public static final StreamCodec<RegistryFriendlyByteBuf, InWorldHUDSnapshot> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC,
            InWorldHUDSnapshot::title,
            ComponentSerialization.TRUSTED_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            InWorldHUDSnapshot::lines,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            InWorldHUDSnapshot::items,
            InWorldHUDSnapshot::new
    );

    public InWorldHUDSnapshot {
        title = title == null ? Optional.empty() : title;
        lines = List.copyOf(lines);
        items = items.stream().filter(stack -> !stack.isEmpty()).map(ItemStack::copy).toList();
    }

    public boolean isEmpty() {
        return this.title.isEmpty() && this.lines.isEmpty() && this.items.isEmpty();
    }
}
