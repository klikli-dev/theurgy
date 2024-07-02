// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.kubejs;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;

public class KubeJsIntegrationImpl implements KubeJsIntegration {
    public boolean isLoaded() {
        return ModList.get().isLoaded("kubejs");
    }

    @Override
    public boolean isEmpty(TagKey<Fluid> tag) {
        if (this.isLoaded()) {
            return KubeJsHelper.isEmpty(tag);
        }

        return true;
    }

    public static class KubeJsHelper {
        public static boolean isEmpty(TagKey<Fluid> tag) {
            //TODO: Add functionality once available
            //See https://github.com/KubeJS-Mods/KubeJS/commit/b8970fd4bd867da594952042bd3bd91b265115b3

            //TODO then re-enable sizedFluidIngredient condition generation in recipe providers
            return false;
        }
    }
}
