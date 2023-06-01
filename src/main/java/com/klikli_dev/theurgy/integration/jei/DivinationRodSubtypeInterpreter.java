/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.TheurgyConstants;
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
        var tag = ingredient.getTag();

        if (tag != null) {
            var settingTier = tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_TIER);
            var settingAllowedBlocksTag = tag.getString(TheurgyConstants.Nbt.Divination.SETTING_ALLOWED_BLOCKS_TAG);
            var settingDisallowedBlocksTag = tag.getString(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG);
            var settingRange = tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_RANGE);
            var settingDuration = tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION);
            var settingDurability = tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_DURABILITY);
            var settingAllowAttuning = tag.getBoolean(TheurgyConstants.Nbt.Divination.SETTING_ALLOW_ATTUNING);

            return String.format("%s_%s_%s_%s_%s_%s_%s", settingTier, settingAllowedBlocksTag, settingDisallowedBlocksTag, settingRange, settingDuration, settingDurability, settingAllowAttuning);
        }

        return IIngredientSubtypeInterpreter.NONE;
    }
}
