package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
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

    public static final RegistryObject<GrimoireItem> GRIMOIRE = ITEMS.register("grimoire",
            () -> new GrimoireItem(defaultProperties().maxStackSize(1)));

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
