/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemTagRegistry {

    public static final TagKey<Item> ALCHEMICAL_SULFURS = tag( "alchemical_sulfurs");
    public static final TagKey<Item> ALCHEMICAL_SALTS = tag( "alchemical_salts");
    public static final TagKey<Item> ALCHEMICAL_MERCURIES = tag( "alchemical_mercuries");


    public static TagKey<Item> tag(String id) {
        return tag(Theurgy.loc(id));
    }

    public static TagKey<Item> tag(ResourceLocation id) {
        return TagKey.create(Registries.ITEM, id);
    }

}
