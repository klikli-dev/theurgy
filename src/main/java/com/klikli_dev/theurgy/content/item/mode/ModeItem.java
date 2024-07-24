// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import com.klikli_dev.theurgy.content.item.HandlesOnScroll;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ModeItem extends HandlesOnScroll {
    void changeMode(Player player, ItemStack stack, int shift);

    @Override
    default void onScroll(Player player, ItemStack stack, int shift) {
        Networking.sendToServer(new MessageSetMode(shift));
    }


}
