package com.klikli_dev.theurgy.content.behaviour;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface HasCraftingBehaviour<W extends RecipeWrapper, R extends Recipe<W>, C extends RecipeManager.CachedCheck<W, R>> {
    CraftingBehaviour<W, R, C> craftingBehaviour();
}
