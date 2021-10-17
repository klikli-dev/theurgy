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

package com.klikli_dev.theurgy.api;

import com.klikli_dev.theurgy.api.tooltips.ITooltipDataProvider;
import net.minecraft.world.item.Item;

public interface ITheurgyAPI {
    /**
     * False if a real API instance is provided
     */
    boolean isStub();

    /**
     * Allows to provide additional @{@link net.minecraft.network.chat.TranslatableComponent} as parameter
     * to the main tooltip @{@link net.minecraft.network.chat.TranslatableComponent}
     *
     * Should be called in @{@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
     */
    void registerTooltipDataProvider(Item item, ITooltipDataProvider provider);
}
