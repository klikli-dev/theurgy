package com.klikli_dev.theurgy.client.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.item.CalcinationOvenItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CalcinationOvenItemModel extends GeoModel<CalcinationOvenItem> {
    @Override
    public ResourceLocation getModelResource(CalcinationOvenItem animatable) {
        return new ResourceLocation(Theurgy.MODID, "geo/calcination_oven.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CalcinationOvenItem animatable) {
        return new ResourceLocation(Theurgy.MODID, "textures/block/calcination_oven.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CalcinationOvenItem animatable) {
        return new ResourceLocation(Theurgy.MODID, "animations/calcination_oven.animation.json");
    }
}
