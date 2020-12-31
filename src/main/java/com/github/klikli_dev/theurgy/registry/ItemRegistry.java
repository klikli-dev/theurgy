/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.item.tool.EssentiaGaugeItem;
import com.github.klikli_dev.theurgy.common.item.tool.GrimoireItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    //region Fields
    public static final Map<RegistryObject<Item>, Integer> ESSENTIA_COLORS = new HashMap<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theurgy.MODID);

    //Tools
    public static final RegistryObject<GrimoireItem> GRIMOIRE = ITEMS.register("grimoire",
            () -> new GrimoireItem(defaultProperties().maxStackSize(1)));

    public static final RegistryObject<EssentiaGaugeItem> ESSENTIA_GAUGE = ITEMS.register("essentia_gauge",
            () -> new EssentiaGaugeItem(defaultProperties().maxStackSize(1)));

    public static final RegistryObject<Item> PURE_CRYSTAL_STIRRER = ITEMS.register("pure_crystal_stirrer",
            () -> new Item(defaultProperties().maxStackSize(1).maxDamage(50)));
    public static final RegistryObject<Item> PRIMA_MATERIA_CRYSTAL_STIRRER =
            ITEMS.register("prima_materia_crystal_stirrer",
                    () -> new Item(defaultProperties().maxStackSize(1).maxDamage(50)));

    //Essentia
    public static final RegistryObject<Item> AER_ESSENTIA = registerEssentia("aer_essentia", 0xfcfc03); //yellow
    public static final RegistryObject<Item> AQUA_ESSENTIA = registerEssentia("aqua_essentia", 0x00aeff); //light blue
    public static final RegistryObject<Item> IGNIS_ESSENTIA = registerEssentia("ignis_essentia", 0xe89b00); //orange
    public static final RegistryObject<Item> TERRA_ESSENTIA = registerEssentia("terra_essentia", 0x008f0e); //green

    //endregion Fields

    //region Static Methods
    public static Item.Properties defaultProperties() {
        return new Item.Properties().group(Theurgy.ITEM_GROUP);
    }

    public static RegistryObject<Item> registerEssentia(String name, int color) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new Item(defaultProperties().maxStackSize(1024 * 100)));
        ESSENTIA_COLORS.put(item, color);
        return item;
    }
    //endregion Static Methods

}
