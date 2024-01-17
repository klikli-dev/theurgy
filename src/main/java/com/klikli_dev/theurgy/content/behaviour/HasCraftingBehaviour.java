// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public interface HasCraftingBehaviour<W extends RecipeWrapper, R extends Recipe<W>, C extends RecipeManager.CachedCheck<W, R>> {
    CraftingBehaviour<W, R, C> craftingBehaviour();
}
