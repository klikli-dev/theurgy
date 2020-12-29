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

import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.List;

public class PureCrystalSpreader implements ICrystalSpreadHandler {

    //region Fields
    public ICrystalSpreadCondition pureCondition;
    public ICrystalSpreadCondition primaMateriaCondition;
    public ICrystalSpreadCondition aerCondition;
    public ICrystalSpreadCondition aquaCondition;
    public ICrystalSpreadCondition ignisCondition;
    public ICrystalSpreadCondition terraCondition;
    //endregion Fields

    //region Initialization
    public PureCrystalSpreader(ICrystalSpreadCondition pureCondition,
                               ICrystalSpreadCondition primaMateriaCondition,
                               ICrystalSpreadCondition aerCondition,
                               ICrystalSpreadCondition aquaCondition,
                               ICrystalSpreadCondition ignisCondition,
                               ICrystalSpreadCondition terraCondition) {
        this.pureCondition = pureCondition;
        this.primaMateriaCondition = primaMateriaCondition;
        this.aerCondition = aerCondition;
        this.aquaCondition = aquaCondition;
        this.ignisCondition = ignisCondition;
        this.terraCondition = terraCondition;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public boolean handleSpread(CrystalBlock sourceCrystalType, IWorld world, BlockState sourceState,
                                BlockPos sourcePos) {

        //get possible blocks to spread to,
        List<BlockPos> possibleTargets = this.getPossibleSpreadBlockPos(world, sourcePos);
        if (possibleTargets.size() == 0)
            return false;

        //Check aer
        CrystalPlacementInfo spreadTo = this.getValidSpreadPosition(this.aerCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            world.setBlockState(spreadTo.pos, BlockRegistry.AER_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }

        //Check aqua
        spreadTo = this.getValidSpreadPosition(this.aquaCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            world.setBlockState(spreadTo.pos, BlockRegistry.AQUA_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }

        //Check ignis
        spreadTo = this.getValidSpreadPosition(this.ignisCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            world.setBlockState(spreadTo.pos, BlockRegistry.IGNIS_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }

        //Check terra
        spreadTo = this.getValidSpreadPosition(this.terraCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            world.setBlockState(spreadTo.pos, BlockRegistry.TERRA_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }

        //if no essentia crystals can grow, attempt to grow prima materia
        spreadTo = this.getValidSpreadPosition(this.primaMateriaCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            //TODO: consume essentia from chunk
            world.setBlockState(spreadTo.pos, BlockRegistry.PRIMA_MATERIA_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }

        //If no other crystal type is valid, check pure condition
        spreadTo = this.getValidSpreadPosition(this.pureCondition, world, possibleTargets, sourcePos);
        if (spreadTo != null) {
            world.setBlockState(spreadTo.pos, BlockRegistry.PURE_CRYSTAL.get().getDefaultState().with(BlockStateProperties.FACING, spreadTo.direction), 2);
            return true;
        }
        return false;
    }
    //endregion Overrides

}
