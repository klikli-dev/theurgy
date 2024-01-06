// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItemTagRegistry {

    //complementary tag to Tags.Items.ORES, Tags.Items.RAW_MATERIALS, Tags.Items.INGOTS and Tags.Items.GEMS
    public static final TagKey<Item> OTHER_MINERALS = tag("other_minerals");

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

    public static final TagKey<Item> ALCHEMICAL_SULFURS_ABUNDANT = tag("alchemical_sulfurs/abundant");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_COMMON = tag("alchemical_sulfurs/common");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_RARE = tag("alchemical_sulfurs/rare");
    public static final TagKey<Item> ALCHEMICAL_SULFURS_PRECIOUS = tag("alchemical_sulfurs/precious");

    public static final TagKey<Item> ALCHEMICAL_SALTS = tag("alchemical_salts");
    public static final TagKey<Item> ALCHEMICAL_MERCURIES = tag("alchemical_mercuries");

    public static final TagKey<Item> SAL_AMMONIAC_GEMS = forgeTag("gems/sal_ammoniac");
    public static final TagKey<Item> SAL_AMMONIAC_ORES = forgeTag("ores/sal_ammoniac");


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

    public static final TagKey<Item> SUGAR = forgeTag("sugar");

    public static TagKey<Item> tag(String id) {
        return tag(Theurgy.loc(id));
    }

    public static TagKey<Item> forgeTag(String id) {
        return tag(new ResourceLocation("forge", id));
    }

    public static TagKey<Item> tag(ResourceLocation id) {
        return TagKey.create(Registries.ITEM, id);
    }

}
