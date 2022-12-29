/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.integration.jei;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Theurgy.loc("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {

        ItemRegistry.ITEMS.getEntries().stream().filter(item -> item.get() instanceof DivinationRodItem).forEach(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK,
                    item.get(), DivinationRodSubtypeInterpreter.get());
            Theurgy.LOGGER.debug("Registered Divination Rod JEI Subtype Interpreter for: {}", item.getKey());
        });
    }
}
