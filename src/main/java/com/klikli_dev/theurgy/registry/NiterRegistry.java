// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalNiterItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NiterRegistry {
    public static final DeferredRegister.Items NITERS = DeferredRegister.createItems(Theurgy.MODID);

    public static final DeferredItem<AlchemicalNiterItem> GEMS_ABUNDANT = register("gems_abundant", ItemRegistry.GEMS_ABUNDANT_ICON, AlchemicalDerivativeTier.ABUNDANT);
    public static final DeferredItem<AlchemicalNiterItem> GEMS_COMMON = register("gems_common", ItemRegistry.GEMS_COMMON_ICON, AlchemicalDerivativeTier.COMMON);
    public static final DeferredItem<AlchemicalNiterItem> GEMS_RARE = register("gems_rare", ItemRegistry.GEMS_RARE_ICON, AlchemicalDerivativeTier.RARE);
    public static final DeferredItem<AlchemicalNiterItem> GEMS_PRECIOUS = register("gems_precious", ItemRegistry.GEMS_PRECIOUS_ICON, AlchemicalDerivativeTier.PRECIOUS);

    public static final DeferredItem<AlchemicalNiterItem> METALS_ABUNDANT = register("metals_abundant", ItemRegistry.METALS_ABUNDANT_ICON, AlchemicalDerivativeTier.ABUNDANT);
    public static final DeferredItem<AlchemicalNiterItem> METALS_COMMON = register("metals_common", ItemRegistry.METALS_COMMON_ICON, AlchemicalDerivativeTier.COMMON);
    public static final DeferredItem<AlchemicalNiterItem> METALS_RARE = register("metals_rare", ItemRegistry.METALS_RARE_ICON, AlchemicalDerivativeTier.RARE);
    public static final DeferredItem<AlchemicalNiterItem> METALS_PRECIOUS = register("metals_precious", ItemRegistry.METALS_PRECIOUS_ICON, AlchemicalDerivativeTier.PRECIOUS);

    public static final DeferredItem<AlchemicalNiterItem> OTHER_MINERALS_ABUNDANT = register("other_minerals_abundant", ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, AlchemicalDerivativeTier.ABUNDANT);
    public static final DeferredItem<AlchemicalNiterItem> OTHER_MINERALS_COMMON = register("other_minerals_common", ItemRegistry.OTHER_MINERALS_COMMON_ICON, AlchemicalDerivativeTier.COMMON);
    public static final DeferredItem<AlchemicalNiterItem> OTHER_MINERALS_RARE = register("other_minerals_rare", ItemRegistry.OTHER_MINERALS_RARE_ICON, AlchemicalDerivativeTier.RARE);
    public static final DeferredItem<AlchemicalNiterItem> OTHER_MINERALS_PRECIOUS = register("other_minerals_precious", ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, AlchemicalDerivativeTier.PRECIOUS);

    public static DeferredItem<AlchemicalNiterItem> register(String name, DeferredItem<?> sourceStack, AlchemicalDerivativeTier tier) {
        return register(name, () -> new AlchemicalNiterItem(new Item.Properties().component(
                DataComponentRegistry.SOURCE_ITEM,
                DeferredHolder.create(Registries.ITEM, sourceStack.getId())
        ), tier));
    }

    public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> sup) {
        return NITERS.register("alchemical_niter_" + name, sup);
    }

    /**
     * We generally register all niters
     */
    public static void onBuildCreativeModTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeModeTabRegistry.THEURGY.get()) {

            NITERS.getEntries().forEach(n -> event.accept(n.get()));
        }
    }
}
