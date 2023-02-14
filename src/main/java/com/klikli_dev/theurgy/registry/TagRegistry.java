/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TagRegistry {

    public static final TagKey<Block> DIVINATION_ROD_T1_ALLOWED_BLOCKS = makeBlockTag( "divination_rod_t1_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T2_ALLOWED_BLOCKS = makeBlockTag( "divination_rod_t2_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T3_ALLOWED_BLOCKS = makeBlockTag( "divination_rod_t3_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T4_ALLOWED_BLOCKS = makeBlockTag( "divination_rod_t4_allowed_blocks");

    public static final TagKey<Block> DIVINATION_ROD_T1_DISALLOWED_BLOCKS = makeBlockTag( "divination_rod_t1_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T2_DISALLOWED_BLOCKS = makeBlockTag( "divination_rod_t2_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T3_DISALLOWED_BLOCKS = makeBlockTag( "divination_rod_t3_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T4_DISALLOWED_BLOCKS = makeBlockTag( "divination_rod_t4_disallowed_blocks");

    public static TagKey<Block> makeBlockTag(String id) {
        return makeBlockTag(Theurgy.loc(id));
    }

    public static TagKey<Block> makeBlockTag(ResourceLocation id) {
        return TagKey.create(Registries.BLOCK, id);
    }
}
