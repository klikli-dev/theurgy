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

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.api.TheurgyConstants;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class GraftingHedgeSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    private static final GraftingHedgeSubtypeInterpreter instance = new GraftingHedgeSubtypeInterpreter();

    private GraftingHedgeSubtypeInterpreter() {

    }

    public static GraftingHedgeSubtypeInterpreter get() {
        return instance;
    }

    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        CompoundTag blockEntityTag = itemStack.getTagElement("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA)) {
            return blockEntityTag.getString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA);
        }

        return IIngredientSubtypeInterpreter.NONE;
    }
}
