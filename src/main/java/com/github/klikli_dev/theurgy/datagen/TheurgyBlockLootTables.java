package com.github.klikli_dev.theurgy.datagen;

import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraftforge.fml.RegistryObject;

public class TheurgyBlockLootTables extends BlockLootTables {
    //region Fields
    TheurgyLootTableProvider lootTableProvider;
    //endregion Fields

    //region Initialization
    public TheurgyBlockLootTables(TheurgyLootTableProvider lootTableProvider) {
        this.lootTableProvider = lootTableProvider;
    }
    //endregion Initialization

    //region Overrides
    protected void addTables() {
        BlockRegistry.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(block -> {
                    BlockRegistry.BlockDataGenSettings settings = BlockRegistry.BLOCK_DATA_GEN_SETTINGS
                                                                          .get(block.getRegistryName());
                    if (settings.lootTableType == BlockRegistry.LootTableType.EMPTY) {
                        this.registerLootTable(block, LootTable.builder().addLootPool(LootPool.builder()));
                    }
                    else if (settings.lootTableType == BlockRegistry.LootTableType.DROP_SELF) {
                        this.registerDropSelfLootTable(block);
                    }
                });
    }

    @Override
    protected void registerLootTable(Block blockIn, LootTable.Builder table) {
        this.lootTableProvider.blockLootTable.put(blockIn, table);
    }
    //endregion Overrides
}
