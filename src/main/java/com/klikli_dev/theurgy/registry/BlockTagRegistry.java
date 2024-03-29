// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTagRegistry {

    public static final TagKey<Block> DIVINATION_ROD_T1_ALLOWED_BLOCKS = tag("divination_rod_t1_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T2_ALLOWED_BLOCKS = tag("divination_rod_t2_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T3_ALLOWED_BLOCKS = tag("divination_rod_t3_allowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T4_ALLOWED_BLOCKS = tag("divination_rod_t4_allowed_blocks");

    public static final TagKey<Block> DIVINATION_ROD_T1_DISALLOWED_BLOCKS = tag("divination_rod_t1_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T2_DISALLOWED_BLOCKS = tag("divination_rod_t2_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T3_DISALLOWED_BLOCKS = tag("divination_rod_t3_disallowed_blocks");
    public static final TagKey<Block> DIVINATION_ROD_T4_DISALLOWED_BLOCKS = tag("divination_rod_t4_disallowed_blocks");


    public static final TagKey<Block> INCUBATOR_VESSELS = tag("incubator_vessels");

    public static final TagKey<Block> REFORMATION_SOURCE_PEDESTALS = tag("reformation_source_pedestals");
    public static final TagKey<Block> REFORMATION_TARGET_PEDESTALS = tag("reformation_target_pedestals");
    public static final TagKey<Block> REFORMATION_RESULT_PEDESTALS = tag("reformation_result_pedestals");
    public static final TagKey<Block> REFORMATION_PEDESTALS = tag("reformation_pedestals");

    public static final TagKey<Block> SAL_AMMONIAC_ORES = forgeTag("ores/sal_ammoniac");


    public static TagKey<Block> tag(String id) {
        return tag(Theurgy.loc(id));
    }

    public static TagKey<Block> forgeTag(String id) {
        return tag(new ResourceLocation("forge", id));
    }

    public static TagKey<Block> tag(ResourceLocation id) {
        return TagKey.create(Registries.BLOCK, id);
    }

}
