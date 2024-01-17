// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DistillerBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DistillerBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return DistillerBEWLR.get();
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //do not show anims on item
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
