/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;

import net.minecraft.world.item.BowlFoodItem;

import com.klikli_dev.theurgy.item.DivinationRodItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);
    public static final RegistryObject<Item> EMPTY_JAR =
            ITEMS.register("empty_jar", () -> new Item(defaultProperties()));

    //helper item for sulfur rendering
    public static final RegistryObject<Item> EMPTY_JAR_LABELED =
            ITEMS.register("empty_jar_labeled", () -> new Item(new Item.Properties()));

    //helper item for hermetica rendering
    public static final RegistryObject<Item> THE_HERMETICA_ICON =
            ITEMS.register("the_hermetica_icon", () -> new Item(new Item.Properties()));

    //helper item for sulfur rendering
    public static final RegistryObject<Item> JAR_LABEL =
            ITEMS.register("jar_label", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALCHEMICAL_SULFUR =
            ITEMS.register("alchemical_sulfur", () -> new AlchemicalSulfurItem(defaultProperties()));

    public static final RegistryObject<Item> ALCHEMICAL_SALT_ORE =
            ITEMS.register("alchemical_salt_ore", () -> new Item(defaultProperties().food(FoodRegistry.ALCHEMICAL_SALT)));

    public static final RegistryObject<Item> DIVINATION_ROD_T1 =
            ITEMS.register("divination_rod_t1", () -> new DivinationRodItem(
                    defaultProperties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, TagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, TagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 3, true));

    public static final RegistryObject<Item> DIVINATION_ROD_T2 =
            ITEMS.register("divination_rod_t2", () -> new DivinationRodItem(
                    defaultProperties().stacksTo(1).defaultDurability(1),
                    Tiers.IRON, TagRegistry.DIVINATION_ROD_T2_ALLOWED_BLOCKS, TagRegistry.DIVINATION_ROD_T2_DISALLOWED_BLOCKS,
                    96, 40, 6, true));

    public static final RegistryObject<Item> DIVINATION_ROD_T3 =
            ITEMS.register("divination_rod_t3", () -> new DivinationRodItem(
                    defaultProperties().stacksTo(1).defaultDurability(1),
                    Tiers.IRON, TagRegistry.DIVINATION_ROD_T3_ALLOWED_BLOCKS, TagRegistry.DIVINATION_ROD_T3_DISALLOWED_BLOCKS,
                    96, 40, 9, true));

    public static final RegistryObject<Item> DIVINATION_ROD_T4 =
            ITEMS.register("divination_rod_t4", () -> new DivinationRodItem(
                    defaultProperties().stacksTo(1).defaultDurability(1),
                    Tiers.IRON, TagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS, TagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS,
                    96, 40, 12, true));
    //TODO: in the future, no attuning for higher tier rods


    public static final RegistryObject<BlockItem> CALCINATION_OVEN =
            ITEMS.register("calcination_oven", () -> new BlockItem(BlockRegistry.CALCINATION_OVEN.get(), defaultProperties()));

    public static final RegistryObject<BlockItem> PYROMANTIC_BRAZIER =
            ITEMS.register("pyromantic_brazier", () -> new BlockItem(BlockRegistry.PYROMANTIC_BRAZIER.get(), defaultProperties()));

    public static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Theurgy.CREATIVE_MODE_TAB);
    }
}
