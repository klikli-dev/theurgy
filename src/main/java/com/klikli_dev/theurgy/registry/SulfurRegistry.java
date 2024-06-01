// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurType;
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
import java.util.function.Supplier;

public class SulfurRegistry {
    public static final DeferredRegister<Item> SULFURS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    public static final RegistryObject<AlchemicalSulfurItem> GENERIC = registerWithTagSourceNameOverride("generic", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);

    public static final RegistryObject<AlchemicalSulfurItem> GEMS_ABUNDANT = registerNiter("gems_abundant", ItemRegistry.GEMS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_COMMON = registerNiter("gems_common", ItemRegistry.GEMS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_RARE = registerNiter("gems_rare", ItemRegistry.GEMS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> GEMS_PRECIOUS = registerNiter("gems_precious", ItemRegistry.GEMS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> METALS_ABUNDANT = registerNiter("metals_abundant", ItemRegistry.METALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_COMMON = registerNiter("metals_common", ItemRegistry.METALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_RARE = registerNiter("metals_rare", ItemRegistry.METALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> METALS_PRECIOUS = registerNiter("metals_precious", ItemRegistry.METALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);

    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT = registerNiter("other_minerals_abundant", ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON, AlchemicalSulfurTier.ABUNDANT);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_COMMON = registerNiter("other_minerals_common", ItemRegistry.OTHER_MINERALS_COMMON_ICON, AlchemicalSulfurTier.COMMON);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_RARE = registerNiter("other_minerals_rare", ItemRegistry.OTHER_MINERALS_RARE_ICON, AlchemicalSulfurTier.RARE);
    public static final RegistryObject<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS = registerNiter("other_minerals_precious", ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON, AlchemicalSulfurTier.PRECIOUS);


    public static final RegistryObject<AlchemicalSulfurItem> LOGS = registerWithTagSourceNameOverride("logs", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);

    //Crops
    public static final RegistryObject<AlchemicalSulfurItem> WHEAT = registerDefault("wheat", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.MISC);


    //Common Metals
    public static final RegistryObject<AlchemicalSulfurItem> IRON = registerWithSourceNameOverride("iron", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> COPPER = registerWithSourceNameOverride("copper", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> SILVER = registerWithSourceNameOverride("silver", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> GOLD = registerWithSourceNameOverride("gold", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> NETHERITE = registerWithSourceNameOverride("netherite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> URANIUM = registerWithSourceNameOverride("uranium", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> AZURE_SILVER = registerWithSourceNameOverride("azure_silver", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ZINC = registerWithSourceNameOverride("zinc", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> OSMIUM = registerWithSourceNameOverride("osmium", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> NICKEL = registerWithSourceNameOverride("nickel", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> LEAD = registerWithSourceNameOverride("lead", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> ALLTHEMODIUM = registerWithSourceNameOverride("allthemodium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> UNOBTAINIUM = registerWithSourceNameOverride("unobtainium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> IRIDIUM = registerWithSourceNameOverride("iridium", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> TIN = registerWithSourceNameOverride("tin", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CINNABAR = registerWithSourceNameOverride("cinnabar", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CRIMSON_IRON = registerWithSourceNameOverride("crimson_iron", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> PLATINUM = registerWithSourceNameOverride("platinum", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> VIBRANIUM = registerWithSourceNameOverride("vibranium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);

    public static final RegistryObject<AlchemicalSulfurItem> DESH = registerWithSourceNameOverride("desh", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> OSTRUM = registerWithSourceNameOverride("ostrum", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> CALORITE = registerWithSourceNameOverride("calorite", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);
    public static final RegistryObject<AlchemicalSulfurItem> IESNIUM = registerWithSourceNameOverride("iesnium", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.METALS);

    //Common Gems

    public static final RegistryObject<AlchemicalSulfurItem> DIAMOND = registerWithSourceNameOverride("diamond", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> EMERALD = registerWithSourceNameOverride("emerald", AlchemicalSulfurTier.PRECIOUS, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> LAPIS = registerWithSourceNameOverride("lapis", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> QUARTZ = registerWithSourceNameOverride("quartz", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> AMETHYST = registerWithSourceNameOverride("amethyst", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> PRISMARINE = registerWithSourceNameOverride("prismarine", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> RUBY = registerWithSourceNameOverride("ruby", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> APATITE = registerWithSourceNameOverride("apatite", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> PERIDOT = registerWithSourceNameOverride("peridot", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> FLUORITE = registerWithSourceNameOverride("fluorite", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> SAPPHIRE = registerWithSourceNameOverride("sapphire", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);

    public static final RegistryObject<AlchemicalSulfurItem> SAL_AMMONIAC = registerWithSourceNameOverride("sal_ammoniac", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> CERTUS_QUARTZ = registerWithSourceNameOverride("certus_quartz", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> FLUIX = registerWithSourceNameOverride("fluix", AlchemicalSulfurTier.RARE, AlchemicalSulfurType.GEMS);
    public static final RegistryObject<AlchemicalSulfurItem> NITER = registerWithSourceNameOverride("niter", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.GEMS);

    //Other Common Minerals
    public static final RegistryObject<AlchemicalSulfurItem> REDSTONE = registerWithSourceNameOverride("redstone", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> COAL = registerDefault("coal", AlchemicalSulfurTier.ABUNDANT, AlchemicalSulfurType.OTHER_MINERALS);
    public static final RegistryObject<AlchemicalSulfurItem> SULFUR = registerWithSourceNameOverride("sulfur", AlchemicalSulfurTier.COMMON, AlchemicalSulfurType.OTHER_MINERALS);

    /**
     * Sulfurs for which we return true will not be exlcuded from jei/modonomicon renderers despite not having a liquefaction recipe
     */
    public static boolean keepInItemLists(AlchemicalSulfurItem sulfur) {
        return sulfur.type() == AlchemicalSulfurType.NITER;
    }

    public static RegistryObject<AlchemicalSulfurItem> registerWithTagSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideTagSourceName(true).tier(tier).type(type));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerWithSourceNameOverride(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).overrideSourceName(true).tier(tier).type(type));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerNiter(String name, Supplier<Item> sourceStackSupplier, AlchemicalSulfurTier tier) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties(), Suppliers.memoize(() -> new ItemStack(sourceStackSupplier.get()))).overrideSourceName(true).autoTooltip(true, false).autoName(true, false).withJarIcon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()))).tier(tier).type(AlchemicalSulfurType.NITER));
    }

    public static RegistryObject<AlchemicalSulfurItem> registerDefault(String name, AlchemicalSulfurTier tier, AlchemicalSulfurType type) {
        return register(name, () -> new AlchemicalSulfurItem(new Item.Properties()).tier(tier).type(type));
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
