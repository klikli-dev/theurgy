// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;
import java.util.function.Function;

public class ScrollInput extends AbstractButton implements TickableGuiEventListener {

    protected final Component scrollToModify = Component.translatable(TheurgyConstants.I18n.Gui.SCROLL_TO_MODIFY);
    protected final Component shiftScrollsFaster = Component.translatable(TheurgyConstants.I18n.Gui.SCROLL_SHIFT_SCROLLS_FASTER);
    protected Consumer<Integer> onScroll;
    protected int state;
    protected Component title = Component.translatable(TheurgyConstants.I18n.Gui.SCROLL_DEFAULT_TITLE);
    protected Component hint = null;
    protected Label displayLabel;
    protected boolean inverted;
    protected boolean soundPlayed;
    protected Function<Integer, Component> formatter;

    protected int min, max;
    protected int shiftStep;
    Function<StepContext, Integer> step;

    public ScrollInput(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn);
        this.tooltip = new WidgetTooltipHolderWithDefaultPositioner();
        this.state = 0;
        this.min = 0;
        this.max = 1;
        this.shiftStep = 5;
        this.step = this.standardStep();
        this.formatter = i -> Component.literal(String.valueOf(i));
        this.soundPlayed = false;
    }

    public Function<StepContext, Integer> standardStep() {
        return c -> c.shift ? this.shiftStep : 1;
    }

    public ScrollInput inverted() {
        this.inverted = true;
        return this;
    }

    public ScrollInput withRange(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public ScrollInput calling(Consumer<Integer> onScroll) {
        this.onScroll = onScroll;
        return this;
    }

    public ScrollInput format(Function<Integer, Component> formatter) {
        this.formatter = formatter;
        return this;
    }

    public ScrollInput removeCallback() {
        this.onScroll = null;
        return this;
    }

    public ScrollInput titled(MutableComponent title) {
        this.title = title;
        this.updateTooltip();
        return this;
    }

    public ScrollInput addHint(MutableComponent hint) {
        this.hint = hint;
        this.updateTooltip();
        return this;
    }

    public ScrollInput withStepFunction(Function<StepContext, Integer> step) {
        this.step = step;
        return this;
    }

    public ScrollInput writingTo(Label label) {
        this.displayLabel = label;
        if (label != null)
            this.writeToLabel();
        return this;
    }


    @Override
    public void tick() {
        this.soundPlayed = false;
    }

    public int getState() {
        return this.state;
    }

    public ScrollInput setState(int state) {
        this.state = state;
        this.clampState();
        this.updateTooltip();
        if (this.displayLabel != null)
            this.writeToLabel();
        return this;
    }

    public ScrollInput withShiftStep(int step) {
        this.shiftStep = step;
        return this;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (this.inverted)
            pScrollY *= -1;

        StepContext context = new StepContext();
        context.control = Screen.hasControlDown();
        context.shift = Screen.hasShiftDown();
        context.currentValue = this.state;
        context.forward = pScrollY > 0;

        int priorState = this.state;
        boolean shifted = Screen.hasShiftDown();
        int step = (int) Math.signum(pScrollY) * this.step.apply(context);

        this.state += step;
        if (shifted)
            this.state -= this.state % this.shiftStep;

        this.clampState();

        if (priorState != this.state) {
            if (!this.soundPlayed)
                Minecraft.getInstance()
                        .getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_HAT,
                                1.5f + 0.1f * (this.state - this.min) / (this.max - this.min)));
            this.soundPlayed = true;
            this.onChanged();
        }

        return priorState != this.state;
    }

    protected void clampState() {
        if (this.state >= this.max)
            this.state = this.max - 1;
        if (this.state < this.min)
            this.state = this.min;
    }

    public void onChanged() {
        if (this.displayLabel != null)
            this.writeToLabel();
        if (this.onScroll != null)
            this.onScroll.accept(this.state);
        this.updateTooltip();
    }

    protected void writeToLabel() {
        this.displayLabel.text = this.formatter.apply(this.state);
    }

    public void updateTooltip() {
        var tooltip = Component.empty();

        if (this.title != null)
            tooltip.append(this.title.plainCopy().withStyle(s -> s.withColor(HEADER_RGB)));


        if (this.hint != null)
            tooltip.append("\n").append(this.hint.plainCopy()
                    .withStyle(s -> s.withColor(HINT_RGB)));

        tooltip.append("\n").append(this.scrollToModify.plainCopy()
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
        tooltip.append("\n").append(this.shiftScrollsFaster.plainCopy()
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));

        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

    }


    public static class StepContext {
        public int currentValue;
        public boolean forward;
        public boolean shift;
        public boolean control;
    }
}
