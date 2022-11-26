/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy;

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
    }

    public static class I18n{

    }
}
