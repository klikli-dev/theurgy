// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

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
            public static final String LINKED_BLOCK_ID_PREVIEW_MODE = PREFIX + "divination.linked_block_id.preview_mode";

            public static final String SETTING_TIER = PREFIX + "divination.setting.tier";
            public static final String SETTING_ALLOWED_BLOCKS_TAG = PREFIX + "divination.setting.allowed_blocks_tag";
            public static final String SETTING_DISALLOWED_BLOCKS_TAG = PREFIX + "divination.setting.disallowed_blocks_tag";
            public static final String SETTING_RANGE = PREFIX + "divination.setting.range";
            public static final String SETTING_DURATION = PREFIX + "divination.setting.duration";
            public static final String SETTING_DURABILITY = PREFIX + "divination.setting.max_damage";

            public static final String SETTING_ALLOW_ATTUNING = PREFIX + "divination.setting.allow_attuning";
        }

    }

    public static class ItemProperty {

        public static final ResourceLocation DIVINATION_DISTANCE = Theurgy.loc("divination_distance");


    }

    public static class I18n {

        public static final String ITEM_GROUP = "itemGroup." + Theurgy.MODID;

        public static class Item {

            public static final String ALCHEMICAL_SULFUR_SOURCE_SUFFIX = ".source";
            public static final String ALCHEMICAL_SALT_SOURCE_SUFFIX = ".source";
            private static final String PREFIX = "item." + Theurgy.MODID + ".";
            public static final String ALCHEMICAL_SULFUR_UNKNOWN_SOURCE = PREFIX + "alchemical_sulfur.unknown_source";
            public static final String DIVINATION_ROD_UNKNOWN_LINKED_BLOCK = PREFIX + "divination_rod.unknown_linked_block";
        }

        public static class Tooltip {

            public static final String SHOW_EXTENDED = "tooltip." + Theurgy.MODID + ".show_extended";
            public static final String EXTENDED_HEADING = "tooltip." + Theurgy.MODID + ".extended_heading";
            public static final String SHOW_USAGE = "tooltip." + Theurgy.MODID + ".show_usage";
            public static final String USAGE_HEADING = "tooltip." + Theurgy.MODID + ".usage_heading";
            public static final String SUFFIX = ".tooltip";
            public static final String EXTENDED_SUFFIX = ".tooltip.extended";
            public static final String USAGE_SUFFIX = ".tooltip.usage";
            private static final String PREFIX = "tooltip." + Theurgy.MODID + ".";
            public static final String DIVINATION_ROD_LINKED_TO = PREFIX + ".divination_rod.linked_to";
            public static final String DIVINATION_ROD_NO_LINK = PREFIX + ".divination_rod.no_link";
            public static final String DIVINATION_ROD_LAST_RESULT = PREFIX + ".divination_rod.last_result";
        }

        public static class Message {
            private static final String PREFIX = "message." + Theurgy.MODID + ".";

            public static final String DIVINATION_ROD_LINKED = PREFIX + "divination_rod.linked";
            public static final String DIVINATION_ROD_TIER_TOO_LOW = PREFIX + "divination_rod.tier_too_low";
            /**
             * Not in allowed tag
             */
            public static final String DIVINATION_ROD_BLOCK_NOT_ALLOWED = PREFIX + "divination_rod.block_not_allowed";
            /**
             * In disallowed tag
             */
            public static final String DIVINATION_ROD_BLOCK_DISALLOWED = PREFIX + "divination_rod.block_disallowed";
            public static final String DIVINATION_ROD_NO_LINK = PREFIX + "divination_rod.no_link";
            public static final String DIVINATION_ROD_ATTUNING_NOT_ALLOWED = PREFIX + "divination_rod.attuning_not_allowed";
        }

        public static class JEI {
            public static final String PREFIX = Theurgy.MODID + ".jei.";
            public static final String CALCINATION_CATEGORY = PREFIX + "category.calcination";
            public static final String LIQUEFACTION_CATEGORY = PREFIX + "category.liquefaction";
            public static final String DISTILLATION_CATEGORY = PREFIX + "category.distillation";
            public static final String INCUBATION_CATEGORY = PREFIX + "category.incubation";
            public static final String ACCUMULATION_CATEGORY = PREFIX + "category.accumulation";
            public static final String REFORMATION_CATEGORY = PREFIX + "category.reformation";
            public static final String FERMENTATION_CATEGORY = PREFIX + "category.fermentation";
            public static final String DIGESTION_CATEGORY = PREFIX + "category.digestion";
            public static final String MERCURY_FLUX = PREFIX + "misc.mercury_flux";
            public static final String SOURCE_PEDESTAL_COUNT = PREFIX + "misc.source_pedestal_count";

            public static final String TARGET_SULFUR_TOOLTIP = PREFIX + "misc.source_sulfur.tooltip";
        }

        public static class Misc {
            public static final String PREFIX = Theurgy.MODID + ".misc.";

            public static final String UNIT_MILLIBUCKETS = PREFIX + "unit.millibuckets";
        }

        public static class Behaviour {
            public static final String PREFIX = Theurgy.MODID + ".behaviour.";
            public static final String SELECTION_MODE = PREFIX + "selection.mode";
            public static final String SELECTION_MODE_CALORIC_FLUX_EMITTER = PREFIX + "selection.mode.caloric_flux_emitter";
            public static final String SELECTION_OUTSIDE_RANGE = PREFIX + "selection.outside_range";
            public static final String SELECTION_SUMMARY_CALORIC_FLUX_EMITTER = PREFIX + "selection.summary.caloric_flux_emitter";
            public static final String SELECTION_SUMMARY_CALORIC_FLUX_EMITTER_NO_SELECTION = PREFIX + "selection.summary.caloric_flux_emitter.no_selection";

            public static final String SELECTION_MODE_SULFURIC_FLUX_EMITTER = PREFIX + "selection.mode.sulfuric_flux_emitter";
            public static final String SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER = PREFIX + "selection.summary.sulfuric_flux_emitter";
            public static final String SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SELECTION = PREFIX + "selection.summary.sulfuric_flux_emitter.no_selection";
            public static final String SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_TARGET = PREFIX + "selection.summary.sulfuric_flux_emitter.no_target";
            public static final String SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SOURCES = PREFIX + "selection.summary.sulfuric_flux_emitter.no_sources";
            public static final String SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_RESULT = PREFIX + "selection.summary.sulfuric_flux_emitter.no_result";

            public static final String SELECTION_MODE_LOGISTICS_NODE = PREFIX + "selection.mode.logistics_node";

            public static final String INTERACTION_FERMENTATION_VAT_NO_RECIPE = PREFIX + "interaction.fermentation_vat.no_recipe";
            public static final String INTERACTION_FERMENTATION_VAT_CLOSED = PREFIX + "interaction.fermentation_vat.closed";

            public static final String INTERACTION_DIGESTION_VAT_NO_RECIPE = PREFIX + "interaction.digestion_vat.no_recipe";
            public static final String INTERACTION_DIGESTION_VAT_CLOSED = PREFIX + "interaction.digestion_vat.closed";
        }
    }

}
