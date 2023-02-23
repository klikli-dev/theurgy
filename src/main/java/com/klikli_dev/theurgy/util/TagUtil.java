/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.util;

import com.klikli_dev.theurgy.integration.almostunified.AlmostUnifiedIntegration;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class TagUtil {

    @Nullable
    public static Item getItemForTag(TagKey<Item> tag) {
        var item = AlmostUnifiedIntegration.getPreferredItemForTag(tag);

        return item != null ? item : BuiltInRegistries.ITEM.getTag(tag)
                .map(HolderSet.ListBacked::stream)
                .flatMap(Stream::findFirst)
                .map(Holder::value)
                .orElse(null);
    }

    public static ItemStack getItemStackForTag(TagKey<Item> tag) {
        var item = getItemForTag(tag);
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
}
