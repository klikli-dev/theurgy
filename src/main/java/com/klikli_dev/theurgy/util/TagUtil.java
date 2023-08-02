// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.klikli_dev.theurgy.integration.almostunified.AlmostUnifiedIntegration;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class TagUtil {

    @Nullable
    public static Item getItemForTag(TagKey<Item> tag) {
        var item = AlmostUnifiedIntegration.getPreferredItemForTag(tag);

        return item != null ? item : ForgeRegistries.ITEMS.tags().getTag(tag).stream().findFirst().orElse(null);
    }

    public static ItemStack getItemStackForTag(TagKey<Item> tag) {
        var item = getItemForTag(tag);
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
}
