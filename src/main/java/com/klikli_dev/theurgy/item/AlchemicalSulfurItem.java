/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.klikli_dev.theurgy.client.render.SulfurBEWLR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

public class AlchemicalSulfurItem extends Item {
    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SulfurBEWLR.get();
            }
        });
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DistHelper.fillItemCategory(this, tab, items);
        }
    }

    public static class DistHelper {
        public static void fillItemCategory(AlchemicalSulfurItem item, CreativeModeTab tab, NonNullList<ItemStack> items) {
            var level = Minecraft.getInstance().level;
            if (level != null) {
                var recipeManager = level.getRecipeManager();
                recipeManager.getRecipes().forEach((recipe) -> {
                    if (recipe.getResultItem().getItem() == item) {
                        items.add(recipe.getResultItem().copy());
                    }
                });
            }
        }
    }
}
