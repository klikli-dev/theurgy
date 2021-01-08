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
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class PureCrystalSpreader implements ICrystalSpreadHandler {

    //region Fields
    public ICrystalSpreadCondition pureCondition;
    public ICrystalSpreadCondition primaMateriaCondition;
    public ICrystalSpreadCondition aerCondition;
    public ICrystalSpreadCondition aquaCondition;
    public ICrystalSpreadCondition ignisCondition;
    public ICrystalSpreadCondition terraCondition;
    public Supplier<Integer> pureCrystalChanceToSpread;
    public Supplier<Integer> primaMateriaCrystalChanceToSpread;
    public Supplier<Integer> aerCrystalChanceToSpread;
    public Supplier<Integer> aquaCrystalChanceToSpread;
    public Supplier<Integer> ignisCrystalChanceToSpread;
    public Supplier<Integer> terraCrystalChanceToSpread;
    //endregion Fields

    //region Initialization
    public PureCrystalSpreader(ICrystalSpreadCondition pureCondition,
                               Supplier<Integer> pureCrystalChanceToSpread,
                               ICrystalSpreadCondition primaMateriaCondition,
                               Supplier<Integer> primaMateriaCrystalChanceToSpread,
                               ICrystalSpreadCondition aerCondition,
                               Supplier<Integer> aerCrystalChanceToSpread,
                               ICrystalSpreadCondition aquaCondition,
                               Supplier<Integer> aquaCrystalChanceToSpread,
                               ICrystalSpreadCondition ignisCondition,
                               Supplier<Integer> ignisCrystalChanceToSpread,
                               ICrystalSpreadCondition terraCondition,
                               Supplier<Integer> terraCrystalChanceToSpread) {
        this.pureCondition = pureCondition;
        this.primaMateriaCondition = primaMateriaCondition;
        this.aerCondition = aerCondition;
        this.aquaCondition = aquaCondition;
        this.ignisCondition = ignisCondition;
        this.terraCondition = terraCondition;
        this.pureCrystalChanceToSpread = pureCrystalChanceToSpread;
        this.primaMateriaCrystalChanceToSpread = primaMateriaCrystalChanceToSpread;
        this.aerCrystalChanceToSpread = aerCrystalChanceToSpread;
        this.aquaCrystalChanceToSpread = aquaCrystalChanceToSpread;
        this.ignisCrystalChanceToSpread = ignisCrystalChanceToSpread;
        this.terraCrystalChanceToSpread = terraCrystalChanceToSpread;

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
        if (world.getRandom().nextInt(this.aerCrystalChanceToSpread.get()) == 0) {
            CrystalPlacementInfo spreadTo =
                    this.getValidSpreadPosition(this.aerCondition, world, possibleTargets, sourceState, sourcePos);
            if (spreadTo != null) {
                world.setBlockState(spreadTo.pos, BlockRegistry.AER_CRYSTAL.get().getDefaultState()
                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
                return true;
            }
        }

        //Check aqua
        if (world.getRandom().nextInt(this.aquaCrystalChanceToSpread.get()) == 0) {
            CrystalPlacementInfo spreadTo =
                    this.getValidSpreadPosition(this.aquaCondition, world, possibleTargets, sourceState, sourcePos);
            if (spreadTo != null) {
                world.setBlockState(spreadTo.pos, BlockRegistry.AQUA_CRYSTAL.get().getDefaultState()
                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
                return true;
            }
        }


        //Check ignis
        if (world.getRandom().nextInt(this.ignisCrystalChanceToSpread.get()) == 0) {
            CrystalPlacementInfo spreadTo =
                    this.getValidSpreadPosition(this.ignisCondition, world, possibleTargets, sourceState, sourcePos);
            if (spreadTo != null) {
                world.setBlockState(spreadTo.pos, BlockRegistry.IGNIS_CRYSTAL.get().getDefaultState()
                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
                return true;
            }
        }


        //Check terra
        if (world.getRandom().nextInt(this.terraCrystalChanceToSpread.get()) == 0) {
            CrystalPlacementInfo spreadTo =
                    this.getValidSpreadPosition(this.terraCondition, world, possibleTargets, sourceState, sourcePos);
            if (spreadTo != null) {
                world.setBlockState(spreadTo.pos, BlockRegistry.TERRA_CRYSTAL.get().getDefaultState()
                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
                return true;
            }
        }

        if (world.getRandom().nextInt(this.primaMateriaCrystalChanceToSpread.get()) == 0) {
            //if no essentia crystals can grow, attempt to grow prima materia
            CrystalPlacementInfo spreadTo =
                    this.getValidSpreadPosition(this.primaMateriaCondition, world, possibleTargets, sourceState,
                            sourcePos);
            if (spreadTo != null) {
                EssentiaChunk chunkEssentia = EssentiaChunkHandler.getOrCreateEssentiaChunk(
                        ((World) world).getDimensionKey(), new ChunkPos(spreadTo.pos));

                //consume essentia from chunk
                chunkEssentia.essentiaCapability.getEssentia().forEach((item, amount) -> {
                    chunkEssentia.essentiaCapability.extractEssentia(item, Theurgy.CONFIG.crystalSettings.primaMateriaSpreadEssentia.get(), false);
                });
                chunkEssentia.markDirty();

                world.setBlockState(spreadTo.pos, BlockRegistry.PRIMA_MATERIA_CRYSTAL.get().getDefaultState()
                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
                return true;
            }
        }

        //Disable pure crystals spreading into more pure crystals. Serves no purpose except covering everything.
//        if (world.getRandom().nextInt(this.pureCrystalChanceToSpread.get()) == 0){
//        //If no other crystal type is valid, check pure condition
//            CrystalPlacementInfo spreadTo = this.getValidSpreadPosition(this.pureCondition, world, possibleTargets, sourceState, sourcePos);
//            if (spreadTo != null) {
//                world.setBlockState(spreadTo.pos, BlockRegistry.PURE_CRYSTAL.get().getDefaultState()
//                                                          .with(BlockStateProperties.FACING, spreadTo.direction), 2);
//                return true;
//            }
//        }
        return false;
    }
    //endregion Overrides

}
