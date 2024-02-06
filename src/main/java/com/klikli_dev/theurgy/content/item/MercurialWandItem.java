package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.content.render.itemhud.ItemHUDProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MercurialWandItem extends Item implements ItemHUDProvider {
    public MercurialWandItem(Properties pProperties) {
        super(pProperties);
    }

    public void changeMode(Player player, ItemStack stack, int shift) {
        //TODO: when we change mode we should run  player.displayClientMessage() to show the new mode above the hotbar
    }

    @Override
    public void appendHUDText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        pTooltipComponents.add(Component.translatable("test"));
    }
}
