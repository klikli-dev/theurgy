package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;

import java.util.List;
import java.util.stream.Stream;

public class SulfurMappings {
    public static final List<AlchemicalSulfurItem> GEMS_ABUNDANT = List.of(
            SulfurRegistry.APATITE.get(),
            SulfurRegistry.FLUORITE.get(),
            SulfurRegistry.SAL_AMMONIAC.get()
    );

    public static final List<AlchemicalSulfurItem> GEMS_COMMON = List.of(
            SulfurRegistry.QUARTZ.get(),
            SulfurRegistry.LAPIS.get()
    );

    public static final List<AlchemicalSulfurItem> GEMS_RARE = List.of(
            SulfurRegistry.PRISMARINE.get(),
            SulfurRegistry.AMETHYST.get(),
            SulfurRegistry.PERIDOT.get(),
            SulfurRegistry.RUBY.get(),
            SulfurRegistry.SAPPHIRE.get()
    );

    public static final List<AlchemicalSulfurItem> GEMS_PRECIOUS = List.of(
            SulfurRegistry.DIAMOND.get(),
            SulfurRegistry.EMERALD.get()
    );

    public static final List<AlchemicalSulfurItem> GEMS = Stream.of(
            GEMS_ABUNDANT,
            GEMS_COMMON,
            GEMS_RARE,
            GEMS_PRECIOUS
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> METALS_ABUNDANT = List.of(
            SulfurRegistry.COPPER.get()
    );

    public static final List<AlchemicalSulfurItem> METALS_COMMON = List.of(
            SulfurRegistry.IRON.get(),
            SulfurRegistry.TIN.get(),
            SulfurRegistry.ZINC.get(),
            SulfurRegistry.LEAD.get(),
            SulfurRegistry.NICKEL.get(),
            SulfurRegistry.CINNABAR.get(),
            SulfurRegistry.OSMIUM.get()
    );

    public static final List<AlchemicalSulfurItem> METALS_RARE= List.of(
            SulfurRegistry.CRIMSON_IRON.get(),
            SulfurRegistry.SILVER.get(),
            SulfurRegistry.GOLD.get(),
            SulfurRegistry.URANIUM.get(),
            SulfurRegistry.AZURE_SILVER.get(),
            SulfurRegistry.IRIDIUM.get(),
            SulfurRegistry.PLATINUM.get()
    );

    public static final List<AlchemicalSulfurItem> METALS_PRECIOUS = List.of(
            SulfurRegistry.NETHERITE.get(),
            SulfurRegistry.ALLTHEMODIUM.get(),
            SulfurRegistry.UNOBTAINIUM.get(),
            SulfurRegistry.VIBRANIUM.get()
    );

    public static final List<AlchemicalSulfurItem> METALS = Stream.of(
            METALS_ABUNDANT,
            METALS_COMMON,
            METALS_RARE,
            METALS_PRECIOUS
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT = List.of(
            SulfurRegistry.COAL.get()
    );

    public static final List<AlchemicalSulfurItem> OTHER_MINERALS_COMMON = List.of(
            SulfurRegistry.REDSTONE.get(),
            SulfurRegistry.SULFUR.get()
    );

    public static final List<AlchemicalSulfurItem> OTHER_MINERALS_RARE = List.of(
    );

    public static final List<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS = List.of(
    );

    public static final List<AlchemicalSulfurItem> OTHER_MINERALS = Stream.of(
            OTHER_MINERALS_ABUNDANT,
            OTHER_MINERALS_COMMON,
            OTHER_MINERALS_RARE,
            OTHER_MINERALS_PRECIOUS
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> ABUNDANT = Stream.of(
            GEMS_ABUNDANT,
            METALS_ABUNDANT,
            OTHER_MINERALS_ABUNDANT
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> COMMON = Stream.of(
            GEMS_COMMON,
            METALS_COMMON,
            OTHER_MINERALS_COMMON
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> RARE = Stream.of(
            GEMS_RARE,
            METALS_RARE,
            OTHER_MINERALS_RARE
    ).flatMap(List::stream).toList();

    public static final List<AlchemicalSulfurItem> PRECIOUS = Stream.of(
            GEMS_PRECIOUS,
            METALS_PRECIOUS,
            OTHER_MINERALS_PRECIOUS
    ).flatMap(List::stream).toList();
}
