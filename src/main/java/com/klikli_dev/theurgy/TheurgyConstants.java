// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy;

import net.minecraft.resources.ResourceLocation;

public class TheurgyConstants {

    public static class ItemProperty {

        public static final ResourceLocation DIVINATION_DISTANCE = Theurgy.loc("divination_distance");

    }

    public static class I18n {

        public static final String ITEM_GROUP = "itemGroup." + Theurgy.MODID;

        public static class Item {

            public static final String ALCHEMICAL_DERIVATIVE_SOURCE_SUFFIX = ".source";
            private static final String PREFIX = "item." + Theurgy.MODID + ".";
            public static final String ALCHEMICAL_DERIVATIVE_UNKNOWN_SOURCE = PREFIX + "alchemical_derivative.unknown_source";
            public static final String DIVINATION_ROD_UNKNOWN_LINKED_BLOCK = PREFIX + "divination_rod.unknown_linked_block";
            public static final String ALCHEMICAL_DERIVATIVE_TYPE_NITER = PREFIX + "alchemical_derivative.type.niter";

            public static class Mode {
                public static final String PREFIX = "item.mode." + Theurgy.MODID + ".";
                public static final String MERCURIAL_WAND_SELECT_DIRECTION = PREFIX + "mercurial_wand.select_direction";
                public static final String MERCURIAL_WAND_SELECT_DIRECTION_SUCCESS = PREFIX + "mercurial_wand.select_direction.success";
                public static final String MERCURIAL_WAND_SET_SELECTED_DIRECTION = PREFIX + "mercurial_wand.set_selected_direction";
                public static final String MERCURIAL_WAND_SET_SELECTED_DIRECTION_WITH_TARGET = PREFIX + "mercurial_wand.set_selected_direction.with_target";
                public static final String MERCURIAL_WAND_SET_SELECTED_DIRECTION_SUCCESS = PREFIX + "mercurial_wand.set_selected_direction.success";
                public static final String MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION = PREFIX + "mercurial_wand.rotate_selected_direction";
                public static final String MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION_WITH_TARGET = PREFIX + "mercurial_wand.rotate_selected_direction.with_target";
                public static final String MERCURIAL_WAND_CYCLE_SELECTED_DIRECTION_SUCCESS = PREFIX + "mercurial_wand.rotate_selected_direction.success";
                public static final String MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED = PREFIX + "mercurial_wand.switch_logistics_enabled";
                public static final String MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_HUD = PREFIX + "mercurial_wand.switch_logistics_enabled.hud";
                public static final String MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_SUCCESS = PREFIX + "mercurial_wand.switch_logistics_enabled.success";
                public static final String MERCURIAL_WAND_ENABLED = PREFIX + "mercurial_wand.enabled";
                public static final String MERCURIAL_WAND_DISABLED = PREFIX + "mercurial_wand.disabled";

                public static final String MERCURIAL_WAND_SELECT_FREQUENCY = PREFIX + "mercurial_wand.select_frequency";
                public static final String MERCURIAL_WAND_SET_SELECTED_FREQUENCY = PREFIX + "mercurial_wand.set_selected_frequency";
                public static final String MERCURIAL_WAND_SET_SELECTED_FREQUENCY_WITH_TARGET = PREFIX + "mercurial_wand.set_selected_frequency.with_target";
                public static final String MERCURIAL_WAND_SET_SELECTED_FREQUENCY_SUCCESS = PREFIX + "mercurial_wand.set_selected_frequency.success";
            }
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

        public static class Key {
            public static final String PREFIX = Theurgy.MODID + ".key.";
            public static final String CATEGORY = PREFIX + "theurgy.category";
            public static final String CHANGE_ITEM_MODE = PREFIX + "change_item_mode";
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

        public static class Gui {
            public static final String PREFIX = Theurgy.MODID + ".gui.";

            public static final String SMELTING_TIME_SECONDS = PREFIX + "category.smelting.time.seconds";

            public static final String FILTER_RESET_BUTTON_TOOLTIP = PREFIX + "filter.reset_button.tooltip";
            public static final String FILTER_CONFIRM_BUTTON_TOOLTIP = PREFIX + "filter.confirm_button.tooltip";

            public static final String LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP = PREFIX + "filter.deny_list_button.tooltip";
            public static final String LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT = PREFIX + "filter.deny_list_button.tooltip.shift";

            public static final String LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP = PREFIX + "filter.accept_list_button.tooltip";
            public static final String LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP_SHIFT = PREFIX + "filter.accept_list_button.tooltip.shift";

            public static final String ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP = PREFIX + "attribute_filter.accept_list_or_button.tooltip";
            public static final String ATTRIBUTE_FILTER_ACCEPT_LIST_OR_BUTTON_TOOLTIP_SHIFT = PREFIX + "attribute_filter.accept_list_or_button.tooltip.shift";

            public static final String ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP = PREFIX + "attribute_filter.accept_list_and_button.tooltip";
            public static final String ATTRIBUTE_FILTER_ACCEPT_LIST_AND_BUTTON_TOOLTIP_SHIFT = PREFIX + "attribute_filter.accept_list_and_button.tooltip.shift";

            public static final String ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP = PREFIX + "attribute_filter.deny_list_button.tooltip";
            public static final String ATTRIBUTE_FILTER_DENY_LIST_BUTTON_TOOLTIP_SHIFT = PREFIX + "attribute_filter.deny_list_button.tooltip.shift";

            public static final String FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP = PREFIX + "filter.ignore_data_components_button.tooltip";
            public static final String FILTER_IGNORE_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT = PREFIX + "filter.ignore_data_components_button.tooltip.shift";

            public static final String FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP = PREFIX + "filter.respect_data_components_button.tooltip";
            public static final String FILTER_RESPECT_DATA_COMPONENTS_BUTTON_TOOLTIP_SHIFT = PREFIX + "filter.respect_data_components_button.tooltip.shift";

            public static final String ATTRIBUTE_FILTER_ADD_BUTTON_TOOLTIP = PREFIX + "attribute_filter.add_button.tooltip";
            public static final String ATTRIBUTE_FILTER_ADD_INVERTED_BUTTON_TOOLTIP = PREFIX + "attribute_filter.add_inverted.tooltip";

            public static final String ATTRIBUTE_FILTER_ADD_REFERENCE_ITEM = PREFIX + "attribute_filter.add_reference_item";
            public static final String ATTRIBUTE_FILTER_NO_SELECTED_ATTRIBUTES = PREFIX + "attribute_filter.no_selected_attributes";
            public static final String ATTRIBUTE_FILTER_SELECTED_ATTRIBUTES = PREFIX + "attribute_filter.selected_attributes";

            public static final String SCROLL_DEFAULT_TITLE = PREFIX + "scroll_input.default_title";
            public static final String SCROLL_TO_MODIFY = PREFIX + "scroll_input.scroll_to_modify";
            public static final String SCROLL_TO_SELECT = PREFIX + "scroll_input.scroll_to_select";
            public static final String SCROLL_SHIFT_SCROLLS_FASTER = PREFIX + "scroll_input.shift_scrolls_faster";
        }
    }

}
