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

package com.github.klikli_dev.theurgy.common.block;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.theurgy.IEssentiaInformationProvider;
import com.github.klikli_dev.theurgy.common.tile.CrucibleTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CrucibleBlock extends Block implements IEssentiaInformationProvider {

    //region Fields
    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE =
            VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(),
                    VoxelShapes.or(
                            makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
                            makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
                            makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
                            INSIDE),
                    IBooleanFunction.ONLY_FIRST);
    //endregion Fields

    //region Initialization
    public CrucibleBlock(Properties properties) {
        super(properties);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
                                             Hand handIn, BlockRayTraceResult hit) {
        if (this.hasTileEntity(state)) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof CrucibleTileEntity) {
                return ((CrucibleTileEntity) tileEntity)
                               .onTileEntityActivated(state, pos, player, handIn);
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrucibleTileEntity();
    }

    @Override
    public void getEssentiaInformation(World world, BlockPos pos, BlockState state,
                                       List<ITextComponent> tooltip) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CrucibleTileEntity) {
            CrucibleTileEntity crucible = (CrucibleTileEntity) tileEntity;
            tooltip.add(new TranslationTextComponent(
                    "tooltip." + Theurgy.MODID + ".essentia_information.block.heading",
                    I18n.format(this.getTranslationKey()))
                                .mergeStyle(TextFormatting.BOLD)
                                .mergeStyle(TextFormatting.GOLD));
            if (crucible.essentiaCache.essentia.size() > 0) {
                crucible.essentiaCache.essentia.forEach((item, amount) -> {
                    tooltip.add(new TranslationTextComponent(
                            "tooltip." + Theurgy.MODID + ".essentia_information.block.content",
                            I18n.format(item.getTranslationKey()), amount));
                });
            }
            else {
                tooltip.add(new TranslationTextComponent(
                        "tooltip." + Theurgy.MODID + ".essentia_information.block.no_content"));
            }

        }
    }
    //endregion Overrides
}
