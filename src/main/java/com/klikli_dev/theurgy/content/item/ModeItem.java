package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ModeItem {
    void changeMode(Player player, ItemStack stack, int shift);

    default void onScroll(Player player, ItemStack stack, int shift) {
        Networking.sendToServer(new MessageSetMode(shift));
    }
}
