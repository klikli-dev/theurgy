package com.github.klikli_dev.theurgy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CrucibleBlock extends Block {

    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public CrucibleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return false; //TODO: return true
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null; //TODO: return cauldron tile entity heres
    }
}
