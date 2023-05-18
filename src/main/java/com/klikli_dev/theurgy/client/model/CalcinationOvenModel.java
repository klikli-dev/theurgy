package com.klikli_dev.theurgy.client.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.CalcinationOvenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CalcinationOvenModel extends GeoModel<CalcinationOvenEntity> {
    @Override
    public ResourceLocation getModelResource(CalcinationOvenEntity animatable) {
        return new ResourceLocation(Theurgy.MODID, "geo/calcination_oven.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CalcinationOvenEntity animatable) {
        return new ResourceLocation(Theurgy.MODID, "textures/block/calcination_oven.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CalcinationOvenEntity animatable) {
        return new ResourceLocation(Theurgy.MODID, "animations/calcination_oven.animation.json");
    }
}
