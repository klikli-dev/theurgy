/*
 * MIT License
 *
 * Copyright 2021 klikli-dev, TheOnlyTails
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

package com.klikli_dev.theurgy.datagen

import com.google.gson.GsonBuilder
import com.klikli_dev.theurgy.Theurgy
import net.minecraft.block.Block
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache
import net.minecraft.data.IDataProvider
import net.minecraft.data.LootTableProvider
import net.minecraft.data.loot.BlockLootTables
import net.minecraft.loot.LootTable
import net.minecraft.loot.LootTableManager
import net.minecraft.util.IItemProvider
import net.minecraft.util.ResourceLocation

/**
 * See https://github.com/TheOnlyTails/RubyMod/blob/master/src/main/kotlin/com/theonlytails/rubymod/datagen/BlockLootTablesGenerator.kt
 */
class BlockLootTablesGenerator(private val generator: DataGenerator) : LootTableProvider(generator) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val tables = hashMapOf<Block, LootTable>()

    private fun addLootTables(loot: BlockLootTablesGenerator) {
        //loot.dropSelf(BlockRegistry.myBlock)
    }

    /**
     * Copied from BlockLootTables.java#registerDropSelfTable
     */
    private fun dropSelf(block: Block) {
        this.registerDropping(block, block)
    }

    /**
     * Copied from BlockLootTables.java
     */
    private fun registerDropping(blockIn: Block, drop: IItemProvider) {
        this.registerLootTable(blockIn, BlockLootTables.dropping(drop).build())
    }

    private fun registerLootTable(block: Block, loot: LootTable) {
        tables[block] = loot
    }

    /**
     * Performs this provider's action.
     */
    override fun act(cache: DirectoryCache) {
        addLootTables(this)

        val namespacedTables = hashMapOf<ResourceLocation, LootTable>()

        for (entry in tables) {
            namespacedTables[entry.key.lootTable] = entry.value
        }

        writeLootTables(namespacedTables, cache)
    }

    private fun writeLootTables(tables: Map<ResourceLocation, LootTable>, cache: DirectoryCache) {
        val output = generator.outputFolder

        tables.forEach { (key, table) ->
            val path = output.resolve("data/${key.namespace}/loot_tables/${key.path}.json")

            try {
                IDataProvider.save(gson, cache, LootTableManager.toJson(table), path)
            } catch (e: Exception) {
                Theurgy.LOGGER.error("Couldn't write loot table $path", e)
            }
        }
    }
}
