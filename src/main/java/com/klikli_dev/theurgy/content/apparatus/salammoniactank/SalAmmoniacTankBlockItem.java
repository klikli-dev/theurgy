// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniactank;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SalAmmoniacTankBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SalAmmoniacTankBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllerRegistrar) {
        //do not show anims on item
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final Supplier<GeoItemRenderer<SalAmmoniacTankBlockItem>> renderer = Suppliers.memoize(SalAmmoniacTankItemRenderer::new);

            @Override
            public @Nullable GeoItemRenderer<SalAmmoniacTankBlockItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
    }
}
