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

package com.github.klikli_dev.theurgy.common.block.crystal;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunk;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class PrimaMateriaCrystalSpreader implements ICrystalSpreadHandler {

    //region Fields
    public ICrystalSpreadCondition condition;
    public Supplier<Block> crystalBlock;
    public Supplier<Integer> chanceToSpread;
    //endregion Fields

    //region Initialization
    public PrimaMateriaCrystalSpreader(ICrystalSpreadCondition condition, Supplier<Integer> chanceToSpread, Supplier<Block> crystalBlock) {
        this.condition = condition;
        this.chanceToSpread = chanceToSpread;
        this.crystalBlock = crystalBlock;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public boolean handleSpread(CrystalBlock sourceCrystalType, IWorld world, BlockState sourceState,
                                BlockPos sourcePos) {

        if (world.getRandom().nextInt(this.chanceToSpread.get()) != 0)
            return false;

        //get possible blocks to spread to,
        List<BlockPos> possibleTargets = this.getPossibleSpreadBlockPos(world, sourcePos);
        if (possibleTargets.size() == 0)
            return false;

        CrystalPlacementInfo spreadTo =
                this.getValidSpreadPosition(this.condition, world, possibleTargets, sourceState, sourcePos);
        if (spreadTo != null) {
            EssentiaChunk chunkEssentia = EssentiaChunkHandler.getOrCreateEssentiaChunk(
                    ((World) world).getDimensionKey(), new ChunkPos(spreadTo.pos));

            //consume essentia from chunk
            chunkEssentia.essentiaCapability.getEssentia().forEach((item, amount) -> {
                chunkEssentia.essentiaCapability
                        .extractEssentia(item, Theurgy.CONFIG.crystalSettings.primaMateriaSpreadEssentia.get(), false);
            });
            chunkEssentia.markDirty();

            world.setBlockState(spreadTo.pos,
                    this.crystalBlock.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }
        return false;
    }
    //endregion Overrides

}
