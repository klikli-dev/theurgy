// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.filter.FilterMode;
import com.klikli_dev.theurgy.content.behaviour.filter.attribute.ItemAttribute;
import com.klikli_dev.theurgy.content.gui.*;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetListFilterScreenOption;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeFilterScreen extends AbstractFilterScreen<AttributeFilterMenu> {

    private static final Component ADD_REFERENCE_ITEM = Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_REFERENCE_ITEM);
    private static final Component NO_SELECTED_ATTRIBUTES = Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_NO_SELECTED_ATTRIBUTES);
    private static final Component SELECTED_ATTRIBUTES = Component.translatable(TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_SELECTED_ATTRIBUTES);
    private final List<ItemAttribute> attributesOfItem = new ArrayList<>();
    private final List<Component> selectedAttributes = new ArrayList<>();
    protected IconButton acceptListOrButton;
    protected IconButton acceptListAndButton;
    protected IconButton denyListButton;
    protected Indicator acceptListOrIndicator;
    protected Indicator acceptListAndIndicator;
    protected Indicator denyListIndicator;
    protected IconButton addButton;
    protected IconButton addInvertedButton;
    protected SelectionScrollInput attributeSelector;
    protected Label attributeSelectorLabel;
    private ItemStack lastItemScanned = ItemStack.EMPTY;

    public AttributeFilterScreen(AttributeFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GuiTextures.ATTRIBUTE_FILTER);
    }

    @Override
    protected int getScreenTitleColor() {
        return 0x592424;
    }

    @Override
    protected boolean isButtonActive(IconButton button) {
        if (button == this.denyListButton)
            return this.menu.filterMode != FilterMode.DENY_LIST;
        if (button == this.acceptListOrButton)
            return this.menu.filterMode != FilterMode.ACCEPT_LIST_OR;
        if (button == this.acceptListAndButton)
            return this.menu.filterMode != FilterMode.ACCEPT_LIST_AND;
        return true;
    }

    @Override
    protected boolean isIndicatorOn(Indicator indicator) {
        if (indicator == this.denyListIndicator)
            return this.menu.filterMode == FilterMode.DENY_LIST;
        if (indicator == this.acceptListOrIndicator)
            return this.menu.filterMode == FilterMode.ACCEPT_LIST_OR;
        if (indicator == this.acceptListAndIndicator)
            return this.menu.filterMode == FilterMode.ACCEPT_LIST_AND;
        return false;
    }

    @Override
    protected List<IconButton> getButtons() {
        return Arrays.asList(this.denyListButton, this.acceptListAndButton, this.acceptListOrButton);
    }

    @Override
    protected List<Indicator> getIndicators() {
        return Arrays.asList(this.denyListIndicator, this.acceptListAndIndicator, this.acceptListOrIndicator);
    }

    @Override
    protected void init() {
        super.init();

        int x = this.leftPos;
        int y = this.topPos;

        this.acceptListOrButton = new IconButton(x + 47, y + 61, GuiIcons.ACCEPT_LIST_OR);
        this.acceptListOrButton.withOnClick(() -> {
            this.menu.filterMode = FilterMode.ACCEPT_LIST_OR;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.ACCEPT_LIST);
        });
        this.acceptListOrButton.withTooltip(
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP_SHIFT
        );
        this.addRenderableWidget(this.acceptListOrButton);

        this.acceptListAndButton = new IconButton(x + 65, y + 61, GuiIcons.ACCEPT_LIST_AND);
        this.acceptListAndButton.withOnClick(() -> {
            this.menu.filterMode = FilterMode.ACCEPT_LIST_AND;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.ACCEPT_LIST);
        });
        this.acceptListAndButton.withTooltip(
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP_SHIFT
        );
        this.addRenderableWidget(this.acceptListAndButton);

        this.denyListButton = new IconButton(x + 83, y + 61, GuiIcons.DENY_LIST_ALT);
        this.denyListButton.withOnClick(() -> {
            this.menu.filterMode = FilterMode.DENY_LIST;
            this.sendOptionUpdate(MessageSetListFilterScreenOption.Option.DENY_LIST);
        });
        this.denyListButton.withTooltip(
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP,
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT
        );
        this.addRenderableWidget(this.denyListButton);

        this.acceptListOrIndicator = new Indicator(x + 47, y + 55, Component.empty());
        this.addRenderableWidget(this.acceptListOrIndicator);

        this.acceptListAndIndicator = new Indicator(x + 65, y + 55, Component.empty());
        this.addRenderableWidget(this.acceptListAndIndicator);

        this.denyListIndicator = new Indicator(x + 83, y + 55, Component.empty());
        this.addRenderableWidget(this.denyListIndicator);


        this.addButton = new IconButton(x + 182, y + 23, GuiIcons.ADD);
        this.addButton.withOnClick(() -> {
            this.handleAddedAttibute(false);
        });
        this.addButton.withTooltip(
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_BUTTON_TOOLTIP
        );
        this.addRenderableWidget(this.addButton);

        this.addInvertedButton = new IconButton(x + 200, y + 23, GuiIcons.ADD_INVERTED);
        this.addInvertedButton.withOnClick(() -> {
            this.handleAddedAttibute(true);
        });
        this.addInvertedButton.withTooltip(
                TheurgyConstants.I18n.Gui.ATTRIBUTE_FILTER_ADD_INVERTED_BUTTON_TOOLTIP
        );
        this.addRenderableWidget(this.addInvertedButton);

        this.updateIndicatorState();

        this.attributeSelectorLabel = new Label(x + 43, y + 28, Component.empty()).colored(0xF3EBDE)
                .withShadow();
        this.addRenderableWidget(this.attributeSelectorLabel);

        this.attributeSelector = new SelectionScrollInput(x + 39, y + 23, 137, 18);
        this.attributeSelector.forOptions(List.of(Component.empty()));
        this.attributeSelector.removeCallback();
        this.addRenderableWidget(this.attributeSelector);


        this.referenceItemChanged(this.menu.ghostInventory.getStackInSlot(0));


        this.selectedAttributes.clear();
        this.selectedAttributes.add((this.menu.selectedAttributes.isEmpty() ? NO_SELECTED_ATTRIBUTES : SELECTED_ATTRIBUTES).plainCopy()
                .withStyle(ChatFormatting.YELLOW));
        this.menu.selectedAttributes.forEach(at -> this.selectedAttributes.add(Component.literal("- ")
                .append(at.getFirst()
                        .format(at.getSecond()))
                .withStyle(ChatFormatting.GRAY)));
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        ItemStack stack = this.menu.ghostInventory.getStackInSlot(1);
        PoseStack matrixStack = pGuiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 150);
        pGuiGraphics.renderItemDecorations(this.font, stack, this.leftPos + 22, this.topPos + 59,
                String.valueOf(this.selectedAttributes.size() - 1));
        matrixStack.popPose();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ItemStack stackInSlot = this.menu.ghostInventory.getStackInSlot(0);
        if (!ItemStack.isSameItemSameComponents(stackInSlot, this.lastItemScanned))
            this.referenceItemChanged(stackInSlot);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == 37) {
                graphics.renderComponentTooltip(this.font, this.selectedAttributes, mouseX, mouseY);
                return;
            }
            graphics.renderTooltip(this.font, this.hoveredSlot.getItem(), mouseX, mouseY);
        }
        super.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void clearContents() {
        this.selectedAttributes.clear();
        this.selectedAttributes.add(NO_SELECTED_ATTRIBUTES.plainCopy()
                .withStyle(ChatFormatting.YELLOW));
        if (!this.lastItemScanned.isEmpty()) {
            this.addButton.active = true;
            this.addInvertedButton.active = true;
        }
    }

    protected void sendOptionUpdate(MessageSetListFilterScreenOption.Option option) {
        Networking.sendToServer(new MessageSetListFilterScreenOption(option));
    }

    protected boolean handleAddedAttibute(boolean inverted) {
        int index = this.attributeSelector.getState();
        if (index >= this.attributesOfItem.size())
            return false;
        this.addButton.active = false;
        this.addInvertedButton.active = false;
        CompoundTag tag = new CompoundTag();
        ItemAttribute itemAttribute = this.attributesOfItem.get(index);
        itemAttribute.serializeNBT(Minecraft.getInstance().player.registryAccess(), tag);

        this.sendOptionUpdate(inverted ? MessageSetListFilterScreenOption.Option.ADD_INVERTED_TAG : MessageSetListFilterScreenOption.Option.ADD_TAG);

        this.menu.selectedAttributes.add(Pair.of(itemAttribute, inverted));

        if (this.menu.selectedAttributes.size() == 1)
            this.selectedAttributes.set(0, SELECTED_ATTRIBUTES.plainCopy()
                    .withStyle(ChatFormatting.YELLOW));
        this.selectedAttributes.add(Component.literal("- ").append(itemAttribute.format(inverted))
                .withStyle(ChatFormatting.GRAY));
        return true;
    }

    private void referenceItemChanged(ItemStack stack) {
        this.lastItemScanned = stack;

        if (stack.isEmpty()) {
            this.attributeSelector.active = false;
            this.attributeSelector.visible = false;
            this.attributeSelectorLabel.text = ADD_REFERENCE_ITEM.plainCopy()
                    .withStyle(ChatFormatting.ITALIC);
            this.addButton.active = false;
            this.addInvertedButton.active = false;
            this.attributeSelector.calling(s -> {
            });
            return;
        }

        this.addButton.active = true;
        this.addInvertedButton.active = true;

        this.attributeSelector.titled(stack.getHoverName()
                .plainCopy()
                .append("..."));
        this.attributesOfItem.clear();
        for (ItemAttribute itemAttribute : ItemAttribute.types)
            this.attributesOfItem.addAll(itemAttribute.listAttributesOf(stack, this.minecraft.level));
        List<Component> options = this.attributesOfItem.stream()
                .map(a -> a.format(false))
                .collect(Collectors.toList());
        this.attributeSelector.forOptions(options);
        this.attributeSelector.active = true;
        this.attributeSelector.visible = true;
        this.attributeSelector.setState(0);
        this.attributeSelector.calling(i -> {
            this.attributeSelectorLabel.setTextAndTrim(options.get(i), true, 112);
            ItemAttribute selected = this.attributesOfItem.get(i);
            for (Pair<ItemAttribute, Boolean> existing : this.menu.selectedAttributes) {
                CompoundTag testTag = new CompoundTag();
                CompoundTag testTag2 = new CompoundTag();
                existing.getFirst()
                        .serializeNBT(this.minecraft.player.registryAccess(), testTag);
                selected.serializeNBT(this.minecraft.player.registryAccess(), testTag2);
                if (testTag.equals(testTag2)) {
                    this.addButton.active = false;
                    this.addInvertedButton.active = false;
                    return;
                }
            }
            this.addButton.active = true;
            this.addInvertedButton.active = true;
        });
        this.attributeSelector.onChanged();
    }
}
