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

import com.klikli_dev.theurgy.registry.TooltipRegistry.withTooltip
import com.klikli_dev.theurgy.registry.ItemRegistry
import com.klikli_dev.theurgy.registry.SoundRegistry
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import vazkii.patchouli.api.PatchouliAPI

@Mod(Theurgy.MOD_ID)
object Theurgy {

    const val MOD_ID: String = "theurgy"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        MOD_BUS.register(ItemRegistry.items)
        MOD_BUS.register(SoundRegistry.sounds)

        //register event buses
        MOD_BUS.addListener(Theurgy::commonSetup)
        MOD_BUS.addListener(Theurgy::serverSetup)
    }

    private fun commonSetup(@Suppress("UNUSED_PARAMETER") event: FMLCommonSetupEvent) {
        LOGGER.info("Common setup complete.")

        //add tooltip to the hermetica / guide book
        PatchouliAPI.get().getBookStack(id("grimoire")).item.withTooltip(true) {
            it.tag?.getString("patchouli:book") == "theurgy:grimoire"
        };
    }

    private fun serverSetup(@Suppress("UNUSED_PARAMETER") event: FMLDedicatedServerSetupEvent) {
        LOGGER.info("Dedicated server setup complete.")
    }

    fun id(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)
}
