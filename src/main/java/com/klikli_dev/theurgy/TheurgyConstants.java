/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy;

import net.minecraft.resources.ResourceLocation;

public class TheurgyConstants {

    public static class Nbt {

        protected static final String PREFIX = Theurgy.MODID + ":";

        /**
         * Used to store item id of the item the sulfur was created from.
         * Should be a ResourceLocation string.
         * Sample Command: /give @s theurgy:alchemical_sulfur{"theurgy:sulfur.source.id":"minecraft:oak_log"}
         */
        public static final String SULFUR_SOURCE_ID = PREFIX + "sulfur.source.id";

        /**
         * Used to store stack NBT of the stack the sulfur was created from.
         * Should be a CompoundTag.
         */
        public static final String SULFUR_SOURCE_NBT = PREFIX + "sulfur.source.nbt";


        public static class Divination {

            public static final String DISTANCE = PREFIX + "divination.distance";
            public static final String POS = PREFIX + "divination.pos";
            public static final String LINKED_BLOCK_ID = PREFIX + "divination.linked_block_id";

            public static final String SETTING_TIER = PREFIX + "divination.setting.tier";
            public static final String SETTING_ALLOWED_BLOCKS_TAG = PREFIX + "divination.setting.allowed_blocks_tag";
            public static final String SETTING_RANGE = PREFIX + "divination.setting.range";
            public static final String SETTING_DURATION = PREFIX + "divination.setting.duration";
            public static final String SETTING_DURABILITY = PREFIX + "divination.setting.max_damage";
        }

    }

    public static class ItemProperty {

        public static final ResourceLocation DIVINATION_DISTANCE = Theurgy.loc("divination_distance");


    }

    public static class I18n {

        public static class Tooltip {

            private static final String PREFIX = "tooltip." + Theurgy.MODID + ".";
            public static final String SHOW_EXTENDED = "tooltip." + Theurgy.MODID + ".show_extended";
            public static final String SHOW_USAGE = "tooltip." + Theurgy.MODID + ".show_usage";
            public static final String SUFFIX = ".tooltip";
            public static final String EXTENDED_SUFFIX = ".tooltip.extended";
            public static final String USAGE_SUFFIX = ".tooltip.usage";

            public static final String DIVINATION_ROD_LINKED_TO = PREFIX + ".divination_rod.linked_to";
            public static final String DIVINATION_ROD_NO_LINK = PREFIX + ".divination_rod.no_link";
            public static final String DIVINATION_ROD_LAST_RESULT = PREFIX + ".divination_rod.last_result";
        }

        public static class Message {
            private static final String PREFIX = "message." + Theurgy.MODID + ".";

            public static final String DIVINATION_ROD_LINKED = PREFIX + ".divination_rod.linked";
            public static final String DIVINATION_ROD_TIER_TOO_LOW = PREFIX + ".divination_rod.tier_too_low";
            public static final String DIVINATION_ROD_BLOCK_NOT_ALLOWED = PREFIX + ".divination_rod.block_not_allowed";
            public static final String DIVINATION_ROD_NO_LINK = PREFIX + ".divination_rod.no_link";
        }
    }
}
