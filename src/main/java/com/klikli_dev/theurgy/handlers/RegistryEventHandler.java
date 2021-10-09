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

package com.klikli_dev.theurgy.handlers;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEventHandler {

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        //Register custom registries here
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        // Register BlockItems for blocks without custom items
        // Theurgy.LOGGER.info("Registered block items");

        // Theurgy.LOGGER.info("Registered spawn egg items");

        // Register compostable items

        // Theurgy.LOGGER.info("Registered compostable Iítems");
    }

    public static void registerSpawnEgg(IForgeRegistry<Item> registry, EntityType<? extends Mob> entityType,
                                        String name, int primaryColor, int secondaryColor) {
        SpawnEggItem spawnEggItem = new SpawnEggItem(entityType, primaryColor, secondaryColor,
                new Item.Properties().tab(Theurgy.CREATIVE_MODE_TAB));
        spawnEggItem.setRegistryName(Theurgy.id("spawn_egg/" + name));
        registry.register(spawnEggItem);
    }
}
