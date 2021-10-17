/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.handlers;


import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.api.TheurgyConstants;
import com.klikli_dev.theurgy.api.tooltips.ITooltipDataProvider;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class TooltipHandler {

    private static Map<Item, ITooltipDataProvider> tooltipDataProviders = new HashMap<>();

    @SubscribeEvent
    public static void onAddInformation(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        //only run for theurgy items
        if (stack.getItem().getRegistryName().getNamespace().equals(Theurgy.MODID)) {
            String tooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_SUFFIX;
            String extendedTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_EXTENDED_SUFFIX;
            String usageTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_USAGE_SUFFIX;

            boolean tooltipExists = net.minecraft.client.resources.language.I18n.exists(tooltipKey);
            boolean extendedTooltipExists = net.minecraft.client.resources.language.I18n.exists(extendedTooltipKey);
            boolean usageTooltipExists = net.minecraft.client.resources.language.I18n.exists(usageTooltipKey);

            TranslatableComponent[] additionalTooltipData = getAdditionalTooltipData(stack);

            //first check usage tooltip
            if (Screen.hasShiftDown() && Screen.hasControlDown() && usageTooltipExists) {
                event.getToolTip().add(new TranslatableComponent(usageTooltipKey, (Object[]) additionalTooltipData));
            }
            //then extended tooltip - hint at usage, if available
            else if (Screen.hasShiftDown() && extendedTooltipExists) {
                event.getToolTip().add(new TranslatableComponent(extendedTooltipKey,  (Object[]) additionalTooltipData));

                if (usageTooltipExists) {
                    event.getToolTip().add(new TranslatableComponent(TheurgyConstants.I18n.TOOLTIP_SHOW_USAGE,  (Object[]) additionalTooltipData));
                }
            }
            //then regular tooltip - hint at extended and usage, if available
            else {
                //only add the default tooltip if we have one
                if (tooltipExists) {
                    event.getToolTip().add(new TranslatableComponent(tooltipKey,  (Object[]) additionalTooltipData));
                }
                //but if we have other tooltips, hint at them
                if (extendedTooltipExists) {
                    event.getToolTip().add(new TranslatableComponent(TheurgyConstants.I18n.TOOLTIP_SHOW_EXTENDED,  (Object[]) additionalTooltipData));
                }
                if (usageTooltipExists) {
                    event.getToolTip().add(new TranslatableComponent(TheurgyConstants.I18n.TOOLTIP_SHOW_USAGE,  (Object[]) additionalTooltipData));
                }
            }
        }
    }

    /**
     * Allows to provide additional @{@link net.minecraft.network.chat.TranslatableComponent} as parameter
     * to the main tooltip @{@link net.minecraft.network.chat.TranslatableComponent}
     *
     * Should be called in @{@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerTooltipDataProvider(Item item, ITooltipDataProvider provider){
        tooltipDataProviders.put(item, provider);
    }

    public static TranslatableComponent[] getAdditionalTooltipData(ItemStack stack) {
        ITooltipDataProvider provider = tooltipDataProviders.get(stack.getItem());
        if (provider != null) {
            return provider.getTooltipData(stack);
        }
        return new TranslatableComponent[0];
    }
}
