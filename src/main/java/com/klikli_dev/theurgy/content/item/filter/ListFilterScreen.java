// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiIcons;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.gui.IconButton;
import com.klikli_dev.theurgy.content.gui.Indicator;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetListFilterScreenOption;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;
import java.util.List;

public class ListFilterScreen extends AbstractFilterScreen<ListFilterMenu> {

    protected IconButton acceptListButton;
    protected IconButton denyListButton;

    protected IconButton respectDataComponentsButton;
    protected IconButton ignoreDataComponentsButton;

    protected Indicator acceptListIndicator;
    protected Indicator denyListIndicator;

    protected Indicator respectDataComponentsIndicator;
    protected Indicator ignoreDataComponentsIndicator;

    public ListFilterScreen(ListFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GuiTextures.FILTER);
    }

    @Override
    protected int getScreenTitleColor() {
        return 0x303030;
    }

    @Override
    protected boolean isButtonActive(IconButton button) {
        if (button == this.denyListButton)
            return !this.menu.isDenyList;
        if (button == this.acceptListButton)
            return this.menu.isDenyList;
        if (button == this.respectDataComponentsButton)
            return !this.menu.respectDataComponents;
        if (button == this.ignoreDataComponentsButton)
            return this.menu.respectDataComponents;
        return true;
    }

    @Override
    protected boolean isIndicatorOn(Indicator indicator) {
        if (indicator == this.denyListIndicator)
            return this.menu.isDenyList;
        if (indicator == this.acceptListIndicator)
            return !this.menu.isDenyList;
        if (indicator == this.respectDataComponentsIndicator)
            return this.menu.respectDataComponents;
        if (indicator == this.ignoreDataComponentsIndicator)
            return !this.menu.respectDataComponents;
        return false;
    }

    @Override
    protected List<IconButton> getButtons() {
        return Arrays.asList(this.denyListButton, this.acceptListButton, this.respectDataComponentsButton, this.ignoreDataComponentsButton);
    }

    @Override
    protected List<Indicator> getIndicators() {
        return Arrays.asList(this.denyListIndicator, this.acceptListIndicator, this.respectDataComponentsIndicator, this.ignoreDataComponentsIndicator);
    }

    @Override
    protected void init() {
        super.init();

        int x = this.leftPos;
        int y = this.topPos;

        this.denyListButton = new IconButton(x + 18, y + 75, GuiIcons.DENY_LIST);
        this.denyListButton.withTooltip(
                TheurgyConstants.I18n.Gui.LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT
        );
        this.denyListButton.withOnClick(() -> {
            this.menu.isDenyList = true;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.DENY_LIST);
        });
        this.addRenderableWidget(this.denyListButton);

        this.acceptListButton = new IconButton(x + 36, y + 75, GuiIcons.ACCEPT_LIST);
        this.acceptListButton.withTooltip(
                TheurgyConstants.I18n.Gui.LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP_SHIFT
        );
        this.acceptListButton.withOnClick(() -> {
            this.menu.isDenyList = false;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.ACCEPT_LIST);
        });
        this.addRenderableWidget(this.acceptListButton);

        this.denyListIndicator = new Indicator(x + 18, y + 69, Component.empty());
        this.addRenderableWidget(this.denyListIndicator);

        this.acceptListIndicator = new Indicator(x + 36, y + 69, Component.empty());
        this.addRenderableWidget(this.acceptListIndicator);

        this.respectDataComponentsButton = new IconButton(x + 60, y + 75, GuiIcons.RESPECT_DATA_COMPONENTS);
        this.respectDataComponentsButton.withTooltip(
                TheurgyConstants.I18n.Gui.FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT
        );
        this.respectDataComponentsButton.withOnClick(() -> {
            this.menu.respectDataComponents = true;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.RESPECT_DATA);
        });
        this.addRenderableWidget(this.respectDataComponentsButton);

        this.ignoreDataComponentsButton = new IconButton(x + 78, y + 75, GuiIcons.IGNORE_DATA_COMPONENTS);
        this.ignoreDataComponentsButton.withTooltip(
                TheurgyConstants.I18n.Gui.FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT
        );
        this.ignoreDataComponentsButton.withOnClick(() -> {
            this.menu.respectDataComponents = false;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.IGNORE_DATA);
        });
        this.addRenderableWidget(this.ignoreDataComponentsButton);

        this.respectDataComponentsIndicator = new Indicator(x + 60, y + 69, Component.empty());
        this.addRenderableWidget(this.respectDataComponentsIndicator);

        this.ignoreDataComponentsIndicator = new Indicator(x + 78, y + 69, Component.empty());
        this.addRenderableWidget(this.ignoreDataComponentsIndicator);

        this.updateIndicatorState();
    }

    protected void sendOptionUpdate(MessageSetListFilterScreenOption.Option option) {
        Networking.sendToServer(new MessageSetListFilterScreenOption(option));
    }
}
