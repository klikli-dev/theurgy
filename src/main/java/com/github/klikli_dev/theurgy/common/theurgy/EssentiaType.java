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

package com.github.klikli_dev.theurgy.common.theurgy;

import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3f;

import java.util.function.Supplier;

public enum EssentiaType {
    AER(new Vector3f(240.0f/255.0f, 252.0f/255.0f, 0.0f), ()-> ItemRegistry.AER_ESSENTIA.get()),
    AQUA(new Vector3f(0.0f, 115/255.0f, 252.0f/255.0f), ()-> ItemRegistry.AQUA_ESSENTIA.get()),
    IGNIS(new Vector3f(1.0f, 64.0f/255.0f, 16.0f/255.0f), ()-> ItemRegistry.IGNIS_ESSENTIA.get()),
    TERRA(new Vector3f(60.0f/255.0f, 1.0f, 16.0f/255.0f), ()-> ItemRegistry.TERRA_ESSENTIA.get());

    private final Vector3f essentiaBallColor;
    private final Supplier<Item> essentiaItem;

    EssentiaType(Vector3f essentiaBallColor, Supplier<Item> essentiaItem) {
        this.essentiaBallColor = essentiaBallColor;
        this.essentiaItem = essentiaItem;
    }

    public Vector3f getEssentiaBallColor() {
        return this.essentiaBallColor;
    }

    public Supplier<Item> getEssentiaItem() {
        return essentiaItem;
    }

    public EssentiaType next() {
        return values()[(this.ordinal() + 1) % EssentiaType.values().length];
    }
}
