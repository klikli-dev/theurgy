/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.tooltips;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TooltipHandler {
    private static final Map<Item, TooltipDataProvider> tooltipDataProviders = new HashMap<>();
    private static final List<String> namespacesToListenFor = new ArrayList<>();

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        var itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        //only run for enabled namespaces to easily improve performance
        if (namespacesToListenFor.contains(itemId.getNamespace())) {
            String tooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.SUFFIX;
            String extendedTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.EXTENDED_SUFFIX;
            String usageTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.USAGE_SUFFIX;

            boolean tooltipExists = I18n.exists(tooltipKey);
            boolean extendedTooltipExists = I18n.exists(extendedTooltipKey);
            boolean usageTooltipExists = I18n.exists(usageTooltipKey);

            var additionalTooltipData = getAdditionalTooltipData(stack);

            if (tooltipExists) {
                event.getToolTip().add(new TranslatableComponent(tooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            }

            if (extendedTooltipExists) {
                if(Screen.hasShiftDown()){
                    event.getToolTip().add(new TranslatableComponent(extendedTooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                } else {
                    event.getToolTip().add(new TranslatableComponent(TheurgyConstants.I18n.Tooltip.SHOW_EXTENDED, additionalTooltipData.toArray()));
                }
            }

            if (usageTooltipExists) {
                if(Screen.hasControlDown()){
                    event.getToolTip().add(new TranslatableComponent(usageTooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                } else {
                    event.getToolTip().add(new TranslatableComponent(TheurgyConstants.I18n.Tooltip.SHOW_USAGE, additionalTooltipData.toArray()));
                }
            }
        }
    }


    /**
     * Allows to provide additional @{@link net.minecraft.network.chat.MutableComponent}s as parameter
     * to the main tooltip @{@link net.minecraft.network.chat.MutableComponent}
     * <p>
     * Should be called in @{@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerTooltipDataProvider(Item item, TooltipDataProvider provider) {
        tooltipDataProviders.put(item, provider);
    }

    /**
     * Register a namespace (= mod id) of items to listen for during tooltip handling.
     * Should be called in @{@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerNamespaceToListenTo(String namespace) {
        namespacesToListenFor.add(namespace);
    }

    public static List<MutableComponent> getAdditionalTooltipData(ItemStack stack) {
        var provider = tooltipDataProviders.get(stack.getItem());
        if (provider != null) {
            return provider.getTooltipData(stack);
        }
        return ImmutableList.of();
    }
}
