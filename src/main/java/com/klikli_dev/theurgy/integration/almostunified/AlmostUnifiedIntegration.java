/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.almostunified;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class AlmostUnifiedIntegration {
    public static boolean isLoaded() {
        return ModList.get().isLoaded("almostunified");
    }

    @Nullable
    public static Item getPreferredItemForTag(TagKey<Item> tag) {
        //TODO: enable
//        if (isLoaded()) {
//            return AlmostUnifiedHelper.getPreferredItemForTag(tag);
//        }

        return null;
    }

//    public static class AlmostUnifiedHelper {
//        @Nullable
//        public static Item getPreferredItemForTag(TagKey<Item> tag) {
//            return AlmostUnifiedLookup.INSTANCE.getPreferredItemForTag(tag);
//        }
//    }
}
