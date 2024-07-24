// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItemTagRegistry {

    //complementary tag to Tags.Items.ORES, Tags.Items.RAW_MATERIALS, Tags.Items.INGOTS and Tags.Items.GEMS
    public static final TagKey<Item> OTHER_MINERALS = tag("other_minerals");

    public static final TagKey<Item> ALCHEMICAL_SULFURS_AND_NITERS = tag("alchemical_sulfurs_and_niters");
    public static final TagKey<Item> ALCHEMICAL_NITERS = tag("alchemical_niters");
    public static final TagKey<Item> ALCHEMICAL_SULFURS = tag("alchemical_sulfurs");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_METALS = tag("alchemical_sulfurs/metals");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_METALS_ABUNDANT = tag("alchemical_sulfurs/metals/abundant");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_METALS_COMMON = tag("alchemical_sulfurs/metals/common");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_METALS_RARE = tag("alchemical_sulfurs/metals/rare");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_METALS_PRECIOUS = tag("alchemical_sulfurs/metals/precious");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_GEMS = tag("alchemical_sulfurs/gems");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_GEMS_ABUNDANT = tag("alchemical_sulfurs/gems/abundant");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_GEMS_COMMON = tag("alchemical_sulfurs/gems/common");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_GEMS_RARE = tag("alchemical_sulfurs/gems/rare");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_GEMS_PRECIOUS = tag("alchemical_sulfurs/gems/precious");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_OTHER_MINERALS = tag("alchemical_sulfurs/other_minerals");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT = tag("alchemical_sulfurs/other_minerals/abundant");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON = tag("alchemical_sulfurs/other_minerals/common");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE = tag("alchemical_sulfurs/other_minerals/rare");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS = tag("alchemical_sulfurs/other_minerals/precious");

    public static final TagKey<Item> ALCHEMICAL_SULFURS_LOGS = tag("alchemical_sulfurs/logs");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_LOGS_ABUNDANT = tag("alchemical_sulfurs/logs/abundant");

    public static final TagKey<Item> ALCHEMICAL_SULFURS_ABUNDANT = tag("alchemical_sulfurs/abundant");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_COMMON = tag("alchemical_sulfurs/common");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_RARE = tag("alchemical_sulfurs/rare");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_PRECIOUS = tag("alchemical_sulfurs/precious");

    public static final TagKey<Item> ALCHEMICAL_SALTS = tag("alchemical_salts");
    public static final TagKey<Item> ALCHEMICAL_MERCURIES = tag("alchemical_mercuries");

    public static final TagKey<Item> LOW_MERCURY_ORES = tag("ores/mercury/low");
    public static final TagKey<Item> MEDIUM_MERCURY_ORES = tag("ores/mercury/medium");
    public static final TagKey<Item> HIGH_MERCURY_ORES = tag("ores/mercury/high");

    public static final TagKey<Item> LOW_MERCURY_RAW_MATERIALS = tag("raw_materials/mercury/low");
    public static final TagKey<Item> MEDIUM_MERCURY_RAW_MATERIALS = tag("raw_materials/mercury/medium");
    public static final TagKey<Item> HIGH_MERCURY_RAW_MATERIALS = tag("raw_materials/mercury/high");

    public static final TagKey<Item> LOW_MERCURY_METALS = tag("metals/mercury/low");
    public static final TagKey<Item> MEDIUM_MERCURY_METALS = tag("metals/mercury/medium");
    public static final TagKey<Item> HIGH_MERCURY_METALS = tag("metals/mercury/high");

    public static final TagKey<Item> LOW_MERCURY_GEMS = tag("gems/mercury/low");
    public static final TagKey<Item> MEDIUM_MERCURY_GEMS = tag("gems/mercury/medium");
    public static final TagKey<Item> HIGH_MERCURY_GEMS = tag("gems/mercury/high");

    public static final TagKey<Item> LOW_MERCURY_OTHER_MINERALS = tag("other_minerals/mercury/low");
    public static final TagKey<Item> MEDIUM_MERCURY_OTHER_MINERALS = tag("other_minerals/mercury/medium");
    public static final TagKey<Item> HIGH_MERCURY_OTHER_MINERALS = tag("other_minerals/mercury/high");

    public static final TagKey<Item> GEMS_SAL_AMMONIAC = cTag("gems/sal_ammoniac");
    public static final TagKey<Item> ORES_SAL_AMMONIAC = cTag("ores/sal_ammoniac");

    //Common tags for other mods start here:

    //Common Logs
    public static final TagKey<Item> LOGS_ROWAN = cTag("logs/rowan");
    public static final TagKey<Item> LOGS_FIR = cTag("logs/fir");
    public static final TagKey<Item> LOGS_REDWOOD = cTag("logs/redwood");
    public static final TagKey<Item> LOGS_MAHOGANY = cTag("logs/mahogany");
    public static final TagKey<Item> LOGS_JACARANDA = cTag("logs/jacaranda");
    public static final TagKey<Item> LOGS_PALM = cTag("logs/palm");
    public static final TagKey<Item> LOGS_WILLOW = cTag("logs/willow");
    public static final TagKey<Item> LOGS_DEAD = cTag("logs/dead"); //biomes o plenty
    public static final TagKey<Item> LOGS_MAGIC = cTag("logs/magic"); //biomes o plenty
    public static final TagKey<Item> LOGS_UMBRAN = cTag("logs/umbran");
    public static final TagKey<Item> LOGS_HELLBARK = cTag("logs/hellbark");
    public static final TagKey<Item> LOGS_CINNAMON = cTag("logs/cinnamon");
    public static final TagKey<Item> LOGS_GLACIAN = cTag("logs/glacian");
    public static final TagKey<Item> LOGS_ARCHWOOD = cTag("logs/archwood");
    public static final TagKey<Item> LOGS_BLUEBRIGHT = cTag("logs/bluebright");
    public static final TagKey<Item> LOGS_STARLIT = cTag("logs/starlit");
    public static final TagKey<Item> LOGS_FROSTBRIGHT = cTag("logs/frostbright");
    public static final TagKey<Item> LOGS_COMET = cTag("logs/comet");
    public static final TagKey<Item> LOGS_LUNAR = cTag("logs/lunar");
    public static final TagKey<Item> LOGS_DUSK = cTag("logs/dusk");
    public static final TagKey<Item> LOGS_MAPLE = cTag("logs/maple");
    public static final TagKey<Item> LOGS_CRYSTALLIZED = cTag("logs/crystallized");
    public static final TagKey<Item> LOGS_LIVINGWOOD = cTag("logs/livingwood");
    public static final TagKey<Item> LOGS_GLIMMERING_LIVINGWOOD = cTag("logs/glimmering_livingwood");
    public static final TagKey<Item> LOGS_DREAMWOOD = cTag("logs/dreamwood");
    public static final TagKey<Item> LOGS_DREAMWOOD_GLIMMERING = cTag("logs/glimmering_dreamwood");
    public static final TagKey<Item> LOGS_WALNUT = cTag("logs/walnut");
    public static final TagKey<Item> LOGS_FIG = cTag("logs/fig");
    public static final TagKey<Item> LOGS_WOLFBERRY = cTag("logs/wolfberry");
    public static final TagKey<Item> LOGS_ECHO = cTag("logs/echo");
    public static final TagKey<Item> LOGS_ILLWOOD = cTag("logs/illwood");
    public static final TagKey<Item> LOGS_UNDEAD = cTag("logs/undead");
    public static final TagKey<Item> LOGS_AURUM = cTag("logs/aurum");
    public static final TagKey<Item> LOGS_MENRIL = cTag("logs/menril");
    //productive trees are skipped as the mod provides the means to create all its trees
    public static final TagKey<Item> LOGS_ASHEN = cTag("logs/ashen");
    public static final TagKey<Item> LOGS_AZALEA = cTag("logs/azalea");
    public static final TagKey<Item> LOGS_TRUMPET = cTag("logs/trumpet");
    public static final TagKey<Item> LOGS_NETHERWOOD = cTag("logs/netherwood");
    public static final TagKey<Item> LOGS_SKYROOT = cTag("logs/skyroot");
    public static final TagKey<Item> LOGS_GOLDEN_OAK = cTag("logs/golden_oak");
    public static final TagKey<Item> LOGS_TWILIGHT_OAK = cTag("logs/twilight_oak");
    public static final TagKey<Item> LOGS_CANOPY_TREE = cTag("logs/canopy_tree");
    public static final TagKey<Item> LOGS_DARKWOOD = cTag("logs/darkwood");
    public static final TagKey<Item> LOGS_TIMEWOOD = cTag("logs/timewood");
    public static final TagKey<Item> LOGS_TRANSWOOD = cTag("logs/transwood");
    public static final TagKey<Item> LOGS_SORTINGWOOD = cTag("logs/sortingwood");
    public static final TagKey<Item> LOGS_MINEWOOD = cTag("logs/minewood");
    public static final TagKey<Item> LOGS_SMOGSTEM = cTag("logs/smogstem");
    public static final TagKey<Item> LOGS_WIGGLEWOOD = cTag("logs/wigglewood");
    public static final TagKey<Item> LOGS_GRONGLE = cTag("logs/grongle");
    public static final TagKey<Item> LOGS_RUBBERWOOD = cTag("logs/rubberwood");
    public static final TagKey<Item> LOGS_OTHERWORLD = cTag("logs/otherworld");


    //Common Metal Ores
    public static final TagKey<Item> ORES_URANIUM = cTag("ores/uranium");
    public static final TagKey<Item> ORES_SILVER = cTag("ores/silver");
    public static final TagKey<Item> ORES_AZURE_SILVER = cTag("ores/azure_silver");
    public static final TagKey<Item> ORES_ZINC = cTag("ores/zinc");
    public static final TagKey<Item> ORES_OSMIUM = cTag("ores/osmium");
    public static final TagKey<Item> ORES_NICKEL = cTag("ores/nickel");
    public static final TagKey<Item> ORES_LEAD = cTag("ores/lead");
    public static final TagKey<Item> ORES_ALLTHEMODIUM = cTag("ores/allthemodium");
    public static final TagKey<Item> ORES_UNOBTAINIUM = cTag("ores/unobtainium");
    public static final TagKey<Item> ORES_IRIDIUM = cTag("ores/iridium");
    public static final TagKey<Item> ORES_TIN = cTag("ores/tin");
    public static final TagKey<Item> ORES_CINNABAR = cTag("ores/cinnabar");
    public static final TagKey<Item> ORES_CRIMSON_IRON = cTag("ores/crimson_iron");
    public static final TagKey<Item> ORES_PLATINUM = cTag("ores/platinum");
    public static final TagKey<Item> ORES_VIBRANIUM = cTag("ores/vibranium");

    public static final TagKey<Item> ORES_DESH = cTag("ores/desh");
    public static final TagKey<Item> ORES_OSTRUM = cTag("ores/ostrum");
    public static final TagKey<Item> ORES_CALORITE = cTag("ores/calorite");
    public static final TagKey<Item> ORES_IESNIUM = cTag("ores/iesnium");

    // Common Gem Ores
    public static final TagKey<Item> ORES_RUBY = cTag("ores/ruby");
    public static final TagKey<Item> ORES_APATITE = cTag("ores/apatite");
    public static final TagKey<Item> ORES_PERIDOT = cTag("ores/peridot");
    public static final TagKey<Item> ORES_FLUORITE = cTag("ores/fluorite");
    public static final TagKey<Item> ORES_SAPPHIRE = cTag("ores/sapphire");

    public static final TagKey<Item> ORES_NITER = cTag("ores/niter");
    public static final TagKey<Item> ORES_CERTUS_QUARTZ = cTag("ores/certus_quartz");
