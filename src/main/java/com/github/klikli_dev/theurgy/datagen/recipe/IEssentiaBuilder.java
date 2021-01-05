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

package com.github.klikli_dev.theurgy.datagen.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

import java.util.List;

public interface IEssentiaBuilder<T extends IEssentiaBuilder<?>> {
    default T essentia(List<ItemStack> essentia) {
        return this.essentia(essentia, 1);
    }

    @SuppressWarnings("unchecked")
    default T essentia(List<ItemStack> essentia, float multiplier) {
        essentia.forEach(stack -> this.essentia(stack.getItem(), (int)(stack.getCount() * multiplier)));
        return (T)this;
    }

    T essentia(IItemProvider essentia, int count);
}
