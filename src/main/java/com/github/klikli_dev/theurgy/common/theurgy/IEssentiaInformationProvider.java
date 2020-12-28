package com.github.klikli_dev.theurgy.common.theurgy;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

public interface IEssentiaInformationProvider {
    /**
     * Gets the essentia information to render for this block when looked at.
     * @param world the world.
     * @param pos the block position.
     * @param state the block state.
     * @param tooltip the current tooltip.
     * @return the updated tooltip.
     */
    List<ITextComponent> getEssentiaInformation(World world, BlockPos pos, BlockState state, List<ITextComponent> tooltip);
}
