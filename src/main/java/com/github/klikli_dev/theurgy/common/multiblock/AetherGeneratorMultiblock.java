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

package com.github.klikli_dev.theurgy.common.multiblock;

import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;

import java.util.HashMap;
import java.util.Map;

public class AetherGeneratorMultiblock extends MultiblockStructure {
    //region Overrides
    @Override
    protected IMultiblock setupMultiblock() {
        Map<BlockPos, IStateMatcher> template = new HashMap<>();

        //main block - generator
        //TODO: use real generator block here
        template.put(new BlockPos(0, 0, 0), this.api.looseBlockMatcher(BlockRegistry.CREATIVE_AETHER_SOURCE.get()));
        //below generator, place "condenser"
        //TODO: use real condenser here. Should have crystals to 4 sides.
        template.put(new BlockPos(0, -1, 0), this.api.looseBlockMatcher(BlockRegistry.AETHER_EMITTER.get()));

        //one layer down and to all 4 sides: end infused crystals
        template.put(new BlockPos(-4, -2, 0), this.api.looseBlockMatcher(BlockRegistry.END_INFUSED_CRYSTAL.get()));
        template.put(new BlockPos(4, -2, 0), this.api.looseBlockMatcher(BlockRegistry.END_INFUSED_CRYSTAL.get()));
        template.put(new BlockPos(0, -2, -4), this.api.looseBlockMatcher(BlockRegistry.END_INFUSED_CRYSTAL.get()));
        template.put(new BlockPos(0, -2, 4), this.api.looseBlockMatcher(BlockRegistry.END_INFUSED_CRYSTAL.get()));

        //one more layer down, below the crystals, place end stone brick
        template.put(new BlockPos(-4, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICKS));
        template.put(new BlockPos(4, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICKS));
        template.put(new BlockPos(0, -3, -4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICKS));
        template.put(new BlockPos(0, -3, 4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICKS));

        //on the same layer, around the end stone bricks, place end stone slabs
        template.put(new BlockPos(-3, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(-4, -3, -1), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(-4, -3, 1), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(-5, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));

        template.put(new BlockPos(3, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(4, -3, -1), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(4, -3, 1), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(5, -3, 0), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));

        template.put(new BlockPos(0, -3, -3), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(-1, -3, -4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(1, -3, -4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(0, -3, -5), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));

        template.put(new BlockPos(0, -3, 3), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(-1, -3, 4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(1, -3, 4), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));
        template.put(new BlockPos(0, -3, 5), this.api.looseBlockMatcher(Blocks.END_STONE_BRICK_SLAB));

        return this.api.makeSparseMultiblock(template).setSymmetrical(true);
    }
    //endregion Overrides
}
