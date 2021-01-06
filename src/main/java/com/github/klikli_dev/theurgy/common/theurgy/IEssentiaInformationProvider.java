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

package com.github.klikli_dev.theurgy.common.theurgy;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IEssentiaInformationProvider {
    //region Methods

    IFormattableTextComponent getTranslatedName();

    /**
     * Gets the essentia information to render for this block when looked at.
     *
     * @param world   the world.
     * @param pos     the block position.
     * @param state   the block state.
     * @param tooltip the list of tooltips to insert into.
     */
    default void getEssentiaInformation(World world, BlockPos pos, BlockState state, List<ITextComponent> tooltip){
            TileEntity tileEntity = world.getTileEntity(pos);
            tileEntity.getCapability(CapabilityRegistry.ESSENTIA).ifPresent(cap -> {
                tooltip.add(new TranslationTextComponent(
                        "tooltip." + Theurgy.MODID + ".essentia_information.block.heading", this.getTranslatedName())
                                    .mergeStyle(TextFormatting.BOLD)
                                    .mergeStyle(TextFormatting.GOLD));
                if(!cap.isEmpty()){
                    cap.getEssentia().forEach((essentia, amount) -> {
                        tooltip.add(new TranslationTextComponent(
                                "tooltip." + Theurgy.MODID + ".essentia_information.block.content",
                                new TranslationTextComponent(essentia.getTranslationKey()), amount));
                    });
                } else {
                    tooltip.add(new TranslationTextComponent(
                            "tooltip." + Theurgy.MODID + ".essentia_information.block.no_content"));
                }
            });
    }
    //endregion Methods
}
