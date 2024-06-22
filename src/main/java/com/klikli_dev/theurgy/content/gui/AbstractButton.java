// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.util.TriConsumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractButton extends AbstractWidget {

    protected TriConsumer<Double, Double, Integer> onClick = (_$, _$$, _$$$) -> {
    };

    protected AbstractButton(int x, int y) {
        this(x, y, 16, 16);
    }

    protected AbstractButton(int x, int y, int width, int height) {
        this(x, y, width, height, Component.empty());
    }

    protected AbstractButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public <T extends AbstractButton> T withOnClick(TriConsumer<Double, Double, Integer> cb) {
        this.onClick = cb;
        //noinspection unchecked
        return (T) this;
    }

    public <T extends AbstractButton> T withOnClick(Runnable cb) {
        return this.withOnClick((_$, _$$, _$$$) -> cb.run());
    }

    public <T extends AbstractButton> T withTooltip(Tooltip tooltip) {
        this.setTooltip(tooltip);
        //noinspection unchecked
        return (T) this;
    }

    public <T extends AbstractButton> T withTooltip(String component) {
        return this.withTooltip(Component.translatable(component));
    }

    public <T extends AbstractButton> T withTooltip(Component component) {
        return this.withTooltip(Tooltip.create(component));
    }

    public <T extends AbstractButton> T withTooltip(String component, String shiftDownComponent) {
        return this.withTooltip(Component.translatable(component), (Component.translatable(shiftDownComponent)));
    }

    public <T extends AbstractButton> T withTooltip(Component component, Component shiftDownComponent) {
        return this.withTooltip(DynamicTooltip.create(component, shiftDownComponent));
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.onClick.accept(mouseX, mouseY, button);
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }
}
