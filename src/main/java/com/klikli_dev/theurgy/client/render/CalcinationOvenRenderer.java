package com.klikli_dev.theurgy.client.render;

import com.klikli_dev.theurgy.block.CalcinationOvenEntity;
import com.klikli_dev.theurgy.client.model.CalcinationOvenModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CalcinationOvenRenderer extends GeoBlockRenderer<CalcinationOvenEntity> {
    public CalcinationOvenRenderer(BlockEntityRendererProvider.Context context) {
        super(new CalcinationOvenModel());
    }
}
