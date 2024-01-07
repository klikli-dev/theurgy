// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.util.LevelUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister<Item> SULFURS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<AlchemicalSulfurItem> GENERIC = registerWithTagSourceNameOverride("generic", AlchemicalSulfurTier.ABUNDANT);

    public static final RegistryObject<AlchemicalSulfurItem> GEMS_ABUNDANT = registerGeneric("gems_abundant", ItemRegistry.GEMS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_COMMON = registerGeneric("gems_common", ItemRegistry.GEMS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_RARE = registerGeneric("gems_rare", ItemRegistry.GEMS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_PRECIOUS = registerGeneric("gems_precious", ItemRegistry.GEMS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> METALS_ABUNDANT = registerGeneric("metals_abundant", ItemRegistry.METALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_COMMON = registerGeneric("metals_common", ItemRegistry.METALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_RARE = registerGeneric("metals_rare", ItemRegistry.METALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_PRECIOUS = registerGeneric("metals_precious", ItemRegistry.METALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT = registerGeneric("other_minerals_abundant", ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_COMMON = registerGeneric("other_minerals_common", ItemRegistry.OTHER_MINERALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_RARE = registerGeneric("other_minerals_rare", ItemRegistry.OTHER_MINERALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS = registerGeneric("other_minerals_precious", ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);


    public static final RegistryObject<AlchemicalSulfurItem> LOGS = registerWithTagSourceNameOverride("logs", AlchemicalSulfurTier.ABUNDANT);

    //Crops
    public static final RegistryObject<AlchemicalSulfurItem> WHEAT = registerDefault("wheat", AlchemicalSulfurTier.ABUNDANT);


    //Common Metals
    public static final RegistryObject<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride("iron", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride("copper", AlchemicalSulfurTier.ABUNDANT);

    public static final RegistryObject<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride("silver", AlchemicalSulfurTier.RARE);

    public static final RegistryObject<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride("gold", AlchemicalSulfurTier.RARE);

    public static final RegistryObject<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride("netherite", AlchemicalSulfurTier.PRECIOUS);
    public static final RegistryObject<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride("uranium", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride("azure_silver", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride("zinc", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride("osmium", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride("nickel", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride("lead", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride("allthemodium", AlchemicalSulfurTier.PRECIOUS);
    public static final RegistryObject<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride("unobtainium", AlchemicalSulfurTier.PRECIOUS);
    public static final RegistryObject<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride("iridium", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride("tin", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride("cinnabar", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride("crimson_iron", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride("platinum", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride("vibranium", AlchemicalSulfurTier.PRECIOUS);

    //Common Gems

    public static final RegistryObject<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride("diamond", AlchemicalSulfurTier.PRECIOUS);
    public static final RegistryObject<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride("emerald", AlchemicalSulfurTier.PRECIOUS);
    public static final RegistryObject<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride("lapis", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride("quartz", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride("amethyst", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride("prismarine", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride("ruby", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride("apatite", AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride("peridot", AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride("fluorite", AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride("sapphire", AlchemicalSulfurTier.RARE);

    public static final RegistryObject<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride("sal_ammoniac", AlchemicalSulfurTier.ABUNDANT);

    //Other Common Minerals
    public static final RegistryObject<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride("redstone", AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> COAL = registerDefault("coal", AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride("sulfur", AlchemicalSulfurTier.COMMON);

    /**
     * Sulfurs in this list will not be exlcuded from jei/modonomicon renderers despite not having a liquefaction recipe
     */
    public static final Supplier<Set<AlchemicalSulfurItem>> SULFURS_TO_KEEP_IN_ITEM_LISTS = Suppliers.memoize(() -> Set.of(
            GEMS_ABUNDANT.get(),
            GEMS_COMMON.get(),
            GEMS_RARE.get(),
            GEMS_PRECIOUS.get(),
            METALS_ABUNDANT.get(),
            METALS_COMMON.get(),
            METALS_RARE.get(),
            METALS_PRECIOUS.get(),
            OTHER_MINERALS_ABUNDANT.get(),
            OTHER_MINERALS_COMMON.get(),
            OTHER_MINERALS_RARE.get(),
            OTHER_MINERALS_PRECIOUS.get()
    ));

    public static RegistryObject<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true).tier(tier));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerWithSourceNameOverride(String name, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(tier));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerGeneric(String name, Supplier<Item> sourceStackSupplier, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties(), Suppliers.memoize(() -> new ItemStack(sourceStackSupplier.get()))).overrideSourceName(true).autoTooltip(true, false).autoName(true, false).withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()))).tier(tier));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerDefault(String name, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).tier(tier));
    }

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return SULFURS.register("alchemical_sulfur_" + name, sup);
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

            SULFURS.getEntries().stream().map(RegistryObject::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
                var preferred = getPreferredSulfurVariant(sulfur, liquefactionRecipes, level);
                preferred.ifPresent(itemStack -> event.accept(itemStack.copyWithCount(1)));
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

    /**
     * We want sulfurs to display with the most recognizable source items: ingots, gems, dusts.
     * This method selects these sulfurs, and otherwise gets the first matching one.
     */
    public static Optional<ItemStack> getPreferredSulfurVariant(AlchemicalSulfurItem sulfur, List<LiquefactionRecipe> liquefactionRecipes, Level level) {
        var matchingRecipes = liquefactionRecipes.stream()
                .filter(recipe -> recipe.getResultItem(level.registryAccess()) != null && recipe.getResultItem(level.registryAccess()).getItem() == sulfur).toList();

        //prefer ingot/gems
        var sulfurWithNbt = matchingRecipes.stream().filter(r -> Arrays.stream(r.getIngredient().getItems()).anyMatch(i -> i.is(Tags.Items.INGOTS) || i.is(Tags.Items.GEMS))).findFirst().map(r -> r.getResultItem(level.registryAccess()));

        //second choice: dusts (e.g redstone, glowstone)
        if (sulfurWithNbt.isEmpty())
            sulfurWithNbt = matchingRecipes.stream().filter(r -> Arrays.stream(r.getIngredient().getItems()).anyMatch(i -> i.is(Tags.Items.DUSTS))).findFirst().map(r -> r.getResultItem(level.registryAccess()));

        //but fall back to any other
        if (sulfurWithNbt.isEmpty())
            sulfurWithNbt = matchingRecipes.stream().findFirst().map(r -> r.getResultItem(level.registryAccess()));

        return sulfurWithNbt;
    }
}
