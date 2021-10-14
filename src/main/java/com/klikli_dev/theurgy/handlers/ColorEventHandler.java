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
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorEventHandler {

    @SubscribeEvent
    public static void onRegisterBlockColors(ColorHandlerEvent.Block event) {
        var blockColors = event.getBlockColors();
        //blockAndTintGetter/pos == null -> we're handling item colors
        blockColors.register((state, blockAndTintGetter, pos, tintIndex) -> switch (tintIndex) {
            case 0 -> //leaves
                    blockAndTintGetter != null && pos != null ?
                            BiomeColors.getAverageFoliageColor(blockAndTintGetter, pos) :
                            FoliageColor.getDefaultColor();
            case 1 -> //fruit
                    0xFFFFFF;
            default -> 0xFFFFFF; //white
        }, BlockRegistry.GRAFTING_HEDGE.get());

        Theurgy.LOGGER.info("Block color registration complete.");
    }

    @SubscribeEvent
    public static void onRegisterItemColors(ColorHandlerEvent.Item event) {

        var blockColors = event.getBlockColors();
        var itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> {
                            BlockState blockstate = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                            return blockColors.getColor(blockstate, null, null, tintIndex);
                        }, //oak leaves color
                        BlockRegistry.GRAFTING_HEDGE.get());

        Theurgy.LOGGER.info("Item color registration complete.");
    }
}
