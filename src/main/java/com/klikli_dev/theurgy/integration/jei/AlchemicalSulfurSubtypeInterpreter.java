/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AlchemicalSulfurSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

    private static final AlchemicalSulfurSubtypeInterpreter instance = new AlchemicalSulfurSubtypeInterpreter();


    public static AlchemicalSulfurSubtypeInterpreter get() {
        return instance;
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        CompoundTag nbtTagCompound = ingredient.getTag();
        if (nbtTagCompound == null || nbtTagCompound.isEmpty()) {
            return IIngredientSubtypeInterpreter.NONE;
        }

        //sulfurs represent all minerals, so they should not differentiate the source
        //this is important, otherwise JEI will not allow certain sulfurs in the recipe while the actual crafting does allow it
        return nbtTagCompound.toString()
                .replace("#forge:ores", "#theurgy:minerals")
                .replace("#forge:ingots", "#theurgy:minerals")
                .replace("#forge:gems", "#theurgy:minerals")
                .replace("#forge:raw_materials", "#theurgy:minerals");
    }
}
