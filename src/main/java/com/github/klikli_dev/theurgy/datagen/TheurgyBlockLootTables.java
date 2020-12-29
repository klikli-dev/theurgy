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
