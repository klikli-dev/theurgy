// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;


public class DivinationRodSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

    private static final DivinationRodSubtypeInterpreter instance = new DivinationRodSubtypeInterpreter();


    public static DivinationRodSubtypeInterpreter get() {
        return instance;
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        var settingTier = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_TIER, "");
        var settingAllowedBlocksTag = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG, "");
        var settingDisallowedBlocksTag = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG, "");
        var settingRange = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, 0);
        var settingDuration = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, 0);
        var settingDurability = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE, 0);
        var settingAllowAttuning = ingredient.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, false);

        return String.format("%s_%s_%s_%s_%s_%s_%s", settingTier, settingAllowedBlocksTag, settingDisallowedBlocksTag, settingRange, settingDuration, settingDurability, settingAllowAttuning);
    }
}
