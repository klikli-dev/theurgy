/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<Item> EMPTY_JAR =
            ITEMS.register("empty_jar", () -> new Item(defaultProperties()));

    public static final RegistryObject<Item> JAR_LABEL =
            ITEMS.register("jar_label", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALCHEMICAL_SULFUR =
            ITEMS.register("alchemical_sulfur", () -> new AlchemicalSulfurItem(defaultProperties()));

    public static final RegistryObject<Item> DIVINATION_ROD =
            ITEMS.register("divination_rod", () -> new DivinationRodItem(defaultProperties().stacksTo(1), () -> Tiers.WOOD,() -> 40, () -> 96));

    public static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Theurgy.CREATIVE_MODE_TAB);
    }
}
