// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
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
    public void updateTooltip() {
        var tooltip = Component.empty();

        if (this.title != null)
            tooltip.append(this.title.plainCopy().withStyle(s -> s.withColor(HEADER_RGB)));

        // Calculate visible range
        int visibleItemCount = 8; // Maximum number of items to show
        int halfVisibleItemCount = visibleItemCount / 2;
        int start = Math.max(this.state - halfVisibleItemCount, this.min);
        int end = Math.min(start + visibleItemCount, this.max);
        // Adjust if the calculated range is too large
        if (end - start > visibleItemCount) {
            start = end - visibleItemCount;
        }
        // Ensure the range does not exceed the list bounds
        start = Math.max(start, this.min);
        end = Math.min(end, this.max);

        // Indicate more items above, if applicable
        if (start > this.min) {
            tooltip.append("\n").append(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));
        }

        // Append items within the visible range
        for (int i = start; i < end; i++) {
            if (i == this.state)
                tooltip.append("\n").append(Component.empty()
                        .append("-> ")
                        .append(this.options.get(i))
                        .withStyle(ChatFormatting.WHITE));
            else
                tooltip.append("\n").append(Component.empty()
                        .append("> ")
                        .append(this.options.get(i))
                        .withStyle(ChatFormatting.GRAY));
        }

        // Indicate more items below, if applicable
        if (end < this.max) {
            tooltip.append("\n").append(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));
        }

        if (this.hint != null)
            tooltip.append("\n").append(this.hint.plainCopy()
                    .withStyle(s -> s.withColor(HINT_RGB)));
        tooltip.append("\n").append(this.scrollToSelect.plainCopy()
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));

        this.setTooltip(Tooltip.create(tooltip));
    }
}
