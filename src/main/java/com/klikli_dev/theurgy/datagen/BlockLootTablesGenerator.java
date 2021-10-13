/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
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

package com.klikli_dev.theurgy.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

import java.util.HashMap;
import java.util.Map;

public class BlockLootTablesGenerator extends LootTableProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final HashMap<Block, LootTable> tables = new HashMap<>();
    private final DataGenerator generator;

    public BlockLootTablesGenerator(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    private void addLootTables(BlockLootTablesGenerator loot) {
        this.dropSelf(BlockRegistry.GRAFTING_HEDGE.get());
    }

    private void dropSelf(Block block) {
        this.registerDropping(block, block);
    }

    private void registerDropping(Block blockIn, ItemLike drop) {
        this.registerLootTable(blockIn, BlockLoot.createSingleItemTable(drop).build());
    }

    private void registerLootTable(Block block, LootTable loot) {
        this.tables.put(block, loot);
    }


    @Override
    public void run(HashCache cache) {
        this.addLootTables(this);

        var namespacedTables = new HashMap<ResourceLocation, LootTable>();

        for (var entry : this.tables.entrySet()) {
            namespacedTables.put(entry.getKey().getLootTable(), entry.getValue());
        }

        this.writeLootTables(namespacedTables, cache);
    }

    private void writeLootTables(Map<ResourceLocation, LootTable> tables, HashCache cache) {
        var output = this.generator.getOutputFolder();

        tables.forEach((key, table) -> {
            var path = output.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");

            try {
                DataProvider.save(GSON, cache, LootTables.serialize(table), path);
            } catch (Exception e) {
                Theurgy.LOGGER.error("Couldn't write loot table " + path, e);
            }
        });
    }
}
