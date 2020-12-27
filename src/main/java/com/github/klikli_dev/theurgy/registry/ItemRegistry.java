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
    public static final RegistryObject<Item> ESSENTIA_AER = registerEssentia("essentia_aer", 0xfcfc03); //yellow
    public static final RegistryObject<Item> ESSENTIA_AETHER = registerEssentia("essentia_aether", 0xcccccc); //light grey
    public static final RegistryObject<Item> ESSENTIA_AQUA = registerEssentia("essentia_aqua", 0x00aeff); //light blue
    public static final RegistryObject<Item> ESSENTIA_IGNIS= registerEssentia("essentia_ignis", 0xe89b00); //orange
    public static final RegistryObject<Item> ESSENTIA_TAINT= registerEssentia("essentia_taint", 0xab009d); //purple
    public static final RegistryObject<Item> ESSENTIA_TERRA= registerEssentia("essentia_terra", 0x008f0e); //green

    //endregion Fields

    //region Static Methods
    public static Item.Properties defaultProperties() {
        return new Item.Properties().group(Theurgy.ITEM_GROUP);
    }

    public static RegistryObject<Item> registerEssentia(String name, int color) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new Item(defaultProperties().maxStackSize(1024)));
        ESSENTIA_COLORS.put(item, color);
        return item;
    }
    //endregion Static Methods

}
