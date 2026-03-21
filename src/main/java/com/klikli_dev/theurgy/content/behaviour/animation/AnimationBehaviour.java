// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.animation;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AnimationBehaviour<T extends BlockEntity & GeoBlockEntity> {

    protected final AnimatableInstanceCache animatableInstanceCache;
    protected T blockEntity;
    protected boolean wasProcessingLastTick;

    public AnimationBehaviour(T blockEntity) {
        this.blockEntity = blockEntity;
        this.animatableInstanceCache = GeckoLibUtil.createInstanceCache(blockEntity);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public abstract PlayState animationHandler(AnimationTest<T> event);

}
