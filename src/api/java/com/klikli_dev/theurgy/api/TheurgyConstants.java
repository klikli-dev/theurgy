/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.api;

public class TheurgyConstants {

    public static class Data {
        public static final String THEURGY_DATA_PATH = TheurgyAPI.ID + "_data";
        public static final String GRAFTING_HEDGES_PATH = THEURGY_DATA_PATH + "/grafting_hedges";
    }

    public static class Nbt {
        public static final String PREFIX = TheurgyAPI.ID + ":";
        public static final String GRAFTING_HEDGE_DATA = PREFIX + "grafting_hedge_data";
    }

    public static class I18n {
        public static final String ITEM_GROUP = "itemGroup." + TheurgyAPI.ID;

        public static final String TOOLTIP_SHOW_EXTENDED = "tooltip." + TheurgyAPI.ID +".show_extended";
        public static final String TOOLTIP_SHOW_USAGE = "tooltip." + TheurgyAPI.ID + ".show_usage";
        public static final String TOOLTIP_SUFFIX = ".tooltip";
        public static final String TOOLTIP_EXTENDED_SUFFIX = ".tooltip.extended";
        public static final String TOOLTIP_USAGE_SUFFIX = ".tooltip.usage";
        public static final String GRAFTED_SUFFIX = ".grafted";
        public static final String JEI_GRAFTING_HEDGE_HARVEST_CATEGORY = "jei." + TheurgyAPI.ID + "category.grafting_hedge_harvest";
        public static final String JEI_GRAFTING_HEDGE_HARVEST_CATEGORY_TOOLTIP = JEI_GRAFTING_HEDGE_HARVEST_CATEGORY + ".tooltip";
        public static final String JEI_GRAFTING_HEDGE_GRAFTING_CATEGORY = "jei." + TheurgyAPI.ID + "category.grafting_hedge_graft";
        public static final String JEI_GRAFTING_HEDGE_GRAFTING_CATEGORY_TOOLTIP = JEI_GRAFTING_HEDGE_GRAFTING_CATEGORY + ".tooltip";
    }
}
