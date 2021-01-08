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

package com.github.klikli_dev.theurgy.common.block;

import com.github.klikli_dev.theurgy.common.theurgy.IAetherInformationProvider;
import com.github.klikli_dev.theurgy.common.theurgy.IEssentiaInformationProvider;
import com.github.klikli_dev.theurgy.common.tile.AetherReceiverTile;
import com.github.klikli_dev.theurgy.common.tile.EssentiaReceiverTileEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.Map;

public class AetherReceiverBlock extends DirectionalAttachedBlock implements IAetherInformationProvider {

    //region Fields
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.<Direction, VoxelShape>builder()
                    .put(Direction.EAST, Block.makeCuboidShape(0, 4, 4, 8, 12, 12))
                    .put(Direction.WEST, Block.makeCuboidShape(8, 4, 4, 16, 12, 12))
                    .put(Direction.NORTH, Block.makeCuboidShape(4, 4, 8, 12, 12, 16))
                    .put(Direction.SOUTH, Block.makeCuboidShape(4, 4, 0, 12, 12, 8))
                    .put(Direction.UP, Block.makeCuboidShape(4, 0, 4, 12, 8, 12))
                    .put(Direction.DOWN, Block.makeCuboidShape(4, 8, 4, 12, 16, 12))
                    .build());
    //endregion Fields

    //region Initialization
    public AetherReceiverBlock(Properties properties) {
        super(properties);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(BlockStateProperties.FACING));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AetherReceiverTile();
    }
    //endregion Overrides
}
