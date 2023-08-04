package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.content.behaviour.interaction.CaloricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.content.behaviour.interaction.SelectionBehaviour;
import net.minecraft.world.level.block.Block;

public class CaloricFluxEmitterBlock extends Block {

    protected SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour;

    public CaloricFluxEmitterBlock(Properties pProperties, SelectionBehaviour<CaloricFluxEmitterSelectedPoint> selectionBehaviour) {
        super(pProperties);
        this.selectionBehaviour = selectionBehaviour;
    }

    public SelectionBehaviour<CaloricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return this.selectionBehaviour;
    }
}
