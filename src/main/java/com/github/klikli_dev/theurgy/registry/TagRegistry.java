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
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TagRegistry {

    //region Fields

    // Block Tags
    /**
     * Blocks that heat crucibles independent of their blockstate
     */
    public static final ITag<Block> HEAT_SOURCES =
            makeBlockTag(new ResourceLocation(Theurgy.MODID, "heat_sources"));
    /**
     * Blocks that heat crucibles only if they are in the blockstate BlockStateProperties.LIT=true
     */
    public static final ITag<Block> HEAT_SOURCES_LIT =
            makeBlockTag(new ResourceLocation(Theurgy.MODID, "heat_sources_lit"));

    public static final ITag<Block> OBSIDIAN =
            makeBlockTag(new ResourceLocation("forge", "obsidian"));

    public static final ITag<Block> CRYSTALS =
            makeBlockTag(new ResourceLocation("theurgy", "crystals"));

    //Item Tags
    public static final ITag<Item> RODS_WOODEN =
            makeItemTag(new ResourceLocation("forge", "rods/wooden"));


    //Entity Tags

    //endregion Fields

    //region Static Methods
    public static ITag.INamedTag<Item> makeItemTag(ResourceLocation id) {
        return ItemTags.makeWrapperTag(id.toString());
    }

    public static ITag.INamedTag<Block> makeBlockTag(ResourceLocation id) {
        return BlockTags.makeWrapperTag(id.toString());
    }

    public static ITag.INamedTag<EntityType<?>> makeEntityTypeTag(ResourceLocation id) {
        return EntityTypeTags.getTagById(id.toString());
    }
    //endregion Static Methods
}
