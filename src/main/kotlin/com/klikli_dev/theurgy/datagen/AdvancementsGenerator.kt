/*
 * MIT License
 *
 * Copyright 2021 klikli-dev, sirttas
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
import com.klikli_dev.theurgy.Theurgy.id
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache
import net.minecraft.data.IDataProvider
import net.minecraft.item.Item
import net.minecraft.tags.ITag
import net.minecraft.util.IItemProvider
import java.io.IOException
import java.nio.file.Path

/**
 * See also https://github.com/Sirttas/ElementalCraft/blob/1.16/src/main/java/sirttas/elementalcraft/datagen/ECAdvancementProvider.java
 */
class AdvancementsGenerator(private val generator: DataGenerator) : IDataProvider {
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /**
     * Performs this provider's action.
     */
    @Throws(IOException::class)
    override fun act(cache: DirectoryCache) {
        val path = generator.outputFolder

        //TODO: generate advancements for relevant items
        //IDataProvider.save(GSON, cache, itemPickup(item).serialize(), getPickupPath(path, item))
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    override fun getName(): String {
        return "Theurgy Advancements"
    }

    /**
     * Advancement builder for item pickup
     */
    private fun itemPickup(item: IItemProvider): Advancement.Builder {
        return Advancement.Builder.builder().withParentId(id("root")).withCriterion(
            "has_" + item.asItem().registryName!!
                .path,
            hasItem(item)
        )
    }

    /**
     * Gets the path for a theurgy pickup advancement
     */
    private fun getPickupPath(path: Path, item: IItemProvider): Path {
        val name = item.asItem().registryName
        return path.resolve("data/" + name!!.namespace + "/advancements/pickup/" + name.path + ".json")
    }

    /**
     * Creates a new [InventoryChangeTrigger] that checks for a player having
     * a certain item.
     */
    private fun hasItem(item: IItemProvider?): InventoryChangeTrigger.Instance {
        return hasItem(ItemPredicate.Builder.create().item(item).build())
    }

    /**
     * Creates a new [InventoryChangeTrigger] that checks for a player having
     * an item within the given tag.
     */
    private fun hasItem(tag: ITag<Item?>?): InventoryChangeTrigger.Instance {
        return hasItem(ItemPredicate.Builder.create().tag(tag).build())
    }

    /**
     * Creates a new [InventoryChangeTrigger] that checks for a player having
     * a certain item.
     */
    private fun hasItem(vararg predicate: ItemPredicate?): InventoryChangeTrigger.Instance {
        return InventoryChangeTrigger.Instance(
            EntityPredicate.AndPredicate.ANY_AND,
            MinMaxBounds.IntBound.UNBOUNDED,
            MinMaxBounds.IntBound.UNBOUNDED,
            MinMaxBounds.IntBound.UNBOUNDED,
            predicate
        )
    }

}