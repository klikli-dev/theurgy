/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);
    public static final RegistryObject<Item> EMPTY_JAR =
            ITEMS.register("empty_jar", () -> new Item(new Item.Properties()));

    //helper item for sulfur rendering
    public static final RegistryObject<Item> EMPTY_JAR_LABELED =
            ITEMS.register("empty_jar_labeled", () -> new Item(new Item.Properties()));

    //helper item for sulfur rendering
    public static final RegistryObject<Item> JAR_LABEL =
            ITEMS.register("jar_label", () -> new Item(new Item.Properties()));
    public static final RegistryObject<AlchemicalSulfurItem> ALCHEMICAL_SULFUR =
            ITEMS.register("alchemical_sulfur", () -> new AlchemicalSulfurItem(new Item.Properties()));

//    public static final RegistryObject<Item> SULFUR_INGOT =
//            ITEMS.register("sulfur_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T1 =
            ITEMS.register("divination_rod_t1", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, TagRegistry.DIVINATION_ROD_T1_ALLOWED_BLOCKS, TagRegistry.DIVINATION_ROD_T1_DISALLOWED_BLOCKS,
                    96, 40, 10, true));

    public static final RegistryObject<DivinationRodItem> DIVINATION_ROD_T4 =
            ITEMS.register("divination_rod_t4", () -> new DivinationRodItem(
                    new Item.Properties().stacksTo(1).defaultDurability(1),
                    Tiers.STONE, TagRegistry.DIVINATION_ROD_T4_ALLOWED_BLOCKS,  TagRegistry.DIVINATION_ROD_T4_DISALLOWED_BLOCKS,
                    96, 40, 10, false));

    public static void onRegisterCreativeModeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation("occultism:occultism"),
                (builder) -> {
                    builder.icon(() -> new ItemStack(EMPTY_JAR.get()))
                            .title(Component.translatable(TheurgyConstants.I18n.ITEM_GROUP)).build();
                    builder.displayItems((featureFlagSet, output, hasPermission) -> {
                        output.accept(EMPTY_JAR.get());

                        if (FMLEnvironment.dist == Dist.CLIENT) {
                            AlchemicalSulfurItem.DistHelper.registerCreativeModeTabs(ALCHEMICAL_SULFUR.get(), output);
                            DivinationRodItem.DistHelper.registerCreativeModeTabs(DIVINATION_ROD_T1.get(), output);
                            DivinationRodItem.DistHelper.registerCreativeModeTabs(DIVINATION_ROD_T4.get(), output);
                        }
                    });

                }
        );
    }
}
