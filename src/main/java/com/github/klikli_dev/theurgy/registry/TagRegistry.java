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
