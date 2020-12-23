/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy;

import com.github.klikli_dev.theurgy.common.TheurgyItemGroup;
import com.github.klikli_dev.theurgy.common.config.TheurgyConfig;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Theurgy.MODID)
public class Theurgy {
    //region Fields
    public static final String MODID = "theurgy";
    public static final String NAME = "Theurgy";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ItemGroup ITEM_GROUP = new TheurgyItemGroup();
    private static final TheurgyConfig CONFIG = new TheurgyConfig();
    public static Theurgy INSTANCE;
    //endregion Fields

    //region Initialization
    public Theurgy() {
        INSTANCE = this;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG.spec);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.ITEMS.register(modEventBus);

        //register event buses
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::serverSetup);
        modEventBus.addListener(this::onModConfigEvent);

        MinecraftForge.EVENT_BUS.register(this);
    }

    //endregion Initialization
    //region Methods
    public void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == CONFIG.spec) {
            //Clear the config cache on reload.
            CONFIG.clear();
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        LOGGER.info("Common setup complete.");
    }

    private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Dedicated server setup complete.");
    }
    //endregion Methods
}
