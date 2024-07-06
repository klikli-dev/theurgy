// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class SelectionScrollInput extends ScrollInput {

    private final MutableComponent scrollToSelect = Component.translatable(TheurgyConstants.I18n.Gui.SCROLL_TO_SELECT);
    protected List<? extends Component> options;

    public SelectionScrollInput(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn);
        this.options = new ArrayList<>();
        this.inverted();
    }

    public ScrollInput forOptions(List<? extends Component> options) {
        this.options = options;
        this.max = options.size();
        this.format(options::get);
        this.updateTooltip();
        return this;
    }

    @Override
    protected void updateTooltip() {
        var tooltip = Component.empty();

        if (this.title != null)
            tooltip.append(this.title.plainCopy().withStyle(s -> s.withColor(HEADER_RGB)));

        int min = Math.min(this.max - 16, this.state - 7);
        int max = Math.max(this.min + 16, this.state + 8);
        min = Math.max(min, this.min);
        max = Math.min(max, this.max);
        if (this.min + 1 == min)
            min--;
        if (min > this.min)
            tooltip.append(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));
        if (this.max - 1 == max)
            max++;
        for (int i = min; i < max; i++) {
            if (i == this.state)
                tooltip.append(Component.empty()
                        .append("-> ")
                        .append(this.options.get(i))
                        .withStyle(ChatFormatting.WHITE));
            else
                tooltip.append(Component.empty()
                        .append("> ")
                        .append(this.options.get(i))
                        .withStyle(ChatFormatting.GRAY));
        }
        if (max < this.max)
            tooltip.append(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));

        if (this.hint != null)
            tooltip.append(this.hint.plainCopy()
                    .withStyle(s -> s.withColor(HINT_RGB)));
        tooltip.append(this.scrollToSelect.plainCopy()
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }
}
