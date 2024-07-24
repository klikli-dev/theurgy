// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.sulfur;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AlchemicalNiterItem extends AlchemicalDerivativeItem {

    public AlchemicalNiterItem(Properties pProperties, AlchemicalDerivativeTier tier) {
        super(pProperties);

        this.useCustomSourceName(true);
        this.autoTooltip(true, false);
        this.autoName(true, false);
        this.withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get())));
        this.tier(tier);
    }

    @Override
    public List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        var result = super.getTooltipData(sulfurStack);
        if (this.provideAutomaticTooltipData) {
            result.add(ComponentUtils.wrapInSquareBrackets(
                    Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_TYPE_NITER)
                            .withStyle(Style.EMPTY
                                    .withColor(ChatFormatting.DARK_GRAY)
                                    .withItalic(true))
            ));
        }

        return result;
    }

    @Override
    public List<MutableComponent> getNameData(ItemStack stack) {
        var result = super.getNameData(stack);

        if (this.useAutomaticNameRendering) {
            result.add(ComponentUtils.wrapInSquareBrackets(
                    Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_TYPE_NITER)
                            .withStyle(Style.EMPTY
                                    .withColor(ChatFormatting.DARK_GRAY)
                                    .withItalic(true))
            ));
        }

        return result;
    }
}
