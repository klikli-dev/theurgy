// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiIcons;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.gui.IconButton;
import com.klikli_dev.theurgy.content.gui.Indicator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public abstract class AbstractFilterScreen<T extends AbstractFilterMenu> extends AbstractContainerScreen<T> {

    protected GuiTextures background;

    protected IconButton resetButton;
    protected IconButton confirmButton;

    public AbstractFilterScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, GuiTextures background) {
        super(pMenu, pPlayerInventory, pTitle);

        this.background = background;
    }

    @Override
    protected void init() {
        //set width and height before super init so the left/top pos are calculated correctly
        this.imageWidth = Math.max(this.background.width, GuiTextures.PLAYER_INVENTORY.width);
        this.imageHeight = this.background.height + 4 + GuiTextures.PLAYER_INVENTORY.height;
        super.init();

        int x = this.leftPos;
        int y = this.topPos;

        this.resetButton = new IconButton(x + this.background.width - 62, y + this.background.height - 24, GuiIcons.TRASH);
        this.resetButton.withTooltip(Component.translatable(TheurgyConstants.I18n.Gui.FILTER_RESET_BUTTON_TOOLTIP));
        this.resetButton.withOnClick(() -> {
            this.menu.clearContents();
            this.menu.sendClearPacket();
        });
        this.addRenderableWidget(this.resetButton);

        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, GuiIcons.CONFIRM);
        this.confirmButton.withTooltip(Component.translatable(TheurgyConstants.I18n.Gui.FILTER_CONFIRM_BUTTON_TOOLTIP));
        this.confirmButton.withOnClick(() -> {
            this.minecraft.player.closeContainer();
        });
        this.addRenderableWidget(this.confirmButton);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //prevent automatic rendering of container title
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int invX = this.getLeftOfCentered(GuiTextures.PLAYER_INVENTORY.width);
        int invY = this.topPos + this.background.height + 4;
        this.renderPlayerInventory(pGuiGraphics, invX, invY);

        int x = this.leftPos;
        int y = this.topPos;

        this.background.render(pGuiGraphics, x, y);
        pGuiGraphics.drawString(this.font, this.title, x + (this.background.width - 8) / 2 - this.font.width(this.title) / 2, y + 4, this.getScreenTitleColor(), false);
    }

    @Override
    protected void containerTick() {
        if (!ItemStack.isSameItem(this.menu.player.getMainHandItem(), this.menu.contentHolder))
            this.menu.player.closeContainer();

        super.containerTick();

        this.updateButtonState();
        this.updateIndicatorState();
    }

    public int getLeftOfCentered(int textureWidth) {
        return this.leftPos + (this.imageWidth - textureWidth) / 2;
    }

    public void renderPlayerInventory(GuiGraphics graphics, int x, int y) {
        GuiTextures.PLAYER_INVENTORY.render(graphics, x, y);
        graphics.drawString(this.font, this.playerInventoryTitle, x + 8, y + 6, 0x404040, false);
    }

    public void updateButtonState() {
        for (var button : this.getButtons())
            button.active = this.isButtonActive(button);
    }

    public void updateIndicatorState() {
        for (Indicator indicator : this.getIndicators())
            indicator.state = this.isIndicatorOn(indicator) ? Indicator.State.ON : Indicator.State.OFF;
    }

    protected List<MutableComponent> getTooltipDescriptions() {
        return Collections.emptyList();
    }

    protected List<Indicator> getIndicators() {
        return Collections.emptyList();
    }

    protected List<IconButton> getButtons() {
        return Collections.emptyList();
    }

    protected abstract int getScreenTitleColor();

    protected abstract boolean isButtonActive(IconButton button);

    protected abstract boolean isIndicatorOn(Indicator indicator);
}
