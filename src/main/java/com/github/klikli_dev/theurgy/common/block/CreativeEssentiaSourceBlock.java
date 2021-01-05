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


import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.theurgy.IEssentiaInformationProvider;
import com.github.klikli_dev.theurgy.common.tile.CreativeEssentiaSourceTileEntity;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CreativeEssentiaSourceBlock extends Block implements IEssentiaInformationProvider {
    //region Initialization
    public CreativeEssentiaSourceBlock(Properties properties) {
        super(properties);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CreativeEssentiaSourceTileEntity();
    }

    @Override
    public void getEssentiaInformation(World world, BlockPos pos, BlockState state, List<ITextComponent> tooltip) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CreativeEssentiaSourceTileEntity) {
            tileEntity.getCapability(CapabilityRegistry.ESSENTIA).ifPresent(cap -> {
                tooltip.add(new TranslationTextComponent(
                        "tooltip." + Theurgy.MODID + ".essentia_information.block.heading",
                        I18n.format(this.getTranslationKey()))
                                    .mergeStyle(TextFormatting.BOLD)
                                    .mergeStyle(TextFormatting.GOLD));
                cap.getEssentia().forEach((essentia, amount) -> {
                    tooltip.add(new TranslationTextComponent(
                            "tooltip." + Theurgy.MODID + ".essentia_information.block.content",
                            I18n.format(essentia.getTranslationKey()), amount));
                });
            });
        }
    }
    //endregion Overrides
}
