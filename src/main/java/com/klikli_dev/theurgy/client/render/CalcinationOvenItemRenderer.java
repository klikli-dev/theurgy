package com.klikli_dev.theurgy.client.render;

import com.klikli_dev.theurgy.client.model.CalcinationOvenItemModel;
import com.klikli_dev.theurgy.item.CalcinationOvenItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CalcinationOvenItemRenderer extends GeoItemRenderer<CalcinationOvenItem> {
    public CalcinationOvenItemRenderer() {
        super(new CalcinationOvenItemModel());
    }
}
