// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister.Items SULFURS = DeferredRegister.createItems(Theurgy.MODID);


    //Crops
    public static final DeferredItem<AlchemicalSulfurItem> WHEAT = registerForSourceItem(Items.WHEAT, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.MISC);

    //Common Metals
    public static final DeferredItem<AlchemicalSulfurItem> IRON = registerForSourceTag(Tags.Items.INGOTS_IRON, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> COPPER = registerForSourceTag(Tags.Items.INGOTS_COPPER, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> SILVER = registerForSourceTag(ItemTagRegistry.INGOTS_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> GOLD = registerForSourceTag(Tags.Items.INGOTS_GOLD, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> NETHERITE = registerForSourceTag(Tags.Items.INGOTS_NETHERITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> URANIUM = registerForSourceTag(ItemTagRegistry.INGOTS_URANIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> AZURE_SILVER = registerForSourceTag(ItemTagRegistry.INGOTS_AZURE_SILVER, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ZINC = registerForSourceTag(ItemTagRegistry.INGOTS_ZINC, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSMIUM = registerForSourceTag(ItemTagRegistry.INGOTS_OSMIUM, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> NICKEL = registerForSourceTag(ItemTagRegistry.INGOTS_NICKEL, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> LEAD = registerForSourceTag(ItemTagRegistry.INGOTS_LEAD, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ALLTHEMODIUM = registerForSourceTag(ItemTagRegistry.INGOTS_ALLTHEMODIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> UNOBTAINIUM = registerForSourceTag(ItemTagRegistry.INGOTS_UNOBTAINIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IRIDIUM = registerForSourceTag(ItemTagRegistry.INGOTS_IRIDIUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> TIN = registerForSourceTag(ItemTagRegistry.INGOTS_TIN, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CINNABAR = registerForSourceTag(ItemTagRegistry.INGOTS_CINNABAR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CRIMSON_IRON = registerForSourceTag(ItemTagRegistry.INGOTS_CRIMSON_IRON, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> PLATINUM = registerForSourceTag(ItemTagRegistry.INGOTS_PLATINUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> VIBRANIUM = registerForSourceTag(ItemTagRegistry.INGOTS_VIBRANIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> DESH = registerForSourceTag(ItemTagRegistry.INGOTS_DESH, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSTRUM = registerForSourceTag(ItemTagRegistry.INGOTS_OSTRUM, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CALORITE = registerForSourceTag(ItemTagRegistry.INGOTS_CALORITE, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IESNIUM = registerForSourceTag(ItemTagRegistry.INGOTS_IESNIUM, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final DeferredItem<AlchemicalSulfurItem> DIAMOND = registerForSourceTag(Tags.Items.GEMS_DIAMOND, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> EMERALD = registerForSourceTag(Tags.Items.GEMS_EMERALD, AlchemicalDerivativeTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> LAPIS = registerForSourceTag(Tags.Items.GEMS_LAPIS, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> QUARTZ = registerForSourceTag(Tags.Items.GEMS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> AMETHYST = registerForSourceTag(Tags.Items.GEMS_AMETHYST, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PRISMARINE = registerForSourceTag(Tags.Items.GEMS_PRISMARINE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> RUBY = registerForSourceTag(ItemTagRegistry.GEMS_RUBY, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> APATITE = registerForSourceTag(ItemTagRegistry.GEMS_APATITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PERIDOT = registerForSourceTag(ItemTagRegistry.GEMS_PERIDOT, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUORITE = registerForSourceTag(ItemTagRegistry.GEMS_FLUORITE, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAPPHIRE = registerForSourceTag(ItemTagRegistry.GEMS_SAPPHIRE, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAL_AMMONIAC = registerForSourceTag(ItemTagRegistry.GEMS_SAL_AMMONIAC, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> CERTUS_QUARTZ = registerForSourceTag(ItemTagRegistry.GEMS_CERTUS_QUARTZ, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUIX = registerForSourceTag(ItemTagRegistry.GEMS_FLUIX, AlchemicalDerivativeTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> NITER = registerForSourceTag(ItemTagRegistry.GEMS_NITER, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final DeferredItem<AlchemicalSulfurItem> REDSTONE = registerForSourceTag(Tags.Items.DUSTS_REDSTONE, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> COAL = registerForSourceTag(ItemTags.COALS, AlchemicalDerivativeTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> SULFUR = registerForSourceTag(ItemTagRegistry.GEMS_SULFUR, AlchemicalDerivativeTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);


    /**
     * The source tag does not need to cover all possible sources (e.g. a "uranium" tag that covers ore, raw metal, ingot, ...) but rather one possible source that should be used to get the icon from.
     */
    public static DeferredItem<AlchemicalSulfurItem> registerForSourceTag(TagKey<Item> source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> AlchemicalSulfurItem.ofSource(source, tier, type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerForSourceItem(Item source, AlchemicalDerivativeTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> AlchemicalSulfurItem.ofSource(source, tier, type));
    }

    public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
    }

    private static String name(TagKey<Item> source) {
        var slashIndex = source.location().getPath().lastIndexOf("/");
        return source.location().getPath().substring(slashIndex + 1);
    }

    private static String name(Item source) {
        //noinspection deprecation
        var namePath = source.builtInRegistryHolder().key().location().getPath();
        var slashIndex = namePath.lastIndexOf("/");
        return namePath.substring(slashIndex + 1);
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
                if (result != null && !result.isEmpty() && result.getItem() instanceof AlchemicalSulfurItem && !event.getParentEntries().contains(result)) {
                    var stack = result.copyWithCount(1);

                    if (!event.getParentEntries().contains(stack)) {
                        event.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
                }
            });
        }
    }
}
