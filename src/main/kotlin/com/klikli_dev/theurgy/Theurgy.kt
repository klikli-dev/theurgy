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
package com.klikli_dev.theurgy

import com.klikli_dev.theurgy.config.TheurgyClientConfig
import com.klikli_dev.theurgy.config.TheurgyCommonConfig
import com.klikli_dev.theurgy.config.TheurgyServerConfig
import com.klikli_dev.theurgy.network.TheurgyPackets
import com.klikli_dev.theurgy.registry.ItemRegistry
import com.klikli_dev.theurgy.registry.SoundRegistry
import com.klikli_dev.theurgy.registry.TooltipRegistry.withTooltip
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemModelsProperties
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import vazkii.patchouli.api.PatchouliAPI

@Mod(Theurgy.MOD_ID)
object Theurgy {

    const val MOD_ID: String = "theurgy"

    val itemGroup: ItemGroup = object : ItemGroup("theurgy") {
        override fun createIcon() = ItemStack(ItemRegistry.theurgy)
    }

    val logger: Logger = LogManager.getLogger(MOD_ID)

    init {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TheurgyServerConfig.spec)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TheurgyCommonConfig.spec)
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TheurgyClientConfig.spec)

        ItemRegistry.items.register(MOD_BUS);
        SoundRegistry.sounds.register(MOD_BUS);

        //register event buses
        MOD_BUS.addListener(Theurgy::clientSetup)
        MOD_BUS.addListener(Theurgy::commonSetup)
        MOD_BUS.addListener(Theurgy::serverSetup)
    }

    private fun onModConfigEvent(event: ModConfig.ModConfigEvent) {
        //Clear the config cache on reload.
        if (event.config.spec === TheurgyServerConfig.spec) {
            TheurgyServerConfig.clear()
        }
        if (event.config.spec === TheurgyCommonConfig.spec) {
            TheurgyCommonConfig.clear()
        }
        if (event.config.spec === TheurgyClientConfig.spec) {
            TheurgyClientConfig.clear()
        }
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        logger.info("Client setup complete.")

        //register item model properties
        event.enqueueWork {

            //Register item model properties
            ItemModelsProperties.registerProperty(
                ItemRegistry.divinationRodT1,
                id("linked")
            ) { itemStack: ItemStack, _, _ ->
                itemStack.tag?.getFloat(TheurgyNbt.DIVINATION_IS_LINKED) ?: 0.0f
            }
            logger.debug("Registered Item Model Properties")
        }
    }

    private fun commonSetup(@Suppress("UNUSED_PARAMETER") event: FMLCommonSetupEvent) {
        TheurgyPackets.registerMessages()

        //add tooltip to the hermetica / guide book
        PatchouliAPI.get().getBookStack(id("grimoire")).item.withTooltip(true) {
            it.tag?.getString("patchouli:book") == "theurgy:grimoire"
        };

        logger.info("Common setup complete.")
    }

    private fun serverSetup(@Suppress("UNUSED_PARAMETER") event: FMLDedicatedServerSetupEvent) {
        logger.info("Dedicated server setup complete.")
    }

    fun id(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)
}
