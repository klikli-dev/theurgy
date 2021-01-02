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
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunkHandler;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;

public class PrimaMateriaCrystalCondition extends PureCrystalCondition {
    //region Overrides
    @Override
    public List<Direction> getValidPlacementDirections() {
        List<Direction> validPlacements = super.getValidPlacementDirections();
        validPlacements.remove(Direction.UP); //cannot grow on floors
        return validPlacements;
    }

    @Override
    public Direction canSpreadTo(IWorld world, BlockState targetState, BlockPos targetPos, BlockState sourceState,
                                 BlockPos sourcePos) {

        //Prima materia crystals require all 4 essences to spread

        EssentiaCache cache =
                EssentiaChunkHandler.getEssentiaCache(((World) world).getDimensionKey(), new ChunkPos(targetPos));
        if (cache.min(
                ItemRegistry.AER_ESSENTIA.get(),
                ItemRegistry.AQUA_ESSENTIA.get(),
                ItemRegistry.IGNIS_ESSENTIA.get(),
                ItemRegistry.TERRA_ESSENTIA.get()
        ) < Theurgy.CONFIG.crystalSettings.primaMateriaSpreadEssentia.get())
            return null;

        return super.canSpreadTo(world, targetState, targetPos, sourceState, sourcePos);
    }
    //endregion Overrides
}
