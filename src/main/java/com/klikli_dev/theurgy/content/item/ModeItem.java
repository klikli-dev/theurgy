package com.klikli_dev.theurgy.content.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ModeItem {
    void changeMode(Player player, ItemStack stack, int shift);
}
