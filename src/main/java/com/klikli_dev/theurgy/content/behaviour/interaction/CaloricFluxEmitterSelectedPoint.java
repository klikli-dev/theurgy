package com.klikli_dev.theurgy.content.behaviour.interaction;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.render.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CaloricFluxEmitterSelectedPoint extends SelectedPoint{

    public static final Color color = new Color(0xDDC166, false);

    protected CaloricFluxEmitterSelectedPoint(Level level, BlockPos blockPos, BlockState blockState) {
        super(level, blockPos, blockState);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_CALORIC_FLUX_EMITTER);
    }

    @Override
    public void cycleMode() {
        //has no mode
    }
}
