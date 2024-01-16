// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.klikli_dev.theurgy.integration.almostunified.AlmostUnifiedIntegration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;


public class TagUtil {

    @Nullable
    public static Item getItemForTag(TagKey<Item> tag) {
        var item = AlmostUnifiedIntegration.get().getPreferredItemForTag(tag);

        return item != null ? item :
                BuiltInRegistries.ITEM.getTag(tag)
                        .flatMap(t -> t.stream().map(Holder::value).findFirst())
                        .orElse(null);
    }

    public static ItemStack getItemStackForTag(TagKey<Item> tag) {
        var item = getItemForTag(tag);
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
}
