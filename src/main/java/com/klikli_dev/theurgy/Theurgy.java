/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy;

import com.klikli_dev.theurgy.datagen.DataGenerators;
import com.klikli_dev.theurgy.item.TheurgyCreativeModeTab;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Theurgy.MODID)
public class Theurgy {
    public static final String MODID = "theurgy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab CREATIVE_MODE_TAB = new TheurgyCreativeModeTab();

    public static Theurgy INSTANCE;

    public Theurgy() {
        INSTANCE = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.ITEMS.register(modEventBus);

        modEventBus.addListener(DataGenerators::gatherData);
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
