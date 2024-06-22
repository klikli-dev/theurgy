package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.gui.IconButton;
import com.klikli_dev.theurgy.content.gui.Indicator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ListFilterScreen extends AbstractFilterScreen<ListFilterMenu>{
    public ListFilterScreen(ListFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GuiTextures.FILTER);
    }

    @Override
    protected int getScreenTitleColor() {
        //For other filter: 0x592424
        return 0x303030;
    }

    @Override
    protected boolean isButtonActive(IconButton button) {
        return true;
    }

    @Override
    protected boolean isIndicatorOn(Indicator indicator) {
        return true;
    }
}
