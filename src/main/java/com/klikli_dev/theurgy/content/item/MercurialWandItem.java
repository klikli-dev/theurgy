package com.klikli_dev.theurgy.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MercurialWandItem extends Item {
    public MercurialWandItem(Properties pProperties) {
        super(pProperties);
    }

    public Component getOverlayComponent(ItemStack stack){
        return null;
    }

    public void changeMode(Player player, ItemStack stack, int shift) {

    }
}
