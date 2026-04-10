// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public final class JeiIngredients {
    private JeiIngredients() {
    }

    /**
     * Resolve vanilla ingredient displays through the client recipe-display context so JEI can
     * render non-deprecated stack variants without relying on Ingredient.items().
     */
    public static List<ItemStack> getStacks(Ingredient ingredient) {
        return ingredient.display().resolveForStacks(context());
    }

    public static List<ItemStack> getStacks(Ingredient ingredient, int count) {
        return getStacks(ingredient).stream().map(stack -> stack.copyWithCount(count)).toList();
    }

    public static List<ItemStack> getStacks(SizedIngredient ingredient) {
        return getStacks(ingredient.ingredient(), ingredient.count());
    }

    private static ContextMap context() {
        var level = Minecraft.getInstance().level;
        return level != null ? SlotDisplayContext.fromLevel(level) : ContextMap.EMPTY;
    }
}
