// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SulfurRegistry {
    public static final DeferredRegister.Items SULFURS = DeferredRegister.createItems(Theurgy.MODID);

    public static final DeferredItem<AlchemicalSulfurItem> GEMS_ABUNDANT = registerNiter("gems_abundant", ItemRegistry.GEMS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final DeferredItem<AlchemicalSulfurItem> GEMS_COMMON = registerNiter("gems_common", ItemRegistry.GEMS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final DeferredItem<AlchemicalSulfurItem> GEMS_RARE = registerNiter("gems_rare", ItemRegistry.GEMS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final DeferredItem<AlchemicalSulfurItem> GEMS_PRECIOUS = registerNiter("gems_precious", ItemRegistry.GEMS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final DeferredItem<AlchemicalSulfurItem> METALS_ABUNDANT = registerNiter("metals_abundant", ItemRegistry.METALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final DeferredItem<AlchemicalSulfurItem> METALS_COMMON = registerNiter("metals_common", ItemRegistry.METALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final DeferredItem<AlchemicalSulfurItem> METALS_RARE = registerNiter("metals_rare", ItemRegistry.METALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final DeferredItem<AlchemicalSulfurItem> METALS_PRECIOUS = registerNiter("metals_precious", ItemRegistry.METALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final DeferredItem<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT = registerNiter("other_minerals_abundant", ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final DeferredItem<AlchemicalSulfurItem> OTHER_MINERALS_COMMON = registerNiter("other_minerals_common", ItemRegistry.OTHER_MINERALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final DeferredItem<AlchemicalSulfurItem> OTHER_MINERALS_RARE = registerNiter("other_minerals_rare", ItemRegistry.OTHER_MINERALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final DeferredItem<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS = registerNiter("other_minerals_precious", ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);


    public static final DeferredItem<AlchemicalSulfurItem> LOGS = registerWithTagSourceNameOverride(ItemTags.LOGS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);

    //Crops
    public static final DeferredItem<AlchemicalSulfurItem> WHEAT = registerDefault("wheat", Items.WHEAT, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);


    //Common Metals
    public static final DeferredItem<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride(Tags.Items.INGOTS_IRON, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride(Tags.Items.INGOTS_COPPER, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_SILVER, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride(Tags.Items.INGOTS_GOLD, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride(Tags.Items.INGOTS_NETHERITE, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_URANIUM, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final DeferredItem<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_AZURE_SILVER, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_ZINC, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_OSMIUM, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_NICKEL, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_LEAD, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_ALLTHEMODIUM, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_UNOBTAINIUM, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_IRIDIUM, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_TIN, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CINNABAR, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CRIMSON_IRON, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_PLATINUM, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_VIBRANIUM, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> DESH = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_DESH, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> OSTRUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_OSTRUM, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> CALORITE = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_CALORITE, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final DeferredItem<AlchemicalSulfurItem> IESNIUM = registerWithSourceNameOverride(ItemTagRegistry.INGOTS_IESNIUM, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final DeferredItem<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride(Tags.Items.GEMS_DIAMOND, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride(Tags.Items.GEMS_EMERALD, AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride(Tags.Items.GEMS_LAPIS, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride(Tags.Items.GEMS_QUARTZ, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride(Tags.Items.GEMS_AMETHYST, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride(Tags.Items.GEMS_PRISMARINE, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride(ItemTagRegistry.GEMS_RUBY, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_APATITE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride(ItemTagRegistry.GEMS_PERIDOT, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_FLUORITE, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SAPPHIRE, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SAL_AMMONIAC, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> CERTUS_QUARTZ = registerWithSourceNameOverride(ItemTagRegistry.GEMS_CERTUS_QUARTZ, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> FLUIX = registerWithSourceNameOverride(ItemTagRegistry.GEMS_FLUIX, AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final DeferredItem<AlchemicalSulfurItem> NITER = registerWithSourceNameOverride(ItemTagRegistry.GEMS_NITER, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final DeferredItem<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride(Tags.Items.DUSTS_REDSTONE, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> COAL = registerDefault("coal", ItemTags.COALS, AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final DeferredItem<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride(ItemTagRegistry.GEMS_SULFUR, AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);

    /**
     * Sulfurs for which we return true will not be exlcuded from jei/modonomicon renderers despite not having a liquefaction recipe
     */
    public static boolean keepInItemLists(AlchemicalSulfurItem sulfur) {
        return sulfur.type() == AlchemicalSulfurType.NITER;
    }

    public static DeferredItem<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerWithTagSourceNameOverride(TagKey<Item> source, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SULFUR_SOURCE_TAG,
                        source)
        ).overrideTagSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerWithSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(tier).type(type));
    }

    /**
     * The source tag does not need to cover all possible sources (e.g. a "uranium" tag that covers ore, raw metal, ingot, ...) but rather one possible source that should be used to get the icon from.
     */
    public static DeferredItem<AlchemicalSulfurItem> registerWithSourceNameOverride(TagKey<Item> source, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name(source), () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SULFUR_SOURCE_TAG,
                        source
                )).overrideSourceName(true).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerNiter(String name, DeferredItem<?> sourceStack, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties().component(
                DataComponentRegistry.SULFUR_SOURCE_ITEM,
                DeferredHolder.create(Registries.ITEM, sourceStack.getId())
        )).overrideSourceName(true).autoTooltip(true, false).autoName(true, false).withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()))).tier(tier).type(AlchemicalSulfurType.NITER));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, TagKey<Item> source, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SULFUR_SOURCE_TAG,
                        source)
        ).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, DeferredItem<?> sourceStack, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SULFUR_SOURCE_ITEM,
                        DeferredHolder.create(Registries.ITEM, sourceStack.getId())
                )).tier(tier).type(type));
    }

    public static DeferredItem<AlchemicalSulfurItem> registerDefault(String name, Item sourceStack, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()
                .component(
                        DataComponentRegistry.SULFUR_SOURCE_ITEM,
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
                if (result != null && !result.isEmpty() && result.getItem() instanceof AlchemicalSulfurItem){
                    event.accept(result.copyWithCount(1));
                }
            });

            event.accept(SulfurRegistry.GEMS_ABUNDANT.get());
            event.accept(SulfurRegistry.GEMS_COMMON.get());
            event.accept(SulfurRegistry.GEMS_RARE.get());
            event.accept(SulfurRegistry.GEMS_PRECIOUS.get());
            event.accept(SulfurRegistry.METALS_ABUNDANT.get());
            event.accept(SulfurRegistry.METALS_COMMON.get());
            event.accept(SulfurRegistry.METALS_RARE.get());
            event.accept(SulfurRegistry.METALS_PRECIOUS.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_COMMON.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_RARE.get());
            event.accept(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get());
        }
    }
}
