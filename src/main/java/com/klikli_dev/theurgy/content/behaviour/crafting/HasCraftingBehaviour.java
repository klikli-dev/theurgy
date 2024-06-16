// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;


public interface HasCraftingBehaviour<W extends RecipeInput, R extends Recipe<W>, C extends RecipeManager.CachedCheck<W, R>> {
    CraftingBehaviour<W, R, C> craftingBehaviour();
}
