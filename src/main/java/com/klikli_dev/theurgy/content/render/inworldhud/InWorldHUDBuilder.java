// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InWorldHUDBuilder {

    private final List<Component> lines = new ArrayList<>();
    private final List<ItemStack> items = new ArrayList<>();
    private @Nullable Component title;

    public void setTitleIfAbsent(Component title) {
        if (this.title == null) {
            this.title = title;
        }
    }

    public void addLine(Component line) {
        if (!this.lines.contains(line)) {
            this.lines.add(line);
        }
    }

    public void addItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        this.items.add(stack.copy());
    }

    public void append(InWorldHUDSnapshot snapshot) {
        snapshot.title().ifPresent(this::setTitleIfAbsent);
        snapshot.lines().forEach(this::addLine);
        snapshot.items().forEach(this::addItem);
    }

    public InWorldHUDSnapshot build() {
        if (this.title == null && this.lines.isEmpty() && this.items.isEmpty()) {
            return InWorldHUDSnapshot.EMPTY;
        }
        return new InWorldHUDSnapshot(Optional.ofNullable(this.title), this.lines, this.items);
    }
}