//    public static final TagKey<Item> ORES_FLUIX = cTag("ores/fluix"); //no ore exists


    //Other Common Mineral Ores
    public static final TagKey<Item> ORES_SULFUR = cTag("ores/sulfur");

    //Common Raw Materials
    public static final TagKey<Item> RAW_MATERIALS_URANIUM = cTag("raw_materials/uranium");
    public static final TagKey<Item> RAW_MATERIALS_AZURE_SILVER = cTag("raw_materials/azure_silver");
    public static final TagKey<Item> RAW_MATERIALS_SILVER = cTag("raw_materials/silver");
    public static final TagKey<Item> RAW_MATERIALS_ZINC = cTag("raw_materials/zinc");
    public static final TagKey<Item> RAW_MATERIALS_OSMIUM = cTag("raw_materials/osmium");
    public static final TagKey<Item> RAW_MATERIALS_NICKEL = cTag("raw_materials/nickel");
    public static final TagKey<Item> RAW_MATERIALS_LEAD = cTag("raw_materials/lead");
    public static final TagKey<Item> RAW_MATERIALS_ALLTHEMODIUM = cTag("raw_materials/allthemodium");
    public static final TagKey<Item> RAW_MATERIALS_UNOBTAINIUM = cTag("raw_materials/unobtainium");
    public static final TagKey<Item> RAW_MATERIALS_IRIDIUM = cTag("raw_materials/iridium");
    public static final TagKey<Item> RAW_MATERIALS_TIN = cTag("raw_materials/tin");
    public static final TagKey<Item> RAW_MATERIALS_CINNABAR = cTag("raw_materials/cinnabar");
    public static final TagKey<Item> RAW_MATERIALS_CRIMSON_IRON = cTag("raw_materials/crimson_iron");
    public static final TagKey<Item> RAW_MATERIALS_PLATINUM = cTag("raw_materials/platinum");
    public static final TagKey<Item> RAW_MATERIALS_VIBRANIUM = cTag("raw_materials/vibranium");

    public static final TagKey<Item> RAW_MATERIALS_DESH = cTag("raw_materials/desh");
    public static final TagKey<Item> RAW_MATERIALS_OSTRUM = cTag("raw_materials/ostrum");
    public static final TagKey<Item> RAW_MATERIALS_CALORITE = cTag("raw_materials/calorite");
    public static final TagKey<Item> RAW_MATERIALS_IESNIUM = cTag("raw_materials/iesnium");

    //Common Metal Ingots
    public static final TagKey<Item> INGOTS_URANIUM = cTag("ingots/uranium");
    public static final TagKey<Item> INGOTS_SILVER = cTag("ingots/silver");
    public static final TagKey<Item> INGOTS_AZURE_SILVER = cTag("ingots/azure_silver");
    public static final TagKey<Item> INGOTS_ZINC = cTag("ingots/zinc");
    public static final TagKey<Item> INGOTS_OSMIUM = cTag("ingots/osmium");
    public static final TagKey<Item> INGOTS_NICKEL = cTag("ingots/nickel");
    public static final TagKey<Item> INGOTS_LEAD = cTag("ingots/lead");
    public static final TagKey<Item> INGOTS_ALLTHEMODIUM = cTag("ingots/allthemodium");
    public static final TagKey<Item> INGOTS_UNOBTAINIUM = cTag("ingots/unobtainium");
    public static final TagKey<Item> INGOTS_IRIDIUM = cTag("ingots/iridium");
    public static final TagKey<Item> INGOTS_TIN = cTag("ingots/tin");
    public static final TagKey<Item> INGOTS_CINNABAR = cTag("ingots/cinnabar");
    public static final TagKey<Item> INGOTS_CRIMSON_IRON = cTag("ingots/crimson_iron");
    public static final TagKey<Item> INGOTS_PLATINUM = cTag("ingots/platinum");
    public static final TagKey<Item> INGOTS_VIBRANIUM = cTag("ingots/vibranium");
    public static final TagKey<Item> INGOTS_DESH = cTag("ingots/desh");
    public static final TagKey<Item> INGOTS_OSTRUM = cTag("ingots/ostrum");
    public static final TagKey<Item> INGOTS_CALORITE = cTag("ingots/calorite");
    public static final TagKey<Item> INGOTS_IESNIUM = cTag("ingots/iesnium");

    //Common Gems
    public static final TagKey<Item> GEMS_RUBY = cTag("gems/ruby");
    public static final TagKey<Item> GEMS_APATITE = cTag("gems/apatite");
    public static final TagKey<Item> GEMS_PERIDOT = cTag("gems/peridot");
    public static final TagKey<Item> GEMS_FLUORITE = cTag("gems/fluorite");
    public static final TagKey<Item> GEMS_SAPPHIRE = cTag("gems/sapphire");
    public static final TagKey<Item> GEMS_NITER = cTag("gems/niter");
    public static final TagKey<Item> GEMS_CERTUS_QUARTZ = cTag("gems/certus_quartz");
    public static final TagKey<Item> GEMS_FLUIX = cTag("gems/fluix");


    //Other Common Minerals
    public static final TagKey<Item> GEMS_SULFUR = cTag("gems/sulfur");

    public static final TagKey<Item> SUGARS = cTag("sugars");

    public static TagKey<Item> tag(String id) {
        return ItemTags.create(Theurgy.loc(id));
    }

    private static TagKey<Item> cTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c",  name));
    }
}
