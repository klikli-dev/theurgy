// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister.Items SULFURS = DeferredRegister.createItems(Theurgy.MODID);

    public static final DeferredItem<AlchemicalSulfurItem> LOGS = registerWithTagSourceNameOverride(ItemTags.LOGS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.MISC);

    //Crops
    public static final DeferredItem<AlchemicalSulfurItem> WHEAT = registerDefault("wheat", Items.WHEAT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.MISC);

    //Common Metals
    public static final DeferredItem<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride(Tags.Items.INGOTS_IRON, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride(Tags.Items.INGOTS_COPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride(Tags.Items.INGOTS_GOLD, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride(Tags.Items.INGOTS_NETHERITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_URANIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_AZURE_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_ZINC, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_OSMIUM, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_NICKEL, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_LEAD, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_ALLTHEMODIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_UNOBTAINIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_IRIDIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_TIN, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CINNABAR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CRIMSON_IRON, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_PLATINUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_VIBRANIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> DESH = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_DESH, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSTRUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_OSTRUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CALORITE = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CALORITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IESNIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_IESNIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final DeferredItem<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride(Tags.Items.GEMS_DIAMOND, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride(Tags.Items.GEMS_EMERALD, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride(Tags.Items.GEMS_LAPIS, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride(Tags.Items.GEMS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride(Tags.Items.GEMS_AMETHYST, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride(Tags.Items.GEMS_PRISMARINE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride(ItemTagRegistry.GEMS_RUBY, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_APATITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride(ItemTagRegistry.GEMS_PERIDOT, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_FLUORITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SAPPHIRE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SAL_AMMONIAC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> CERTUS_QUARTZ = registerWithSourceNameOverride(ItemTagRegistry.GEMS_CERTUS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUIX = registerWithSourceNameOverride(ItemTagRegistry.GEMS_FLUIX, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> NITER = registerWithSourceNameOverride(ItemTagRegistry.GEMS_NITER, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final DeferredItem<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride(Tags.Items.DUSTS_REDSTONE, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> COAL = registerDefault("coal", ItemTags.COALS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SULFUR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);


    public static DeferredItem<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).useTagAsSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerWithTagSourceNameOverride(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_TAG,
                        source)
        ).useTagAsSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerWithSourceNameOverride(String name, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).useCustomSourceName(true).tier(tier).type(type));
    }

    /**
     * The source tag does not need to cover all possible sources (e.g. a "uranium" tag that covers ore, raw metal, ingot, ...) but rather one possible source that should be used to get the icon from.
     */
    public static DeferredItem<AlchemicalSulfurItem> registerWithSourceNameOverride(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_TAG,
                        source
                )).useCustomSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_TAG,
                        source)
        ).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, DeferredItem<?> sourceStack, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_ITEM,
                        DeferredHolder.create(Registries.ITEM, sourceStack.getId())
                )).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, Item sourceStack, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SOURCE_ITEM,
                        sourceStack.builtInRegistryHolder()
                )).tier(tier).type(type));
    }

    public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
    }

    private static String name(TagKey<Item> source) {
        var slashIndex = source.location().getPath().lastIndexOf("/");
        return source.location().getPath().substring(slashIndex + 1);
    }


    /**
     * We add only those sulfurs that have a recipe to the creative tab.
     * Other sulfurs are registered, but should not be shown to players, as the related items are from mods that are not loaded
     */
    public static void onBuildCreativeModTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeModeTabRegistry.THEURGY.get()) {
            var level = LevelUtil.getLevelWithoutContext();
            if (level == null) {
                return;
            }

            var recipeManager = level.getRecipeManager();
            var liquefactionRecipes = recipeManager.getAllRecipesFor(RecipeTypeRegistry.LIQUEFACTION.get());

            //Register only sulfurs that have a liquefaction recipe
            liquefactionRecipes.forEach(r -> {
                var result = r.value().getResultItem(level.registryAccess());
                if (result != null && !result.isEmpty() && result.getItem() instanceof AlchemicalSulfurItem && !event.getParentEntries().contains(result)){
                    var stack = result.copyWithCount(1);

                    if (!event.getParentEntries().contains(stack)) {
                        event.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
                }
            });
        }
    }
}
